package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "notifications")
data class AppNotification(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val message: String,
    val type: String, // ORDER, PRICE_ALERT, MARKET_DISPATCH, BUYER_INQUIRY
    val referenceId: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val timeFormatted: String = "Just now",
    val isRead: Boolean = false,
    val emojiIcon: String = "🔔"
)
