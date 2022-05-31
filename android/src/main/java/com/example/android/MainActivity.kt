package com.example.android


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.common.compose.Body
import com.example.common.logic.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ticker


val levelDelayedMap = mapOf<Int, Long>(1 to 400, 2 to 300, 3 to 200)


class MainActivity : ComponentActivity() {

    private var soundUtil:AndroidSoundUtil? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        soundUtil = AndroidSoundUtil(this.applicationContext)
        val viewModel = MainViewModel(lifecycleScope, soundUtil)

        setContent {
            MaterialTheme {
                LaunchedEffect(key1 = Unit) {
                    while (isActive) {
                        delay(levelDelayedMap[viewModel.viewState.value.level] ?: 200)
                        if (viewModel.isGameRunning()) {

                            viewModel.move(Move.Down, false)
                            viewModel.checkMoveResult()
                        }
                    }
                }
                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(key1 = Unit) {
                    val observer = object : DefaultLifecycleObserver {
                        override fun onResume(owner: LifecycleOwner) {
                            viewModel.setGameStatus(GameStatus.Onboard)
                        }

                        override fun onPause(owner: LifecycleOwner) {
                            viewModel.setGameStatus(GameStatus.Paused)
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }

                Body(viewModel)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundUtil?.release()
    }
}

