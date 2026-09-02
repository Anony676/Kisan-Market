package com.example.ui.i18n

import androidx.compose.runtime.compositionLocalOf

data class AppStrings(
    // App Branding
    val appName: String,
    val directBadge: String,
    val appSubtitle: String,

    // Top Bar & Global
    val searchProducePlaceholder: String,
    val changeRegion: String,
    val selectLanguageTitle: String,
    val selectLanguageSubtitle: String,
    val notifications: String,
    val filters: String,
    val apply: String,
    val cancel: String,
    val done: String,
    val close: String,
    val save: String,
    val select: String,
    val search: String,
    val all: String,

    // Navigation Tabs
    val tabMarket: String,
    val tabMandiRates: String,
    val tabSellProduce: String,
    val tabOrders: String,
    val tabFarmerHub: String,

    // Market Screen
    val heroTitle: String,
    val heroSubtitle: String,
    val heroBadge1: String,
    val heroBadge2: String,
    val heroBadge3: String,
    val freshHarvestTitle: String,
    val freshHarvestSubtitle: String,
    val noListingsFound: String,
    val certifiedOrganic: String,
    val verifiedFarmer: String,
    val directFromFarm: String,
    val mandiBenchmark: String,
    val directPrice: String,
    val availableStock: String,
    val minOrder: String,
    val harvestDate: String,
    val savingsVsMandi: String,
    val viewLot: String,
    val placeOrder: String,
    val contactFarmer: String,
    val organicBadge: String = "Organic",
    val verifiedBadge: String = "Verified",
    val variety: String = "Variety",
    val viewProfile: String = "View Profile",
    val availableQty: String = "Available",
    val buyDirect: String = "Buy Direct",
    val savePercentVsRetail: String = "Save",
    val directFarmHarvest: String = "Direct Farm Harvest",
    val directRate: String = "Direct Rate",
    val qualityGradeLabel: String = "Quality Grade",
    val produceDescCondition: String = "Produce Description & Condition:",
    val viewBio: String = "View Bio",

    // Categories
    val catAll: String,
    val catVegetables: String,
    val catGrains: String,
    val catFruits: String,
    val catPulses: String,
    val catSpices: String,
    val catOrganic: String,

    // Mandi Rates Screen
    val mandiRatesTitle: String,
    val mandiRatesSubtitle: String,
    val searchMandiRatesPlaceholder: String,
    val colCommodity: String,
    val colMandi: String,
    val colMinPrice: String,
    val colMaxPrice: String,
    val colModalPrice: String,
    val colTrend: String,
    val liveUpdatesBadge: String,
    val mandiIntelligenceTitle: String = "APMC Mandi Intelligence",
    val mandiIntelligenceSubtitle: String = "Real-time government market arrivals & rates",
    val switchMandi: String = "Switch Mandi",
    val activeMandiHub: String = "Active Mandi Hub",
    val directBenefit: String = "Direct Benefit",
    val zeroMiddleman: String = "Zero Middleman Cut",
    val updatedStatus: String = "Updated",
    val todayLive: String = "Today Live",
    val officialAPMCFeed: String = "Official APMC feed",
    val dailyPriceBoard: String = "Commodity Daily Price Board",
    val priceAlertsEnabled: String = "Price Alerts Enabled",

    // Sell Produce Screen
    val sellProduceTitle: String,
    val sellProduceSubtitle: String,
    val fieldCropTitle: String,
    val fieldCropCategory: String,
    val fieldVariety: String,
    val fieldQuantity: String,
    val fieldUnit: String,
    val fieldPrice: String,
    val fieldMandiRefPrice: String,
    val fieldLocation: String,
    val fieldOrganicCheck: String,
    val fieldDescription: String,
    val btnPublishListing: String,
    val browseVarietiesPill: String,
    val listingSuccessMsg: String,

    // Orders Screen
    val ordersTitle: String,
    val ordersSubtitle: String,
    val orderStatusAll: String,
    val orderStatusPlaced: String,
    val orderStatusAccepted: String,
    val orderStatusInTransit: String,
    val orderStatusDelivered: String,
    val orderIdLabel: String,
    val orderTotalLabel: String,
    val orderBuyerLabel: String,
    val orderFarmerLabel: String,
    val orderEscrowSecured: String,
    val btnCall: String,
    val btnWhatsApp: String,
    val btnOrderDetails: String,
    val noOrdersFound: String,

    // Farmer Profile Screen
    val farmerProfileTitle: String,
    val farmerProfileSubtitle: String,
    val switchFarmerProfile: String,
    val farmSizeLabel: String,
    val primaryCropsLabel: String,
    val contactPhoneLabel: String,
    val paymentUpiLabel: String,
    val activeListingsCount: String,
    val farmerRatingLabel: String,

    // Notifications Screen
    val notificationsTitle: String,
    val notificationsSubtitle: String,
    val markAllAsRead: String,
    val noNotifications: String,

    // Place Order Dialog
    val placeOrderTitle: String,
    val placeOrderSubtitle: String,
    val quantityRequired: String,
    val buyerName: String,
    val buyerPhone: String,
    val deliveryAddress: String,
    val paymentMethod: String,
    val subtotal: String,
    val transportFee: String,
    val totalPayable: String,
    val btnConfirmOrder: String,

    // Order Success Dialog
    val orderPlacedSuccess: String,
    val orderConnectPrompt: String,

    // Filter Dialog
    val filterDialogTitle: String,
    val filterSortBy: String,
    val filterCategories: String,
    val filterOrganicOnly: String,
    val filterVerifiedOnly: String,
    val btnResetFilters: String,
    val btnApplyFilters: String,

    // Region Selector
    val regionSelectorTitle: String,
    val regionSelectorSubtitle: String,
    val searchRegionPlaceholder: String
)

val LocalAppStrings = compositionLocalOf<AppStrings> {
    AppStringsProvider.getStrings(AppLanguage.ENGLISH)
}

val LocalAppLanguage = compositionLocalOf<AppLanguage> {
    AppLanguage.ENGLISH
}
