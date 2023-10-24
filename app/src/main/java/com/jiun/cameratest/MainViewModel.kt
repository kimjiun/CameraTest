package com.jiun.cameratest

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {
    val pictureArray = mutableStateListOf<Bitmap>()
}