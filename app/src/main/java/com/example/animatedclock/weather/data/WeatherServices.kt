package com.example.animatedclock.weather.data

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServices {
    @GET("v1/forecast?current=temperature_2m&hourly=temperature_2m&temperature_unit=celsius")
    suspend fun getWeatherInfo(
        @Query("latitude")lat:Double,
        @Query("longitude")long:Double
    ):WeatherDto
}