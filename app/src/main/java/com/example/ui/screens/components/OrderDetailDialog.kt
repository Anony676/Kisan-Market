package com.example.ui.screens.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.Order
import com.example.ui.i18n.LocalAppLanguage
import com.example.ui.i18n.LocalAppStrings
import com.example.ui.i18n.localizedCropName
import com.example.ui.i18n.localizedMandiRegion
import com.example.ui.i18n.localizedUnit
import com.example.ui.i18n.localizedVariety
import com.example.ui.theme.AgroGreenAccent
import com.example.ui.theme.AgroGreenPrimary
import com.example.ui.theme.HarvestAmber
import com.example.ui.theme.PriceUpGreen

@Composable
fun OrderDetailDialog(
    order: Order,
    onDismiss: () -> Unit,
    onAdvanceStatus: (String) -> Unit
) {
    val strings = LocalAppStrings.current
    val lang = LocalAppLanguage.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Top Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ReceiptLong,
                            contentDescription = null,
                            tint = AgroGreenPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${strings.orderIdLabel} #${order.id}",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_order_detail_button")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Status Timeline Steps
                StatusTimelineRow(currentStatus = order.status)

                Spacer(modifier = Modifier.height(16.dp))

                // Item Details Box
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF8FAF5),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = order.emojiIcon, fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = order.localizedCropName(lang),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = "${order.localizedVariety(lang)} • ${order.localizedMandiRegion(lang)}",
                                    fontSize = 12.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFE2E8DC))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "${strings.quantityRequired}:", fontSize = 12.sp, color = Color(0xFF64748B))
                            Text(text = "${order.quantity.toInt()} ${order.localizedUnit(lang)}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Unit Rate:", fontSize = 12.sp, color = Color(0xFF64748B))
                            Text(text = "₹${order.unitPrice}/${order.localizedUnit(lang)}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "${strings.subtotal}:", fontSize = 12.sp, color = Color(0xFF64748B))
                            Text(text = "₹${order.totalProduceAmount.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "${strings.transportFee}:", fontSize = 12.sp, color = Color(0xFF64748B))
                            Text(text = "₹${order.transportCess.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE2E8DC))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "${strings.totalPayable}:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            Text(text = "₹${order.totalAmount.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = AgroGreenPrimary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Parties Info
                Text(text = "Direct Trade Contact Details:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF1F5F9),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "👨‍🌾 ${strings.orderFarmerLabel}: ${order.farmerName} (${order.farmerPhone})", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "👤 ${strings.orderBuyerLabel}: ${order.buyerName} (${order.buyerPhone})", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "📍 ${strings.deliveryAddress}: ${order.deliveryAddress}", fontSize = 12.sp, color = Color(0xFF475569))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "🚚 Logistics: ${order.vehicleOrTransportNote}", fontSize = 12.sp, color = AgroGreenPrimary, fontWeight = FontWeight.Medium)
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Action advance buttons
                if (order.status != "Delivered" && order.status != "Cancelled") {
                    val nextAction = when (order.status) {
                        "Placed" -> strings.orderStatusAccepted to "Confirmed"
                        "Confirmed" -> "Pack Produce" to "Packed"
                        "Packed" -> "Dispatch Logistics" to "In Transit"
                        "In Transit" -> strings.orderStatusDelivered to "Delivered"
                        else -> strings.orderStatusDelivered to "Delivered"
                    }

                    Button(
                        onClick = { onAdvanceStatus(nextAction.second) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AgroGreenPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("detail_advance_status_button")
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = nextAction.first, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun StatusTimelineRow(currentStatus: String) {
    val strings = LocalAppStrings.current
    val steps = listOf(
        strings.orderStatusPlaced,
        strings.orderStatusAccepted,
        strings.orderStatusInTransit,
        strings.orderStatusDelivered
    )
    val currentIndex = when (currentStatus) {
        "Placed" -> 0
        "Confirmed", "Packed" -> 1
        "In Transit" -> 2
        "Delivered" -> 3
        else -> 0
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, step ->
            val isDone = index <= currentIndex
            val isCurrent = index == currentIndex

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(if (isDone) AgroGreenPrimary else Color(0xFFE2E8DC))
                ) {
                    if (isDone) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    } else {
                        Text(text = "${index + 1}", fontSize = 11.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = step,
                    fontSize = 10.sp,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                    color = if (isDone) AgroGreenPrimary else Color(0xFF94A3B8)
                )
            }
        }
    }
}

