package com.example.common.logic


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.common.dp2px
import com.example.common.getBrickWidth
import com.example.common.px2dp

fun Offset(x: Int, y: Int) = Offset(x.toFloat(), y.toFloat())
fun Size(width: Int, height: Int) = androidx.compose.ui.geometry.Size(width.toFloat(), height.toFloat())

const val boardColumnX = 10
const val boardRowY = 20
var brickWidth = getBrickWidth()
//棋盘大小
var boardWidthPix = px2dp((boardColumnX * brickWidth))
var boardHeightPix = px2dp((boardRowY * brickWidth))


var brickOffset = dp2px(1f)


var brickWidthFirst = brickWidth - brickOffset * 2
var brickWidthSecond = brickWidthFirst - brickOffset * 2
var brickWidthInner = brickWidthSecond - brickOffset * 2

val brickSize = androidx.compose.ui.geometry.Size(brickWidth, brickWidth)
val brickSizeFirst = androidx.compose.ui.geometry.Size(brickWidthFirst, brickWidthFirst)
val brickSizeSecond = androidx.compose.ui.geometry.Size(brickWidthSecond, brickWidthSecond)
val brickSizeInner = androidx.compose.ui.geometry.Size(brickWidthInner, brickWidthInner)


const val DARK = 0
const val LIGHT = 1
val BrickColor = listOf(
    listOf(Color(0xFF879372), Color(0xFF9ead86), Color(0xFF879372)),//Z
    listOf(Color(0xDD000000), Color(0xFF8a9775), Color(0xDD000000)),
)

/**
 * 画棋盘
 */
fun DrawScope.drawBoard(board: Board) {

    for (y in 0 until boardRowY) {
        for (x in 0 until boardColumnX) {
            drawBrick(Offset(x, y), if (board.boardValue[x][y]) LIGHT else DARK)
        }
    }
}

/**
 * 画俄罗斯方块
 */
fun DrawScope.drawSpirit(spirit: Spirit) {
    spirit.shape.forEach {
        val offset = spirit.location.plus(it)
        if(offset.y >= 0)drawBrick(offset)
    }
}


/**
 * 画单个方块，四个矩形叠加成一个方块
 */

fun DrawScope.drawBrick(location: Offset, arr: Int = LIGHT) {
    drawRect(
        color = BrickColor[arr][1],
        topLeft = location.times(brickWidth),
        size = brickSize,
    )
    drawRect(
        color = BrickColor[arr][0],
        topLeft = location.times(brickWidth).plus(Offset(brickOffset * 1, brickOffset * 1)),
        size = brickSizeFirst,
    )

    drawRect(
        color = BrickColor[arr][1],
        topLeft = location.times(brickWidth).plus(Offset(brickOffset * 2, brickOffset * 2)),
        size = brickSizeSecond
    )

    drawRect(
        color = BrickColor[arr][2],
        topLeft = location.times(brickWidth).plus(Offset(brickOffset * 3, brickOffset * 3)),
        size = brickSizeInner
    )
}

class Move {
    companion object {
        const val Left = 0
        const val Right = 2
        const val Down = 3

        val LeftPair = -1 to 0
        val RightPair = 1 to 0
        val DownPair = 0 to 1

        const val SUCCESS = 1
        const val FAIL_BOTTOM = 2
        const val FAIL_OUT = 3
        const val FAIL_WALL = 4

    }
}
enum class GameStatus {
    Onboard, //游戏欢迎页
    Running, //游戏进行中
    LineClearing,// 消行动画中
    Paused,//游戏暂停
    ScreenClearing, //清屏动画中
    GameOver//游戏结束
}

