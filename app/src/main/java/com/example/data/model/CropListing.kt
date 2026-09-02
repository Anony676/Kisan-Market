package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "crop_listings")
data class CropListing(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val category: String, // Vegetables, Grains, Fruits, Pulses, Spices, Organic
    val variety: String,
    val farmerName: String,
    val farmerId: String,
    val farmerPhone: String,
    val farmLocation: String,
    val mandiRegion: String,
    val pricePerUnit: Double,
    val unit: String, // kg, quintal, crate, bag
    val quantityAvailable: Double,
    val minOrderQty: Double,
    val qualityGrade: String, // Grade A+, Grade A, Organic Certified, Export Quality
    val isOrganic: Boolean,
    val isVerifiedFarmer: Boolean,
    val harvestDate: String,
    val description: String,
    val mandiBenchmarkPrice: Double, // local mandi middleman price for comparison
    val emojiIcon: String = "🌾",
    val distanceKm: Double = 12.0,
    val imageUrl: String = "",
    val imagesJson: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {
    val images: List<String>
        get() {
            val list = mutableListOf<String>()
            if (imagesJson.isNotBlank()) {
                list.addAll(imagesJson.split("|||").filter { it.isNotBlank() })
            } else if (imageUrl.isNotBlank()) {
                list.add(imageUrl)
            }
            return list
        }

    val primaryPhoto: String?
        get() = images.firstOrNull() ?: imageUrl.takeIf { it.isNotBlank() }

    val priceSavingsPercent: Int
        get() {
            if (mandiBenchmarkPrice <= 0) return 0
            val savings = ((mandiBenchmarkPrice * 1.2 - pricePerUnit) / (mandiBenchmarkPrice * 1.2)) * 100
            return savings.toInt().coerceAtLeast(0)
        }
}
