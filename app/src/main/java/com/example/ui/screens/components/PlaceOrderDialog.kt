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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.CropListing
import com.example.ui.i18n.LocalAppLanguage
import com.example.ui.i18n.LocalAppStrings
import com.example.ui.i18n.localizedQualityGrade
import com.example.ui.i18n.localizedTitle
import com.example.ui.i18n.localizedUnit
import com.example.ui.theme.AgroGreenPrimary
import com.example.ui.theme.PriceUpGreen

@Composable
fun PlaceOrderDialog(
    listing: CropListing,
    initialBuyerName: String = "Sunil Deshmukh",
    initialBuyerPhone: String = "+91 98234 56789",
    initialDeliveryAddress: String = "Shop 14, APMC Market Gate 2, Pune",
    onDismiss: () -> Unit,
    onConfirmOrder: (quantity: Double, buyerName: String, buyerPhone: String, deliveryAddress: String, paymentMethod: String) -> Unit
) {
    val strings = LocalAppStrings.current
    val lang = LocalAppLanguage.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val safeDismiss = {
        keyboardController?.hide()
        focusManager.clearFocus()
        onDismiss()
    }

    var quantity by remember { mutableDoubleStateOf(listing.minOrderQty.coerceAtLeast(10.0)) }
    var buyerName by remember { mutableStateOf(initialBuyerName) }
    var buyerPhone by remember { mutableStateOf(initialBuyerPhone) }
    var deliveryAddress by remember { mutableStateOf(initialDeliveryAddress) }
    var selectedPayment by remember { mutableStateOf("Direct UPI / Escrow Guarantee") }

    val produceAmount = listing.pricePerUnit * quantity
    val transportEstimate = (produceAmount * 0.04).coerceAtLeast(120.0)
    val totalAmount = produceAmount + transportEstimate

    Dialog(
        onDismissRequest = safeDismiss,
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
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = listing.emojiIcon,
                            fontSize = 24.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = strings.placeOrderTitle,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "${strings.orderFarmerLabel}: ${listing.farmerName}",
                                fontSize = 12.sp,
                                color = AgroGreenPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    IconButton(
                        onClick = safeDismiss,
                        modifier = Modifier.testTag("close_order_dialog_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = strings.close,
                            tint = Color(0xFF64748B)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Crop Summary Pill
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF8FAF5),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = listing.localizedTitle(lang),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "${strings.directPrice}: ₹${listing.pricePerUnit}/${listing.localizedUnit(lang)} • ${listing.localizedQualityGrade(lang)}",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Quantity Selector Stepper
                Text(
                    text = "${strings.quantityRequired} (${listing.localizedUnit(lang)}):",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF1F5F9),
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                    ) {
                        IconButton(
                            onClick = {
                                val step = if (listing.unit == "kg") 10.0 else 1.0
                                if (quantity - step >= listing.minOrderQty) {
                                    quantity -= step
                                }
                            },
                            modifier = Modifier.testTag("decrease_order_qty_button")
                        ) {
                            Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease")
                        }
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${quantity.toInt()} ${listing.localizedUnit(lang)}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AgroGreenPrimary
                        )
                        Text(
                            text = "${strings.minOrder}: ${listing.minOrderQty.toInt()} ${listing.localizedUnit(lang)} • Max: ${listing.quantityAvailable.toInt()} ${listing.localizedUnit(lang)}",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF1F5F9),
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                    ) {
                        IconButton(
                            onClick = {
                                val step = if (listing.unit == "kg") 10.0 else 1.0
                                if (quantity + step <= listing.quantityAvailable) {
                                    quantity += step
                                }
                            },
                            modifier = Modifier.testTag("increase_order_qty_button")
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Increase")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Buyer Contact & Address
                Text(
                    text = "${strings.buyerName} & ${strings.deliveryAddress}:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = buyerName,
                    onValueChange = { buyerName = it },
                    label = { Text(strings.buyerName) },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AgroGreenPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("order_buyer_name_input")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = buyerPhone,
                    onValueChange = { buyerPhone = it },
                    label = { Text(strings.buyerPhone) },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AgroGreenPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("order_buyer_phone_input")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = deliveryAddress,
                    onValueChange = { deliveryAddress = it },
                    label = { Text(strings.deliveryAddress) },
                    maxLines = 2,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AgroGreenPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("order_delivery_address_input")
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Escrow Guarantee Badge
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFECFDF5),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Safe",
                            tint = PriceUpGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = strings.heroBadge2 + ": " + strings.heroSubtitle,
                            fontSize = 11.sp,
                            color = Color(0xFF065F46),
                            lineHeight = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Price Breakdown
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAF5)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "${strings.subtotal} (${quantity.toInt()} ${listing.localizedUnit(lang)}):", fontSize = 12.sp, color = Color(0xFF64748B))
                            Text(text = "₹${produceAmount.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "${strings.transportFee}:", fontSize = 12.sp, color = Color(0xFF64748B))
                            Text(text = "₹${transportEstimate.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE2E8DC))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "${strings.totalPayable}:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            Text(text = "₹${totalAmount.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = AgroGreenPrimary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Submit Button
                Button(
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        onConfirmOrder(quantity, buyerName, buyerPhone, deliveryAddress, selectedPayment)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AgroGreenPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("confirm_place_order_button")
                ) {
                    Icon(imageVector = Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${strings.btnConfirmOrder} (₹${totalAmount.toInt()})",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
