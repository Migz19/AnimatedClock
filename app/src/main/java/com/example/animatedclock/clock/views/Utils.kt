package com.example.animatedclock.clock.views

import android.os.Build
import java.time.LocalTime
import java.util.Calendar
import java.util.Date

fun getCurrentHour(): Pair<Int,String> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        val currentHour= LocalTime.now().hour
        if (currentHour>12) Pair(currentHour-12,"PM") else if (currentHour==0 ) Pair(12,"AM") else Pair(currentHour,"AM")
    }

    else
    {
        val currentHour=  Calendar.getInstance().get(Calendar.HOUR)
        val amp=Calendar.getInstance().get(Calendar.AM_PM)
        if (amp==Calendar.AM) Pair(currentHour,"AM") else Pair(currentHour,"PM")
    }

}
fun getCurrentMinutes():Int{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        LocalTime.now().minute
    else
        Calendar.getInstance().get(Calendar.MINUTE)
}
fun getCurrentDay(): Int {
    return Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

}