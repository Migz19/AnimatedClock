package com.example.animatedclock.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
@ExperimentalCoroutinesApi
class LocationTracker(
    private val locationProviderClient: FusedLocationProviderClient,
    private val application: Application,
) {
    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Location? {

      val isValidPerms:Boolean =checkLocationPermissions()
        if (!isValidPerms)
            return null
        return suspendCancellableCoroutine {
            execute->
            locationProviderClient.lastLocation.apply {
                if (isComplete) {
                    if (isSuccessful)
                        execute.resume(result)
                    else
                        execute.resume(null)
                }else{
                    return@suspendCancellableCoroutine}
                addOnSuccessListener {
                    execute.resume(it)
                }
                addOnFailureListener{
                    execute.resume(null)
                }
                addOnCanceledListener {
                    execute.cancel()
                }

            }
        }
    }
    private fun checkLocationPermissions():Boolean{
        val hasAccessFineLoc = ContextCompat.checkSelfPermission(application, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val hasCoarseFineLoc = ContextCompat.checkSelfPermission(application, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val locationManager=application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpaEnabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!hasAccessFineLoc||hasCoarseFineLoc||isGpaEnabled)
            return false
        return true
    }

}