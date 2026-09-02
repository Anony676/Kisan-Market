package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.i18n.AppStringsProvider
import com.example.ui.i18n.LocalAppLanguage
import com.example.ui.i18n.LocalAppStrings
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.CreateListingScreen
import com.example.ui.screens.FarmerProfileScreen
import com.example.ui.screens.MarketUpdatesScreen
import com.example.ui.screens.MarketplaceScreen
import com.example.ui.screens.MyProfileScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.components.FilterDialog
import com.example.ui.screens.components.KisanTopAppBar
import com.example.ui.screens.components.LanguageSelectorDialog
import com.example.ui.screens.components.OrderDetailDialog
import com.example.ui.screens.components.OrderSuccessDialog
import com.example.ui.screens.components.PlaceOrderDialog
import com.example.ui.screens.components.ProduceDetailSheet
import com.example.ui.screens.components.ProducePhotoGalleryDialog
import com.example.ui.screens.components.RegionSelectorDialog
import com.example.ui.theme.AgroCanvasLight
import com.example.ui.theme.AgroGreenAccent
import com.example.ui.theme.AgroGreenPrimary
import com.example.ui.theme.HarvestAmber
import com.example.ui.viewmodel.KisanMarketViewModel
import com.example.ui.viewmodel.NavigationTab

