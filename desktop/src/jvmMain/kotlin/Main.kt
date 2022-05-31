// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.example.common.compose.Body
import com.example.common.logic.MainViewModel
import com.example.common.logic.Move

import kotlinx.coroutines.*


@OptIn(DelicateCoroutinesApi::class)
fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "俄罗斯方块", icon = painterResource("logo.png"), state = rememberWindowState(width = 480.dp, height = 860.dp)) {
        val viewModel = MainViewModel(GlobalScope, null)
        LaunchedEffect(key1 = Unit) {
            while (isActive) {
                delay(650L - 55 * (viewModel.viewState.value.level - 1))
                if (viewModel.isGameRunning()) {
                    viewModel.move(Move.Down, false)
                    viewModel.checkMoveResult()
                }
            }
        }
        Body(viewModel)
    }
}
