package com.yonce3.ocero.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import com.yonce3.ocero.R

class CustomBoardView(context: Context, attrs: AttributeSet): View(context, attrs) {
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val strokeWidth = 10f

        val paint = Paint().apply {
            this.strokeWidth = strokeWidth
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
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return super.onKeyDown(keyCode, event)
    }
}