package com.example.animatedclock


import android.content.pm.PackageManager
import android.location.LocationRequest
import android.os.Bundle
import android.Manifest
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.animatedclock.clock.views.ClockView
import com.example.animatedclock.databinding.ActivityMainBinding
import com.example.animatedclock.databinding.ClockCustomLayoutBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var clockView: ClockView
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val contentBinding=ClockCustomLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clockView = ClockView(this)


        }
    }

