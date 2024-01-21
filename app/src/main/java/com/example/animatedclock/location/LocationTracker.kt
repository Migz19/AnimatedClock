package com.example.animatedclock.location

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationTracker(
    private val locationProviderClient: FusedLocationProviderClient,
) {
    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Location? {
        var location: Location? =null
         suspendCancellableCoroutine { continuation ->
            val lastLocationTask: Task<Location> = locationProviderClient.lastLocation
            lastLocationTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                     location = task.result
                    continuation.resume(location)

                } else {
                    continuation.resume(null)
                }
            }

            lastLocationTask.addOnCanceledListener {
                continuation.cancel()
            }

        }
        return location
    }

}