package com.ncueto.weatherapp.domain.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import com.ncueto.weatherapp.domain.model.AppException
import com.ncueto.weatherapp.domain.model.Location
import com.ncueto.weatherapp.domain.model.Result
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationRepositoryImpl(
    private val context: Context
): LocationRepository {

    private val locationManager: LocationManager by lazy {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun isLocationEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(): Flow<Result<Location>> = callbackFlow {
        if (!isLocationEnabled()) {
            trySend(Result.Error(AppException.LocationDisabled))
            close()
            return@callbackFlow
        }

        val locationListener = LocationListener { location ->
            trySend(
                Result.Success(
                    Location(
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                )
            )
        }

        try {
            // Try GPS first, then Network
            val provider = when {
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ->
                    LocationManager.GPS_PROVIDER
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ->
                    LocationManager.NETWORK_PROVIDER
                else -> {
                    trySend(Result.Error(AppException.LocationDisabled))
                    close()
                    return@callbackFlow
                }
            }

            // Get last known location first for faster response
            val lastKnownLocation = locationManager.getLastKnownLocation(provider)
            if (lastKnownLocation != null) {
                trySend(
                    Result.Success(
                        Location(
                            latitude = lastKnownLocation.latitude,
                            longitude = lastKnownLocation.longitude
                        )
                    )
                )
            }

            // Request location updates
            locationManager.requestLocationUpdates(
                provider,
                MIN_TIME_BETWEEN_UPDATES,
                MIN_DISTANCE_CHANGE,
                locationListener
            )
        } catch (e: SecurityException) {
            trySend(Result.Error(AppException.LocationPermissionDenied))
            close()
        } catch (e: Exception) {
            trySend(Result.Error(AppException.LocationUnavailable))
            close()
        }

        awaitClose {
            locationManager.removeUpdates(locationListener)
        }
    }

    companion object {
        private const val MIN_TIME_BETWEEN_UPDATES = 10000L // 10 seconds
        private const val MIN_DISTANCE_CHANGE = 10f // 10 meters
    }
}