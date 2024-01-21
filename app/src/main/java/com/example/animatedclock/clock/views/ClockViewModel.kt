package com.example.animatedclock.clock.views


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.animatedclock.clock.CurrentDay
import com.example.animatedclock.weather.data.WeatherData
import java.util.*

class ClockViewModel : ViewModel() {

    private val minutesValue = MutableLiveData<Int>()
    private val hoursValue = MutableLiveData<Pair<Int, String>>()
    private val dayValue = MutableLiveData<CurrentDay>()

    val weatherData = MutableLiveData<WeatherData>()

    fun observeMinutesValue(): LiveData<Int> {
        minutesValue.value = getCurrentMinutes()
        return minutesValue
    }

    fun observeHoursValue(): LiveData<Pair<Int, String>> {
        hoursValue.value = getCurrentHour()
        return hoursValue
    }

    fun observeCurrentDay(): MutableLiveData<CurrentDay> {
        dayValue.value = getCurrentDay()
        return dayValue
    }

    private val timer: Timer = Timer()
    init {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                minutesValue.postValue(getCurrentMinutes())
                hoursValue.postValue(getCurrentHour())
            }
        }, 0, 1000)

    }


}