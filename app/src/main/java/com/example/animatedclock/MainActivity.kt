package com.example.animatedclock


import android.os.Build
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.example.animatedclock.clock.views.ClockView
import com.example.animatedclock.databinding.ActivityMainBinding
import com.example.animatedclock.databinding.ClockCustomLayoutBinding

import java.time.LocalTime
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      binding = ActivityMainBinding.inflate(layoutInflater)
        val contentbinding = ClockCustomLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val currentTime:LocalTime
        val hour :Int
        val minute :Int
        val second:Int
        var clockView:ClockView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
          currentTime=LocalTime.now()
             hour =currentTime.hour
            minute=currentTime.minute
            second=currentTime.second
            clockView=ClockView(this,null,0)
            clockView.startAnimation(10)
        }else{
            hour=Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            minute=Calendar.getInstance().get(Calendar.MINUTE)
            second=Calendar.getInstance().get(Calendar.SECOND)
            clockView=ClockView(this,null,0)
            clockView.startAnimation(20)

        }



    }
}