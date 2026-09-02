package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ui.screens.components.ProduceCard
import com.example.ui.theme.AgroGreenAccent
import com.example.ui.theme.AgroGreenPrimary
import com.example.ui.theme.HarvestAmber
import com.example.ui.theme.PriceUpGreen

@Composable
fun FarmerProfileScreen(
    farmer: FarmerProfile,
    allFarmers: List<FarmerProfile>,
    farmerListings: List<CropListing>,
    onSelectOtherFarmer: (FarmerProfile) -> Unit,
    onOrderClick: (CropListing) -> Unit,
    onPhotoClick: (CropListing) -> Unit = {},
    onBackClick: () -> Unit = {},
    topHeader: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var selectedSubTab by remember { mutableIntStateOf(0) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 0.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Scrollable Top Header
        if (topHeader != null) {
            item(key = "top_header") {
                topHeader()
            }
        }

        // Back to Marketplace navigation bar
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onBackClick() }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("back_to_marketplace_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to Market",
                    tint = AgroGreenPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Back to Market & Mandi Rates",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = AgroGreenPrimary
                )
            }
        }

        // Quick Farmer Switcher Row (allows viewing profiles of top progressive farmers)
        item {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = "Browse Verified Farmers:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(allFarmers) { f ->
                        val isSelected = f.farmerId == farmer.farmerId
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) AgroGreenPrimary else Color.White,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { onSelectOtherFarmer(f) }
                                .testTag("farmer_chip_${f.farmerId}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = f.avatarEmoji, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = f.name.split(" ").firstOrNull() ?: f.name,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else Color(0xFF1E293B)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Farmer Hero Profile Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFDCFCE7))
                            ) {
                                Text(text = farmer.avatarEmoji, fontSize = 32.sp)
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = farmer.name,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Verified",
                                        tint = PriceUpGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(2.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = Color(0xFF94A3B8),
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = "${farmer.village}, ${farmer.district}, ${farmer.state}",
                                        fontSize = 12.sp,
                                        color = Color(0xFF64748B)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFFEFF6FF),
                                    modifier = Modifier.padding(top = 4.dp)
                                ) {
                                    Text(
                                        text = "🛡️ ${farmer.verificationBadge}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1D4ED8),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = farmer.bio,
                        fontSize = 12.sp,
                        color = Color(0xFF475569),
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Stats Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FarmerMetricCard(
                            title = "Trust Rating",
                            value = "★ ${farmer.rating}",
                            subtitle = "${farmer.totalReviews} Reviews",
                            accentColor = HarvestAmber,
                            modifier = Modifier.weight(1f)
                        )
                        FarmerMetricCard(
                            title = "Total Sold",
                            value = "${farmer.totalSoldQuintals.toInt()} Qtl",
                            subtitle = "Direct P2P Volume",
                            accentColor = AgroGreenPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        FarmerMetricCard(
                            title = "On-Time Dispatch",
                            value = "${farmer.onTimeDispatchPercent}%",
                            subtitle = "Fulfillment Rate",
                            accentColor = PriceUpGreen,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Contact Row
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF8FAF5),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "📞 Direct Contact:", fontSize = 11.sp, color = Color(0xFF64748B))
                                Text(text = farmer.phone, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            }
                            Text(
                                text = "🚜 Land: ${farmer.landSizeAcres} Acres",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AgroGreenAccent
                            )
                        }
                    }
                }
            }
        }

        // Tabs: Active Produce Listings vs Buyer Reviews
        item {
            TabRow(
                selectedTabIndex = selectedSubTab,
                containerColor = Color.Transparent,
                contentColor = AgroGreenPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedSubTab]),
                        color = AgroGreenPrimary
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Tab(
                    selected = selectedSubTab == 0,
                    onClick = { selectedSubTab = 0 },
                    text = { Text("Active Harvest Listings (${farmerListings.size})", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
                )
                Tab(
                    selected = selectedSubTab == 1,
                    onClick = { selectedSubTab = 1 },
                    text = { Text("Buyer Reviews (${farmer.totalReviews})", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        if (selectedSubTab == 0) {
            if (farmerListings.isEmpty()) {
                item {
                    Text(
                        text = "No active listings published by this farmer right now.",
                        fontSize = 13.sp,
                        color = Color(0xFF64748B),
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                    )
                }
            } else {
                items(farmerListings, key = { it.id }) { listing ->
                    ProduceCard(
                        listing = listing,
                        onOrderClick = { onOrderClick(listing) },
                        onFarmerClick = {},
                        onPhotoClick = { onPhotoClick(listing) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        } else {
            // Buyer Reviews sample list
            items(3) { idx ->
                val reviews = listOf(
                    Triple("Vikram Patil (Wholesale Buyer)", "⭐⭐⭐⭐⭐ Outstanding Quality Onions!", "Solid dry Garwa onions with zero rot. Arrived on time with proper truck logistics. Will order regularly."),
                    Triple("Kalyan Retail Mart", "⭐⭐⭐⭐⭐ Transparent Direct Pricing", "Saved 18% compared to middleman prices at Swargate APMC. Excellent moisture control in grains."),
                    Triple("GreenLife Organics", "⭐⭐⭐⭐⭐ Certified & Reliable", "Great direct interaction with farmer. Direct escrow payment released immediately after delivery.")
                )
                val review = reviews[idx]
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = review.first, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            Text(text = "Verified Buyer", fontSize = 10.sp, color = PriceUpGreen, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = review.second, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = HarvestAmber)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = review.third, fontSize = 12.sp, color = Color(0xFF475569))
                    }
                }
            }
        }
    }
}

@Composable
fun FarmerMetricCard(
    title: String,
    value: String,
    subtitle: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFF8FAF5),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontSize = 10.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = accentColor)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, fontSize = 9.sp, color = Color(0xFF94A3B8), maxLines = 1)
        }
    }
}
