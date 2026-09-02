package com.example.data.remote.firestore

import android.util.Log
import com.example.data.model.CropListing
import com.example.data.model.UserAccount
import com.example.data.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Remote Firestore Data Source for Kisan Market.
 * Handles cloud synchronization for User Profiles, User Accounts (Auth), and Marketplace Product Listings.
 * Includes graceful offline handling and error resilience.
 */
class FirestoreMarketDataSource(
    private val firestore: FirebaseFirestore? = try {
        FirebaseFirestore.getInstance()
    } catch (e: Throwable) {
        Log.w(TAG, "Firestore initialization skipped: ${e.message}")
        null
    }
) {

    companion object {
        private const val TAG = "FirestoreMarketDataSource"
        const val COLLECTION_USER_PROFILES = "user_profiles"
        const val COLLECTION_USER_ACCOUNTS = "user_accounts"
        const val COLLECTION_CROP_LISTINGS = "crop_listings"
    }

    val isCloudSyncAvailable: Boolean
        get() = firestore != null

    // ==========================================
    // USER ACCOUNT / AUTH CLOUD OPERATIONS
    // ==========================================

    suspend fun saveUserAccount(account: UserAccount): Boolean {
        val db = firestore ?: return false
        return try {
            val accountMap = hashMapOf<String, Any>(
                "id" to account.id,
                "name" to account.name,
                "email" to account.email.lowercase().trim(),
                "phone" to account.phone.trim(),
                "normalizedPhone" to account.normalizedPhone,
                "password" to account.passwordHash,
                "passwordHash" to account.passwordHash,
                "role" to account.role,
                "location" to account.location,
                "kisanId" to account.kisanId,
                "avatarEmoji" to account.avatarEmoji,
                "verifiedStatus" to account.verifiedStatus,
                "memberSince" to account.memberSince,
                "rating" to account.rating,
                "completedDeals" to account.completedDeals,
                "activeListings" to account.activeListings,
                "createdAt" to account.createdAt
            )
            db.collection(COLLECTION_USER_ACCOUNTS)
                .document(account.id)
                .set(accountMap, SetOptions.merge())
                .await()
            Log.d(TAG, "UserAccount synced to Firestore: ${account.id} (${account.phone} / ${account.email})")
            true
        } catch (e: Exception) {
            Log.w(TAG, "Failed to sync user account to Firestore: ${e.message}")
            false
        }
    }

    suspend fun fetchAllUserAccounts(): List<UserAccount> {
        val db = firestore ?: return emptyList()
        return try {
            val snapshot = db.collection(COLLECTION_USER_ACCOUNTS).get().await()
            snapshot.documents.mapNotNull { doc ->
                try {
                    val rawPhone = doc.getString("phone") ?: ""
                    val normalized = doc.getString("normalizedPhone")
                        ?: rawPhone.filter { it.isDigit() }
                    val pwd = doc.getString("password")
                        ?: doc.getString("passwordHash")
                        ?: ""

                    UserAccount(
                        id = doc.getString("id") ?: doc.id,
                        name = doc.getString("name") ?: "Kisan Member",
                        email = doc.getString("email") ?: "",
                        phone = rawPhone,
                        normalizedPhone = normalized,
                        passwordHash = pwd,
                        role = doc.getString("role") ?: "Farmer / Producer",
                        location = doc.getString("location") ?: "",
                        kisanId = doc.getString("kisanId") ?: "KISAN-00000",
                        avatarEmoji = doc.getString("avatarEmoji") ?: "👨‍🌾",
                        verifiedStatus = doc.getString("verifiedStatus") ?: "KYC Verified",
                        memberSince = doc.getString("memberSince") ?: "August 2026",
                        rating = doc.getDouble("rating") ?: 5.0,
                        completedDeals = doc.getLong("completedDeals")?.toInt() ?: 0,
                        activeListings = doc.getLong("activeListings")?.toInt() ?: 0,
                        createdAt = doc.getLong("createdAt") ?: System.currentTimeMillis()
                    )
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to fetch user accounts from Firestore: ${e.message}")
            emptyList()
        }
    }

    fun observeUserAccounts(): Flow<List<UserAccount>> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        var listener: ListenerRegistration? = null
        try {
            listener = db.collection(COLLECTION_USER_ACCOUNTS)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.w(TAG, "Listen error for UserAccounts: ${error.message}")
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        val accounts = snapshot.documents.mapNotNull { doc ->
                            try {
                                val rawPhone = doc.getString("phone") ?: ""
                                val normalized = doc.getString("normalizedPhone")
                                    ?: rawPhone.filter { it.isDigit() }
                                val pwd = doc.getString("password")
                                    ?: doc.getString("passwordHash")
                                    ?: ""

                                UserAccount(
                                    id = doc.getString("id") ?: doc.id,
                                    name = doc.getString("name") ?: "Kisan Member",
                                    email = doc.getString("email") ?: "",
                                    phone = rawPhone,
                                    normalizedPhone = normalized,
                                    passwordHash = pwd,
                                    role = doc.getString("role") ?: "Farmer / Producer",
                                    location = doc.getString("location") ?: "",
                                    kisanId = doc.getString("kisanId") ?: "KISAN-00000",
                                    avatarEmoji = doc.getString("avatarEmoji") ?: "👨‍🌾",
                                    verifiedStatus = doc.getString("verifiedStatus") ?: "KYC Verified",
                                    memberSince = doc.getString("memberSince") ?: "August 2026",
                                    rating = doc.getDouble("rating") ?: 5.0,
                                    completedDeals = doc.getLong("completedDeals")?.toInt() ?: 0,
                                    activeListings = doc.getLong("activeListings")?.toInt() ?: 0,
                                    createdAt = doc.getLong("createdAt") ?: System.currentTimeMillis()
                                )
                            } catch (e: Exception) {
                                null
                            }
                        }
                        trySend(accounts)
                    }
                }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to attach UserAccounts listener: ${e.message}")
        }

        awaitClose { listener?.remove() }
    }

    // ==========================================
    // USER PROFILE CLOUD OPERATIONS
    // ==========================================

    suspend fun saveUserProfile(profile: UserProfile): Boolean {
        val db = firestore ?: return false
        return try {
            val profileMap = hashMapOf<String, Any>(
                "id" to profile.id,
                "name" to profile.name,
                "email" to profile.email,
                "phone" to profile.phone,
                "location" to profile.location,
                "role" to profile.role,
                "bio" to profile.bio,
                "avatarUrl" to profile.avatarUrl,
                "avatarEmoji" to profile.avatarEmoji,
                "kisanId" to profile.kisanId,
                "verifiedStatus" to profile.verifiedStatus,
                "memberSince" to profile.memberSince,
                "rating" to profile.rating,
                "completedDeals" to profile.completedDeals,
                "activeListings" to profile.activeListings
            )
            db.collection(COLLECTION_USER_PROFILES)
                .document(profile.id)
                .set(profileMap, SetOptions.merge())
                .await()
            Log.d(TAG, "UserProfile synced to Firestore: ${profile.id}")
            true
        } catch (e: Exception) {
            Log.w(TAG, "Failed to sync user profile to Firestore: ${e.message}")
            false
        }
    }

    suspend fun fetchUserProfile(userId: String = "usr_001"): UserProfile? {
        val db = firestore ?: return null
        return try {
            val snapshot = db.collection(COLLECTION_USER_PROFILES)
                .document(userId)
                .get()
                .await()

            if (snapshot.exists()) {
                UserProfile(
                    id = snapshot.getString("id") ?: userId,
                    name = snapshot.getString("name") ?: "Your Name",
                    email = snapshot.getString("email") ?: "yourgmail gmail com",
                    phone = snapshot.getString("phone") ?: "+91 00000 00000",
                    location = snapshot.getString("location") ?: "Your Location",
                    role = snapshot.getString("role") ?: "Progressive Farmer & Trader",
                    bio = snapshot.getString("bio") ?: "",
                    avatarUrl = snapshot.getString("avatarUrl") ?: "",
                    avatarEmoji = snapshot.getString("avatarEmoji") ?: "👨‍🌾",
                    kisanId = snapshot.getString("kisanId") ?: "KISAN-00000",
                    verifiedStatus = snapshot.getString("verifiedStatus") ?: "KYC Verified",
                    memberSince = snapshot.getString("memberSince") ?: "August 2026",
                    rating = snapshot.getDouble("rating") ?: 5.0,
                    completedDeals = snapshot.getLong("completedDeals")?.toInt() ?: 0,
                    activeListings = snapshot.getLong("activeListings")?.toInt() ?: 0
                )
            } else {
                null
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to fetch user profile from Firestore: ${e.message}")
            null
        }
    }

    fun observeUserProfile(userId: String = "usr_001"): Flow<UserProfile?> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(null)
            close()
            return@callbackFlow
        }

        var listener: ListenerRegistration? = null
        try {
            listener = db.collection(COLLECTION_USER_PROFILES)
                .document(userId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.w(TAG, "Listen error for UserProfile: ${error.message}")
                        return@addSnapshotListener
                    }
                    if (snapshot != null && snapshot.exists()) {
                        val profile = UserProfile(
                            id = snapshot.getString("id") ?: userId,
                            name = snapshot.getString("name") ?: "Your Name",
                            email = snapshot.getString("email") ?: "yourgmail gmail com",
                            phone = snapshot.getString("phone") ?: "+91 00000 00000",
                            location = snapshot.getString("location") ?: "Your Location",
                            role = snapshot.getString("role") ?: "Progressive Farmer & Trader",
                            bio = snapshot.getString("bio") ?: "",
                            avatarUrl = snapshot.getString("avatarUrl") ?: "",
                            avatarEmoji = snapshot.getString("avatarEmoji") ?: "👨‍🌾",
                            kisanId = snapshot.getString("kisanId") ?: "KISAN-00000",
                            verifiedStatus = snapshot.getString("verifiedStatus") ?: "KYC Verified",
                            memberSince = snapshot.getString("memberSince") ?: "August 2026",
                            rating = snapshot.getDouble("rating") ?: 5.0,
                            completedDeals = snapshot.getLong("completedDeals")?.toInt() ?: 0,
                            activeListings = snapshot.getLong("activeListings")?.toInt() ?: 0
                        )
                        trySend(profile)
                    }
                }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to attach UserProfile listener: ${e.message}")
        }

        awaitClose { listener?.remove() }
    }

    // ==========================================
    // CROP LISTINGS CLOUD OPERATIONS
    // ==========================================

    suspend fun saveCropListing(listing: CropListing): Boolean {
        val db = firestore ?: return false
        return try {
            val validImages = listing.images.filter { it.isNotBlank() }
            val primaryImg = if (listing.imageUrl.isNotBlank()) {
                listing.imageUrl
            } else {
                validImages.firstOrNull() ?: ""
            }
            val serializedImagesJson = if (listing.imagesJson.isNotBlank()) {
                listing.imagesJson
            } else {
                validImages.joinToString("|||")
            }

            val listingMap = hashMapOf<String, Any>(
                "id" to listing.id,
                "title" to listing.title,
                "category" to listing.category,
                "variety" to listing.variety,
                "farmerName" to listing.farmerName,
                "farmerId" to listing.farmerId,
                "farmerPhone" to listing.farmerPhone,
                "farmLocation" to listing.farmLocation,
                "mandiRegion" to listing.mandiRegion,
                "pricePerUnit" to listing.pricePerUnit,
                "unit" to listing.unit,
                "quantityAvailable" to listing.quantityAvailable,
                "minOrderQty" to listing.minOrderQty,
                "qualityGrade" to listing.qualityGrade,
                "isOrganic" to listing.isOrganic,
                "isVerifiedFarmer" to listing.isVerifiedFarmer,
                "harvestDate" to listing.harvestDate,
                "description" to listing.description,
                "mandiBenchmarkPrice" to listing.mandiBenchmarkPrice,
                "emojiIcon" to listing.emojiIcon,
                "distanceKm" to listing.distanceKm,
                "imageUrl" to primaryImg,
                "images" to validImages,
                "imagesJson" to serializedImagesJson,
                "timestamp" to listing.timestamp
            )
            db.collection(COLLECTION_CROP_LISTINGS)
                .document(listing.id)
                .set(listingMap, SetOptions.merge())
                .await()
            Log.d(TAG, "CropListing synced to Firestore: ${listing.id} with ${validImages.size} photos")
            true
        } catch (e: Exception) {
            Log.w(TAG, "Failed to save CropListing to Firestore: ${e.message}")
            false
        }
    }

    suspend fun deleteCropListing(listingId: String): Boolean {
        val db = firestore ?: return false
        return try {
            db.collection(COLLECTION_CROP_LISTINGS)
                .document(listingId)
                .delete()
                .await()
            Log.d(TAG, "CropListing deleted from Firestore: $listingId")
            true
        } catch (e: Exception) {
            Log.w(TAG, "Failed to delete CropListing from Firestore: ${e.message}")
            false
        }
    }

    suspend fun fetchRemoteListings(): List<CropListing> {
        val db = firestore ?: return emptyList()
        return try {
            val snapshot = db.collection(COLLECTION_CROP_LISTINGS).get().await()
            snapshot.documents.mapNotNull { doc ->
                try {
                    val rawImages = doc.get("images") as? List<*> ?: doc.get("imageUrls") as? List<*>
                    val remoteImagesList = rawImages?.mapNotNull { it?.toString() }?.filter { it.isNotBlank() } ?: emptyList()
                    val directImageUrl = doc.getString("imageUrl")
                        ?: doc.getString("photoUrl")
                        ?: doc.getString("photo_url")
                        ?: ""
                    val effectivePrimaryUrl = if (directImageUrl.isNotBlank()) directImageUrl else remoteImagesList.firstOrNull() ?: ""
                    val directImagesJson = doc.getString("imagesJson") ?: ""
                    val effectiveImagesJson = when {
                        directImagesJson.isNotBlank() -> directImagesJson
                        remoteImagesList.isNotEmpty() -> remoteImagesList.joinToString("|||")
                        effectivePrimaryUrl.isNotBlank() -> effectivePrimaryUrl
                        else -> ""
                    }

                    CropListing(
                        id = doc.getString("id") ?: doc.id,
                        title = doc.getString("title") ?: return@mapNotNull null,
                        category = doc.getString("category") ?: "Vegetables",
                        variety = doc.getString("variety") ?: "",
                        farmerName = doc.getString("farmerName") ?: "Farmer",
                        farmerId = doc.getString("farmerId") ?: "frm_01",
                        farmerPhone = doc.getString("farmerPhone") ?: "",
                        farmLocation = doc.getString("farmLocation") ?: "",
                        mandiRegion = doc.getString("mandiRegion") ?: "Baramati Mandi",
                        pricePerUnit = doc.getDouble("pricePerUnit") ?: 0.0,
                        unit = doc.getString("unit") ?: "kg",
                        quantityAvailable = doc.getDouble("quantityAvailable") ?: 0.0,
                        minOrderQty = doc.getDouble("minOrderQty") ?: 1.0,
                        qualityGrade = doc.getString("qualityGrade") ?: "Grade A",
                        isOrganic = doc.getBoolean("isOrganic") ?: false,
                        isVerifiedFarmer = doc.getBoolean("isVerifiedFarmer") ?: true,
                        harvestDate = doc.getString("harvestDate") ?: "Fresh Harvest",
                        description = doc.getString("description") ?: "",
                        mandiBenchmarkPrice = doc.getDouble("mandiBenchmarkPrice") ?: 0.0,
                        emojiIcon = doc.getString("emojiIcon") ?: "🌾",
                        distanceKm = doc.getDouble("distanceKm") ?: 10.0,
                        imageUrl = effectivePrimaryUrl,
                        imagesJson = effectiveImagesJson,
                        timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()
                    )
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to fetch remote listings: ${e.message}")
            emptyList()
        }
    }

    fun observeRemoteListings(): Flow<List<CropListing>> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        var listener: ListenerRegistration? = null
        try {
            listener = db.collection(COLLECTION_CROP_LISTINGS)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.w(TAG, "Listen error for CropListings: ${error.message}")
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        val listings = snapshot.documents.mapNotNull { doc ->
                            try {
                                val rawImages = doc.get("images") as? List<*> ?: doc.get("imageUrls") as? List<*>
                                val remoteImagesList = rawImages?.mapNotNull { it?.toString() }?.filter { it.isNotBlank() } ?: emptyList()
                                val directImageUrl = doc.getString("imageUrl")
                                    ?: doc.getString("photoUrl")
                                    ?: doc.getString("photo_url")
                                    ?: ""
                                val effectivePrimaryUrl = if (directImageUrl.isNotBlank()) directImageUrl else remoteImagesList.firstOrNull() ?: ""
                                val directImagesJson = doc.getString("imagesJson") ?: ""
                                val effectiveImagesJson = when {
                                    directImagesJson.isNotBlank() -> directImagesJson
                                    remoteImagesList.isNotEmpty() -> remoteImagesList.joinToString("|||")
                                    effectivePrimaryUrl.isNotBlank() -> effectivePrimaryUrl
                                    else -> ""
                                }

                                CropListing(
                                    id = doc.getString("id") ?: doc.id,
                                    title = doc.getString("title") ?: return@mapNotNull null,
                                    category = doc.getString("category") ?: "Vegetables",
                                    variety = doc.getString("variety") ?: "",
                                    farmerName = doc.getString("farmerName") ?: "Farmer",
                                    farmerId = doc.getString("farmerId") ?: "frm_01",
                                    farmerPhone = doc.getString("farmerPhone") ?: "",
                                    farmLocation = doc.getString("farmLocation") ?: "",
                                    mandiRegion = doc.getString("mandiRegion") ?: "Baramati Mandi",
                                    pricePerUnit = doc.getDouble("pricePerUnit") ?: 0.0,
                                    unit = doc.getString("unit") ?: "kg",
                                    quantityAvailable = doc.getDouble("quantityAvailable") ?: 0.0,
                                    minOrderQty = doc.getDouble("minOrderQty") ?: 1.0,
                                    qualityGrade = doc.getString("qualityGrade") ?: "Grade A",
                                    isOrganic = doc.getBoolean("isOrganic") ?: false,
                                    isVerifiedFarmer = doc.getBoolean("isVerifiedFarmer") ?: true,
                                    harvestDate = doc.getString("harvestDate") ?: "Fresh Harvest",
                                    description = doc.getString("description") ?: "",
                                    mandiBenchmarkPrice = doc.getDouble("mandiBenchmarkPrice") ?: 0.0,
                                    emojiIcon = doc.getString("emojiIcon") ?: "🌾",
                                    distanceKm = doc.getDouble("distanceKm") ?: 10.0,
                                    imageUrl = effectivePrimaryUrl,
                                    imagesJson = effectiveImagesJson,
                                    timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()
                                )
                            } catch (e: Exception) {
                                null
                            }
                        }
                        trySend(listings)
                    }
                }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to attach CropListings listener: ${e.message}")
        }

        awaitClose { listener?.remove() }
    }
}
