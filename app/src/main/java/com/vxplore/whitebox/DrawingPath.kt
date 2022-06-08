package com.vxplore.whitebox

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle

enum class ShapeType{
    PATH,
    LINE,
    LINE_SEGMENT,
    RECTANGLE,
    OVAL
}

data class ArrowHead(
    val center: Offset,
    val target: Offset,
)

data class DrawingPath(
    var strokeColor: Color = Color.Black,
    var strokeWidth: Float = 1f,
    var points: MutableList<Offset> = mutableListOf(),
    val alpha: Float = 1.0f,
    var colorFilter: ColorFilter? = null,
    var blendMode: BlendMode = DrawScope.DefaultBlendMode,
    var type: ShapeType = ShapeType.PATH,
    var lineData: Pair<Offset,Offset> = Pair(Offset.Zero,Offset.Zero),
    var pathEffect: PathEffect? = null,
    var cap: CapType = CapType.ROUND,
    var forwardArrowHead: ArrowHead? = null,
    var backArrowHead: ArrowHead? = null,
    val drawStyle: DrawStyle? = null,
    val fillColor: Color = Color.Black
)
