package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey val id: String = "KM-" + (10000..99999).random(),
    val listingId: String,
    val cropName: String,
    val cropCategory: String,
    val variety: String,
    val farmerName: String,
    val farmerPhone: String,
    val buyerName: String,
    val buyerPhone: String,
    val deliveryAddress: String,
    val mandiRegion: String,
    val quantity: Double,
    val unit: String,
    val unitPrice: Double,
    val totalProduceAmount: Double,
    val transportCess: Double,
    val totalAmount: Double,
    val status: String, // Placed, Confirmed, Packed, In Transit, Delivered, Cancelled
    val orderDate: String,
    val expectedDeliveryDate: String,
    val paymentMethod: String = "Direct Bank UPI / Escrow",
    val paymentStatus: String = "Escrow Secured",
    val vehicleOrTransportNote: String = "Kisan Direct Logistics",
    val emojiIcon: String = "🌾",
    val timestamp: Long = System.currentTimeMillis()
)
