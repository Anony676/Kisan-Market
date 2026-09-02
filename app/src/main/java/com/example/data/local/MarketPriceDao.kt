package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.MarketPriceUpdate
import kotlinx.coroutines.flow.Flow

@Dao
interface MarketPriceDao {
    @Query("SELECT * FROM market_prices ORDER BY cropName ASC")
    fun getAllPrices(): Flow<List<MarketPriceUpdate>>

    @Query("SELECT * FROM market_prices WHERE mandiRegion = :region")
    fun getPricesByRegion(region: String): Flow<List<MarketPriceUpdate>>

    @Query("SELECT * FROM market_prices WHERE isSubscribedAlert = 1")
    fun getSubscribedAlertPrices(): Flow<List<MarketPriceUpdate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPrices(prices: List<MarketPriceUpdate>)

    @Update
    suspend fun updatePrice(price: MarketPriceUpdate)

    @Query("UPDATE market_prices SET isSubscribedAlert = :isSubscribed WHERE id = :id")
    suspend fun setAlertSubscription(id: String, isSubscribed: Boolean)

    @Query("SELECT COUNT(*) FROM market_prices")
    suspend fun getPricesCount(): Int
}
