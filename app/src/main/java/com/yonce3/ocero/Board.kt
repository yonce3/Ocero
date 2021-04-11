package com.yonce3.ocero

class Board {
    lateinit var cellList: ArrayList<Cell>
    var player = true
    var indexCountList = listOf(-1, 7, 8, 9, 1, -7, -8, -9)

    fun init(cellWidth: Float) {
        cellList = arrayListOf()
        player = true
        var w1 = 0F
        var h1 = 0F
        var w2 = cellWidth
        var h2 = cellWidth
        var whiteList = arrayListOf<Cell>()

        for(x in 1..8) {
            for (y in 1..8) {
                val cell = Cell(x, y, w1, h1, w2, h2, (w1 + w2) / 2, (h1 + h2) / 2, false)
                cellList.add(cell)

                // 初期石を作成
                if (x == 4 && y == 4 || x == 5 && y == 5) {
                    cell.set(Color.WHITE)
                    whiteList.add(cell)
                } else if (x == 4 && y == 5 || x == 5 && y == 4) {
                    cell.set(Color.BLACK)
                }
                h1 += cellWidth
                h2 += cellWidth
            }
            h1 = 0F
            h2 = cellWidth
            w1 += cellWidth
            w2 += cellWidth
        }

        whiteList.map {cell ->
            checkCell(cell)
        }
    }

    private fun checkIndexCountList(cell: Cell): List<Int> =
        when {
            cell.x == 1 && cell.y == 1 -> listOf(1, 8, 9)
            cell.x == 1 && cell.y == 8 -> listOf(-1, 7, 8)
            cell.x == 1 -> listOf(-1, 7, 8, 9, 1)
            cell.x == 8 && cell.y == 1 -> listOf(1, -7, -8)
            cell.x == 8 && cell.y == 8 -> listOf(-1, -8, -9)
            cell.x == 8 -> listOf(-1, 1, -7, -8, -9)
            cell.y == 1 -> listOf(7, 8, 1, -7, -8)
            cell.y == 8 -> listOf(-1, 7, 8, -8, -7)
            else -> indexCountList
        }

    private fun checkCell(cell: Cell) {
        checkIndexCountList(cell).map { indexCount ->
            var selfIndex = (cell.x - 1) * 8 + cell.y - 1
            var nextIndex = selfIndex + indexCount
            var flag = true
            var color = if (player) Color.BLACK else Color.WHITE

            while (flag && nextIndex in 0..63 && nextIndex + indexCount in 0..63) {
                if (cell.x != 1 && cell.x != 8 && cell.y != 1 && cell.y != 8 ) { // 始点が端でない
                    val nextCell = cellList[nextIndex]
                    val thirdCell = cellList[nextIndex + indexCount]
                    if (nextCell.x == 1 || nextCell.x == 8 || nextCell.y == 1 || nextCell.y == 8) {
                        if (thirdCell.x == 1 || thirdCell.x == 8 || thirdCell.y == 1 || thirdCell.y == 8) {
                            break
                        }
                    }
                }

                if (cellList[nextIndex].color == color && cellList[nextIndex + indexCount].color == Color.NONE) {
                    cellList[nextIndex + indexCount].isPut = true
                    flag = false
                } else if (cellList[nextIndex].color == color) {
                    nextIndex += indexCount
                } else {
                    flag = false
                }
            }
        }

        // 置く場所がない場合に、次のプレイヤーに変わる
        if (cellList.none { it.isPut }) {
            if (cellList.none { !it.isSet }) {
                player = !player
            }
        }
    }

    private fun checkReverse(addCell: Cell) {
        checkIndexCountList(addCell).map { indexCount ->
            var selfIndex = (addCell.x - 1) * 8 + addCell.y - 1
            var nextIndex = selfIndex + indexCount
            var flag = true
            var color = if (player) Color.BLACK else Color.WHITE
            var reverseColor = if (player) Color.WHITE else Color.BLACK
            var reversibleList = arrayListOf<Cell>()

            while (flag && nextIndex in 0..63 && (nextIndex + indexCount) in 0..63) {
                if (addCell.x != 1 && addCell.x != 8 && addCell.y != 1 && addCell.y != 8 ) { // 始点が端でない
                    val nextCell = cellList[nextIndex]
                    val thirdCell = cellList[nextIndex + indexCount]
                    if (nextCell.x == 1 || nextCell.x == 8 || nextCell.y == 1 || nextCell.y == 8) {
                        if (thirdCell.x == 1 || thirdCell.x == 8 || thirdCell.y == 1 || thirdCell.y == 8) {
                            break
                        }
                    }
                }

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

    fun tapNewCell(addCell: Cell) {
        addCell.apply {
            isSet = true

            if (player) {
                color = Color.WHITE
                checkReverse(this)
                player = false
            } else {
                color = Color.BLACK
                checkReverse(this)
                player = true
            }
        }

        // リセット
        cellList.map {
            it.isPut = false
        }

        var checkList = if (player) cellList.filter { it.color == Color.WHITE } else cellList.filter { it.color == Color.BLACK }
        checkList.map {
            checkCell(it)
        }
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