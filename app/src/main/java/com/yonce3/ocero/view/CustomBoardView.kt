package com.yonce3.ocero.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.yonce3.ocero.Board
import com.yonce3.ocero.MainActivity

class CustomBoardView(context: Context, attrs: AttributeSet): View(context, attrs) {
    private var stonePaintBlack: Paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }
    private var stonePaintWhite: Paint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }
    private var stonePaintGray: Paint = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.FILL
    }
    private var strokePaint: Paint = Paint().apply {
        strokeWidth = 10F
        style = Paint.Style.STROKE
    }
    private var cellWidth: Float = 0F
    private var lockRadius: Float = 0F
    private var pointRadius: Float = 0F
    private var board: Board = Board()
    private var white = com.yonce3.ocero.Color.WHITE
    private var black = com.yonce3.ocero.Color.BLACK

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        // マスのオブジェクトを作成
        cellWidth = (width / 8).toFloat()

        // 石の半径
        lockRadius = (width / 8 / 2).toFloat()

        // ポイントの半径
        pointRadius = lockRadius / 2

        board.init(cellWidth)
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var h = 0F
        var w = 0F
        for (i in 0..8) {
            canvas.drawLine(0F, h, width.toFloat(), h, strokePaint)
            h += width / 8
            canvas.drawLine(w, 0F, w, width.toFloat(), strokePaint)
            w += width / 8
        }

        // 石を描画
        board.cellList.map {
            if (it.isPut) {
                canvas.drawCircle(it.centerX!!, it.centerY!!, pointRadius, stonePaintGray)
            }

            if (it.isSet) {
                when (it.color) {
                    black -> {
                        canvas.drawCircle(it.centerX!!, it.centerY!!, lockRadius, stonePaintBlack)
                    }
                    white -> {
                        canvas.drawCircle(it.centerX!!, it.centerY!!, lockRadius, stonePaintWhite)
                    }
                }
            }
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val xPoint = event.x
        val yPoint = event.y
        val tappedCellList = board.cellList.filter { it.h1?.toInt()!! < yPoint
                && it.h2?.toInt()!! > yPoint
                && it.w1?.toInt()!! < xPoint
                && it.w2?.toInt()!! > xPoint
                && !it.isSet
                && it.isPut}

        if (tappedCellList.isNotEmpty()) {
            tappedCellList.first().let {
                board.tapNewCell(it)
            }
        } else {
            return false
        }

        val activity = this.context
        if (activity is MainActivity) {
            val playerText = if (board.player) "White" else "Black"
            activity.mainViewModel.apply {
                this.playerText.postValue(playerText)
                this.whiteCount.postValue((board.cellList.filter { it.color == com.yonce3.ocero.Color.WHITE }.size).toString())
                this.blackCount.postValue((board.cellList.filter { it.color == com.yonce3.ocero.Color.BLACK }.size).toString())
            }
        }

        invalidate()
        return super.onTouchEvent(event)
    }

    fun clearView() {
        // board.reset()のみを実行したい
        board.resetGame(cellWidth)

        // TODO: ViewModel()に移行して、ViewModelから実行？
        val activity = this.context
        if (activity is MainActivity) {
            val playerText = if (board.player) "White" else "Black"
            activity.mainViewModel.apply {
                this.playerText.postValue(playerText)
                this.whiteCount.postValue((board.cellList.filter { it.color == com.yonce3.ocero.Color.WHITE }.size).toString())
                this.blackCount.postValue((board.cellList.filter { it.color == com.yonce3.ocero.Color.BLACK }.size).toString())
            }
        }
        invalidate()
    }
}