package com.example.ui.screens.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import coil.compose.AsyncImage
import com.example.data.model.CropListing
import com.example.ui.i18n.LocalAppLanguage
import com.example.ui.i18n.LocalAppStrings
import com.example.ui.i18n.localizedHarvestDate
import com.example.ui.i18n.localizedTitle
import com.example.ui.i18n.localizedUnit
import com.example.ui.i18n.localizedVariety
import com.example.ui.theme.AgroGreenPrimary
import com.example.ui.theme.PriceUpGreen

@Composable
fun ProduceCard(
    listing: CropListing,
    onOrderClick: () -> Unit,
    onFarmerClick: () -> Unit,
    onPhotoClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val strings = LocalAppStrings.current
    val lang = LocalAppLanguage.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .testTag("produce_card_${listing.id}")
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Main Details Row: Photo/Emoji Icon + Title & Farm Location
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF8FAF5))
                        .then(
                            if (onPhotoClick != null) {
                                Modifier.clickable { onPhotoClick() }
                            } else {
                                Modifier
                            }
                        )
                        .testTag("produce_photo_${listing.id}")
                ) {
                    val photoUrl = ProducePhotoProvider.getPrimaryPhotoForListing(listing)
                    val allPhotos = ProducePhotoProvider.getGalleryPhotosForListing(listing)
                    val photoCount = if (listing.images.isNotEmpty()) listing.images.size else allPhotos.size

                    if (photoUrl.isNotBlank()) {
                        AsyncImage(
                            model = photoUrl,
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
                        Text(
                            text = listing.emojiIcon,
                            fontSize = 32.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = listing.localizedTitle(lang),
                        fontSize = 15.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "${strings.variety}: ${listing.localizedVariety(lang)}",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF64748B)
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Place,
                            contentDescription = "Location",
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${listing.farmLocation} • ${listing.distanceKm} km away",
                            fontSize = 11.5.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Farmer Credit & Phone Contact Row
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF8FAF5),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onFarmerClick() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "👨‍🌾",
                                fontSize = 13.5.sp
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = listing.farmerName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0F172A)
                            )
                            if (listing.isVerifiedFarmer) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = strings.verifiedBadge,
                                    tint = PriceUpGreen,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "Farmer Phone",
                                tint = AgroGreenPrimary,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = listing.farmerPhone,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF1E293B)
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = listing.localizedHarvestDate(lang),
                            fontSize = 10.5.sp,
                            color = Color(0xFF64748B)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${strings.viewProfile} ›",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AgroGreenPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Price and Availability Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "₹${listing.pricePerUnit}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AgroGreenPrimary
                        )
                        Text(
                            text = "/${listing.localizedUnit(lang)}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B),
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                    Text(
                        text = "${strings.availableQty}: ${listing.quantityAvailable.toInt()} ${listing.localizedUnit(lang)} (${strings.minOrder}: ${listing.minOrderQty.toInt()})",
                        fontSize = 10.5.sp,
                        color = Color(0xFF64748B)
                    )
                }

                // CTA Buttons
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onOrderClick,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AgroGreenPrimary),
                        modifier = Modifier
                            .height(38.dp)
                            .testTag("buy_now_button_${listing.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = strings.buyDirect,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = strings.buyDirect,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
