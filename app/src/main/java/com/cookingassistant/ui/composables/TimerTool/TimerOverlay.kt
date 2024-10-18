package com.cookingassistant.ui.composables.TimerTool

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.livedata.observeAsState

/*
@Composable
fun TimerOverlay() {
    var timeLeft by remember { mutableStateOf(60) }

    LaunchedEffect(key1 = timeLeft) {
        while (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
    }

    Text(text = "Time left: $timeLeft")
}

@Composable
fun CountdownTimerApp(timerViewModel: TimerToolViewModel = viewModel()) {
    val timeLeft:Int by timerViewModel.timeLeft.observeAsState(60)

    CountdownScreen(
        timeLeft = timeLeft,
        onStartClick = { timerViewModel.startCountdown() },
        onResetClick = { timerViewModel.resetCountdown() }
    )
}
*/