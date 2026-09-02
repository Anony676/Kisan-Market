package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CropListing
import kotlinx.coroutines.flow.Flow

@Dao
interface CropListingDao {
    @Query("SELECT * FROM crop_listings ORDER BY timestamp DESC")
    fun getAllListings(): Flow<List<CropListing>>

    @Query("SELECT * FROM crop_listings WHERE category = :category ORDER BY timestamp DESC")
    fun getListingsByCategory(category: String): Flow<List<CropListing>>

    @Query("SELECT * FROM crop_listings WHERE mandiRegion = :region ORDER BY timestamp DESC")
    fun getListingsByRegion(region: String): Flow<List<CropListing>>

    @Query("SELECT * FROM crop_listings WHERE farmerId = :farmerId ORDER BY timestamp DESC")
    fun getListingsByFarmer(farmerId: String): Flow<List<CropListing>>

    @Query("SELECT * FROM crop_listings WHERE id = :id")
    suspend fun getListingById(id: String): CropListing?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListing(listing: CropListing)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllListings(listings: List<CropListing>)

    @Update
    suspend fun updateListing(listing: CropListing)

    @Delete
    suspend fun deleteListing(listing: CropListing)

    @Query("DELETE FROM crop_listings WHERE id LIKE 'crop_%'")
    suspend fun deleteDefaultListings()

    @Query("DELETE FROM crop_listings")
    suspend fun deleteAllListings()

    @Query("SELECT COUNT(*) FROM crop_listings")
    suspend fun getListingsCount(): Int
}
