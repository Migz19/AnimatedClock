package com.example.animatedclock.weather.data

import com.squareup.moshi.Json
import java.time.LocalDateTime

data class WeatherData(
        @field:Json(name = "temperature_2m")
        val temperature_2m: Double,
        @field:Json(name = "interval")
        val interval: Double,
        @field:Json(name = "time")
        val time: String,
)
