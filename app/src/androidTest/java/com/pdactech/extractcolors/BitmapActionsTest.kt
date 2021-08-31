package com.pdactech.extractcolors

import android.graphics.Bitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.pdactech.extractcolors.bitmap.BitmapActions
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
@RunWith(AndroidJUnit4::class)
class BitmapActionsTest {

    /*
    Program uses android test because we need create new bitmap
    and make test with it. And if we will create bitmap in unit test, it always return null
     */

    @Test
    fun splitBitmapTest() {
        //Creating new bitmap
        val newBitmap = Bitmap.createBitmap(10, 5, Bitmap.Config.ARGB_8888)
        //Split bitmap to 5 peaces
        val result = BitmapActions().splitBitmap(newBitmap, 5)
        //Check if size of result (Array) == 5, so we have 5 pieces of bitmap
        assert(result.size == 5)
    }

    @Test
    fun getDominantColorTest() {
        val color = R.color.black
        //Creating new bitmap
        val newBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        newBitmap.setPixel(0, 0, color)
        //Getting dominant color of bitmap
        val result = BitmapActions().getDominantColor(newBitmap)

        //Check if it the same color that we passed
        assert(color - 100000 <= result && result <= color + 100000)
    }

    @Test
    fun getRGBValueOfColorTest() {
        //Creating new color with RGB value = R:18 G:221 B:26.
        //-15540966 it decimal signed 2's value of current color
        //You can check this color in color.xml file - test_color_18_221_26
        val color: Int = -15540966

        //Getting RGB value of color
        val result = BitmapActions().getRGBValueOfColor(color)

        //Check if it the same color that we passed
        assert(result.contains("18") && result.contains("221") && result.contains("26"))
    }

    @Test
    fun getPercentsOfCurrentColorTest() {
        val color: Int = -15540966
        val newBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        newBitmap.setPixel(0, 0, color)
        val result = BitmapActions().getPercentsOfCurrentColor(color, newBitmap)
        //We have bitmap with only one pixel, and this pixel has green color
        //So result how much percent of green in current bitmap must be 100 percent
        assert(result.contains("100"))
    }
}