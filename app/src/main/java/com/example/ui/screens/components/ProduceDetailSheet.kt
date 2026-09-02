package com.example.ui.screens.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.VerifiedUser
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.data.model.CropListing
import com.example.ui.i18n.LocalAppLanguage
import com.example.ui.i18n.LocalAppStrings
import com.example.ui.i18n.localizedCategory
import com.example.ui.i18n.localizedHarvestDate
import com.example.ui.i18n.localizedMandi
import com.example.ui.i18n.localizedQualityGrade
import com.example.ui.i18n.localizedTitle
import com.example.ui.i18n.localizedUnit
import com.example.ui.i18n.localizedVariety
import com.example.ui.theme.AgroGreenAccent
import com.example.ui.theme.AgroGreenPrimary
import com.example.ui.theme.HarvestAmber
import com.example.ui.theme.OrganicBadge
import com.example.ui.theme.PriceUpGreen
import com.example.ui.util.parseHtmlToAnnotatedString

@Composable
fun ProduceDetailSheet(
    listing: CropListing,
    onDismiss: () -> Unit,
    onBuyDirectClick: () -> Unit,
    onViewFarmerClick: () -> Unit
) {
    val strings = LocalAppStrings.current
    val lang = LocalAppLanguage.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
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
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFDCFCE7)
                        ) {
                            Text(
                                text = strings.directFarmHarvest,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF15803D),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        if (listing.priceSavingsPercent > 0) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFFEF3C7)
                            ) {
                                Text(
                                    text = "Save ${listing.priceSavingsPercent}% vs Retail",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB45309),
                                    letterSpacing = 0.2.sp,
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_produce_detail_button")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Hero Icon & Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val primaryPhoto = ProducePhotoProvider.getPrimaryPhotoForListing(listing)
                    val allPhotos = ProducePhotoProvider.getGalleryPhotosForListing(listing)
                    val photoCount = if (listing.images.isNotEmpty()) listing.images.size else allPhotos.size

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFF1F5F9))
                    ) {
                        if (primaryPhoto.isNotBlank()) {
                            AsyncImage(
                                model = primaryPhoto,
                                contentDescription = listing.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            if (photoCount > 1) {
                                Surface(
                                    shape = RoundedCornerShape(topStart = 6.dp),
                                    color = Color.Black.copy(alpha = 0.65f),
                                    modifier = Modifier.align(Alignment.BottomEnd)
                                ) {
                                    Text(
                                        text = "📷 $photoCount",
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        } else {
                            Text(text = listing.emojiIcon, fontSize = 36.sp)
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = listing.localizedTitle(lang),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0F172A),
                            lineHeight = 22.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${strings.variety}: ${listing.localizedVariety(lang)} • ${listing.localizedCategory(lang)}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Key Specs Grid Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SpecCard(
                        title = strings.directRate,
                        value = "₹${listing.pricePerUnit}/${listing.localizedUnit(lang)}",
                        subtitle = "vs ₹${listing.mandiBenchmarkPrice.toInt()} Mandi",
                        accentColor = AgroGreenPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    SpecCard(
                        title = strings.availableStock,
                        value = "${listing.quantityAvailable.toInt()} ${listing.localizedUnit(lang)}",
                        subtitle = "Min: ${listing.minOrderQty.toInt()} ${listing.localizedUnit(lang)}",
                        accentColor = HarvestAmber,
                        modifier = Modifier.weight(1f)
                    )
                    SpecCard(
                        title = strings.qualityGradeLabel,
                        value = listing.localizedQualityGrade(lang).split(" ").firstOrNull() ?: "Grade A",
                        subtitle = if (listing.isOrganic) strings.organicBadge else "Standard",
                        accentColor = if (listing.isOrganic) OrganicBadge else Color(0xFF1D4ED8),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Description & Harvest notes
                Text(
                    text = strings.produceDescCondition,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF8FAFC),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = parseHtmlToAnnotatedString(listing.description),
                        fontSize = 13.sp,
                        color = Color(0xFF334155),
                        lineHeight = 19.sp,
                        softWrap = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Farmer Profile Card
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF8FAF5),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onViewFarmerClick() }
                        .padding(12.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "👨‍🌾", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = listing.farmerName,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0F172A)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = strings.verifiedBadge,
                                            tint = PriceUpGreen,
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }
                                    Text(
                                        text = "${listing.farmLocation} • ${listing.localizedMandi(lang)}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF64748B)
                                    )
                                }
                            }

                            Text(
                                text = "${strings.viewBio} ›",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AgroGreenAccent
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "📞 ${listing.farmerPhone}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF1E293B)
                            )
                            Text(
                                text = "🌾 ${listing.localizedHarvestDate(lang)}",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Bottom Action CTA
                Button(
                    onClick = onBuyDirectClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AgroGreenPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("detail_sheet_buy_now_button")
                ) {
                    Icon(imageVector = Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${strings.btnConfirmOrder} (₹${listing.pricePerUnit}/${listing.localizedUnit(lang)})",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun SpecCard(
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
            Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = accentColor, maxLines = 1)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, fontSize = 9.sp, color = Color(0xFF94A3B8), maxLines = 1)
        }
    }
}
