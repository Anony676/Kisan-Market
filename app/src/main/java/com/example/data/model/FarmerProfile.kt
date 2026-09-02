package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "farmer_profiles")
data class FarmerProfile(
    @PrimaryKey val farmerId: String,
    val name: String,
    val phone: String,
    val village: String,
    val district: String,
    val state: String,
    val experienceYears: Int,
    val landSizeAcres: Double,
    val primaryCrops: String,
    val verificationBadge: String, // Kisan Credit Verified, Soil Health Certified, Organic NPOP
    val rating: Double,
    val totalReviews: Int,
    val totalSoldQuintals: Double,
    val onTimeDispatchPercent: Int,
    val activeListingsCount: Int,
    val bio: String,
    val avatarEmoji: String = "👨‍🌾"
)