@Composable
fun KisanMarketApp(
    viewModel: KisanMarketViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val strings = AppStringsProvider.getStrings(uiState.selectedLanguage)
    val snackbarHostState = remember { SnackbarHostState() }

    // Show User Messages in Snackbar
    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearUserMessage()
        }
    }

    // Predictive Back Handling to seamlessly dismiss dialogs and return to home tab
    val hasOpenModal = uiState.latestPlacedOrder != null ||
            uiState.isLanguageSelectorOpen ||
            uiState.activeListingForOrder != null ||
            uiState.activeListingForDetail != null ||
            uiState.activeOrderForDetail != null ||
            uiState.isRegionSelectorOpen ||
            uiState.isNotificationsOpen ||
            uiState.isFilterSheetOpen

    if (hasOpenModal) {
        BackHandler {
            when {
                uiState.latestPlacedOrder != null -> viewModel.closeOrderSuccessDialog()
                uiState.isLanguageSelectorOpen -> viewModel.closeLanguageSelector()
                uiState.activeListingForOrder != null -> viewModel.closeOrderDialog()
                uiState.activeListingForDetail != null -> viewModel.closeListingDetail()
                uiState.activeOrderForDetail != null -> viewModel.closeOrderDetail()
                uiState.isRegionSelectorOpen -> viewModel.closeRegionSelector()
                uiState.isNotificationsOpen -> viewModel.closeNotifications()
                uiState.isFilterSheetOpen -> viewModel.closeFilterSheet()
            }
        }
    } else if (uiState.isEditingProfile) {
        BackHandler {
            viewModel.cancelEditingProfile()
        }
    } else if (uiState.selectedFarmer != null) {
        BackHandler {
            viewModel.clearSelectedFarmer()
        }
    } else if (uiState.selectedTab != NavigationTab.MARKETPLACE) {
        BackHandler {
            viewModel.selectTab(NavigationTab.MARKETPLACE)
        }
    }

    if (uiState.isAuthScreenVisible) {
        AuthScreen(
            onLogin = viewModel::login,
            onRegister = viewModel::register,
            errorMessage = uiState.authErrorMessage,
            isLoading = uiState.isAuthLoading,
            onClearError = viewModel::clearAuthError,
            modifier = modifier
        )
        return
    }

    CompositionLocalProvider(
        LocalAppStrings provides strings,
        LocalAppLanguage provides uiState.selectedLanguage
    ) {
        val renderTopHeader: @Composable (showSearchBar: Boolean) -> Unit = { showSearchBar ->
            KisanTopAppBar(
                selectedRegion = uiState.selectedRegion,
                selectedLanguage = uiState.selectedLanguage,
                unreadNotificationsCount = uiState.unreadNotificationsCount,
                searchQuery = uiState.searchQuery,
                onSearchQueryChange = viewModel::setSearchQuery,
                onRegionClick = viewModel::openRegionSelector,
                onLanguageClick = viewModel::openLanguageSelector,
                onNotificationsClick = viewModel::openNotifications,
                onFilterClick = viewModel::openFilterSheet,
                showSearchBar = showSearchBar
            )
        }

        Scaffold(
            containerColor = AgroCanvasLight,
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 6.dp,
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                    NavigationTab.values().forEach { tab ->
                        val isSelected = uiState.selectedTab == tab
                        val tabLabel = when (tab) {
                            NavigationTab.MARKETPLACE -> strings.tabMarket
                            NavigationTab.MANDI_RATES -> strings.tabMandiRates
                            NavigationTab.SELL_PRODUCE -> strings.tabSellProduce
                            NavigationTab.ORDERS -> strings.tabOrders
                            NavigationTab.FARMER_HUB -> strings.tabFarmerHub
                        }

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.selectTab(tab) },
                            icon = {
                                val icon = when (tab) {
                                    NavigationTab.MARKETPLACE -> if (isSelected) Icons.Filled.Storefront else Icons.Outlined.Storefront
                                    NavigationTab.MANDI_RATES -> if (isSelected) Icons.Filled.TrendingUp else Icons.Outlined.TrendingUp
                                    NavigationTab.SELL_PRODUCE -> if (isSelected) Icons.Filled.AddCircle else Icons.Outlined.AddCircleOutline
                                    NavigationTab.ORDERS -> if (isSelected) Icons.Filled.LocalShipping else Icons.Outlined.LocalShipping
                                    NavigationTab.FARMER_HUB -> if (isSelected) Icons.Filled.AccountCircle else Icons.Outlined.AccountCircle
                                }
                                Icon(imageVector = icon, contentDescription = tabLabel)
                            },
                            label = {
                                Text(
                                    text = tabLabel,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = AgroGreenPrimary,
                                selectedTextColor = AgroGreenPrimary,
                                indicatorColor = Color(0xFFDCFCE7),
                                unselectedIconColor = Color(0xFF64748B),
                                unselectedTextColor = Color(0xFF64748B)
                            ),
                            modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                        )
                    }
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AgroCanvasLight)
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = uiState.selectedTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "MainScreenTabTransition"
                ) { targetTab ->
                    when (targetTab) {
                        NavigationTab.MARKETPLACE -> {
                            val selectedFarmer = uiState.selectedFarmer
                            if (selectedFarmer != null) {
                                val farmerListings = remember(uiState.allListings, selectedFarmer) {
                                    uiState.allListings.filter {
                                        it.farmerId == selectedFarmer.farmerId ||
                                        it.farmerName.equals(selectedFarmer.name, ignoreCase = true)
                                    }
                                }
                                FarmerProfileScreen(
                                    farmer = selectedFarmer,
                                    allFarmers = uiState.allFarmers,
                                    farmerListings = farmerListings,
                                    onSelectOtherFarmer = viewModel::selectFarmer,
                                    onOrderClick = viewModel::openOrderDialog,
                                    onPhotoClick = viewModel::openPhotoGallery,
                                    onBackClick = viewModel::clearSelectedFarmer,
                                    topHeader = { renderTopHeader(false) }
                                )
                            } else {
                                MarketplaceScreen(
                                    listings = uiState.filteredListings,
                                    prices = uiState.filteredPrices,
                                    selectedCategory = uiState.selectedCategory,
                                    onSelectCategory = viewModel::selectCategory,
                                    onOrderClick = viewModel::openOrderDialog,
                                    onFarmerClick = { farmer ->
                                        if (farmer != null) {
                                            viewModel.selectFarmer(farmer)
                                        }
                                    },
                                    onPhotoClick = viewModel::openPhotoGallery,
                                    onViewMandiIntelClick = { viewModel.selectTab(NavigationTab.MANDI_RATES) },
                                    onNavigateToSell = { viewModel.selectTab(NavigationTab.SELL_PRODUCE) },
                                    farmers = uiState.allFarmers,
                                    isRefreshing = uiState.isRefreshing,
                                    onRefresh = viewModel::refreshMarket,
                                    topHeader = { renderTopHeader(true) }
                                )
                            }
                        }

                        NavigationTab.MANDI_RATES -> {
                            MarketUpdatesScreen(
                                prices = uiState.filteredPrices,
                                selectedRegion = uiState.selectedRegion,
                                onRegionClick = viewModel::openRegionSelector,
                                onToggleAlert = viewModel::togglePriceAlert,
                                topHeader = { renderTopHeader(false) }
                            )
                        }

                        NavigationTab.SELL_PRODUCE -> {
                            CreateListingScreen(
                                currentFarmer = uiState.selectedFarmer,
                                userProfile = uiState.userProfile,
                                selectedRegion = uiState.selectedRegion,
                                onSubmitListing = { title, category, variety, price, unit, qty, minOrder, grade, isOrganic, harvestDate, phone, desc, farmLoc, mandiRegion, imageUris ->
                                    viewModel.createListing(
                                        title = title,
                                        category = category,
                                        variety = variety,
                                        pricePerUnit = price,
                                        unit = unit,
                                        quantity = qty,
                                        minOrderQty = minOrder,
                                        qualityGrade = grade,
                                        isOrganic = isOrganic,
                                        harvestDate = harvestDate,
                                        farmerPhone = phone,
                                        description = desc,
                                        farmLocation = farmLoc,
                                        mandiRegion = mandiRegion,
                                        imageUris = imageUris
                                    )
                                },
                                topHeader = { renderTopHeader(false) }
                            )
                        }

                        NavigationTab.ORDERS -> {
                            OrdersScreen(
                                orders = uiState.filteredOrders,
                                activeStatusFilter = uiState.orderStatusFilter,
                                onSelectStatusFilter = viewModel::setOrderStatusFilter,
                                onOrderClick = viewModel::openOrderDetail,
                                onAdvanceStatus = { orderId, newStatus ->
                                    viewModel.updateOrderStatus(orderId, newStatus)
                                },
                                onNavigateToMarket = { viewModel.selectTab(NavigationTab.MARKETPLACE) },
                                onNavigateToSell = { viewModel.selectTab(NavigationTab.SELL_PRODUCE) },
                                topHeader = { renderTopHeader(false) }
                            )
                        }

                        NavigationTab.FARMER_HUB -> {
                            val userListings = remember(uiState.allListings, uiState.userProfile, uiState.selectedFarmer) {
                                uiState.allListings.filter { listing ->
                                    val prof = uiState.userProfile
                                    val farmer = uiState.selectedFarmer
                                    listing.farmerName.equals(prof.name, ignoreCase = true) ||
                                    (prof.kisanId.isNotBlank() && listing.farmerId == prof.kisanId) ||
                                    (farmer != null && (listing.farmerId == farmer.farmerId || listing.farmerName.equals(farmer.name, ignoreCase = true))) ||
                                    (listing.farmerPhone.isNotBlank() && prof.phone.isNotBlank() && listing.farmerPhone.replace(" ", "") == prof.phone.replace(" ", ""))
                                }
                            }

                            MyProfileScreen(
                                userProfile = uiState.userProfile,
                                activeListings = userListings,
                                isEditing = uiState.isEditingProfile,
                                onStartEditing = viewModel::startEditingProfile,
                                onCancelEditing = viewModel::cancelEditingProfile,
                                onSaveProfile = { name, email, phone, bio, location, role, emoji ->
                                    viewModel.updateUserProfile(
                                        name = name,
                                        email = email,
                                        phone = phone,
                                        bio = bio,
                                        location = location,
                                        role = role,
                                        avatarEmoji = emoji
                                    )
                                },
                                onDeleteListing = viewModel::deleteListing,
                                onAddNewListing = {
                                    viewModel.selectTab(NavigationTab.SELL_PRODUCE)
                                },
                                onLogout = viewModel::logout,
                                onSwitchAccount = viewModel::openAuthScreen,
                                topHeader = { renderTopHeader(false) }
                            )
                        }
                    }
                }
            }
        }

        // Modal Dialogs & Sheets
        // 1. Language Selector Dialog
        if (uiState.isLanguageSelectorOpen) {
            LanguageSelectorDialog(
                currentLanguage = uiState.selectedLanguage,
                onLanguageSelected = viewModel::setLanguage,
                onDismiss = viewModel::closeLanguageSelector
            )
        }

        // 2. Place Order Sheet
        uiState.activeListingForOrder?.let { listing ->
            PlaceOrderDialog(
                listing = listing,
                initialBuyerName = uiState.buyerName,
                initialBuyerPhone = uiState.buyerPhone,
                initialDeliveryAddress = uiState.deliveryAddress,
                onDismiss = viewModel::closeOrderDialog,
                onConfirmOrder = { qty, buyerName, buyerPhone, address, payment ->
                    viewModel.placeOrder(listing, qty, buyerName, buyerPhone, address, payment)
                }
            )
        }

        // 2b. Order Placed Direct Contact Dialog
        uiState.latestPlacedOrder?.let { order ->
            OrderSuccessDialog(
                order = order,
                onDone = viewModel::closeOrderSuccessDialog
            )
        }

        // 3. Produce Detail Sheet
        uiState.activeListingForDetail?.let { listing ->
            ProduceDetailSheet(
                listing = listing,
                onDismiss = viewModel::closeListingDetail,
                onBuyDirectClick = {
                    viewModel.closeListingDetail()
                    viewModel.openOrderDialog(listing)
                },
                onViewFarmerClick = {
                    viewModel.closeListingDetail()
                    val farmer = uiState.allFarmers.find { it.farmerId == listing.farmerId }
                    farmer?.let { viewModel.selectFarmer(it) }
                }
            )
        }

        // 4. Order Detail Slip
        uiState.activeOrderForDetail?.let { order ->
            OrderDetailDialog(
                order = order,
                onDismiss = viewModel::closeOrderDetail,
                onAdvanceStatus = { newStatus ->
                    viewModel.updateOrderStatus(order.id, newStatus)
                }
            )
        }

        // 5. Mandi Region Selector
        if (uiState.isRegionSelectorOpen) {
            RegionSelectorDialog(
                currentRegion = uiState.selectedRegion,
                regions = uiState.availableRegions,
                onSelectRegion = viewModel::selectRegion,
                onDismiss = viewModel::closeRegionSelector
            )
        }

        // 6. Notifications Dialog
        if (uiState.isNotificationsOpen) {
            NotificationsScreen(
                notifications = uiState.notifications,
                onMarkAllRead = viewModel::markAllNotificationsRead,
                onClearAll = viewModel::clearAllNotifications,
                onDismiss = viewModel::closeNotifications
            )
        }

        // 7. Filter Dialog
        if (uiState.isFilterSheetOpen) {
            FilterDialog(
                selectedSort = uiState.selectedSort,
                organicOnly = uiState.organicOnlyFilter,
                verifiedOnly = uiState.verifiedOnlyFilter,
                onSortChange = viewModel::setSortOption,
                onToggleOrganic = viewModel::toggleOrganicFilter,
                onToggleVerified = viewModel::toggleVerifiedFilter,
                onDismiss = viewModel::closeFilterSheet
            )
        }

        // 8. Produce Photo Gallery Large Pop-up Viewer (Swipeable at least 3 photos)
        uiState.activeListingForPhotoGallery?.let { listing ->
            ProducePhotoGalleryDialog(
                listing = listing,
                onDismiss = viewModel::closePhotoGallery,
                onOrderClick = {
                    viewModel.closePhotoGallery()
                    viewModel.openOrderDialog(listing)
                }
            )
        }
    }
}
