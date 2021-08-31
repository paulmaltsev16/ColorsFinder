package com.pdactech.extractcolors.activities

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pdactech.extractcolors.R
import com.pdactech.extractcolors.activities.viewmodel.MainViewModel
import com.pdactech.extractcolors.analyzer.DominantColorsAnalyzer
import com.pdactech.extractcolors.data_classes.ItemRGB
import com.pdactech.extractcolors.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }


    private val TAG = "PDACTech Colors"
    var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initMainViewModel()

        if (allPermissionsGranted()) {
            startCamera()
            initObserver()
        } else {
            requestCameraPermission()
        }

    }

    fun startCamera() {
        //This is used to bind the lifecycle of cameras to the lifecycle owner
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(
            {
                // Used to bind the lifecycle of cameras to the lifecycle owner
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                // Preview
                val preview = Preview
                    .Builder()
                    .build().also {
                        it.setSurfaceProvider(viewFinder.surfaceProvider)
                    }

                //Program have heavy task to perform (getPercentsOfCurrentColor())
                //For improve user experience uses new thread
                //ImageAnalyzer run on new thread by default
                val imageAnalyzer = initImageAnalyzer(cameraExecutor)

                // Select back camera as a default
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    // Unbind use cases before rebinding
                    cameraProvider.unbindAll()


                    cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview, imageAnalyzer
                    )

                } catch (exc: Exception) {
                    Log.e(TAG, "Use case binding failed", exc)
                }
            },
            //This returns an Executor that runs on the main thread.
            ContextCompat.getMainExecutor(this)
        )
    }

    private fun initImageAnalyzer(cameraExecutor: ExecutorService): ImageAnalysis {
        return ImageAnalysis.Builder()
            .build()
            .also {
                it.setAnalyzer(cameraExecutor, DominantColorsAnalyzer()
                    .apply {
                        setOnDominantColorListener(object :
                            DominantColorsAnalyzer.OnDominantColorsListener {
                            override fun onDominantColorsInitialized(
                                itemRgbResult: ArrayList<ItemRGB>
                            ) {
                                viewModel._rgbResultArray.value = itemRgbResult
                            }
                        })
                    })
            }
    }

    private fun initMainViewModel() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private fun initObserver() {
        viewModel.rgbResultArray.observe(this) {
            //When method start first time it's called from on create method
            //and array not initialized. To avoid crashes uses next statement
            if (it.size == 0)
                return@observe

            binding.firstColor.itemBackgroundColor.setBackgroundColor(it[0].backgroundColor)
            binding.firstColor.itemColorPercent.text = it[0].percentOfColor
            binding.firstColor.itemRgbResult.text = it[0].rgbResult

            binding.secondColor.itemBackgroundColor.setBackgroundColor(it[1].backgroundColor)
            binding.secondColor.itemColorPercent.text = it[1].percentOfColor
            binding.secondColor.itemRgbResult.text = it[1].rgbResult

            binding.thirdColor.itemBackgroundColor.setBackgroundColor(it[2].backgroundColor)
            binding.thirdColor.itemColorPercent.text = it[2].percentOfColor
            binding.thirdColor.itemRgbResult.text = it[2].rgbResult

            binding.fourthColor.itemBackgroundColor.setBackgroundColor(it[3].backgroundColor)
            binding.fourthColor.itemColorPercent.text = it[3].percentOfColor
            binding.fourthColor.itemRgbResult.text = it[3].rgbResult

            binding.fifthColor.itemBackgroundColor.setBackgroundColor(it[4].backgroundColor)
            binding.fifthColor.itemColorPercent.text = it[4].percentOfColor
            binding.fifthColor.itemRgbResult.text = it[4].rgbResult
        }
    }

    //region Permissions
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    //Method check if permission is granted from the user.
    //If not informs him about it
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    R.string.permission_not_granted_by_user,
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
        )
    }
    //endregion

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}

