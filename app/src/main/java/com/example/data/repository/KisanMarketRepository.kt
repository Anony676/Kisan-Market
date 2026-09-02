package com.example.data.repository

import com.example.data.local.CropListingDao
import com.example.data.local.FarmerProfileDao
import com.example.data.local.MarketPriceDao
import com.example.data.local.NotificationDao
import com.example.data.local.OrderDao
import com.example.data.local.UserAccountDao
import com.example.data.local.UserProfileDao
import com.example.data.model.AppNotification
import com.example.data.model.CropListing
import com.example.data.model.FarmerProfile
import com.example.data.model.MandiRegion
import com.example.data.model.MarketPriceUpdate
import com.example.data.model.Order
import com.example.data.model.UserAccount
import com.example.data.model.UserProfile
import com.example.data.remote.firestore.FirestoreMarketDataSource
import com.example.data.remote.storage.FirebaseStorageDataSource
import com.example.data.sample.SampleData
import com.example.data.session.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class AuthResult {
    data class Success(val account: UserAccount) : AuthResult()
    data class Failure(val message: String) : AuthResult()
}

/**
 * Hybrid Room/Firestore Data Repository.
 *
 * Provides offline-first local persistence via Room database,
 * coupled with cloud synchronization to Firebase Firestore for
 * User Profiles, Authentication Accounts, and Marketplace Product Listings.
 */
