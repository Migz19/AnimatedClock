package com.example.animatedclock.clock.views

import android.app.Application
import android.os.Build
import com.example.animatedclock.clock.CurrentDay
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

fun getCurrentHour(): Pair<Int, String> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val currentHour = LocalTime.now().hour
        if (currentHour > 12) Pair(currentHour - 12, "PM") else if (currentHour == 0) Pair(12, "AM") else Pair(currentHour, "AM")
    } else {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR)
        val amp = Calendar.getInstance().get(Calendar.AM_PM)
        if (amp == Calendar.AM) Pair(currentHour, "AM") else Pair(currentHour, "PM")
    }

}
fun provideFusedLocationClient(app: Application): FusedLocationProviderClient {
    return  LocationServices.getFusedLocationProviderClient(app)
}
fun getCurrentMinutes(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        LocalTime.now().minute
    else
        Calendar.getInstance().get(Calendar.MINUTE)
}

fun getCurrentDay(): CurrentDay {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        CurrentDay(LocalDate.now().dayOfMonth, LocalDate.now().monthValue, LocalDate.now().year) else
        CurrentDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.YEAR))

}