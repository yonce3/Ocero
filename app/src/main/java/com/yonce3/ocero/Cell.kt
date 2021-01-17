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
                var color: Color = Color.NONE
)

enum class Color {
    NONE,
    BLACK,
    WHITE
}
