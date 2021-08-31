package com.pdactech.extractcolors.activities.viewmodel

import android.app.Application
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.pdactech.extractcolors.activities.MainActivity
import com.pdactech.extractcolors.analyzer.DominantColorsAnalyzer
import com.pdactech.extractcolors.data_classes.ItemRGB
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainViewModel(application: Application) : AndroidViewModel(application) {

      var _rgbResultArray = MutableLiveData<ArrayList<ItemRGB>>()
        .apply { value = arrayListOf() }
    val rgbResultArray: LiveData<ArrayList<ItemRGB>> get() = _rgbResultArray

}