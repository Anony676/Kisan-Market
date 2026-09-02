package com.example.ui.screens.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Security
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.Order
import com.example.ui.i18n.LocalAppLanguage
import com.example.ui.i18n.LocalAppStrings
import com.example.ui.i18n.localizedCropName
import com.example.ui.i18n.localizedUnit
import com.example.ui.i18n.localizedVariety
import com.example.ui.theme.AgroGreenAccent
import com.example.ui.theme.AgroGreenPrimary
import com.example.ui.theme.PriceUpGreen

private val WhatsAppBrandGreen = Color(0xFF25D366)
private val DarkGreenForest = Color(0xFF14532D)

@Composable
fun OrderSuccessDialog(
    order: Order,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalAppStrings.current
    val lang = LocalAppLanguage.current
    val context = LocalContext.current

    val handleCallFarmer = {
        val cleanPhone = order.farmerPhone.replace(" ", "").trim()
        val dialUri = Uri.parse("tel:$cleanPhone")
        val dialIntent = Intent(Intent.ACTION_DIAL, dialUri)
        try {
            context.startActivity(dialIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open dialer for $cleanPhone", Toast.LENGTH_SHORT).show()
        }
    }

    val handleWhatsAppFarmer = {
        // Clean digits only: e.g. +91 98234 56789 -> 919823456789
        val digitsOnly = order.farmerPhone.filter { it.isDigit() }
        val prefilledText = "Namaste ${order.farmerName} ji! I have placed order #${order.id} on Kisan Market for ${order.quantity.toInt()} ${order.localizedUnit(lang)} of ${order.localizedCropName(lang)} (Total ₹${order.totalAmount.toInt()}). Looking forward to coordinating the harvest dispatch."
        val encodedMessage = Uri.encode(prefilledText)
        val waUrl = "https://wa.me/$digitsOnly?text=$encodedMessage"
        val waIntent = Intent(Intent.ACTION_VIEW, Uri.parse(waUrl)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        try {
            context.startActivity(waIntent)
        } catch (e: Exception) {
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=$digitsOnly&text=$encodedMessage"))
                context.startActivity(browserIntent)
            } catch (ex: Exception) {
                Toast.makeText(context, "Could not open WhatsApp for $digitsOnly", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Dialog(
        onDismissRequest = onDone,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .testTag("order_success_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Dismiss Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = onDone,
                        modifier = Modifier
                            .size(28.dp)
                            .testTag("order_success_close_x_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = strings.close,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Success Animated Badge
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFDCFCE7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = strings.orderPlacedSuccess,
                        tint = PriceUpGreen,
                        modifier = Modifier.size(42.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = strings.orderPlacedSuccess,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DarkGreenForest,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF1F5F9),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = "${strings.orderIdLabel}: #${order.id}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF475569),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Order Details Summary Box
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFF8FAF5),
                    border = BorderStroke(1.dp, Color(0xFFE2E8DC)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = order.emojiIcon,
                                    fontSize = 24.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = order.localizedCropName(lang),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A)
                                    )
                                    Text(
                                        text = "${order.quantity.toInt()} ${order.localizedUnit(lang)} (${order.localizedVariety(lang)})",
                                        fontSize = 12.sp,
                                        color = Color(0xFF64748B)
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "₹${order.totalAmount.toInt()}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = AgroGreenPrimary
                                )
                                Text(
                                    text = order.paymentStatus,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = PriceUpGreen
                                )
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 10.dp),
                            color = Color(0xFFE2E8DC)
                        )

                        // Farmer & Contact Info
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "👨‍🌾", fontSize = 13.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = order.farmerName,
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1E293B)
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "📞 ${order.farmerPhone}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = AgroGreenAccent
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFECFDF5)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Security,
                                        contentDescription = null,
                                        tint = PriceUpGreen,
                                        modifier = Modifier.size(11.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = strings.orderEscrowSecured,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF065F46)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = strings.orderConnectPrompt,
                    fontSize = 12.sp,
                    color = Color(0xFF475569),
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Action Buttons: Call & WhatsApp
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Call Button
                    Button(
                        onClick = handleCallFarmer,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AgroGreenPrimary,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("order_success_call_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = strings.btnCall,
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = strings.btnCall,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // WhatsApp Button
                    Button(
                        onClick = handleWhatsAppFarmer,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = WhatsAppBrandGreen,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("order_success_whatsapp_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Forum,
                            contentDescription = strings.btnWhatsApp,
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = strings.btnWhatsApp,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Done Button
                OutlinedButton(
                    onClick = onDone,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.5.dp, Color(0xFFCBD5E1)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF334155)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("order_success_done_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = strings.done,
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFF334155)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = strings.done,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
