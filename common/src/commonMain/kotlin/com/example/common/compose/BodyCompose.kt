package com.example.common.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.common.theme.BrickSpirit
import com.example.common.theme.ScreenBackground

import com.example.common.logic.*


@Composable
fun Body(viewModel: MainViewModel) {
    val viewStateFlow = viewModel.viewState.collectAsState()
    val viewState = viewStateFlow.value
    Box(
        Modifier
            .background(Color(0xffefcc19))
            .fillMaxSize()
    ) {

        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.padding(10.dp).weight(5f), contentAlignment = Alignment.Center
            ) {

                Row(
                    Modifier
                        .background(ScreenBackground)
                        .border(width = 5.dp, color = Color(0xFFC2A204), shape = RoundedCornerShape(2.dp))
                        .padding(6.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        Modifier
                            .padding(5.dp)
                            .background(BrickSpirit)
                            .padding(0.7.dp)
                    ) {
                        Canvas(
                            modifier = Modifier
                                .background(ScreenBackground)
                                .padding(0.5.dp)
                                .size(boardWidthPix.dp, boardHeightPix.dp)
                        ) {
                            drawBoard(viewState.board)
                            drawSpirit(viewState.spirit)
                        }
                    }
                    Column(
                        Modifier
                            .width((boardWidthPix * 0.7).dp)
                            .height(boardHeightPix.dp)
                            .padding(top = 15.dp, bottom = 15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(text = "Score ${viewState.score}", Modifier.weight(1f), style = MaterialTheme.typography.labelLarge)
                        Text(text = "Lines ${viewState.lines}", Modifier.weight(1f), style = MaterialTheme.typography.labelLarge)
                        Text(text = "Level ${viewState.level}", Modifier.weight(1f), style = MaterialTheme.typography.labelLarge)

                        Spacer(Modifier.weight(1f))
                    }
                }
            }
            GameButtonLayout(modifier = Modifier.weight(2.5f), viewModel = viewModel)
            Spacer(Modifier.weight(0.5f))
        }

    }
}