package com.example.common.logic

import androidx.compose.ui.geometry.Offset


data class Board(var boardValue: Array<BooleanArray> = Array(boardColumnX) { BooleanArray(boardRowY) }) {

    fun setBoardValue(spirit: Spirit) {
        //把各个方块的值设置到棋盘上
        spirit.shape.forEach {
            val offset = spirit.location.plus(it)
            val x = offset.x.toInt()
            val y = offset.y.toInt()
            //不在棋盘内或者是棋盘上的位置已经有方块
            if(x >= 0 && y >= 0 && isInBoardExceptBottom(offset)) {
                boardValue[x][y] = true
            }
        }
    }

    /**
     * 验证 移动 或 旋转  后的方块是否正常
     */
    fun spiritMoveOrRotateResult(spirit: Spirit, direction:Int = Move.Down): Int {
        spirit.shape.forEach {
            val offset = spirit.location.plus(it)
            val x = offset.x.toInt()
            val y = offset.y.toInt()
            //不在棋盘内或者是棋盘上的位置已经有方块
            if (!isInBoardExceptBottom(offset)) {
                return Move.FAIL_OUT
            }

            if(y >= boardRowY){
                return Move.FAIL_BOTTOM
            }

            if(x >=0 && y >= 0 && boardValue[x][y]){// || y == boardRow - 1
                return if(direction == Move.Down) Move.FAIL_BOTTOM else Move.FAIL_WALL
            }
        }
        return Move.SUCCESS
    }

    /**
     * 单个方块是否在棋盘内
     */
    private fun isInBoardExceptBottom(offset: Offset): Boolean {
        if (offset.x.toInt() in 0 until boardColumnX) {// && offset.y.toInt() >= 0
            return true
        }
        return false
    }


    fun clearRow(viewState: ViewState):Boolean {
        //看那一行已经铺满方块了
        var fillY = -1
        for (y in boardRowY - 1 downTo 0) {
            var isFull = true
            for (x in 0 until boardColumnX) {
                isFull = boardValue[x][y] and isFull
            }
            if (isFull) fillY = y
        }


        if(fillY > -1){
            viewState.score = viewState.score + viewState.level * 10
            viewState.lines += 1
            if(viewState.score >= viewState.level * 120){
                viewState.level += 1
            }
        }

        //数据往下移动，相当于清除方块
        for (y in fillY downTo 1) {
            for (x in 0 until boardColumnX) {
                boardValue[x][y] = boardValue[x][y - 1]
            }
        }
        return fillY > -1
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Board

        if (!boardValue.contentDeepEquals(other.boardValue)) return false

        return true
    }

    override fun hashCode(): Int {
        return boardValue.contentDeepHashCode()
    }
    
    //    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (javaClass != other?.javaClass) return false
//
//        other as Board
//
//        if (!boardValue.contentDeepEquals(other.boardValue)) return false
//
//        return true
//    }
//
//    override fun hashCode(): Int {
//        return boardValue.contentDeepHashCode()
//    }
}