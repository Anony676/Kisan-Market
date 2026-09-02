package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.AppNotification
import com.example.data.model.CropListing
import com.example.data.model.FarmerProfile
import com.example.data.model.MarketPriceUpdate
import com.example.data.model.Order
import com.example.data.model.UserAccount
import com.example.data.model.UserProfile

@Database(
    entities = [
        CropListing::class,
        MarketPriceUpdate::class,
        Order::class,
        FarmerProfile::class,
        AppNotification::class,
        UserProfile::class,
        UserAccount::class
    ],
    version = 4,
    exportSchema = false
)
abstract class KisanMarketDatabase : RoomDatabase() {
    abstract fun cropListingDao(): CropListingDao
    abstract fun marketPriceDao(): MarketPriceDao
    abstract fun orderDao(): OrderDao
    abstract fun farmerProfileDao(): FarmerProfileDao
    abstract fun notificationDao(): NotificationDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun userAccountDao(): UserAccountDao

    companion object {
        @Volatile
        private var INSTANCE: KisanMarketDatabase? = null

        fun getDatabase(context: Context): KisanMarketDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KisanMarketDatabase::class.java,
                    "kisan_market_db"
                ).fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
