package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "market_prices")
data class MarketPriceUpdate(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val cropName: String,
    val variety: String,
    val category: String,
    val mandiRegion: String,
    val state: String,
    val minPrice: Double,
    val maxPrice: Double,
    val modalPrice: Double,
    val unit: String = "Quintal",
    val priceChangePercent: Double, // e.g. +4.2, -1.8
    val trend: String, // UP, DOWN, STABLE
    val lastUpdated: String,
    val arrivalsQuantity: String,
    val emojiIcon: String = "📈",
    val isSubscribedAlert: Boolean = false
)
