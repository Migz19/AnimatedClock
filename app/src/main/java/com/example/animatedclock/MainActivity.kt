package com.example.animatedclock


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.animatedclock.clock.views.ClockView
import com.example.animatedclock.clock.views.provideFusedLocationClient
import com.example.animatedclock.databinding.ActivityMainBinding
import com.example.animatedclock.databinding.ClockCustomLayoutBinding
import com.example.animatedclock.location.LocationTracker
import com.example.animatedclock.weather.data.WeatherBuilder
import com.example.animatedclock.weather.data.WeatherRepository
import com.example.animatedclock.weather.ui.WeatherViewModel
import com.example.animatedclock.weather.ui.WeatherViewModelFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var clockView: ClockView
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private val repository: WeatherRepository by lazy { WeatherRepository(WeatherBuilder.apiServices) }
    private val locationTracker: LocationTracker by lazy { LocationTracker(provideFusedLocationClient(application)) }
   private val weatherViewModel: WeatherViewModel by viewModels { WeatherViewModelFactory(repository, locationTracker) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val customLayoutBinding=ClockCustomLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkLocationPermissions()
        clockView = customLayoutBinding.clockView
        weatherViewModel.getWeatherInfo().observe(this) {
            Log.d("428932849", "onCreate: ${it.temperature_2m}")
            clockView.weatherData.postValue(it)
            clockView.invalidate()
        }
    }

    private fun checkLocationPermissions(): Boolean {

        val permissionsToRequest = mutableListOf<String>()

        val hasAccessFineLoc =
            ContextCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        if (!hasAccessFineLoc) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        val hasCoarseFineLoc =
            ContextCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        if (!hasCoarseFineLoc) {
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        val locationManager =
            application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled =
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled =
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (!isGpsEnabled || !isNetworkEnabled) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return false
        }

        return true
    }
}