class KisanMarketRepository(
    private val cropListingDao: CropListingDao,
    private val marketPriceDao: MarketPriceDao,
    private val orderDao: OrderDao,
    private val farmerProfileDao: FarmerProfileDao,
    private val notificationDao: NotificationDao,
    private val userProfileDao: UserProfileDao? = null,
    private val userAccountDao: UserAccountDao? = null,
    val firestoreDataSource: FirestoreMarketDataSource = FirestoreMarketDataSource(),
    val storageDataSource: FirebaseStorageDataSource? = null,
    val sessionManager: SessionManager? = null
) {
    val availableRegions: List<MandiRegion> = SampleData.MANDI_REGIONS

    private val initialRegion: MandiRegion = run {
        val savedId = sessionManager?.selectedRegionId
        availableRegions.find { it.id == savedId } ?: availableRegions.first()
    }

    private val _selectedRegion = MutableStateFlow(initialRegion)
    val selectedRegion: StateFlow<MandiRegion> = _selectedRegion.asStateFlow()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        coroutineScope.launch {
            try {
                seedDatabaseIfEmpty()
            } catch (_: Exception) {
                // Prevent unhandled exceptions from locking startup
            }
            // Start listening to cloud updates if Firestore is active
            startCloudSyncListeners()
        }
    }

    private fun startCloudSyncListeners() {
        if (!firestoreDataSource.isCloudSyncAvailable) return

        // 1. Observe Remote User Accounts (Auth) and sync into Room
        coroutineScope.launch {
            try {
                firestoreDataSource.observeUserAccounts().collect { remoteAccounts ->
                    if (remoteAccounts.isNotEmpty()) {
                        userAccountDao?.insertAll(remoteAccounts)
                    }
                }
            } catch (_: Exception) {}
        }

        // 2. Observe Remote User Profile updates and sync into Room
        coroutineScope.launch {
            try {
                firestoreDataSource.observeUserProfile("usr_001").collect { remoteProfile ->
                    if (remoteProfile != null) {
                        userProfileDao?.insertOrUpdateProfile(remoteProfile)
                    }
                }
            } catch (_: Exception) {}
        }

        // 3. Observe Remote Crop Listings updates and sync into Room
        coroutineScope.launch {
            try {
                firestoreDataSource.observeRemoteListings().collect { remoteListings ->
                    if (remoteListings.isNotEmpty()) {
                        remoteListings.forEach { listing ->
                            cropListingDao.insertListing(listing)
                        }
                    }
                }
            } catch (_: Exception) {}
        }
    }

    suspend fun seedDatabaseIfEmpty() = withContext(Dispatchers.IO) {
        // Sync registered user accounts from Firestore
        if (firestoreDataSource.isCloudSyncAvailable) {
            try {
                val remoteAccounts = firestoreDataSource.fetchAllUserAccounts()
                if (remoteAccounts.isNotEmpty()) {
                    userAccountDao?.insertAll(remoteAccounts)
                }
            } catch (_: Exception) {}
        }

        // Clean up any default sample crop listings from marketplace
        try {
            cropListingDao.deleteDefaultListings()
        } catch (_: Exception) {}

        // Clean up any default sample orders and purchases from orders page
        try {
            orderDao.deleteDefaultOrders()
        } catch (_: Exception) {}

        if (marketPriceDao.getPricesCount() < SampleData.MARKET_PRICES.size) {
            marketPriceDao.insertAllPrices(SampleData.MARKET_PRICES)
        }
        if (farmerProfileDao.getFarmersCount() == 0) {
            farmerProfileDao.insertAllFarmers(SampleData.FARMER_PROFILES)
        }

        // Initialize User Profile in Room if missing
        if (userProfileDao != null && userProfileDao.getProfileCount() == 0) {
            val initialProfile = UserProfile(
                name = sessionManager?.buyerName ?: "Your Name",
                phone = sessionManager?.buyerPhone ?: "+91 00000 00000",
                location = sessionManager?.deliveryAddress ?: "Your Location"
            )
            userProfileDao.insertOrUpdateProfile(initialProfile)

            // Try fetching from Firestore cloud if existing
            if (firestoreDataSource.isCloudSyncAvailable) {
                try {
                    val remoteProfile = firestoreDataSource.fetchUserProfile("usr_001")
                    if (remoteProfile != null) {
                        userProfileDao.insertOrUpdateProfile(remoteProfile)
                    }
                } catch (_: Exception) {}
            }
        }

        // Try syncing remote product listings from Firestore
        if (firestoreDataSource.isCloudSyncAvailable) {
            try {
                val remoteListings = firestoreDataSource.fetchRemoteListings()
                remoteListings.forEach { listing ->
                    cropListingDao.insertListing(listing)
                }
            } catch (_: Exception) {}
        }

        val isNotifsInitialized = sessionManager?.isNotificationsInitialized ?: false
        val isNotifsCleared = sessionManager?.isNotificationsCleared ?: false
        if (!isNotifsInitialized && !isNotifsCleared) {
            if (notificationDao.getNotificationsCount() == 0) {
                notificationDao.insertAllNotifications(SampleData.NOTIFICATIONS)
            }
            sessionManager?.isNotificationsInitialized = true
        }
    }

    // ==========================================
    // USER AUTHENTICATION & REGISTRATION
    // ==========================================

    suspend fun registerAccount(
        name: String,
        email: String,
        phone: String,
        password: String,
        role: String,
        location: String
    ): AuthResult = withContext(Dispatchers.IO) {
        val cleanPhone = phone.trim()
        val cleanDigits = cleanPhone.filter { it.isDigit() }
        val cleanEmail = email.trim().lowercase()

        // 1. Sync remote accounts first to check for collisions
        if (firestoreDataSource.isCloudSyncAvailable) {
            try {
                val remoteAccounts = firestoreDataSource.fetchAllUserAccounts()
                if (remoteAccounts.isNotEmpty()) {
                    userAccountDao?.insertAll(remoteAccounts)
                }
            } catch (_: Exception) {}
        }

        val existingAccounts = userAccountDao?.getAllAccounts() ?: emptyList()
        val duplicateAccount = existingAccounts.find {
            (cleanDigits.length >= 10 && it.normalizedPhone.endsWith(cleanDigits.takeLast(10))) ||
            (cleanDigits.isNotEmpty() && it.normalizedPhone == cleanDigits) ||
            (cleanEmail.isNotBlank() && it.email.isNotBlank() && it.email.equals(cleanEmail, ignoreCase = true))
        }

        if (duplicateAccount != null) {
            return@withContext AuthResult.Failure(
                "An account with this phone number or email is already registered. Please sign in with your password."
            )
        }

        val accountId = "usr_${System.currentTimeMillis()}"
        val kisanId = "KISAN-${(10000..99999).random()}"
        val avatarEmoji = when {
            role.contains("Trader", ignoreCase = true) || role.contains("Merchant", ignoreCase = true) -> "🏪"
            role.contains("Buyer", ignoreCase = true) || role.contains("Mill", ignoreCase = true) -> "🏭"
            role.contains("Retail", ignoreCase = true) || role.contains("Consumer", ignoreCase = true) -> "🛒"
            else -> "👨‍🌾"
        }

        val newAccount = UserAccount(
            id = accountId,
            name = name.trim(),
            email = cleanEmail,
            phone = cleanPhone,
            normalizedPhone = cleanDigits,
            passwordHash = password,
            role = role,
            location = location.trim(),
            kisanId = kisanId,
            avatarEmoji = avatarEmoji,
            verifiedStatus = "KYC Verified",
            memberSince = "August 2026",
            rating = 5.0,
            createdAt = System.currentTimeMillis()
        )

        // 2. Save locally into Room
        userAccountDao?.insertOrUpdateAccount(newAccount)

        val profile = UserProfile(
            id = "usr_001",
            name = newAccount.name,
            email = newAccount.email,
            phone = newAccount.phone,
            location = if (newAccount.location.isNotBlank()) newAccount.location else "India",
            role = newAccount.role,
            bio = "${newAccount.role} • Registered on Kisan Market Direct Network",
            avatarEmoji = newAccount.avatarEmoji,
            kisanId = newAccount.kisanId,
            verifiedStatus = "KYC Verified",
            rating = 5.0
        )
        userProfileDao?.insertOrUpdateProfile(profile)

        // 3. Update Session
        sessionManager?.isLoggedIn = true
        sessionManager?.userEmail = newAccount.email
        sessionManager?.buyerName = newAccount.name
        sessionManager?.buyerPhone = newAccount.phone
        sessionManager?.deliveryAddress = newAccount.location

        // 4. Save to Firebase Firestore Cloud
        coroutineScope.launch {
            try {
                firestoreDataSource.saveUserAccount(newAccount)
                firestoreDataSource.saveUserProfile(profile)
            } catch (_: Exception) {}
        }

        AuthResult.Success(newAccount)
    }

    suspend fun authenticateUser(
        emailOrPhone: String,
        password: String
    ): AuthResult = withContext(Dispatchers.IO) {
        val trimmedInput = emailOrPhone.trim()
        val isEmail = trimmedInput.contains("@")
        val cleanDigits = trimmedInput.filter { it.isDigit() }

        // 1. Fetch remote accounts from Firestore cloud to ensure multi-device / fresh sync
        if (firestoreDataSource.isCloudSyncAvailable) {
            try {
                val remoteAccounts = firestoreDataSource.fetchAllUserAccounts()
                if (remoteAccounts.isNotEmpty()) {
                    userAccountDao?.insertAll(remoteAccounts)
                }
            } catch (_: Exception) {}
        }

        val allAccounts = userAccountDao?.getAllAccounts() ?: emptyList()

        // 2. Locate account by Phone Number or Email Address
        val matchedAccount = allAccounts.find { account ->
            if (isEmail) {
                account.email.equals(trimmedInput, ignoreCase = true)
            } else {
                account.phone.trim() == trimmedInput ||
                (cleanDigits.length >= 10 && account.normalizedPhone.endsWith(cleanDigits.takeLast(10))) ||
                (cleanDigits.isNotEmpty() && account.normalizedPhone == cleanDigits) ||
                account.email.equals(trimmedInput, ignoreCase = true)
            }
        }

        if (matchedAccount == null) {
            return@withContext AuthResult.Failure(
                "No registered account found for '$trimmedInput'. Please verify your phone/email or register a new account."
            )
        }

        // 3. STRICT PASSWORD VALIDATION: Must match password created at registration
        if (matchedAccount.passwordHash != password) {
            return@withContext AuthResult.Failure(
                "Incorrect password. Please enter the exact password you set during registration."
            )
        }

        // 4. Successful login: update local user profile and session
        val currentProfile = UserProfile(
            id = "usr_001",
            name = matchedAccount.name,
            email = matchedAccount.email,
            phone = matchedAccount.phone,
            location = if (matchedAccount.location.isNotBlank()) matchedAccount.location else "India",
            role = matchedAccount.role,
            bio = "${matchedAccount.role} • Registered on Kisan Market Direct Network",
            avatarEmoji = matchedAccount.avatarEmoji,
            kisanId = matchedAccount.kisanId,
            verifiedStatus = matchedAccount.verifiedStatus,
            rating = matchedAccount.rating
        )
        userProfileDao?.insertOrUpdateProfile(currentProfile)

        sessionManager?.isLoggedIn = true
        sessionManager?.userEmail = matchedAccount.email
        sessionManager?.buyerName = matchedAccount.name
        sessionManager?.buyerPhone = matchedAccount.phone
        sessionManager?.deliveryAddress = matchedAccount.location

        // Also sync profile to Firestore
        coroutineScope.launch {
            try {
                firestoreDataSource.saveUserProfile(currentProfile)
            } catch (_: Exception) {}
        }

        AuthResult.Success(matchedAccount)
    }

    // ==========================================
    // USER PROFILE (ROOM + FIRESTORE HYBRID)
    // ==========================================

    val userProfile: Flow<UserProfile> = userProfileDao?.getUserProfile("usr_001")?.map {
        it ?: UserProfile()
    } ?: MutableStateFlow(UserProfile())

    suspend fun getUserProfile(): UserProfile = withContext(Dispatchers.IO) {
        userProfileDao?.getUserProfileDirect("usr_001") ?: UserProfile()
    }

    suspend fun updateUserProfile(profile: UserProfile) = withContext(Dispatchers.IO) {
        // 1. Instant local Room persistence
        userProfileDao?.insertOrUpdateProfile(profile)

        // 2. Local Session Cache update
        sessionManager?.buyerName = profile.name
        sessionManager?.buyerPhone = profile.phone
        sessionManager?.deliveryAddress = profile.location

        // 3. Asynchronous Firestore Cloud Sync
        coroutineScope.launch {
            try {
                firestoreDataSource.saveUserProfile(profile)
            } catch (_: Exception) {}
        }
    }

    // ==========================================
    // CROP LISTINGS (ROOM + FIRESTORE HYBRID)
    // ==========================================

    val allListings: Flow<List<CropListing>> = cropListingDao.getAllListings()

    fun getListingsByCategory(category: String): Flow<List<CropListing>> =
        if (category.equals("All", ignoreCase = true)) {
            cropListingDao.getAllListings()
        } else {
            cropListingDao.getListingsByCategory(category)
        }

    fun getListingsByFarmer(farmerId: String): Flow<List<CropListing>> =
        cropListingDao.getListingsByFarmer(farmerId)

    suspend fun getListingById(id: String): CropListing? = withContext(Dispatchers.IO) {
        cropListingDao.getListingById(id)
    }

    suspend fun uploadListingPhotos(listingId: String, uris: List<android.net.Uri>): List<String> = withContext(Dispatchers.IO) {
        storageDataSource?.uploadListingImages(listingId, uris) ?: uris.map { it.toString() }
    }

    suspend fun insertListing(listing: CropListing) = withContext(Dispatchers.IO) {
        // 1. Instant local Room persistence
        cropListingDao.insertListing(listing)
        sessionManager?.isNotificationsCleared = false
        notificationDao.insertNotification(
            AppNotification(
                title = "Harvest Listing Live: ${listing.title}",
                message = "${listing.quantityAvailable} ${listing.unit} of ${listing.variety} is now live in ${listing.mandiRegion}.",
                type = "MARKET_DISPATCH",
                referenceId = listing.id,
                timeFormatted = "Just now",
                emojiIcon = listing.emojiIcon
            )
        )

        // 2. Asynchronous Firestore Cloud Sync
        coroutineScope.launch {
            try {
                firestoreDataSource.saveCropListing(listing)
            } catch (_: Exception) {}
        }
    }

    suspend fun deleteListing(listing: CropListing) = withContext(Dispatchers.IO) {
        // 1. Delete from Room
        cropListingDao.deleteListing(listing)

        // 2. Delete from Firestore
        coroutineScope.launch {
            try {
                firestoreDataSource.deleteCropListing(listing.id)
            } catch (_: Exception) {}
        }
    }

    // Market Prices
    val allPrices: Flow<List<MarketPriceUpdate>> = marketPriceDao.getAllPrices()
    val subscribedAlertPrices: Flow<List<MarketPriceUpdate>> = marketPriceDao.getSubscribedAlertPrices()

    suspend fun setAlertSubscription(priceId: String, isSubscribed: Boolean) = withContext(Dispatchers.IO) {
        marketPriceDao.setAlertSubscription(priceId, isSubscribed)
    }

    // Orders
    val allOrders: Flow<List<Order>> = orderDao.getAllOrders()

    suspend fun placeOrder(order: Order) = withContext(Dispatchers.IO) {
        orderDao.insertOrder(order)
        val listing = cropListingDao.getListingById(order.listingId)
        if (listing != null) {
            val updatedQty = (listing.quantityAvailable - order.quantity).coerceAtLeast(0.0)
            val updatedListing = listing.copy(quantityAvailable = updatedQty)
            cropListingDao.updateListing(updatedListing)
            // Sync updated stock to Firestore
            coroutineScope.launch {
                try {
                    firestoreDataSource.saveCropListing(updatedListing)
                } catch (_: Exception) {}
            }
        }
        sessionManager?.isNotificationsCleared = false
        notificationDao.insertNotification(
            AppNotification(
                title = "New Order Placed: #${order.id}",
                message = "Order for ${order.quantity} ${order.unit} ${order.cropName} confirmed with Farmer ${order.farmerName}.",
                type = "ORDER",
                referenceId = order.id,
                timeFormatted = "Just now",
                emojiIcon = "🛒"
            )
        )
    }

    suspend fun updateOrderStatus(orderId: String, newStatus: String) = withContext(Dispatchers.IO) {
        orderDao.updateOrderStatus(orderId, newStatus)
        val order = orderDao.getOrderById(orderId)
        if (order != null) {
            sessionManager?.isNotificationsCleared = false
            notificationDao.insertNotification(
                AppNotification(
                    title = "Order #${order.id} Updated",
                    message = "Status changed to: $newStatus for ${order.cropName}.",
                    type = "ORDER",
                    referenceId = order.id,
                    timeFormatted = "Just now",
                    emojiIcon = when (newStatus) {
                        "In Transit" -> "🚚"
                        "Delivered" -> "✅"
                        "Confirmed" -> "📋"
                        else -> "📦"
                    }
                )
            )
        }
    }

    // Farmers
    val allFarmers: Flow<List<FarmerProfile>> = farmerProfileDao.getAllFarmers()

    fun getFarmerById(farmerId: String): Flow<FarmerProfile?> =
        farmerProfileDao.getFarmerById(farmerId)

    // Notifications
    val allNotifications: Flow<List<AppNotification>> = notificationDao.getAllNotifications()
    val unreadNotificationsCount: Flow<Int> = notificationDao.getUnreadCount()

    suspend fun markAllNotificationsAsRead() = withContext(Dispatchers.IO) {
        notificationDao.markAllAsRead()
    }

    suspend fun clearAllNotifications() = withContext(Dispatchers.IO) {
        notificationDao.clearAllNotifications()
        sessionManager?.isNotificationsCleared = true
        sessionManager?.isNotificationsInitialized = true
    }

    fun setSelectedRegion(region: MandiRegion) {
        _selectedRegion.value = region
        sessionManager?.selectedRegionId = region.id
    }

    suspend fun refreshMandiRatesAndListings() = withContext(Dispatchers.IO) {
        seedDatabaseIfEmpty()
        // Simulate live APMC market rate updates and fresh arrival synchronization
        val updatedPrices = SampleData.MARKET_PRICES.map { price ->
            val deltaPct = ((-2..3).random()) * 0.5
            val newModal = ((price.modalPrice * (1.0 + deltaPct / 100.0)) * 10).toInt() / 10.0
            val newMin = ((price.minPrice * (1.0 + deltaPct / 100.0)) * 10).toInt() / 10.0
            val newMax = ((price.maxPrice * (1.0 + deltaPct / 100.0)) * 10).toInt() / 10.0
            val trend = if (deltaPct > 0.4) "UP" else if (deltaPct < -0.4) "DOWN" else "STABLE"

            price.copy(
                modalPrice = newModal.coerceAtLeast(5.0),
                minPrice = newMin.coerceAtLeast(4.0),
                maxPrice = newMax.coerceAtLeast(6.0),
                trend = trend,
                priceChangePercent = ((price.priceChangePercent + deltaPct) * 10).toInt() / 10.0,
                lastUpdated = "Updated just now"
            )
        }
        marketPriceDao.insertAllPrices(updatedPrices)
    }
}
