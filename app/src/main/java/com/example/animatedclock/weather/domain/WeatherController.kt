package com.example.animatedclock.weather.domain

import android.os.Build
import com.example.animatedclock.weather.data.WeatherData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WeatherController {
    fun getCurrentDayInfo(weatherData: WeatherData){
        val today= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.parse(weatherData.time, DateTimeFormatter.ISO_DATE_TIME)
        } else {

        }
        val temp=weatherData.temperature_2m
        val interval =weatherData.interval
    }
}