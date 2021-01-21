package com.yonce3.ocero.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import com.yonce3.ocero.Cell

class CustomBoardView(context: Context, attrs: AttributeSet): View(context, attrs) {
    private var cellList = arrayListOf<Cell>()
    private var paintBlack: Paint
    private var paintWhite: Paint
    private var strokePaint: Paint
    private var switch: Boolean = true
    private var cellWidth: Float = 0F
    private var lockRadius: Float = 0F
    private var pointRadius: Float = 0F

    init {
        // 黒の石のペイント
        paintBlack = Paint().apply {
            this.color = Color.BLACK
            this.style = Paint.Style.FILL
        }

        // 白の石のペイント
        paintWhite = Paint().apply {
            this.color = Color.WHITE
            this.style = Paint.Style.FILL
        }

        strokePaint = Paint().apply {
            this.strokeWidth = 10F
            this.style = Paint.Style.STROKE
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        // マスのオブジェクトを作成
        cellWidth = (width / 8).toFloat()

        // 石の半径
        lockRadius = (width / 8 / 2).toFloat()

        // ポイントの半径
        pointRadius = lockRadius / 2

        setInit(cellWidth)

        val whiteList = cellList.filter { it.color == com.yonce3.ocero.Color.WHITE }
        whiteList.map {cell ->
            checkLeft(cell)

            checkRight(cell)
        }
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
        cellList.map {
            if (it.isPut) {
                canvas.drawCircle(it.centerX!!, it.centerY!!, pointRadius, paintBlack)
            }

            if (it.isSet) {
                when (it.color) {
                    com.yonce3.ocero.Color.BLACK -> {
                        canvas.drawCircle(it.centerX!!, it.centerY!!, lockRadius, paintBlack)
                    }
                    com.yonce3.ocero.Color.WHITE -> {
                        canvas.drawCircle(it.centerX!!, it.centerY!!, lockRadius, paintWhite)
                        canvas.drawText(it.x.toString(), it.centerX!!, it.centerY, paintBlack)
                    }
                }
            }
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val xPoint = event.x
        val yPoint = event.y
        val tappedCell = cellList.filter { it.h1?.toInt()!! < yPoint
                && it.h2?.toInt()!! > yPoint
                && it.w1?.toInt()!! < xPoint
                && it.w2?.toInt()!! > xPoint
                && !it.isSet
                && it.isPut}

        if (tappedCell.isNotEmpty()) {
            tappedCell.first().apply {
                isSet = true
                if (switch) {
                    color = com.yonce3.ocero.Color.WHITE
                    switch = false
                    checkReverse(this)
                } else {
                    color = com.yonce3.ocero.Color.BLACK
                    switch = true
                    checkReverse(this)
                }
            }
        }

        checkPutAbleArea()
        invalidate()
        return super.onTouchEvent(event)
    }

    fun clearView() {
        cellList.clear()
        setInit(cellWidth)
        invalidate()
    }

    private fun setInit(cellWidth: Float) {
        var w1 = 0F
        var h1 = 0F
        var w2 = cellWidth
        var h2 = cellWidth

        for(x in 1..8) {
            for (y in 1..8) {
                cellList.add(Cell(x, y, w1, h1, w2, h2, (w1 + w2)/2, (h1 + h2)/2, false))
                h1 += cellWidth
                h2 += cellWidth
            }
            h1 = 0F
            h2 = cellWidth
            w1 += cellWidth
            w2 += cellWidth
        }

        // 初期石を作成
        cellList.filter { it.x == 4 && it.y == 4 || it.x == 5 && it.y == 5}.map {
            it.isSet = true
            it.color = com.yonce3.ocero.Color.WHITE
        }
        cellList.filter { it.x == 4 && it.y == 5 || it.x == 5 && it.y == 4 }.map {
            it.isSet = true
            it.color = com.yonce3.ocero.Color.BLACK
        }
    }

    private fun checkLeft(cell: Cell) {
        var selfIndex = (cell.x - 1) * 8 + cell.y - 1
        var leftIndex = if (cell.x == 1) cell.y else selfIndex - 8
        if (cellList[leftIndex].color == com.yonce3.ocero.Color.BLACK) {
            cellList[leftIndex - 8].isPut = true
        }
    }

    private fun checkRight(cell: Cell) {
        var selfIndex = (cell.x - 1) * 8 + cell.y - 1
        var rightIndex = if (cell.x == 1) cell.y else selfIndex + 8
        if (switch && cellList[rightIndex].color == com.yonce3.ocero.Color.BLACK) {
            cellList[rightIndex + 8].isPut = true
        } else if (!switch && cellList[rightIndex].color == com.yonce3.ocero.Color.WHITE) {
            cellList[rightIndex + 8].isPut = true
        }
    }

    private fun checkReverse(addedCell: Cell) {
        var selfIndex = (addedCell.x - 1) * 8 + addedCell.y - 1
        var rightIndex = if (addedCell.x == 1) addedCell.y else selfIndex + 8
        if (cellList[rightIndex].color == com.yonce3.ocero.Color.BLACK) {
            if (cellList[rightIndex + 8].color == com.yonce3.ocero.Color.WHITE) {
                cellList[rightIndex].color = com.yonce3.ocero.Color.WHITE
            }
        }
    }

    private fun checkPutAbleArea() {

        // タップ可能判定をリセット
        cellList.map {
            it.isPut = false
        }

        if (switch) {

        } else {

        }
    }
}