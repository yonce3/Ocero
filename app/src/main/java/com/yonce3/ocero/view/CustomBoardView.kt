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
        setInit(cellWidth)
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

        var radius = (width / 8 / 2).toFloat()
        // 石を描画
        cellList.map {
            if (it.isSet) {
                when (it.color) {
                    com.yonce3.ocero.Color.BLACK -> {
                        canvas.drawCircle(it.centerX!!, it.centerY!!, radius, paintBlack)
                    }
                    com.yonce3.ocero.Color.WHITE -> {
                        canvas.drawCircle(it.centerX!!, it.centerY!!, radius, paintWhite)
                    }
                }
            }
        }

        // 挟まれていたら、ひっくり返す

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // TODO: タップした位置のマスを判定して、石を追加する
        val xPoint = event.x
        val yPoint = event.y
        val tappedCell = cellList.filter { it.h1?.toInt()!! < yPoint
                && it.h2?.toInt()!! > yPoint
                && it.w1?.toInt()!! < xPoint
                && it.w2?.toInt()!! > xPoint
                && !it.isSet}

        if (tappedCell.isNotEmpty()) {
            tappedCell.first().apply {
                isSet = true
                if (switch) {
                    color = com.yonce3.ocero.Color.WHITE
                    switch = false
                } else {
                    color = com.yonce3.ocero.Color.BLACK
                    switch = true
                }
            }
        }
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
}