package com.vxplore.whitebox

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class DrawingPath(
    var color: Color = Color.Black,
    var strokeWidth: Float = 1f,
    var points: MutableList<Offset> = mutableListOf()
)
