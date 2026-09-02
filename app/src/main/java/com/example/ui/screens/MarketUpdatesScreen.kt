package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingFlat
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MandiRegion
import com.example.data.model.MarketPriceUpdate
import com.example.ui.i18n.LocalAppLanguage
import com.example.ui.i18n.LocalAppStrings
import com.example.ui.i18n.localizedCropName
import com.example.ui.i18n.localizedDistrict
import com.example.ui.i18n.localizedMandiRegion
import com.example.ui.i18n.localizedName
import com.example.ui.i18n.localizedUnit
import com.example.ui.i18n.localizedVariety
import com.example.ui.theme.AgroGreenAccent
import com.example.ui.theme.AgroGreenPrimary
import com.example.ui.theme.HarvestAmber
import com.example.ui.theme.PriceDownRed
import com.example.ui.theme.PriceUpGreen

@Composable
fun MarketUpdatesScreen(
    prices: List<MarketPriceUpdate>,
    selectedRegion: MandiRegion,
    onRegionClick: () -> Unit,
    onToggleAlert: (priceId: String, isSubscribed: Boolean) -> Unit,
    topHeader: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val strings = LocalAppStrings.current
    val lang = LocalAppLanguage.current

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Scrollable Top Header (App Identity, Location, Language, Notifications)
        if (topHeader != null) {
            item(key = "top_header") {
                topHeader()
            }
        }

        // Hero Header & Intelligence Overview
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AgroGreenPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 10.dp)
                        ) {
                            Text(
                                text = "📈 ${strings.mandiIntelligenceTitle}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = strings.mandiIntelligenceSubtitle,
                                fontSize = 11.5.sp,
                                color = Color.White.copy(alpha = 0.85f),
                                maxLines = 1
                            )
                        }

                        // Compact Horizontal Rectangle "Switch Mandi" button
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFFEF3C7),
                            border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { onRegionClick() }
                                .testTag("mandi_rates_region_switch")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SwapHoriz,
                                    contentDescription = strings.switchMandi,
                                    tint = Color(0xFF92400E),
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = strings.switchMandi,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF92400E),
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Comparison Insights row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val regName = selectedRegion.localizedName(lang)
                        PriceMetricBox(
                            title = strings.activeMandiHub,
                            value = regName.split(" ").firstOrNull() ?: regName,
                            subtitle = selectedRegion.localizedDistrict(lang),
                            modifier = Modifier.weight(1f)
                        )
                        PriceMetricBox(
                            title = strings.directBenefit,
                            value = "+22% to +35%",
                            subtitle = strings.zeroMiddleman,
                            modifier = Modifier.weight(1f)
                        )
                        PriceMetricBox(
                            title = strings.updatedStatus,
                            value = strings.todayLive,
                            subtitle = strings.officialAPMCFeed,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Section Title
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${strings.dailyPriceBoard} (${prices.size})",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Text(
                    text = "🔔 ${strings.priceAlertsEnabled}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AgroGreenAccent
                )
            }
        }

        // Price Update Cards
        items(prices, key = { it.id }) { priceItem ->
            MandiPriceCard(
                item = priceItem,
                onToggleAlert = { isSubscribed -> onToggleAlert(priceItem.id, isSubscribed) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        // Price Analysis Advisory Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = HarvestAmber,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Kisan Market Advisory",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "• APMC Direct Market: Daily verified market pricing updated directly from mandis.\n• Benchmark Rates: Use these modal averages when setting direct farm gate sale prices to maximize earnings.",
                        fontSize = 12.sp,
                        color = Color(0xFF78350F),
                        lineHeight = 17.sp
                    )
                }
            }
        }
    }
}

@Composable
fun MandiPriceCard(
    item: MarketPriceUpdate,
    onToggleAlert: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalAppStrings.current
    val lang = LocalAppLanguage.current
    val isUp = item.trend == "UP" || item.priceChangePercent > 0
    val isDown = item.trend == "DOWN" || item.priceChangePercent < 0

    val trendColor = when {
        isUp -> PriceUpGreen
        isDown -> PriceDownRed
        else -> Color(0xFF64748B)
    }

    val trendBg = when {
        isUp -> Color(0xFFDCFCE7)
        isDown -> Color(0xFFFEE2E2)
        else -> Color(0xFFF1F5F9)
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Emoji, Name, Trend Badge & Notification Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = item.emojiIcon, fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = item.localizedCropName(lang),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "${item.localizedVariety(lang)} • ${item.localizedMandiRegion(lang)}",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = trendBg
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = when {
                                    isUp -> Icons.AutoMirrored.Filled.TrendingUp
                                    isDown -> Icons.AutoMirrored.Filled.TrendingDown
                                    else -> Icons.AutoMirrored.Filled.TrendingFlat
                                },
                                contentDescription = null,
                                tint = trendColor,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${if (isUp) "+" else ""}${item.priceChangePercent}%",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = trendColor
                            )
                        }
                    }

                    // Alert Toggle
                    IconButton(
                        onClick = { onToggleAlert(!item.isSubscribedAlert) },
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("price_alert_toggle_${item.id}")
                    ) {
                        Icon(
                            imageVector = if (item.isSubscribedAlert) Icons.Default.NotificationsActive else Icons.Default.NotificationsNone,
                            contentDescription = "Price Alert",
                            tint = if (item.isSubscribedAlert) HarvestAmber else Color(0xFF94A3B8)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Price Range Table
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF8FAF5))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = strings.colMinPrice, fontSize = 10.sp, color = Color(0xFF64748B))
                    Text(text = "₹${item.minPrice.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF334155))
                }
                Column {
                    Text(text = strings.colModalPrice, fontSize = 10.sp, color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold)
                    Text(text = "₹${item.modalPrice.toInt()}/${item.localizedUnit(lang)}", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = AgroGreenPrimary)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = strings.colMaxPrice, fontSize = 10.sp, color = Color(0xFF64748B))
                    Text(text = "₹${item.maxPrice.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = PriceUpGreen)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Footer arrival info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📦 ${item.arrivalsQuantity}",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
                Text(
                    text = "🕒 ${item.lastUpdated}",
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8)
                )
            }
        }
    }
}

@Composable
fun PriceMetricBox(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0x22FFFFFF),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f))
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White, maxLines = 1)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, fontSize = 9.sp, color = HarvestAmber, maxLines = 1)
        }
    }
}
