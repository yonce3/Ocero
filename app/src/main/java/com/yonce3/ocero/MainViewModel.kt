package com.yonce3.ocero

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    val playerText = MutableLiveData<String>("White")
    val whiteCount = MutableLiveData("2")
    val blackCount = MutableLiveData("2")
}