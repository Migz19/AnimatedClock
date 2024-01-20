package com.example.animatedclock


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.animatedclock.clock.views.ClockView
import com.example.animatedclock.clock.views.ClockViewModel
import com.example.animatedclock.databinding.ActivityMainBinding
import com.example.animatedclock.databinding.ClockCustomLayoutBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var clockView: ClockView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val contentBinding=ClockCustomLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clockView = ClockView(this)
        contentBinding.clockView.setClockViewSize(10,10)


    }


}