package com.pdactech.extractcolors.analyzer

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.pdactech.extractcolors.bitmap.BitmapActions
import com.pdactech.extractcolors.converters.BitmapConverter
import com.pdactech.extractcolors.data_classes.ItemRGB
import java.util.logging.Handler

class DominantColorsAnalyzer : ImageAnalysis.Analyzer {

    private lateinit var mListener: OnDominantColorsListener
    private val bitmapActions = BitmapActions()
    private val bitmapConverter = BitmapConverter()

    fun setOnDominantColorListener(mListener: OnDominantColorsListener) {
        this.mListener = mListener
    }

    override fun analyze(imageProxy: ImageProxy) {
        val bitmap = bitmapConverter.getBitmapFromImageProxy(imageProxy)

        //The program divides the original image into 5 small ones.
        //Since we have 5 cells to display information to the user into item_color_result.xml
        //For more explanation you can check screenshot1
        //in drawable folder or read README file in source of project
        val arrayOfBitmaps = bitmapActions.splitBitmap(bitmap, 5)

        val rgbResultArray = arrayListOf<ItemRGB>()

        for (element in arrayOfBitmaps) {


            //Getting dominant color. We use 0 because we have
            //only one row of images (5 columns and 1 row)
            val dominantColor = bitmapActions.getDominantColor(element)
            //Getting percents of dominant color in image
            //In second parameter we pass source bitmap, because
            //we need to know how often dominant color appears in all image
            val percentsOfColor =
                bitmapActions.getPercentsOfCurrentColor(dominantColor, bitmap)
            //Getting rgb value of dominant color in string format
            val rgbValue = bitmapActions.getRGBValueOfColor(dominantColor)

            val rgbItem = ItemRGB(dominantColor, percentsOfColor, rgbValue)
            rgbResultArray.add(rgbItem)
        }

        //uses this interface to show the results to the user
        mListener.onDominantColorsInitialized(rgbResultArray)


        imageProxy.close()
    }

    interface OnDominantColorsListener {
        fun onDominantColorsInitialized(
            itemRgbResult: ArrayList<ItemRGB>
        )
    }
}


