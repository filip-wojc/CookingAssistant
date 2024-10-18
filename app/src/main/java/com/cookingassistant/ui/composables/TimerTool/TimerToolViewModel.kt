package com.cookingassistant.ui.composables.TimerTool

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TimerToolViewModel(var time : Int) : ViewModel() {
    // Mutable state to hold the countdown time
    private val _time = MutableLiveData(0)
    var timeLeft : LiveData<Int> = _time // Start with 10 seconds

    // Function to start the countdown
    fun startCountdown() {
        viewModelScope.launch {
            while (_time.value!! > 0) {
                delay(1000) // Wait for 1 second
                _time.value = _time.value!! - 1 // Decrease time by 1 second
            }
        }
    }

    // Function to reset the countdown to 10 seconds
    fun resetCountdown() {
        _time.value = 10
    }
}