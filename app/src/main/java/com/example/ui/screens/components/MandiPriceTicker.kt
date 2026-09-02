package com.example.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingFlat
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.example.data.model.MarketPriceUpdate
import com.example.ui.i18n.LocalAppLanguage
import com.example.ui.i18n.LocalAppStrings
import com.example.ui.i18n.localizedCropName
import com.example.ui.i18n.localizedMandiRegion
import com.example.ui.theme.AgroBorderLight
import com.example.ui.theme.AgroGreenAccent
import com.example.ui.theme.AgroGreenPrimary
import com.example.ui.theme.PriceDownRed
import com.example.ui.theme.PriceUpGreen

@Composable
fun MandiPriceTicker(
    prices: List<MarketPriceUpdate>,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalAppStrings.current
    val lang = LocalAppLanguage.current

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(PriceUpGreen)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = strings.dailyPriceBoard,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
            }

            Text(
                text = "${strings.mandiIntelligenceTitle} ›",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = AgroGreenAccent,
                modifier = Modifier
                    .clickable { onViewAllClick() }
                    .testTag("view_mandi_intelligence_button")
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(prices) { item ->
                MandiTickerItem(item = item)
            }
        }
    }
}

@Composable
fun MandiTickerItem(
    item: MarketPriceUpdate,
    modifier: Modifier = Modifier
) {
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
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .width(170.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.emojiIcon,
                    fontSize = 18.sp
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = trendBg
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = when {
                                isUp -> Icons.AutoMirrored.Filled.TrendingUp
                                isDown -> Icons.AutoMirrored.Filled.TrendingDown
                                else -> Icons.AutoMirrored.Filled.TrendingFlat
                            },
                            contentDescription = "Trend",
                            tint = trendColor,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${if (isUp) "+" else ""}${item.priceChangePercent}%",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = trendColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = item.localizedCropName(lang),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF0F172A),
                maxLines = 1
            )

            Text(
                text = item.localizedMandiRegion(lang).split(" ").firstOrNull() ?: item.localizedMandiRegion(lang),
                fontSize = 11.sp,
                color = Color(0xFF64748B),
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "₹${item.modalPrice.toInt()}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AgroGreenPrimary
                )
                Text(
                    text = "/qtl",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B),
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        }
    }
}
