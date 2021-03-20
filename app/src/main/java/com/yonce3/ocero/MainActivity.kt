package com.yonce3.ocero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.yonce3.ocero.databinding.ActivityMainBinding
import com.yonce3.ocero.view.CustomBoardView
import java.nio.BufferUnderflowException

class MainActivity : AppCompatActivity() {

    val mainViewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            this.viewModel = mainViewModel
            lifecycleOwner = this@MainActivity

            resetButton.setOnClickListener {
                this.boardView.clearView()
            }
        }
    }
}