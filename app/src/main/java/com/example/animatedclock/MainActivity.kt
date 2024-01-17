package com.example.animatedclock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.animatedclock.clock.views.ClockView

class MainActivity : AppCompatActivity() {
    private lateinit var clockView: ClockView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clockView=findViewById(R.id.clockView)
        var startBtn: Button =findViewById(R.id.btnStart)
        startBtn.setOnClickListener {
            clockView.startAnimation(60)
        }
    }
}