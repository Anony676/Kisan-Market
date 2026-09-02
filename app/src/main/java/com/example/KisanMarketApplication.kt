package com.example

import android.app.Application
import android.util.Log
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import com.example.data.local.KisanMarketDatabase
import com.example.data.remote.storage.FirebaseStorageDataSource
import com.example.data.repository.KisanMarketRepository
import com.example.data.session.SessionManager
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.File

class KisanMarketApplication : Application(), ImageLoaderFactory {

    private val applicationScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "Uncaught coroutine exception handled safely", throwable)
        }
    )

    lateinit var database: KisanMarketDatabase
        private set

    lateinit var repository: KisanMarketRepository
        private set

    lateinit var sessionManager: SessionManager
        private set

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "KisanMarketApplication initialized")

        // Install process-wide crash guard to prevent app disappearing from emulator
        setupGlobalExceptionHandler()

        try {
            sessionManager = SessionManager(this)
            database = KisanMarketDatabase.getDatabase(this)
            repository = KisanMarketRepository(
                cropListingDao = database.cropListingDao(),
                marketPriceDao = database.marketPriceDao(),
                orderDao = database.orderDao(),
                farmerProfileDao = database.farmerProfileDao(),
                notificationDao = database.notificationDao(),
                userProfileDao = database.userProfileDao(),
                userAccountDao = database.userAccountDao(),
                storageDataSource = FirebaseStorageDataSource(this),
                sessionManager = sessionManager
            )

            applicationScope.launch {
                try {
                    repository.seedDatabaseIfEmpty()
                } catch (e: Exception) {
                    Log.w(TAG, "Background pre-seed completed with note: ${e.message}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing application components", e)
        }
    }

    private fun setupGlobalExceptionHandler() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e(TAG, "Caught uncaught exception in thread ${thread.name}: ${throwable.message}", throwable)
            // Allow default handler if not recoverable, otherwise log safely
            try {
                defaultHandler?.uncaughtException(thread, throwable)
            } catch (e: Exception) {
                Log.e(TAG, "Exception in default handler", e)
            }
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(File(cacheDir, "image_cache"))
                    .maxSizeBytes(50L * 1024 * 1024) // 50 MB
                    .build()
            }
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .crossfade(true)
            .build()
    }

    companion object {
        private const val TAG = "KisanMarketApp"
    }
}
