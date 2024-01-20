package com.example.animatedclock.weather.data

import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object WeatherBuilder {

     val retrofit =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://api.open-meteo.com/")
            .build()

    val apiServices:WeatherServices by lazy{ retrofit.create(WeatherServices::class.java)}
}