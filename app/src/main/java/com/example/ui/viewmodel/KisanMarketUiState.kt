package com.example.ui.viewmodel

import com.example.data.model.AppNotification
import com.example.data.model.CropListing
import com.example.data.model.FarmerProfile
import com.example.data.model.MandiRegion
import com.example.data.model.MarketPriceUpdate
import com.example.data.model.Order
import com.example.data.model.UserProfile
import com.example.data.sample.SampleData
import com.example.ui.i18n.AppLanguage

enum class NavigationTab(val label: String, val iconName: String) {
    MARKETPLACE("Market", "Storefront"),
    MANDI_RATES("Mandi Rates", "TrendingUp"),
    SELL_PRODUCE("Sell Produce", "AddCircle"),
    ORDERS("Orders", "LocalShipping"),
    FARMER_HUB("My Profile", "AccountCircle")
}

enum class SortOption(val title: String) {
    RECENT("Newest Harvest"),
    PRICE_LOW_HIGH("Price: Low to High"),
    PRICE_HIGH_LOW("Price: High to Low"),
    SAVINGS_HIGH("Highest Savings %"),
    NEAREST("Nearest Farm Distance")
}

data class KisanMarketUiState(
    val selectedTab: NavigationTab = NavigationTab.MARKETPLACE,
    val selectedLanguage: AppLanguage = AppLanguage.ENGLISH,
    val isLanguageSelectorOpen: Boolean = false,
    val selectedRegion: MandiRegion = SampleData.MANDI_REGIONS.first(),
    val availableRegions: List<MandiRegion> = SampleData.MANDI_REGIONS,
    val searchQuery: String = "",
    val selectedCategory: String = "All",
    val selectedSort: SortOption = SortOption.RECENT,
    val organicOnlyFilter: Boolean = false,
    val verifiedOnlyFilter: Boolean = false,
    val allListings: List<CropListing> = SampleData.CROP_LISTINGS,
    val filteredListings: List<CropListing> = SampleData.CROP_LISTINGS,
    val allPrices: List<MarketPriceUpdate> = SampleData.MARKET_PRICES,
    val filteredPrices: List<MarketPriceUpdate> = SampleData.MARKET_PRICES.filter {
        it.mandiRegion.equals(SampleData.MANDI_REGIONS.first().name, ignoreCase = true)
    },
    val allOrders: List<Order> = SampleData.INITIAL_ORDERS,
    val orderStatusFilter: String = "All", // All, Active, In Transit, Delivered
    val filteredOrders: List<Order> = SampleData.INITIAL_ORDERS,
    val allFarmers: List<FarmerProfile> = SampleData.FARMER_PROFILES,
    val selectedFarmer: FarmerProfile? = null,
    val notifications: List<AppNotification> = emptyList(),
    val unreadNotificationsCount: Int = 0,
    // Active Modals & Dialogs
    val activeListingForOrder: CropListing? = null,
    val activeListingForDetail: CropListing? = null,
    val activeListingForPhotoGallery: CropListing? = null,
    val activeOrderForDetail: Order? = null,
    val latestPlacedOrder: Order? = null,
    val isRegionSelectorOpen: Boolean = false,
    val isNotificationsOpen: Boolean = false,
    val isFilterSheetOpen: Boolean = false,
    val userRole: String = "All / Dual View",
    val userProfile: UserProfile = UserProfile(),
    val isEditingProfile: Boolean = false,
    val isRefreshing: Boolean = false,
    val userMessage: String? = null,
    val buyerName: String = "Your Name",
    val buyerPhone: String = "+91 00000 00000",
    val deliveryAddress: String = "Your Location",
    val isLoggedIn: Boolean = false,
    val isAuthScreenVisible: Boolean = false,
    val authErrorMessage: String? = null,
    val isAuthLoading: Boolean = false
)
