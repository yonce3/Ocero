package com.yonce3.ocero

data class Cell(val x: Int,
                val y: Int,
                val w1: Float?,
                val h1: Float?,
                val w2: Float?,
                val h2: Float?,
                val centerX : Float?,
                val centerY: Float?,
                var isSet: Boolean,
                var isPut: Boolean = false,
                var color: Color = Color.NONE
)

// 拡張for文
fun Cell.set(color: Color) {
    this.apply {
        isSet = true
        this.color = color
    }
}

enum class Color {
    NONE,
    BLACK,
    WHITE
}
