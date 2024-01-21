package com.example.animatedclock.weather.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository(private val weatherApi: WeatherServices) {

    suspend fun getWeatherData(lat:Double,long:Double):WeatherDto{
        return withContext(Dispatchers.IO) {
                weatherApi.getWeatherInfo(lat, long)
            }
        }


}