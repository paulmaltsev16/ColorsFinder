package com.pdactech.extractcolors.data_classes

data class ItemRGB(
    //uses to set the background color
    var backgroundColor: Int,
    //notifies the user how much percentage of a given color is in the image
    var percentOfColor: String,
    //rgb value to get current color
    var rgbResult: String
)