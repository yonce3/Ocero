package com.yonce3.ocero

class Board {
    var cellList = arrayListOf<Cell>()
    var player = true
    var blackCount = 0
    var whiteCount = 0
    var indexCountList = listOf(-1, 7, 8, 9, 1, -7, -8, -9)

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

    // それぞれの石の数をカウント
    fun count() {
        whiteCount = cellList.filter { it.color == Color.WHITE }.size
        blackCount = cellList.filter { it.color == Color.BLACK }.size
    }

    private fun checkIndexCountList(cell: Cell): List<Int> =
        when {
            cell.x == 1 && cell.y == 1 -> listOf(1, 8, 9)
            cell.x == 1 && cell.y == 8 -> listOf(-1, 7, 8)
            cell.x == 1 -> listOf(-1, 7, 8, 9, 1)
            cell.x == 8 && cell.y == 1 -> listOf(1, -8, -9)
            cell.x == 8 && cell.y == 8 -> listOf(-1, -8, -9)
            cell.x == 8 -> listOf(-1, 1, -7, -8, -9)
            cell.y == 1 -> listOf(7, 8, 1, -7, -8)
            cell.y == 8 -> listOf(-1, 7, 8, -8, -7)
            else -> indexCountList
        }

    fun checkCell(cell: Cell) {
        checkIndexCountList(cell).map { indexCount ->
            var selfIndex = (cell.x - 1) * 8 + cell.y - 1
            var nextIndex = selfIndex + indexCount
            var flag = true
            var color = if (player) Color.BLACK else Color.WHITE
            val CHECKINDEX = nextIndex + indexCount in 0..63

            println("あああ" + nextIndex + indexCount)

            while (flag && nextIndex < 64 && 0 < nextIndex && nextIndex + indexCount < 64) {
                if (cellList[nextIndex].color == color && CHECKINDEX
                        && cellList[nextIndex + indexCount].color == Color.NONE) {
                    cellList[nextIndex + indexCount].isPut = true
                    flag = false
                } else if (cellList[nextIndex].color == color && cellList[nextIndex].x != 8) {
                    nextIndex += indexCount
                } else {
                    flag = false
                }
            }
        }
    }

    fun checkReverse(addCell: Cell) {
        checkIndexCountList(addCell).map { indexCount ->
            var selfIndex = (addCell.x - 1) * 8 + addCell.y - 1
            var nextIndex = selfIndex + indexCount
            var flag = true
            var color = if (player) Color.BLACK else Color.WHITE
            var reverseColor = if (player) Color.WHITE else Color.BLACK
            var reversibleList = arrayListOf<Cell>()

            println("あああ" + selfIndex + indexCount)

            while (flag && nextIndex < 64 && 0 < nextIndex && nextIndex + indexCount < 64) {
                if (cellList[nextIndex].color == color
                        && cellList[nextIndex + indexCount].color == reverseColor) {
                    reversibleList.add(cellList[nextIndex])
                    reversibleList.map {
                        it.color = reverseColor
                    }
                    flag = false
                } else if (cellList[nextIndex].color == color) {
                    reversibleList.add(cellList[nextIndex])
                    nextIndex += indexCount
                } else {
                    flag = false
                }
            }
        }
    }

    fun reset(): Board {
        return Board()
    }

    // ロジックの記録用: プット可能エリア
    fun checkLeft(isWhite: Boolean, cell: Cell) {
        var selfIndex = (cell.x - 1) * 8 + cell.y - 1
        var leftIndex = if (cell.x == 1) cell.y else selfIndex - 8
        var flag = true
        var cellList = cellList
        var color = if(isWhite) com.yonce3.ocero.Color.BLACK else com.yonce3.ocero.Color.WHITE

        while (flag) {
            if (cellList[leftIndex].color == color
                    && cellList[leftIndex - 8].color == com.yonce3.ocero.Color.NONE) {
                cellList[leftIndex - 8].isPut = true
                flag = false
            } else if (cellList[leftIndex].color == color) {
                leftIndex -= 8
            } else {
                flag = false
            }
        }
    }
}