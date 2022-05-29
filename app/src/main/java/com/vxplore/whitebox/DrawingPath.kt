package com.vxplore.whitebox

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope

enum class ShapeType{
    PATH,
    LINE,
    LINE_SEGMENT,
    RECTANGLE,
    OVAL
}

data class DrawingPath(
    var strokeColor: Color = Color.Black,
    var strokeWidth: Float = 1f,
    var points: MutableList<Offset> = mutableListOf(),
    val alpha: Float = 1.0f,
    var colorFilter: ColorFilter? = null,
    var blendMode: BlendMode = DrawScope.DefaultBlendMode,
    var type: ShapeType = ShapeType.PATH,
    var lineData: Pair<Offset,Offset> = Pair(Offset.Zero,Offset.Zero),
    var pathEffect: PathEffect? = null
)
