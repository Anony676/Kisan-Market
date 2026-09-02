package com.example.ui.screens.components

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Order
import com.example.ui.i18n.LocalAppLanguage
import com.example.ui.i18n.LocalAppStrings
import com.example.ui.i18n.localizedCropName
import com.example.ui.i18n.localizedUnit
import com.example.ui.theme.AgroGreenAccent
import com.example.ui.theme.AgroGreenPrimary
import com.example.ui.theme.HarvestAmber
import com.example.ui.theme.PriceUpGreen

/**
 * PurchaseItemCard for displaying bought produce on the 'My Purchases' tab.
 * Includes grower details, delivery timeline tracker, invoice breakdown, and contact actions.
 */
@Composable
fun PurchaseItemCard(
    order: Order,
    onClick: () -> Unit,
    onTrackClick: () -> Unit = onClick,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
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
            .testTag("purchase_card_${order.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top Row: Purchase ID & Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = null,
                        tint = AgroGreenPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Purchase #${order.id}",
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
                            contentDescription = null,
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

            Spacer(modifier = Modifier.height(12.dp))

            // Produce Item & Amount Information
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF1F8E9))
                ) {
                    Text(
                        text = order.emojiIcon,
                        fontSize = 26.sp
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
                        text = "${order.quantity.toInt()} ${order.localizedUnit(lang)} • ₹${order.unitPrice}/${order.localizedUnit(lang)}",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Text(
                            text = "Grower: ",
                            fontSize = 11.5.sp,
                            color = Color(0xFF94A3B8)
                        )
                        Text(
                            text = order.farmerName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AgroGreenPrimary
                        )
                    }
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
                        fontSize = 10.5.sp,
                        color = PriceUpGreen,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Step Progress Timeline
            DeliveryStepProgress(currentStatus = order.status)

            Spacer(modifier = Modifier.height(10.dp))

            // Delivery Destination & Date Banner
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF8FAFC),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = order.deliveryAddress,
                                fontSize = 11.5.sp,
                                color = Color(0xFF334155),
                                maxLines = 1
                            )
                        }

                        Text(
                            text = if (order.status == "Delivered") "Delivered" else order.expectedDeliveryDate,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (order.status == "Delivered") AgroGreenAccent else HarvestAmber
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Call Farmer Button
                OutlinedButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:${order.farmerPhone}")
                        }
                        try {
                            context.startActivity(intent)
                        } catch (_: Exception) {}
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .height(36.dp)
                        .testTag("call_farmer_btn_${order.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = null,
                        tint = AgroGreenPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = strings.btnCall,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgroGreenPrimary
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onClick,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AgroGreenPrimary),
                        modifier = Modifier
                            .height(36.dp)
                            .testTag("view_purchase_details_${order.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Receipt,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = strings.btnOrderDetails,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DeliveryStepProgress(currentStatus: String) {
    val steps = listOf("Placed", "Confirmed", "In Transit", "Delivered")
    val currentIdx = when (currentStatus) {
        "Placed" -> 0
        "Confirmed", "Packed" -> 1
        "In Transit" -> 2
        "Delivered" -> 3
        else -> 0
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, stepName ->
            val isCompleted = index <= currentIdx
            val isActive = index == currentIdx

            val dotColor = when {
                isCompleted -> AgroGreenPrimary
                else -> Color(0xFFCBD5E1)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(if (isActive) 16.dp else 12.dp)
                        .clip(CircleShape)
                        .background(dotColor)
                ) {
                    if (isCompleted) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = when (stepName) {
                        "In Transit" -> "Shipped"
                        else -> stepName
                    },
                    fontSize = 9.sp,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                    color = if (isCompleted) Color(0xFF1E293B) else Color(0xFF94A3B8)
                )
            }

            if (index < steps.size - 1) {
                Box(
                    modifier = Modifier
                        .weight(0.8f)
                        .height(2.5.dp)
                        .background(
                            if (index < currentIdx) AgroGreenPrimary else Color(0xFFE2E8F0)
                        )
                )
            }
        }
    }
}
