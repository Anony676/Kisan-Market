package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.AppNotification
import com.example.data.model.CropListing
import com.example.data.model.FarmerProfile
import com.example.data.model.MandiRegion
import com.example.data.model.MarketPriceUpdate
import com.example.data.model.Order
import com.example.data.repository.KisanMarketRepository
import com.example.data.sample.SampleData
import com.example.ui.i18n.AppLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class KisanMarketViewModel(
    private val repository: KisanMarketRepository
) : ViewModel() {

    private val sessionManager = repository.sessionManager

    // Initial state restored from SessionManager if available
    private val initialTab = sessionManager?.selectedTab ?: NavigationTab.MARKETPLACE
    private val initialCategory = sessionManager?.selectedCategory ?: "All"
    private val initialSort = sessionManager?.selectedSort ?: SortOption.RECENT
    private val initialOrganic = sessionManager?.isOrganicOnly ?: false
    private val initialVerified = sessionManager?.isVerifiedOnly ?: false
    private val initialBuyerName = sessionManager?.buyerName ?: "Your Name"
    private val initialBuyerPhone = sessionManager?.buyerPhone ?: "+91 00000 00000"
    private val initialAddress = sessionManager?.deliveryAddress ?: "Your Location"
    private val initialRegion = repository.selectedRegion.value
    private val initialIsLoggedIn = sessionManager?.isLoggedIn ?: false

    private val _uiState = MutableStateFlow(
        KisanMarketUiState(
            selectedTab = initialTab,
            selectedRegion = initialRegion,
            availableRegions = repository.availableRegions,
            selectedCategory = initialCategory,
            selectedSort = initialSort,
            organicOnlyFilter = initialOrganic,
            verifiedOnlyFilter = initialVerified,
            buyerName = initialBuyerName,
            buyerPhone = initialBuyerPhone,
            deliveryAddress = initialAddress,
            isLoggedIn = initialIsLoggedIn,
            isAuthScreenVisible = !initialIsLoggedIn,
            filteredListings = computeFilteredListings(
                listings = emptyList(),
                query = "",
                category = initialCategory,
                sort = initialSort,
                organicOnly = initialOrganic,
                verifiedOnly = initialVerified,
                region = initialRegion
            ),
            filteredPrices = computeFilteredPrices(
                prices = SampleData.MARKET_PRICES,
                region = initialRegion
            )
        )
    )
    val uiState: StateFlow<KisanMarketUiState> = _uiState.asStateFlow()

    init {
        observeData()
    }

    private fun observeData() {
        // Single unified observation flow combining Room database flows
        viewModelScope.launch {
            val dbFlow = combine(
                repository.allListings,
                repository.allPrices,
                repository.allOrders,
                repository.allFarmers,
                repository.allNotifications
            ) { listings, prices, orders, farmers, notifs ->
                DataBundle(
                    listings = listings,
                    prices = if (prices.isEmpty()) SampleData.MARKET_PRICES else prices,
                    orders = orders,
                    farmers = if (farmers.isEmpty()) SampleData.FARMER_PROFILES else farmers,
                    notifs = notifs,
                    userProfile = com.example.data.model.UserProfile(),
                    region = repository.selectedRegion.value
                )
            }.combine(repository.userProfile) { bundle, profile ->
                bundle.copy(userProfile = profile)
            }

            dbFlow.combine(repository.selectedRegion) { bundle, region ->
                bundle.copy(region = region)
            }.collect { bundle ->
                withContext(Dispatchers.Default) {
                    val current = _uiState.value
                    val unreadCount = bundle.notifs.count { !it.isRead }
                    val filteredListings = computeFilteredListings(
                        listings = bundle.listings,
                        query = current.searchQuery,
                        category = current.selectedCategory,
                        sort = current.selectedSort,
                        organicOnly = current.organicOnlyFilter,
                        verifiedOnly = current.verifiedOnlyFilter,
                        region = bundle.region
                    )

                    val filteredPrices = computeFilteredPrices(
                        prices = bundle.prices,
                        region = bundle.region
                    )

                    val filteredOrders = computeFilteredOrders(
                        orders = bundle.orders,
                        statusFilter = current.orderStatusFilter
                    )

                    val savedFarmerId = sessionManager?.selectedFarmerId
                    val activeFarmer = current.selectedFarmer
                        ?: if (savedFarmerId != null) bundle.farmers.find { it.farmerId == savedFarmerId } else null

                    _uiState.update { state ->
                        state.copy(
                            selectedRegion = bundle.region,
                            allListings = bundle.listings,
                            filteredListings = filteredListings,
                            allPrices = bundle.prices,
                            filteredPrices = filteredPrices,
                            allOrders = bundle.orders,
                            filteredOrders = filteredOrders,
                            allFarmers = bundle.farmers,
                            selectedFarmer = activeFarmer,
                            userProfile = bundle.userProfile,
                            buyerName = bundle.userProfile.name,
                            buyerPhone = bundle.userProfile.phone,
                            deliveryAddress = bundle.userProfile.location,
                            notifications = bundle.notifs,
                            unreadNotificationsCount = unreadCount,
                            availableRegions = repository.availableRegions
                        )
                    }
                }
            }
        }
    }

    private data class DataBundle(
        val listings: List<CropListing>,
        val prices: List<MarketPriceUpdate>,
        val orders: List<Order>,
        val farmers: List<FarmerProfile>,
        val notifs: List<AppNotification>,
        val userProfile: com.example.data.model.UserProfile,
        val region: MandiRegion
    )

    private fun computeFilteredPrices(
        prices: List<MarketPriceUpdate>,
        region: MandiRegion
    ): List<MarketPriceUpdate> {
        val regionName = region.name.trim().lowercase(Locale.ROOT)
        val regionCity = region.city.trim().lowercase(Locale.ROOT)
        val regionDistrict = region.district.trim().lowercase(Locale.ROOT)
        val regionState = region.state.trim().lowercase(Locale.ROOT)

        val directMatches = prices.filter { price ->
            val pMandi = price.mandiRegion.trim().lowercase(Locale.ROOT)
            val pState = price.state.trim().lowercase(Locale.ROOT)

            pMandi == regionName ||
            pMandi.contains(regionCity) ||
            pMandi.contains(regionDistrict) ||
            regionName.contains(pMandi) ||
            pState == regionState
        }

        // If specific direct matches exist, return them; otherwise if none matched in state, return all prices as fallback
        return if (directMatches.isNotEmpty()) directMatches else prices
    }

    private fun computeFilteredListings(
        listings: List<CropListing>,
        query: String,
        category: String,
        sort: SortOption,
        organicOnly: Boolean,
        verifiedOnly: Boolean,
        region: MandiRegion
    ): List<CropListing> {
        var list = listings

        // Category filter
        if (category != "All") {
            list = if (category == "Organic") {
                list.filter { it.isOrganic }
            } else {
                list.filter { it.category.equals(category, ignoreCase = true) }
            }
        }

        // Search query
        if (query.isNotBlank()) {
            val q = query.trim().lowercase(Locale.ROOT)
            list = list.filter {
                it.title.lowercase(Locale.ROOT).contains(q) ||
                it.variety.lowercase(Locale.ROOT).contains(q) ||
                it.farmerName.lowercase(Locale.ROOT).contains(q) ||
                it.farmLocation.lowercase(Locale.ROOT).contains(q) ||
                it.category.lowercase(Locale.ROOT).contains(q)
            }
        }

        // Checkboxes
        if (organicOnly) {
            list = list.filter { it.isOrganic }
        }
        if (verifiedOnly) {
            list = list.filter { it.isVerifiedFarmer }
        }

        // Sorting
        list = when (sort) {
            SortOption.RECENT -> list.sortedByDescending { it.timestamp }
            SortOption.PRICE_LOW_HIGH -> list.sortedBy { it.pricePerUnit }
            SortOption.PRICE_HIGH_LOW -> list.sortedByDescending { it.pricePerUnit }
            SortOption.SAVINGS_HIGH -> list.sortedByDescending { it.priceSavingsPercent }
            SortOption.NEAREST -> list.sortedBy { it.distanceKm }
        }

        return list
    }

    private fun computeFilteredOrders(orders: List<Order>, statusFilter: String): List<Order> {
        return when (statusFilter) {
            "All" -> orders
            "Active" -> orders.filter { it.status == "Placed" || it.status == "Confirmed" || it.status == "Packed" }
            "In Transit" -> orders.filter { it.status == "In Transit" }
            "Delivered" -> orders.filter { it.status == "Delivered" }
            else -> orders
        }
    }

    fun selectTab(tab: NavigationTab) {
        sessionManager?.selectedTab = tab
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { current ->
            val updated = current.copy(searchQuery = query)
            updated.copy(
                filteredListings = computeFilteredListings(
                    listings = current.allListings,
                    query = query,
                    category = current.selectedCategory,
                    sort = current.selectedSort,
                    organicOnly = current.organicOnlyFilter,
                    verifiedOnly = current.verifiedOnlyFilter,
                    region = current.selectedRegion
                )
            )
        }
    }

    fun selectCategory(category: String) {
        sessionManager?.selectedCategory = category
        _uiState.update { current ->
            val updated = current.copy(selectedCategory = category)
            updated.copy(
                filteredListings = computeFilteredListings(
                    listings = current.allListings,
                    query = current.searchQuery,
                    category = category,
                    sort = current.selectedSort,
                    organicOnly = current.organicOnlyFilter,
                    verifiedOnly = current.verifiedOnlyFilter,
                    region = current.selectedRegion
                )
            )
        }
    }

    fun setSortOption(sort: SortOption) {
        sessionManager?.selectedSort = sort
        _uiState.update { current ->
            val updated = current.copy(selectedSort = sort)
            updated.copy(
                filteredListings = computeFilteredListings(
                    listings = current.allListings,
                    query = current.searchQuery,
                    category = current.selectedCategory,
                    sort = sort,
                    organicOnly = current.organicOnlyFilter,
                    verifiedOnly = current.verifiedOnlyFilter,
                    region = current.selectedRegion
                )
            )
        }
    }

    fun toggleOrganicFilter() {
        val newFilter = !_uiState.value.organicOnlyFilter
        sessionManager?.isOrganicOnly = newFilter
        _uiState.update { current ->
            current.copy(
                organicOnlyFilter = newFilter,
                filteredListings = computeFilteredListings(
                    listings = current.allListings,
                    query = current.searchQuery,
                    category = current.selectedCategory,
                    sort = current.selectedSort,
                    organicOnly = newFilter,
                    verifiedOnly = current.verifiedOnlyFilter,
                    region = current.selectedRegion
                )
            )
        }
    }

    fun toggleVerifiedFilter() {
        val newFilter = !_uiState.value.verifiedOnlyFilter
        sessionManager?.isVerifiedOnly = newFilter
        _uiState.update { current ->
            current.copy(
                verifiedOnlyFilter = newFilter,
                filteredListings = computeFilteredListings(
                    listings = current.allListings,
                    query = current.searchQuery,
                    category = current.selectedCategory,
                    sort = current.selectedSort,
                    organicOnly = current.organicOnlyFilter,
                    verifiedOnly = newFilter,
                    region = current.selectedRegion
                )
            )
        }
    }

    fun setOrderStatusFilter(status: String) {
        _uiState.update { current ->
            current.copy(
                orderStatusFilter = status,
                filteredOrders = computeFilteredOrders(current.allOrders, status)
            )
        }
    }

    fun selectRegion(region: MandiRegion) {
        repository.setSelectedRegion(region)
        _uiState.update { current ->
            current.copy(
                selectedRegion = region,
                isRegionSelectorOpen = false,
                userMessage = "Market region updated to: ${region.name}",
                filteredListings = computeFilteredListings(
                    listings = current.allListings,
                    query = current.searchQuery,
                    category = current.selectedCategory,
                    sort = current.selectedSort,
                    organicOnly = current.organicOnlyFilter,
                    verifiedOnly = current.verifiedOnlyFilter,
                    region = region
                ),
                filteredPrices = computeFilteredPrices(
                    prices = current.allPrices,
                    region = region
                )
            )
        }
    }

    fun openRegionSelector() {
        _uiState.update { it.copy(isRegionSelectorOpen = true) }
    }

    fun closeRegionSelector() {
        _uiState.update { it.copy(isRegionSelectorOpen = false) }
    }

    fun openLanguageSelector() {
        _uiState.update { it.copy(isLanguageSelectorOpen = true) }
    }

    fun closeLanguageSelector() {
        _uiState.update { it.copy(isLanguageSelectorOpen = false) }
    }

    fun setLanguage(language: AppLanguage) {
        _uiState.update { it.copy(selectedLanguage = language, isLanguageSelectorOpen = false) }
    }

    fun openNotifications() {
        _uiState.update { it.copy(isNotificationsOpen = true) }
    }

    fun closeNotifications() {
        _uiState.update { it.copy(isNotificationsOpen = false) }
    }

    fun markAllNotificationsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsAsRead()
        }
    }

    fun clearAllNotifications() {
        viewModelScope.launch {
            repository.clearAllNotifications()
        }
    }

    fun openFilterSheet() {
        _uiState.update { it.copy(isFilterSheetOpen = true) }
    }

    fun closeFilterSheet() {
        _uiState.update { it.copy(isFilterSheetOpen = false) }
    }

    fun openOrderDialog(listing: CropListing) {
        _uiState.update { it.copy(activeListingForOrder = listing) }
    }

    fun closeOrderDialog() {
        _uiState.update { it.copy(activeListingForOrder = null) }
    }

    fun openListingDetail(listing: CropListing) {
        _uiState.update { it.copy(activeListingForDetail = listing) }
    }

    fun closeListingDetail() {
        _uiState.update { it.copy(activeListingForDetail = null) }
    }

    fun openOrderDetail(order: Order) {
        _uiState.update { it.copy(activeOrderForDetail = order) }
    }

    fun closeOrderDetail() {
        _uiState.update { it.copy(activeOrderForDetail = null) }
    }

    fun selectFarmer(farmer: FarmerProfile?) {
        sessionManager?.selectedFarmerId = farmer?.farmerId
        _uiState.update { it.copy(selectedFarmer = farmer) }
    }

    fun clearSelectedFarmer() {
        sessionManager?.selectedFarmerId = null
        _uiState.update { it.copy(selectedFarmer = null) }
    }

    fun togglePriceAlert(priceId: String, isSubscribed: Boolean) {
        viewModelScope.launch {
            repository.setAlertSubscription(priceId, isSubscribed)
            _uiState.update {
                it.copy(
                    userMessage = if (isSubscribed) "Price alert activated! You'll receive daily rate changes." else "Price alert turned off."
                )
            }
        }
    }

    fun openPhotoGallery(listing: CropListing) {
        _uiState.update { it.copy(activeListingForPhotoGallery = listing) }
    }

    fun closePhotoGallery() {
        _uiState.update { it.copy(activeListingForPhotoGallery = null) }
    }

    fun createListing(
        title: String,
        category: String,
        variety: String,
        pricePerUnit: Double,
        unit: String,
        quantity: Double,
        minOrderQty: Double,
        qualityGrade: String,
        isOrganic: Boolean = false,
        harvestDate: String = "Fresh Harvest",
        farmerPhone: String = "",
        description: String = "",
        farmLocation: String = "",
        mandiRegion: String = "",
        imageUris: List<android.net.Uri> = emptyList()
    ) {
        viewModelScope.launch {
            val titleLower = title.lowercase()
            val varietyLower = variety.lowercase()
            val emoji = when {
                titleLower.contains("onion") || varietyLower.contains("onion") || varietyLower.contains("garwa") || varietyLower.contains("bhima") -> "🧅"
                titleLower.contains("tomato") || varietyLower.contains("tomato") || varietyLower.contains("abhinav") || varietyLower.contains("vaishali") -> "🍅"
                titleLower.contains("potato") || varietyLower.contains("potato") || varietyLower.contains("kufri") || varietyLower.contains("jyoti") -> "🥔"
                titleLower.contains("chilli") || titleLower.contains("mirchi") || varietyLower.contains("chilli") || varietyLower.contains("guntur") || varietyLower.contains("teja") -> "🌶️"
                titleLower.contains("pepper") || titleLower.contains("capsicum") || varietyLower.contains("capsicum") || varietyLower.contains("shimla") -> "🫑"
                titleLower.contains("garlic") || varietyLower.contains("garlic") || varietyLower.contains("lahsun") -> "🧄"
                titleLower.contains("ginger") || varietyLower.contains("adrak") -> "🫚"
                titleLower.contains("mango") || varietyLower.contains("kesar") || varietyLower.contains("alphonso") || varietyLower.contains("dasheri") || varietyLower.contains("himsagar") -> "🥭"
                titleLower.contains("apple") || varietyLower.contains("shimla apple") || varietyLower.contains("kinnaur") -> "🍎"
                titleLower.contains("banana") || varietyLower.contains("robusta") || varietyLower.contains("yelakki") || varietyLower.contains("g9") -> "🍌"
                titleLower.contains("grape") || varietyLower.contains("thompson") || varietyLower.contains("sharad") -> "🍇"
                titleLower.contains("pomegranate") || varietyLower.contains("bhagwa") || varietyLower.contains("anar") -> "🫐"
                titleLower.contains("orange") || titleLower.contains("kinnow") || varietyLower.contains("nagpur") -> "🍊"
                titleLower.contains("coconut") || varietyLower.contains("coconut") -> "🥥"
                titleLower.contains("rice") || titleLower.contains("paddy") || titleLower.contains("basmati") || varietyLower.contains("basmati") || varietyLower.contains("sonam") || varietyLower.contains("pusa") -> "🍚"
                titleLower.contains("wheat") || titleLower.contains("gehun") || varietyLower.contains("sharbati") || varietyLower.contains("lokwan") -> "🌾"
                titleLower.contains("maize") || titleLower.contains("corn") || varietyLower.contains("sweet corn") -> "🌽"
                titleLower.contains("soybean") || titleLower.contains("soya") -> "🫘"
                titleLower.contains("turmeric") || varietyLower.contains("salem") || varietyLower.contains("waigaon") || varietyLower.contains("haldi") -> "🫚"
                titleLower.contains("cumin") || titleLower.contains("jeera") -> "🌿"
                titleLower.contains("coriander") || titleLower.contains("dhaniya") -> "🌿"
                titleLower.contains("mustard") || titleLower.contains("sarson") -> "🌻"
                titleLower.contains("gram") || titleLower.contains("chana") || titleLower.contains("dal") || titleLower.contains("pulses") || category == "Pulses" -> "🫘"
                category == "Vegetables" -> "🥦"
                category == "Fruits" -> "🍎"
                category == "Grains" -> "🌾"
                category == "Spices" -> "🌶️"
                category == "Organic" -> "🌿"
                else -> "🌾"
            }

            val userProf = _uiState.value.userProfile
            val activeFarmer = _uiState.value.selectedFarmer
            val effectiveFarmerName = if (userProf.name.isNotBlank()) userProf.name else (activeFarmer?.name ?: "Verified Kisan Producer")
            val effectivePhone = when {
                farmerPhone.isNotBlank() -> farmerPhone
                userProf.phone.isNotBlank() -> userProf.phone
                activeFarmer != null -> activeFarmer.phone
                else -> "+91 98220 12345"
            }
            val effectiveLocation = when {
                farmLocation.isNotBlank() -> farmLocation
                userProf.location.isNotBlank() -> userProf.location
                activeFarmer != null -> "${activeFarmer.village}, ${activeFarmer.district}"
                else -> "${_uiState.value.selectedRegion.city}, ${_uiState.value.selectedRegion.district}"
            }

            val listingId = "lst_${System.currentTimeMillis()}"

            // Upload photos to Firebase Storage / Persistent cache
            val uploadedUrls = if (imageUris.isNotEmpty()) {
                repository.uploadListingPhotos(listingId, imageUris)
            } else {
                emptyList()
            }
            val primaryImageUrl = uploadedUrls.firstOrNull() ?: ""
            val imagesJson = uploadedUrls.joinToString("|||")

            val newListing = CropListing(
                id = listingId,
                title = title,
                category = category,
                variety = variety,
                farmerName = effectiveFarmerName,
                farmerId = activeFarmer?.farmerId ?: "farmer_${System.currentTimeMillis()}",
                farmerPhone = effectivePhone,
                farmLocation = effectiveLocation,
                mandiRegion = if (mandiRegion.isNotBlank()) mandiRegion else _uiState.value.selectedRegion.name,
                pricePerUnit = pricePerUnit,
                unit = unit,
                quantityAvailable = quantity,
                minOrderQty = minOrderQty,
                qualityGrade = qualityGrade,
                isOrganic = isOrganic,
                isVerifiedFarmer = true,
                harvestDate = if (harvestDate.isNotBlank()) harvestDate else "Fresh Harvest",
                description = if (description.isNotBlank()) description else "Fresh harvest direct from verified farm gate.",
                mandiBenchmarkPrice = (pricePerUnit * 0.85).coerceAtLeast(10.0),
                emojiIcon = emoji,
                distanceKm = (5..35).random().toDouble(),
                imageUrl = primaryImageUrl,
                imagesJson = imagesJson
            )

            repository.insertListing(newListing)
            _uiState.update {
                it.copy(
                    selectedTab = NavigationTab.MARKETPLACE,
                    userMessage = "Listing published successfully with ${if (uploadedUrls.isNotEmpty()) "${uploadedUrls.size} photos" else "produce details"}!"
                )
            }
        }
    }

    fun deleteListing(listing: CropListing) {
        viewModelScope.launch {
            repository.deleteListing(listing)
            _uiState.update {
                it.copy(
                    userMessage = "Listing '${listing.title}' deleted successfully."
                )
            }
        }
    }

    fun placeOrder(
        listing: CropListing,
        quantity: Double,
        buyerName: String,
        buyerPhone: String,
        deliveryAddress: String,
        paymentMethod: String
    ) {
        // Update session manager with latest buyer information for auto-fill in future
        if (buyerName.isNotBlank()) sessionManager?.buyerName = buyerName
        if (buyerPhone.isNotBlank()) sessionManager?.buyerPhone = buyerPhone
        if (deliveryAddress.isNotBlank()) sessionManager?.deliveryAddress = deliveryAddress

        viewModelScope.launch {
            val produceAmount = listing.pricePerUnit * quantity
            val transportEstimate = (produceAmount * 0.05).coerceAtLeast(150.0)
            val total = produceAmount + transportEstimate

            val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
            val dateStr = dateFormat.format(Date())

            val order = Order(
                listingId = listing.id,
                cropName = listing.title,
                cropCategory = listing.category,
                variety = listing.variety,
                farmerName = listing.farmerName,
                farmerPhone = listing.farmerPhone,
                buyerName = buyerName.ifBlank { "Kisan Retail Buyer" },
                buyerPhone = buyerPhone.ifBlank { "+91 98765 43210" },
                deliveryAddress = deliveryAddress.ifBlank { "Main Gate, Direct Delivery Hub, Pune" },
                mandiRegion = listing.mandiRegion,
                quantity = quantity,
                unit = listing.unit,
                unitPrice = listing.pricePerUnit,
                totalProduceAmount = produceAmount,
                transportCess = transportEstimate,
                totalAmount = total,
                status = "Placed",
                orderDate = dateStr,
                expectedDeliveryDate = "Delivery in 2 days",
                paymentMethod = paymentMethod,
                paymentStatus = "Escrow Secured",
                vehicleOrTransportNote = "Assigned Kisan Logistics Vehicle",
                emojiIcon = listing.emojiIcon
            )

            repository.placeOrder(order)
            _uiState.update {
                it.copy(
                    activeListingForOrder = null,
                    latestPlacedOrder = order,
                    selectedTab = NavigationTab.ORDERS,
                    buyerName = buyerName.ifBlank { it.buyerName },
                    buyerPhone = buyerPhone.ifBlank { it.buyerPhone },
                    deliveryAddress = deliveryAddress.ifBlank { it.deliveryAddress },
                    userMessage = "Order #${order.id} placed successfully! Farmer notified."
                )
            }
        }
    }

    fun closeOrderSuccessDialog() {
        _uiState.update { it.copy(latestPlacedOrder = null) }
    }

    fun updateOrderStatus(orderId: String, newStatus: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, newStatus)
            _uiState.update { current ->
                val updatedDetail = if (current.activeOrderForDetail?.id == orderId) {
                    current.activeOrderForDetail.copy(status = newStatus)
                } else current.activeOrderForDetail
                current.copy(
                    activeOrderForDetail = updatedDetail,
                    userMessage = "Order #${orderId} status updated to $newStatus"
                )
            }
        }
    }

    fun refreshMarket() {
        if (_uiState.value.isRefreshing) return
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            try {
                kotlinx.coroutines.delay(800)
                repository.refreshMandiRatesAndListings()
                _uiState.update {
                    it.copy(
                        userMessage = "Mandi rates & fresh harvest listings updated!"
                    )
                }
            } catch (_: Exception) {
                // Keep smooth UX
            } finally {
                _uiState.update { it.copy(isRefreshing = false) }
            }
        }
    }

    fun startEditingProfile() {
        _uiState.update { it.copy(isEditingProfile = true) }
    }

    fun cancelEditingProfile() {
        _uiState.update { it.copy(isEditingProfile = false) }
    }

    fun updateUserProfile(
        name: String,
        email: String,
        phone: String,
        bio: String,
        location: String = _uiState.value.userProfile.location,
        role: String = _uiState.value.userProfile.role,
        avatarEmoji: String = _uiState.value.userProfile.avatarEmoji
    ) {
        val updatedProfile = _uiState.value.userProfile.copy(
            name = name.trim(),
            email = email.trim(),
            phone = phone.trim(),
            bio = bio.trim(),
            location = location.trim(),
            role = role.trim(),
            avatarEmoji = avatarEmoji
        )
        // Also keep buyer info synced if updated
        sessionManager?.buyerName = updatedProfile.name
        sessionManager?.buyerPhone = updatedProfile.phone

        _uiState.update {
            it.copy(
                userProfile = updatedProfile,
                buyerName = updatedProfile.name,
                buyerPhone = updatedProfile.phone,
                deliveryAddress = updatedProfile.location,
                isEditingProfile = false,
                userMessage = "Profile updated successfully!"
            )
        }

        viewModelScope.launch {
            repository.updateUserProfile(updatedProfile)
        }
    }

    fun login(emailOrPhone: String, password: String) {
        _uiState.update { it.copy(isAuthLoading = true, authErrorMessage = null) }
        viewModelScope.launch {
            when (val result = repository.authenticateUser(emailOrPhone, password)) {
                is com.example.data.repository.AuthResult.Success -> {
                    val account = result.account
                    val updatedProfile = com.example.data.model.UserProfile(
                        id = "usr_001",
                        name = account.name,
                        email = account.email,
                        phone = account.phone,
                        location = if (account.location.isNotBlank()) account.location else "India",
                        role = account.role,
                        bio = "${account.role} • Registered on Kisan Market Direct Network",
                        avatarEmoji = account.avatarEmoji,
                        kisanId = account.kisanId,
                        verifiedStatus = account.verifiedStatus,
                        rating = account.rating
                    )
                    _uiState.update {
                        it.copy(
                            isLoggedIn = true,
                            isAuthScreenVisible = false,
                            isAuthLoading = false,
                            authErrorMessage = null,
                            buyerName = account.name,
                            buyerPhone = account.phone,
                            deliveryAddress = account.location,
                            userProfile = updatedProfile,
                            userMessage = "Welcome back, ${account.name}!"
                        )
                    }
                }
                is com.example.data.repository.AuthResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoggedIn = false,
                            isAuthScreenVisible = true,
                            isAuthLoading = false,
                            authErrorMessage = result.message
                        )
                    }
                }
            }
        }
    }

    fun register(
        name: String,
        email: String,
        phone: String,
        password: String,
        role: String,
        location: String
    ) {
        _uiState.update { it.copy(isAuthLoading = true, authErrorMessage = null) }
        viewModelScope.launch {
            when (val result = repository.registerAccount(
                name = name,
                email = email,
                phone = phone,
                password = password,
                role = role,
                location = location
            )) {
                is com.example.data.repository.AuthResult.Success -> {
                    val account = result.account
                    val updatedProfile = com.example.data.model.UserProfile(
                        id = "usr_001",
                        name = account.name,
                        email = account.email,
                        phone = account.phone,
                        location = if (account.location.isNotBlank()) account.location else "India",
                        role = account.role,
                        bio = "${account.role} • Registered on Kisan Market Direct Network",
                        avatarEmoji = account.avatarEmoji,
                        kisanId = account.kisanId,
                        verifiedStatus = account.verifiedStatus,
                        rating = account.rating
                    )
                    _uiState.update {
                        it.copy(
                            isLoggedIn = true,
                            isAuthScreenVisible = false,
                            isAuthLoading = false,
                            authErrorMessage = null,
                            buyerName = account.name,
                            buyerPhone = account.phone,
                            deliveryAddress = account.location,
                            userProfile = updatedProfile,
                            userMessage = "Account registered successfully! Welcome to Kisan Market, ${account.name}."
                        )
                    }
                }
                is com.example.data.repository.AuthResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoggedIn = false,
                            isAuthScreenVisible = true,
                            isAuthLoading = false,
                            authErrorMessage = result.message
                        )
                    }
                }
            }
        }
    }

    fun logout() {
        sessionManager?.isLoggedIn = false
        _uiState.update {
            it.copy(
                isLoggedIn = false,
                isAuthScreenVisible = true,
                authErrorMessage = null,
                userMessage = "Logged out successfully."
            )
        }
    }

    fun openAuthScreen() {
        _uiState.update { it.copy(isAuthScreenVisible = true, authErrorMessage = null) }
    }

    fun closeAuthScreen() {
        if (_uiState.value.isLoggedIn) {
            _uiState.update { it.copy(isAuthScreenVisible = false, authErrorMessage = null) }
        }
    }

    fun clearAuthError() {
        _uiState.update { it.copy(authErrorMessage = null) }
    }

    fun clearUserMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }
}

class KisanMarketViewModelFactory(
    private val repository: KisanMarketRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KisanMarketViewModel::class.java)) {
            return KisanMarketViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
