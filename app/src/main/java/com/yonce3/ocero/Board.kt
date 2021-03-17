package com.yonce3.ocero

class Board {
    var cellList = arrayListOf<Cell>()

    fun init() {
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

    fun checkAbleCell() {

    }
}