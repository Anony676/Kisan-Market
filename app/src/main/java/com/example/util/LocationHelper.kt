package com.example.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import com.example.data.model.MandiRegion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

object LocationHelper {

    fun hasLocationPermission(context: Context): Boolean {
        val fineGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fineGranted || coarseGranted
    }

    fun calculateDistanceKm(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Float {
        if (lat1 == 0.0 || lon1 == 0.0 || lat2 == 0.0 || lon2 == 0.0) return Float.MAX_VALUE
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0] / 1000f // in kilometers
    }

    fun findNearestRegion(
        userLat: Double,
        userLng: Double,
        regions: List<MandiRegion>
    ): Pair<MandiRegion, Float>? {
        if (regions.isEmpty()) return null

        var bestRegion: MandiRegion? = null
        var minDistance = Float.MAX_VALUE

        for (region in regions) {
            if (region.latitude != 0.0 && region.longitude != 0.0) {
                val distance = calculateDistanceKm(userLat, userLng, region.latitude, region.longitude)
                if (distance < minDistance) {
                    minDistance = distance
                    bestRegion = region
                }
            }
        }

        return if (bestRegion != null) {
            Pair(bestRegion, minDistance)
        } else {
            Pair(regions.first(), 0f)
        }
    }

    suspend fun reverseGeocode(context: Context, lat: Double, lng: Double): String? =
        withContext(Dispatchers.IO) {
            try {
                if (!Geocoder.isPresent()) return@withContext null
                val geocoder = Geocoder(context, Locale.getDefault())
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(lat, lng, 1)
                if (!addresses.isNullOrEmpty()) {
                    val addr = addresses[0]
                    val city = addr.locality ?: addr.subAdminArea ?: addr.adminArea
                    val state = addr.adminArea
                    return@withContext listOfNotNull(city, state).joinToString(", ")
                }
            } catch (_: Exception) {
                // Return null if network or geocoder fails
            }
            null
        }

    @SuppressLint("MissingPermission")
    fun requestSingleLocation(
        context: Context,
        onLocationFound: (Double, Double) -> Unit,
        onError: (String) -> Unit
    ) {
        if (!hasLocationPermission(context)) {
            onError("Location permission not granted")
            return
        }

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        if (locationManager == null) {
            onError("Location service unavailable")
            return
        }

        // Try getting last known location first from any active provider
        val providers = listOf(
            LocationManager.GPS_PROVIDER,
            LocationManager.NETWORK_PROVIDER,
            LocationManager.PASSIVE_PROVIDER
        )

        var bestLocation: Location? = null
        for (provider in providers) {
            try {
                if (locationManager.isProviderEnabled(provider)) {
                    val loc = locationManager.getLastKnownLocation(provider)
                    if (loc != null) {
                        if (bestLocation == null || loc.accuracy < bestLocation.accuracy) {
                            bestLocation = loc
                        }
                    }
                }
            } catch (_: Exception) {}
        }

        if (bestLocation != null) {
            onLocationFound(bestLocation.latitude, bestLocation.longitude)
            return
        }

        // If last known location is null, register a one-shot listener
        var hasResponded = false
        val handler = Handler(Looper.getMainLooper())

        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                if (!hasResponded) {
                    hasResponded = true
                    try {
                        locationManager.removeUpdates(this)
                    } catch (_: Exception) {}
                    onLocationFound(location.latitude, location.longitude)
                }
            }

            @Deprecated("Deprecated in Java")
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        // Fallback timeout in case GPS doesn't respond in time
        val timeoutRunnable = Runnable {
            if (!hasResponded) {
                hasResponded = true
                try {
                    locationManager.removeUpdates(locationListener)
                } catch (_: Exception) {}
                // If timeout occurs with no GPS, default to center of India / Nashik coordinates
                onLocationFound(19.9975, 73.7898)
            }
        }
        handler.postDelayed(timeoutRunnable, 4000)

        var requested = false
        for (provider in listOf(LocationManager.NETWORK_PROVIDER, LocationManager.GPS_PROVIDER)) {
            try {
                if (locationManager.isProviderEnabled(provider)) {
                    locationManager.requestSingleUpdate(provider, locationListener, Looper.getMainLooper())
                    requested = true
                    break
                }
            } catch (_: Exception) {}
        }

        if (!requested) {
            handler.removeCallbacks(timeoutRunnable)
            // If no provider enabled, fallback to default region coordinates
            onLocationFound(19.9975, 73.7898)
        }
    }
}
