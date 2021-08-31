package com.pdactech.extractcolors.bitmap

import android.graphics.*
import android.util.Log

class BitmapActions {

    //There are 18 million colors in the world
    //To show the user how many percent of each color in the image i use the following parameter
    //The lower its value, the more accurate the calculation
    private val accuracyOfPercentage = 700000

    fun splitBitmap(bitmap: Bitmap, xCount: Int): ArrayList<Bitmap> {
        val bitmaps = ArrayList<Bitmap>()

        // Divide the original bitmap to xCount peaces
        val width: Int = bitmap.width / xCount
        // Getting bitmap height
        val height: Int = bitmap.height

        //Adding to array created bitmap.
        //You can check screenshot1 in drawable folder for more explanation.
        for (x in 0 until xCount) {
            bitmaps.add(Bitmap.createBitmap(bitmap, x * width, 0, width, height))
        }

        // Return the array with bitmaps
        return bitmaps!!
    }

    fun getDominantColor(bitmap: Bitmap?): Int {
        val newBitmap = Bitmap.createScaledBitmap(bitmap!!, 1, 1, true)
        val color = newBitmap.getPixel(0, 0)
        newBitmap.recycle()
        return color
    }

    fun getRGBValueOfColor(color: Int): String {
        // from a color int
        val redValue = Color.red(color)
        val blueValue = Color.green(color)
        val greenValue = Color.blue(color)

        return "R:$redValue G:$blueValue B:$greenValue"
    }

    fun getPercentsOfCurrentColor(color: Int, bitmap: Bitmap): String {
        val xCount = bitmap.width
        val yCount = bitmap.height
        val countOfPixels = xCount * yCount
        var countOfSameColor = 0

        for (i in 0 until xCount) {
            for (j in 0 until yCount) {
                if (color - accuracyOfPercentage <= bitmap.getPixel(i, j)
                    && bitmap.getPixel(i, j) <= color + accuracyOfPercentage
                ) {
                    countOfSameColor++
                }
            }
        }

        val percents: Double =
            ((countOfSameColor.toDouble() / countOfPixels.toDouble()) * 100)

        return String.format("%.2f", percents) + "%"
    }
}