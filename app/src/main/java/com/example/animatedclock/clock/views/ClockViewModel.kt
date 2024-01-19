package com.example.animatedclock.clock.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class ClockViewModel:ViewModel() {

    private val minutesValue = MutableLiveData<Int>()
    private val hoursValue=MutableLiveData<Pair<Int,String>>()
    fun observeMinutesValue(): LiveData<Int> {
        minutesValue.value = getCurrentMinutes()
        return  minutesValue
    }
    fun observeHoursValue():LiveData<Pair<Int,String>>{
        hoursValue.value= getCurrentHour()
        return hoursValue
    }
    fun observeCurrentDay(){
        getCurrentDay()
    }

    private val timer: Timer = Timer()

    init {

        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {

                minutesValue.postValue(getCurrentMinutes())
            }
        }, 0, 1000)
    }

}