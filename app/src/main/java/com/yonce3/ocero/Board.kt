package com.yonce3.ocero

class Board {
    var cellList = arrayListOf<Cell>()
    var player = true
    var blackCount = 0
    var whiteCount = 0

    fun init(cellWidth: Float) {
        var w1 = 0F
        var h1 = 0F
        var w2 = cellWidth
        var h2 = cellWidth

        for(x in 1..8) {
            for (y in 1..8) {
                val cell = Cell(x, y, w1, h1, w2, h2, (w1 + w2)/2, (h1 + h2)/2, false)
                cellList.add(cell)
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
            it.set(Color.WHITE)
        }
        cellList.filter { it.x == 4 && it.y == 5 || it.x == 5 && it.y == 4 }.map {
            it.set(Color.BLACK)
        }
    }

    fun checkAbleCell() {
        // 白色の石の確認
        var whiteList = cellList.filter { it.color == Color.WHITE }
        whiteList.map {

        }
    }

    // それぞれの石の数をカウント
    fun count() {
        whiteCount = cellList.filter { it.color == Color.WHITE }.size
        blackCount = cellList.filter { it.color == Color.BLACK }.size
    }
}