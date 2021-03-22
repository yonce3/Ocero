package com.yonce3.ocero

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.yonce3.ocero.databinding.ActivityMainBinding

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