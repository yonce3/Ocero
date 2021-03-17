package com.yonce3.ocero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.yonce3.ocero.view.CustomBoardView
import java.nio.BufferUnderflowException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val boardView = findViewById<CustomBoardView>(R.id.board_view)

        val resetButton = findViewById<Button>(R.id.reset_button).apply {
            setOnClickListener { _ ->
                boardView.clearView()
            }
        }
    }
}