package com.example.animatedclock.weather.data

import com.squareup.moshi.Json

data class WeatherDto(
    @field:Json(name="current")
    val weatherData:WeatherData

)
