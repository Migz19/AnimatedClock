package com.example.animatedclock.weather.ui

sealed class WeatherState{
    object fetched:WeatherState()
    object loading:WeatherState()
}
