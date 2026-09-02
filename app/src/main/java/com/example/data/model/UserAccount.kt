package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data entity representing a registered user account on Kisan Market.
 * Persisted in local Room database and synced with Firebase Firestore.
 */
@Entity(tableName = "user_accounts")
data class UserAccount(
    @PrimaryKey
    val id: String = "usr_${System.currentTimeMillis()}",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val normalizedPhone: String = "", // Clean digits only for infallible matching
    val passwordHash: String = "",    // Registered account password
    val role: String = "Farmer / Producer",
    val location: String = "",
    val kisanId: String = "KISAN-00000",
    val avatarEmoji: String = "👨‍🌾",
    val verifiedStatus: String = "KYC Verified",
    val memberSince: String = "August 2026",
    val rating: Double = 5.0,
    val completedDeals: Int = 0,
    val activeListings: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
