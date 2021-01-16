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

    private var CellList = arrayListOf<Cell>()
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val paint = Paint().apply {
            this.strokeWidth = 10F
            this.style = Paint.Style.STROKE
        }

        var h = 0F
        for (i in 0..8) {
            canvas?.drawLine(0F, h, width.toFloat(), h, paint)
            h += width / 8
        }

        var w = 0F
        for (i in 0..8) {
            canvas?.drawLine(w, 0F, w, width.toFloat(), paint)
            w += width / 8
        }

        var radius = (width / 8 / 2).toFloat()
        for (i in 0..3) {
            when (i) {
                0 -> {
                    val paintA = Paint().apply {
                        this.color = Color.BLACK
                        this.style = Paint.Style.FILL
                    }
                    paintA.color = Color.BLACK
                    var centerX = (width / 2 - radius).toFloat()
                    var centerY = (width / 2 + radius)
                    canvas?.drawCircle(centerX, centerY, radius, paintA)
                }
                1 -> {
                    val paintB = Paint().apply {
                        this.color = Color.BLACK
                        this.style = Paint.Style.FILL
                    }
                    var centerX = (width / 2 + radius).toFloat()
                    var centerY = (width / 2 - radius)
                    canvas?.drawCircle(centerX, centerY, radius, paintB)
                }
                2 -> {
                    val paintC = Paint().apply {
                        this.color = Color.WHITE
                        this.style = Paint.Style.FILL
                    }
                    var centerX = (width / 2 + radius).toFloat()
                    var centerY = (width / 2 + radius)
                    canvas?.drawCircle(centerX, centerY, radius, paintC)
                }
                3 -> {
                    val paintD = Paint().apply {
                        this.color = Color.WHITE
                        this.style = Paint.Style.FILL
                    }
                    var centerX = (width / 2 - radius).toFloat()
                    var centerY = (width / 2 - radius)
                    canvas?.drawCircle(centerX, centerY, radius, paintD)
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        return super.onKeyDown(keyCode, event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        return super.onTouchEvent(event)
    }

    private fun drawCell(i: Int) {
        when (i) {
            0 -> {

            }
            1 -> {

            }
            2 -> {

            }
            3 -> {

            }
        }
    }
}