package com.example.common.logic

import androidx.compose.ui.geometry.Offset
import kotlin.random.Random

/**
 * 7种砖块定义
 */
val SpiritType = listOf(
    listOf(
        Offset(1, -1),
        Offset(1, 0),
        Offset(0, 0),
        Offset(0, 1)
    ),//Z
    listOf(
        Offset(0, -1),
        Offset(0, 0),
        Offset(1, 0),
        Offset(1, 1)
    ),//S
    listOf(
        Offset(0, -1),
        Offset(0, 0),
        Offset(0, 1),
        Offset(0, 2)
    ),//I
    listOf(
        Offset(0, 1),
        Offset(0, 0),
        Offset(0, -1),
        Offset(1, 0)
    ),//T
    listOf(
        Offset(1, 0),
        Offset(0, 0),
        Offset(1, -1),
        Offset(0, -1)
    ),//O
    listOf(
        Offset(0, -1),
        Offset(1, -1),
        Offset(1, 0),
        Offset(1, 1)
    ),//L
    listOf(
        Offset(1, -1),
        Offset(0, -1),
        Offset(0, 0),
        Offset(0, 1)
    )//J
)

/**
 * 图形变换
 */
data class Spirit(val location: Offset = Offset(Random.nextInt(boardColumnX - 2), -2), val shape: List<Offset> = listOf(
    Offset(0, 0)
)) {
    /**
     * 点绕原点(0,0)旋转90°，（x，y）=>（y，-x）
     */
    fun rotate(): Spirit {
        val newShape = shape.toMutableList()

        if (newShape == SpiritType[4]) {//O型不旋转
            return this
        }

        for (i in shape.indices) {
            newShape[i] = Offset(shape[i].y, -shape[i].x)
        }
        return copy(shape = newShape)
    }

    /**
     * 移动，
     */
    fun moveBy(step: Pair<Int, Int>): Spirit = copy(location = location + Offset(
        step.first,
        step.second
    )
    )


}