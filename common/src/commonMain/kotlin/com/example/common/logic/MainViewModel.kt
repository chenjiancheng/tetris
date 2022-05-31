package com.example.common.logic


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

open class MainViewModel {// : ViewModel()
    private val _viewState = MutableStateFlow(ViewState())
    val viewState: StateFlow<ViewState> = _viewState
    var receiveChannel: ReceiveChannel<Unit>? = null
    private lateinit var scope: CoroutineScope
    private var soundUtil:SoundUtil?

    constructor(scope: CoroutineScope, soundUtil:SoundUtil?) {
        this.scope = scope
        this.soundUtil = soundUtil
    }

    fun isGameRunning():Boolean{
        return _viewState.value.gameStatus == GameStatus.Running
    }

    fun isGameOver(): Boolean {
        for (x in 0 until boardColumnX){
            if(_viewState.value.board.boardValue[x][0]){
                return true
            }
        }
        return false
    }
    fun setGameStatus(status: GameStatus){
        _viewState.value.gameStatus = status
    }

    fun move(direction: Int, sound: Boolean = true): Boolean {

        val state = _viewState.value.copy()

        if (sound) {
            soundUtil?.play(false, SoundType.Move)
            //开始游戏
            if(!isRunning(state)){
                return false
            }
        }
        if(state.gameStatus != GameStatus.Running){
            return false
        }

        val board = state.board
        var step = Move.DownPair


        when (direction) {
            Move.Down -> step = Move.DownPair
            Move.Left -> step = Move.LeftPair
            Move.Right -> step = Move.RightPair
        }

        val moveSpirit = state.spirit.moveBy(step)
        //检查移动结果
        val moveResult = board.spiritMoveOrRotateResult(moveSpirit, direction)

        when (moveResult) {

            Move.SUCCESS -> {
                state.spirit = moveSpirit
            }
            Move.FAIL_BOTTOM -> {
                board.setBoardValue(state.spirit)
                state.spirit = Spirit(shape = SpiritType[Random.nextInt(7)])
            }
            Move.FAIL_OUT -> {}
            Move.FAIL_WALL -> {}
        }


        emit(state)

        return moveResult == Move.FAIL_BOTTOM

    }

    fun checkMoveResult(){
        scope.launch {
            val result = checkBoard()
            if(result){
                soundUtil?.play(false, SoundType.Clean)
            }
            while (checkBoard()){
                delay(50)
            }
            if(isGameOver()){
                resetGame()
            }
        }
    }

    private fun emit(state: ViewState) {
        _viewState.value = state
    }

    fun checkBoard(): Boolean {
        return _viewState.value.board.clearRow(viewState.value)
    }

    fun resetGame() {

        if (_viewState.value.gameStatus == GameStatus.ScreenClearing) return

        emit(ViewState(gameStatus = GameStatus.ScreenClearing))

        soundUtil?.play(false, SoundType.Start)
//        viewModelScope.launch {
            scope.launch {

            (boardRowY - 1 downTo 0).forEach { y ->
                val state = ViewState(gameStatus = GameStatus.ScreenClearing, board = _viewState.value.board)
                for (x in 0 until boardColumnX) state.board.boardValue[x][y] = true
                emit(state)
                delay(50)
            }
            (0 until boardRowY).forEach { y ->
                val state = ViewState(gameStatus = GameStatus.ScreenClearing, board = _viewState.value.board)
                for (x in 0 until boardColumnX) state.board.boardValue[x][y] = false
                emit(state)
                delay(50)
            }
            delay(300)
            emit(ViewState(gameStatus = GameStatus.Onboard))
        }
    }

    fun rotate() {
        soundUtil?.play(false, SoundType.Rotate)
        val state = _viewState.value.copy()

        if(!isRunning(state)){
            return
        }

        state.spirit = state.spirit.rotate()
        if (state.board.spiritMoveOrRotateResult(state.spirit) == Move.SUCCESS) {
            emit(state)
        }
    }
    private fun isRunning(state: ViewState):Boolean{
        if(state.gameStatus == GameStatus.Onboard){
            state.gameStatus = GameStatus.Running
            emit(state)
            return false
        }

        if(state.gameStatus != GameStatus.Running){
            return false
        }
        return true
    }

    fun drop() {

        val state = _viewState.value.copy()

        soundUtil?.play(false, SoundType.Drop)

        if(!isRunning(state)){
            return
        }

        var dropping = true

        while (dropping) {
            val moveSpirit = state.spirit.moveBy(Move.DownPair)
            val moveResult = state.board.spiritMoveOrRotateResult(moveSpirit)
            if(moveResult == Move.SUCCESS) {
                state.spirit = moveSpirit
            }else{
                state.board.setBoardValue(state.spirit)
                state.spirit = Spirit(shape = SpiritType[Random.nextInt(7)])
                dropping = false
            }
        }
        emit(state)
    }

}

data class ViewState(
    var score: Int = 0,//分数
    var lines: Int = 0,//已经消除多少行
    var level: Int = 1,//等级
    var spirit: Spirit = Spirit(shape = SpiritType[Random.nextInt(7)]),
    var nextSpirit: Spirit = Spirit(shape = SpiritType[Random.nextInt(7)]),
    var board: Board = Board(),//记录棋盘上的现有的方块
    var gameStatus: GameStatus = GameStatus.Onboard,
){
//    var spirit: Spirit = nextSpirit//当前显示的
}