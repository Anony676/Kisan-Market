package com.example.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import com.example.data.model.Order
import com.example.ui.i18n.LocalAppLanguage
import com.example.ui.i18n.LocalAppStrings
import com.example.ui.i18n.localizedCropName
import com.example.ui.i18n.localizedUnit
import com.example.ui.theme.AgroGreenPrimary
import com.example.ui.theme.PriceUpGreen

@Composable
fun OrderCard(
    order: Order,
    onClick: () -> Unit,
    onAdvanceStatus: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalAppStrings.current
    val lang = LocalAppLanguage.current

    val statusBg = when (order.status) {
        "Placed" -> Color(0xFFFEF3C7)
        "Confirmed" -> Color(0xFFEFF6FF)
        "Packed" -> Color(0xFFF3E8FF)
        "In Transit" -> Color(0xFFDCFCE7)
        "Delivered" -> Color(0xFFD1FAE5)
        else -> Color(0xFFF1F5F9)
    }

    val statusColor = when (order.status) {
        "Placed" -> Color(0xFFB45309)
        "Confirmed" -> Color(0xFF1D4ED8)
        "Packed" -> Color(0xFF7E22CE)
        "In Transit" -> Color(0xFF15803D)
        "Delivered" -> Color(0xFF047857)
        else -> Color(0xFF475569)
    }

    val displayStatus = when (order.status) {
        "Placed" -> strings.orderStatusPlaced
        "Confirmed" -> strings.orderStatusAccepted
        "In Transit" -> strings.orderStatusInTransit
        "Delivered" -> strings.orderStatusDelivered
        else -> order.status
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("order_card_${order.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Order ID and Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${strings.orderIdLabel} #${order.id}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = statusBg
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = when (order.status) {
                                "Delivered" -> Icons.Default.CheckCircle
                                "In Transit" -> Icons.Default.LocalShipping
                                else -> Icons.Default.Schedule
                            },
                            contentDescription = "Status",
                            tint = statusColor,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = displayStatus,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = statusColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Produce details row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF8FAF5))
                ) {
                    Text(
                        text = order.emojiIcon,
                        fontSize = 24.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = order.localizedCropName(lang),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "${order.quantity.toInt()} ${order.localizedUnit(lang)} @ ₹${order.unitPrice}/${order.localizedUnit(lang)}",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                    Text(
                        text = "${strings.orderFarmerLabel}: ${order.farmerName}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = AgroGreenPrimary
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹${order.totalAmount.toInt()}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AgroGreenPrimary
                    )
                    Text(
                        text = order.paymentStatus,
                        fontSize = 11.sp,
                        color = PriceUpGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Transport & Delivery Note
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF8FAF5),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📍 ${order.deliveryAddress}",
                        fontSize = 11.sp,
                        color = Color(0xFF475569),
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = order.orderDate.split(",").firstOrNull() ?: order.orderDate,
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            // Quick Status Advance Actions
            if (order.status != "Delivered" && order.status != "Cancelled") {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val nextAction = when (order.status) {
                        "Placed" -> strings.orderStatusAccepted to "Confirmed"
                        "Confirmed" -> "Pack Produce" to "Packed"
                        "Packed" -> "Dispatch Vehicle" to "In Transit"
                        "In Transit" -> strings.orderStatusDelivered to "Delivered"
                        else -> strings.orderStatusDelivered to "Delivered"
                    }

                    OutlinedButton(
                        onClick = { onAdvanceStatus(nextAction.second) },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .height(36.dp)
                            .testTag("advance_order_status_${order.id}")
                    ) {
                        Text(
                            text = nextAction.first,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgroGreenPrimary
                        )
                    }
                }
            }
        }
    }
}

