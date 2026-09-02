package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey val id: String = "usr_001",
    val name: String = "Your Name",
    val email: String = "yourgmail gmail com",
    val phone: String = "+91 00000 00000",
    val location: String = "Your Location",
    val role: String = "Progressive Farmer & Trader",
    val bio: String = "Agricultural producer & trader. Direct farm gate producer connected with verified Mandi buyers.",
    val avatarUrl: String = "",
    val avatarEmoji: String = "👨‍🌾",
    val kisanId: String = "KISAN-00000",
    val verifiedStatus: String = "KYC Verified",
    val memberSince: String = "August 2026",
    val rating: Double = 5.0,
    val completedDeals: Int = 0,
    val activeListings: Int = 0
)
