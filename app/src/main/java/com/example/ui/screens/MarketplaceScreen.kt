package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CropListing
import com.example.data.model.FarmerProfile
import com.example.data.model.MarketPriceUpdate
import com.example.ui.i18n.LocalAppStrings
import com.example.ui.screens.components.MandiPriceTicker
import com.example.ui.screens.components.ProduceCard
import com.example.ui.theme.AgroGreenPrimary
import com.example.ui.theme.HarvestAmber
import com.example.ui.theme.PriceUpGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceScreen(
    listings: List<CropListing>,
    prices: List<MarketPriceUpdate>,
    selectedCategory: String,
    onSelectCategory: (String) -> Unit,
    onOrderClick: (CropListing) -> Unit,
    onFarmerClick: (FarmerProfile?) -> Unit,
    onPhotoClick: (CropListing) -> Unit = {},
    onViewMandiIntelClick: () -> Unit,
    onNavigateToSell: () -> Unit,
    farmers: List<FarmerProfile>,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    topHeader: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val strings = LocalAppStrings.current
    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        state = pullToRefreshState,
        modifier = modifier
            .fillMaxSize()
            .testTag("market_pull_to_refresh"),
        indicator = {
            PullToRefreshDefaults.Indicator(
                state = pullToRefreshState,
                isRefreshing = isRefreshing,
                modifier = Modifier.align(Alignment.TopCenter),
                containerColor = Color.White,
                color = AgroGreenPrimary
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 0.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Scrollable Top Header (App Identity, Location, Language, Notifications & Search)
            if (topHeader != null) {
                item(key = "top_header") {
                    topHeader()
                }
            }

            // Live Mandi Ticker
            item {
                MandiPriceTicker(
                    prices = prices,
                    onViewAllClick = onViewMandiIntelClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Listings Header & Count
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${strings.freshHarvestTitle} (${listings.size})",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )

                    Text(
                        text = strings.directPrice,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PriceUpGreen
                    )
                }
            }

            // Empty State
            if (listings.isEmpty()) {
                item {
                    EmptyProduceState(
                        selectedCategory = selectedCategory,
                        onResetFilters = { onSelectCategory("All") },
                        onNavigateToSell = onNavigateToSell
                    )
                }
            } else {
                // List of Produce Cards
                items(listings, key = { it.id }) { listing ->
                    val farmer = farmers.find { it.farmerId == listing.farmerId }
                    ProduceCard(
                        listing = listing,
                        onOrderClick = { onOrderClick(listing) },
                        onFarmerClick = { onFarmerClick(farmer) },
                        onPhotoClick = { onPhotoClick(listing) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyProduceState(
    selectedCategory: String = "All",
    onResetFilters: () -> Unit,
    onNavigateToSell: () -> Unit = {}
) {
    val strings = LocalAppStrings.current

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("empty_produce_state_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "🌾", fontSize = 44.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = if (selectedCategory != "All") "No $selectedCategory Listings Found" else strings.noListingsFound,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (selectedCategory != "All")
                    "Try switching categories or list your produce to sell directly in this Mandi."
                else
                    "There are no active harvest listings in the marketplace yet. Post your harvested crops to start selling directly to verified buyers.",
                fontSize = 13.sp,
                color = Color(0xFF64748B),
                lineHeight = 18.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selectedCategory != "All") {
                    Button(
                        onClick = onResetFilters,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF1F5F9),
                            contentColor = Color(0xFF334155)
                        ),
                        modifier = Modifier.testTag("reset_category_filter_button")
                    ) {
                        Text(strings.all, fontWeight = FontWeight.SemiBold)
                    }
                }

                Button(
                    onClick = onNavigateToSell,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AgroGreenPrimary,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.testTag("empty_state_post_produce_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = strings.tabSellProduce,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
