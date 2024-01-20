package com.example.animatedclock.weather.data

class WeatherRepository(private val weatherApi: WeatherServices) {

    suspend fun getWeatherData(lat:Double,long:Double):WeatherDto?{
        return  try {
            weatherApi.getWeatherInfo(lat,long)
        }catch (e:Exception){
            e.printStackTrace()
            null
        }
    }
}