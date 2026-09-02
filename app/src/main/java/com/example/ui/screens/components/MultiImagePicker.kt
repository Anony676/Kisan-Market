package com.example.ui.screens.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.AgroGreenAccent
import com.example.ui.theme.AgroGreenPrimary
import com.example.ui.theme.HarvestAmber

/**
 * MultiImagePicker Component for 'Sell Produce' screen.
 * Allows uploading and displaying up to 3 product images in a responsive grid
 * with individual thumbnail removal controls and cover photo indicators.
 */
@Composable
fun MultiImagePicker(
    imageUris: List<Uri>,
    onImagesSelected: (List<Uri>) -> Unit,
    onRemoveImage: (Int) -> Unit,
    maxImages: Int = 3,
    modifier: Modifier = Modifier
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(
            maxItems = (maxImages - imageUris.size).coerceAtLeast(1)
        )
    ) { uris ->
        if (uris.isNotEmpty()) {
            val updated = (imageUris + uris).distinct().take(maxImages)
            onImagesSelected(updated)
        }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Collections,
                        contentDescription = null,
                        tint = AgroGreenPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Produce Photos",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (imageUris.size == maxImages) Color(0xFFDCFCE7) else Color(0xFFF1F5F9)
                ) {
                    Text(
                        text = "${imageUris.size}/$maxImages Photos",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (imageUris.size == maxImages) AgroGreenAccent else Color(0xFF64748B),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Upload clear farm gate photos to attract verified wholesale buyers faster.",
                fontSize = 11.5.sp,
                color = Color(0xFF64748B),
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 3-Column Grid Layout for Images & Upload Slot
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (index in 0 until maxImages) {
                    val uri = imageUris.getOrNull(index)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                    ) {
                        if (uri != null) {
                            // Selected Image Preview Card
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        BorderStroke(
                                            1.dp,
                                            if (index == 0) AgroGreenPrimary else Color(0xFFE2E8F0)
                                        ),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .testTag("produce_image_thumbnail_$index")
                            ) {
                                AsyncImage(
                                    model = uri,
                                    contentDescription = "Produce image ${index + 1}",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )

                                // Main/Cover Badge on 1st Photo
                                if (index == 0) {
                                    Surface(
                                        shape = RoundedCornerShape(topStart = 0.dp, bottomEnd = 8.dp),
                                        color = AgroGreenPrimary.copy(alpha = 0.92f),
                                        modifier = Modifier.align(Alignment.TopStart)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = null,
                                                tint = HarvestAmber,
                                                modifier = Modifier.size(10.dp)
                                            )
                                            Spacer(modifier = Modifier.width(2.dp))
                                            Text(
                                                text = "Cover",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }

                                // Remove Button (Top Right)
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFF1E293B).copy(alpha = 0.82f),
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .clickable { onRemoveImage(index) }
                                        .shadow(elevation = 2.dp, shape = CircleShape)
                                        .testTag("remove_produce_image_$index")
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remove image ${index + 1}",
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        } else if (index == imageUris.size) {
                            // Active "Add Photo" Button Slot
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.5.dp, AgroGreenPrimary.copy(alpha = 0.6f)),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        photoPickerLauncher.launch(
                                            PickVisualMediaRequest(
                                                ActivityResultContracts.PickVisualMedia.ImageOnly
                                            )
                                        )
                                    }
                                    .testTag("add_produce_image_button")
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(6.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = Color(0xFFDCFCE7),
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Default.AddPhotoAlternate,
                                                contentDescription = "Add Produce Photo",
                                                tint = AgroGreenPrimary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "+ Add",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AgroGreenPrimary
                                    )
                                    Text(
                                        text = "Photo ${index + 1}",
                                        fontSize = 9.sp,
                                        color = Color(0xFF64748B)
                                    )
                                }
                            }
                        } else {
                            // Empty Inactive Slot
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFF1F5F9).copy(alpha = 0.6f),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(12.dp))
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(6.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AddPhotoAlternate,
                                        contentDescription = null,
                                        tint = Color(0xFFCBD5E1),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Slot ${index + 1}",
                                        fontSize = 9.5.sp,
                                        color = Color(0xFF94A3B8)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

