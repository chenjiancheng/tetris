package com.example.common.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.common.logic.MainViewModel
import com.example.common.logic.Move
import com.example.common.theme.Purple200
import com.example.common.theme.Purple500
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow


/**
 * https://juejin.cn/post/6979777894104956935
 */
@OptIn(ExperimentalComposeUiApi::class, ObsoleteCoroutinesApi::class, InternalCoroutinesApi::class)
@Composable
fun GameButton(
    text: String,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    size: Dp = 70.dp,
    autoInvokeWhenPressed: Boolean = false,
    onClick: () -> Unit = {},
) {

    val backgroundShape = RoundedCornerShape(size / 2)
    val coroutineScope = rememberCoroutineScope()
    val pressedInteraction = remember { mutableStateOf<PressInteraction.Press?>(null) }
    val interactionSource = MutableInteractionSource()

    val isPressed by interactionSource.collectIsPressedAsState()
    val interactions = remember { mutableStateListOf<Interaction>() }

    Button(
        modifier = modifier.shadow(5.dp, shape = backgroundShape)
            .size(size = size)
            .clip(backgroundShape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Purple200,
                        Purple500
                    ),
                    startY = 0f,
                    endY = 80f
                )
            ).indication(interactionSource = interactionSource, indication = rememberRipple()).run {
                clickable { onClick() }
//            if (autoInvokeWhenPressed) {
//                pointerInteropFilter {
//                    when (it.action) {
//                        MotionEvent.ACTION_DOWN -> {
//
//                            viewModel.ticker = ticker(initialDelayMillis = 300, delayMillis = 60)
//                            coroutineScope.launch {
//                                viewModel.ticker!!
//                                    .receiveAsFlow()
//                                    .collect { onClick() }
//                            }
//                        }
//                        MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
//                            viewModel.ticker?.cancel()
//                            onClick()
//                        }
//                        else -> {
//                            if (it.action != MotionEvent.ACTION_MOVE) {
//                                viewModel.ticker?.cancel()
//                                onClick()
//                            }
//                            return@pointerInteropFilter false
//                        }
//                    }
//                    true
//                }
//            } else {
//                clickable { onClick() }
//            }
            }, onClick = onClick
    ) {//
        Text(text = text)
    }
}

@Composable
fun GameButtonLayout(modifier: Modifier = Modifier, viewModel: MainViewModel) {

    Column(
        modifier = modifier
            .padding(10.dp), horizontalAlignment = Alignment.End
    ) {
        val height = 180.dp
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround
        ) {
            Spacer(modifier = Modifier.weight(4f))
            Box(
                modifier = Modifier
                    .height(30.dp)
                    .weight(2f)
            ) {

                Button(onClick = { viewModel.resetGame() }, modifier = Modifier.align(Alignment.Center)) {
                    androidx.compose.material3.Text(
                        text = "重置",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }

        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround
        ) {
            Box(Modifier.height(height).weight(4f)) {

                Box(modifier = Modifier.size(height).align(Alignment.Center)) {
                    GameButton("Drop", modifier = Modifier.align(Alignment.TopCenter), viewModel) {
                        viewModel.drop()
                    }

                    GameButton(
                        "左",
                        modifier = Modifier.align(Alignment.CenterStart),
                        autoInvokeWhenPressed = true,
                        viewModel = viewModel
                    ) {
                        viewModel.move(Move.Left)
                    }

                    GameButton(
                        "右",
                        modifier = Modifier.align(Alignment.CenterEnd),
                        autoInvokeWhenPressed = true,
                        viewModel = viewModel
                    ) {
                        viewModel.move(Move.Right)
                    }

                    GameButton(
                        "下",
                        modifier = Modifier.align(Alignment.BottomCenter),
                        autoInvokeWhenPressed = true,
                        viewModel = viewModel
                    ) {
                        viewModel.move(Move.Down)
                    }
                }
            }

            Box(Modifier.height(height).weight(2f)) {
                GameButton(
                    "旋转",
                    modifier = Modifier.align(Alignment.Center),
                    size = 100.dp,
                    viewModel = viewModel
                ) {
                    viewModel.rotate()
                }
            }

        }
    }
}