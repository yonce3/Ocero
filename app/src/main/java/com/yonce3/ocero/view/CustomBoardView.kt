package com.yonce3.ocero.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.yonce3.ocero.Board
import com.yonce3.ocero.Cell
import com.yonce3.ocero.MainActivity
import java.lang.IndexOutOfBoundsException

class CustomBoardView(context: Context, attrs: AttributeSet): View(context, attrs) {
    private var cellList = arrayListOf<Cell>()
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
    private var switch: Boolean = true
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

        val whiteList = board.cellList.filter { it.color == white }
        whiteList.map {cell ->
            board.checkCell(cell)
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
            tappedCellList.first().apply {
                isSet = true
                if (board.player) {
                    color = white
                    board.checkReverse(this)
                    board.player = false
                } else {
                    color = com.yonce3.ocero.Color.BLACK
                    board.checkReverse(this)
                    board.player = true
                }

                board.cellList
            }
        } else {
            return false
        }

        // リセット
        board.cellList.map {
            it.isPut = false
        }

        var checkList = if (board.player) board.cellList.filter { it.color == white } else board.cellList.filter { it.color == black }
        checkList.map {
            board.checkCell(it)
        }

        val activity = this.context
        if (activity is MainActivity) {
            println("aaa")
            val playerText = if (board.player) "White" else "Black"
            activity.mainViewModel.playerText.postValue(playerText)
        }

        invalidate()
        return super.onTouchEvent(event)
    }

    fun clearView() {
        board = board.reset()
        board.init(cellWidth)

        val whiteList = board.cellList.filter { it.color == white }
        whiteList.map {cell ->
            board.checkCell(cell)
        }
        invalidate()
    }

    private fun checkRightReverse(addedCell: Cell, rightIndex: Int) {
        try {
            if (switch) {
                if (cellList[rightIndex].color == com.yonce3.ocero.Color.BLACK) {
                    if (cellList[rightIndex + 8].color == com.yonce3.ocero.Color.WHITE) {
                        cellList[rightIndex].color = com.yonce3.ocero.Color.WHITE
                    } else {
                        checkRightReverse(addedCell, rightIndex + 8)
                    }
                }
            } else {
                if (cellList[rightIndex].color == com.yonce3.ocero.Color.WHITE) {
                    if (cellList[rightIndex + 8].color == com.yonce3.ocero.Color.BLACK) {
                        cellList[rightIndex].color = com.yonce3.ocero.Color.BLACK
                    } else {
                        checkRightReverse(addedCell, rightIndex + 8)
                    }
                }
            }
        } catch (e: IndexOutOfBoundsException) {
            return
        }
    }

    private fun checkPutAbleArea(addedCell: Cell) {
        var selfIndex = (addedCell.x - 1) * 8 + addedCell.y - 1

        // タップ可能判定をリセット
        board.cellList.map {
            it.isPut = false
        }

        var topIndex = selfIndex - 1
        //checkTopPutAble()

        var bottomIndex = selfIndex + 1
        checkBottomPutAble(bottomIndex)

        var rightIndex = selfIndex + 8
        checkRightPutAble(rightIndex)

        var leftIndex = selfIndex - 8
        checkLeftPutAble(leftIndex)
    }

    private fun checkBottomPutAble(bottomIndex: Int) {
        cellList.filter { it.isSet }.map {
            if (switch) {
                if (cellList[bottomIndex].color == com.yonce3.ocero.Color.BLACK) {
                    if (cellList[bottomIndex + 1].color == com.yonce3.ocero.Color.NONE) {
                        cellList[bottomIndex].isPut = true
                    } else {
                        checkBottomPutAble(bottomIndex + 1)
                    }
                }
            } else {
                if (cellList[bottomIndex].color == com.yonce3.ocero.Color.WHITE) {
                    if (cellList[bottomIndex + 1].color == com.yonce3.ocero.Color.NONE) {
                        cellList[bottomIndex].isPut = true
                    } else {
                        checkBottomPutAble(bottomIndex + 1)
                    }
                }
            }
        }
    }

    private fun checkRightPutAble(rightIndex: Int) {
        cellList.filter { it.isSet }.map {
            if (switch) {
                if (cellList[rightIndex].color == com.yonce3.ocero.Color.BLACK) {
                    if (cellList[rightIndex + 8].color == com.yonce3.ocero.Color.NONE) {
                        cellList[rightIndex + 8].isPut = true
                    } else {
                        checkRightPutAble(rightIndex + 8)
                    }
                }
            } else {
                if (cellList[rightIndex].color == com.yonce3.ocero.Color.WHITE) {
                    if (cellList[rightIndex + 8].color == com.yonce3.ocero.Color.NONE) {
                        cellList[rightIndex + 8].isPut = true
                    } else {
                        checkRightPutAble(rightIndex + 8)
                    }
                }
            }
        }
    }

    private fun checkLeftPutAble(leftIndex: Int) {
        if (switch) {
            cellList.filter { it.isSet && it.color == com.yonce3.ocero.Color.WHITE }.map {
                var selfIndex = (it.x - 1) * 8 + it.y - 1
                var leftIndex = selfIndex - 8
                if (cellList[leftIndex].color == com.yonce3.ocero.Color.BLACK) {
                    if (cellList[leftIndex - 8].color == com.yonce3.ocero.Color.NONE) {
                        cellList[leftIndex - 8].isPut = true
                    } else {
                        //checkLeftPutAble(leftIndex - 8)
                    }
                }
            }
        } else {
            cellList.filter { it.isSet && it.color == com.yonce3.ocero.Color.BLACK }.map {
                var selfIndex = (it.x - 1) * 8 + it.y - 1
                var leftIndex = selfIndex - 8
                if (cellList[leftIndex].color == com.yonce3.ocero.Color.WHITE) {
                    if (cellList[leftIndex - 8].color == com.yonce3.ocero.Color.NONE) {
                        cellList[leftIndex - 8].isPut = true
                    } else {
                        //checkLeftPutAble(leftIndex - 8)
                    }
                }
            }
        }
    }
}