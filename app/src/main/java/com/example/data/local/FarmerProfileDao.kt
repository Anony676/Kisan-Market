package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.FarmerProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface FarmerProfileDao {
    @Query("SELECT * FROM farmer_profiles")
    fun getAllFarmers(): Flow<List<FarmerProfile>>

    @Query("SELECT * FROM farmer_profiles WHERE farmerId = :farmerId LIMIT 1")
    fun getFarmerById(farmerId: String): Flow<FarmerProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFarmer(farmer: FarmerProfile)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFarmers(farmers: List<FarmerProfile>)

    @Update
    suspend fun updateFarmer(farmer: FarmerProfile)

    @Query("SELECT COUNT(*) FROM farmer_profiles")
    suspend fun getFarmersCount(): Int
}
