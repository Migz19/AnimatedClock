package com.example.animatedclock.weather.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.animatedclock.location.LocationTracker
import com.example.animatedclock.weather.data.WeatherData
import com.example.animatedclock.weather.data.WeatherRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class WeatherViewModelFactory(private val weatherRepository: WeatherRepository, private val locationTracker: LocationTracker) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(weatherRepository, locationTracker) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class WeatherViewModel constructor(
    private val weatherRepository: WeatherRepository, private val locationTracker: LocationTracker,
) : ViewModel() {
    val weatherDataValue = MutableLiveData<WeatherData>()

     fun getWeatherInfo(): MutableLiveData<WeatherData> {
       viewModelScope.launch {
            locationTracker.getCurrentLocation()?.let { location ->
               val result = weatherRepository.getWeatherData(location.latitude, location.longitude)
                Log.d("3498342989342", "getWeatherInfo: ")
                weatherDataValue.postValue( result.weatherData)
            }

        }
        return weatherDataValue
    }

}
