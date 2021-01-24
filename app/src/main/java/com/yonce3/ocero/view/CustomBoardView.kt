package com.yonce3.ocero.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.yonce3.ocero.Cell
import java.lang.IndexOutOfBoundsException

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

            checkTop(cell)

            checkBottom(cell)
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
                    checkReverse(this)
                    switch = false
                } else {
                    color = com.yonce3.ocero.Color.BLACK
                    checkReverse(this)
                    switch = true
                }

                checkPutAbleArea(this)
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

    private fun checkBottom(cell: Cell) {
        var selfIndex = (cell.x - 1) * 8 + cell.y - 1
        var bottomIndex = if (cell.x == 1) cell.y else selfIndex + 1
        if (switch && cellList[bottomIndex].color == com.yonce3.ocero.Color.BLACK) {
            cellList[bottomIndex + 1].isPut = true
        } else if (!switch && cellList[bottomIndex].color == com.yonce3.ocero.Color.WHITE) {
            cellList[bottomIndex + 1].isPut = true
        }
    }

    private fun checkTop(cell: Cell) {
        var selfIndex = (cell.x - 1) * 8 + cell.y - 1
        var topIndex = if (cell.x == 1) cell.y else selfIndex - 1
        if (switch && cellList[topIndex].color == com.yonce3.ocero.Color.BLACK) {
            cellList[topIndex - 1].isPut = true
        } else if (!switch && cellList[topIndex].color == com.yonce3.ocero.Color.WHITE) {
            cellList[topIndex - 1].isPut = true
        }
    }

    private fun checkReverse(addedCell: Cell) {
        var selfIndex = (addedCell.x - 1) * 8 + addedCell.y - 1

        var rightIndex = if (addedCell.x == 1) addedCell.y else selfIndex + 8
        checkRightReverse(addedCell, rightIndex)

//        var rightIndex = if (addedCell.x == 1) addedCell.y else selfIndex + 8
//        if (cellList[rightIndex].color == com.yonce3.ocero.Color.BLACK) {
//            if (cellList[rightIndex + 8].color == com.yonce3.ocero.Color.WHITE) {
//                cellList[rightIndex].color = com.yonce3.ocero.Color.WHITE
//            }
//        }

        var leftIndex = if (addedCell.x == 1) addedCell.y else selfIndex - 8
        checkLeftReverse(addedCell, leftIndex)
//        if (cellList[leftIndex].color == com.yonce3.ocero.Color.BLACK) {
//            if (cellList[leftIndex - 8].color == com.yonce3.ocero.Color.WHITE) {
//                cellList[leftIndex].color = com.yonce3.ocero.Color.WHITE
//            }
//        }

        var bottomIndex = if (addedCell.x == 1) addedCell.y else selfIndex + 1
        checkBottomReverse(addedCell, bottomIndex)
//        if (cellList[bottomIndex].color == com.yonce3.ocero.Color.BLACK) {
//            if (cellList[bottomIndex + 1].color == com.yonce3.ocero.Color.WHITE) {
//                cellList[bottomIndex].color = com.yonce3.ocero.Color.WHITE
//            }
//        }

        var topIndex = if (addedCell.x == 1) addedCell.y else selfIndex - 1
        checkTopReverse(addedCell, topIndex)
//        if (cellList[topIndex].color == com.yonce3.ocero.Color.BLACK) {
//            if (cellList[topIndex - 1].color == com.yonce3.ocero.Color.WHITE) {
//                cellList[topIndex].color = com.yonce3.ocero.Color.WHITE
//            }
//        }
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

    private fun checkLeftReverse(addedCell: Cell, leftIndex: Int) {
        try {
            if (switch) {
                if (cellList[leftIndex].color == com.yonce3.ocero.Color.BLACK) {
                    if (cellList[leftIndex - 8].color == com.yonce3.ocero.Color.WHITE) {
                        cellList[leftIndex].color = com.yonce3.ocero.Color.WHITE
                    } else {
                        checkRightReverse(addedCell, leftIndex - 8)
                    }
                }
            } else {
                if (cellList[leftIndex].color == com.yonce3.ocero.Color.WHITE) {
                    if (cellList[leftIndex - 8].color == com.yonce3.ocero.Color.BLACK) {
                        cellList[leftIndex].color = com.yonce3.ocero.Color.BLACK
                    } else {
                        checkRightReverse(addedCell, leftIndex - 8)
                    }
                }
            }
        } catch (e: IndexOutOfBoundsException) {
            return
        }
    }

    private fun checkBottomReverse(addedCell: Cell, bottomIndex: Int) {
        try {
            if (switch) {
                if (cellList[bottomIndex].color == com.yonce3.ocero.Color.BLACK) {
                    if (cellList[bottomIndex + 1].color == com.yonce3.ocero.Color.WHITE) {
                        cellList[bottomIndex].color = com.yonce3.ocero.Color.WHITE
                    } else {
                        checkRightReverse(addedCell, bottomIndex + 1)
                    }
                }
            } else {
                if (cellList[bottomIndex].color == com.yonce3.ocero.Color.WHITE) {
                    if (cellList[bottomIndex + 1].color == com.yonce3.ocero.Color.BLACK) {
                        cellList[bottomIndex].color = com.yonce3.ocero.Color.BLACK
                    } else {
                        checkRightReverse(addedCell, bottomIndex + 1)
                    }
                }
            }
        } catch (e: IndexOutOfBoundsException) {
            return
        }
    }

    private fun checkTopReverse(addedCell: Cell, topIndex: Int) {
        try {
            if (switch) {
                if (cellList[topIndex].color == com.yonce3.ocero.Color.BLACK) {
                    if (cellList[topIndex - 1].color == com.yonce3.ocero.Color.WHITE) {
                        cellList[topIndex].color = com.yonce3.ocero.Color.WHITE
                    } else {
                        checkRightReverse(addedCell, topIndex - 1)
                    }
                }
            } else {
                if (cellList[topIndex].color == com.yonce3.ocero.Color.WHITE) {
                    if (cellList[topIndex - 1].color == com.yonce3.ocero.Color.BLACK) {
                        cellList[topIndex].color = com.yonce3.ocero.Color.BLACK
                    } else {
                        checkRightReverse(addedCell, topIndex - 1)
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
        cellList.map {
            it.isPut = false
        }

        var topIndex = selfIndex - 1
        checkTopPutAble(topIndex)

        var bottomIndex = selfIndex + 1
        checkBottomPutAble(bottomIndex)

        var rightIndex = selfIndex + 8
        checkRightPutAble(rightIndex)

        var leftIndex = selfIndex - 8
        checkLeftPutAble(leftIndex)
    }

    private fun checkTopPutAble() {
        cellList.filter { it.isSet }.map {
            var selfIndex = (it.x - 1) * 8 + it.y - 1
            var topIndex = selfIndex - 1
            if (switch) {
                checkPutForWhite(topIndex)
                if (cellList[topIndex].color == com.yonce3.ocero.Color.BLACK) {
                    if (cellList[topIndex - 1].color == com.yonce3.ocero.Color.NONE) {
                        cellList[topIndex].isPut = true
                    } else {
                        checkTopPutAble()
                    }
                }
            } else {
                if (cellList[topIndex].color == com.yonce3.ocero.Color.WHITE) {
                    if (cellList[topIndex - 1].color == com.yonce3.ocero.Color.NONE) {
                        cellList[topIndex].isPut = true
                    } else {
                        checkTopPutAble(topIndex - 1)
                    }
                }
            }
        }
    }

    private fun checkPutForWhite(topIndex: Int) {
        if (cellList[topIndex].color == com.yonce3.ocero.Color.BLACK) {
            if (cellList[topIndex - 1].color == com.yonce3.ocero.Color.NONE) {
                cellList[topIndex].isPut = true
            } else {
                checkTopPutAble()
            }
        }
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
        cellList.filter { it.isSet }.map {
            if (switch) {
                if (cellList[leftIndex].color == com.yonce3.ocero.Color.BLACK) {
                    if (cellList[leftIndex - 8].color == com.yonce3.ocero.Color.NONE) {
                        cellList[leftIndex - 8].isPut = true
                    } else {
                        checkLeftPutAble(leftIndex - 8)
                    }
                }
            } else {
                if (cellList[leftIndex].color == com.yonce3.ocero.Color.WHITE) {
                    if (cellList[leftIndex - 8].color == com.yonce3.ocero.Color.NONE) {
                        cellList[leftIndex - 8].isPut = true
                    } else {
                        checkLeftPutAble(leftIndex - 8)
                    }
                }
            }
        }
    }
}