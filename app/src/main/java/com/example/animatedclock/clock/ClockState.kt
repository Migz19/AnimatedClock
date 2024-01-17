package com.example.animatedclock.clock

sealed class ClockState{
    object Loading:ClockState()
    object finished :ClockState()
}
