package com.vxplore.whitebox

import android.graphics.Paint
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
    OVAL,
    CIRCLE_WITH_CENTER_AND_RADIUS,
    CIRCLE_WITH_2_POINT,
    TEXT,
    ARC
}

data class ArrowHead(
    val center: Offset,
    val target: Offset,
)

data class DrawingPath(
    val displayName: String,
    var strokeColor: Color = Color.Black,
    var strokeWidth: Float = 1f,
    var points: MutableList<Offset> = mutableListOf(),
    val alpha: Float = 1.0f,
    var colorFilter: ColorFilter? = null,
    var blendMode: BlendMode = DrawScope.DefaultBlendMode,
    var type: ShapeType = ShapeType.PATH,
    var twoPointData: Pair<Offset,Offset> = Pair(Offset.Zero,Offset.Zero),
    var pathEffect: PathEffect? = null,
    var cap: CapType = CapType.ROUND,
    var forwardArrowHead: ArrowHead? = null,
    var backArrowHead: ArrowHead? = null,
    val drawStyle: DrawStyle? = null,
    val fillColor: Color = Color.Black,
    val drawStyleType:DrawStyleType = DrawStyleType.STROKE,
    val text: String? = null,
    val paint: Paint? = null,
    val sweepShortest: Boolean = true,
    val withCenter: Boolean = false,
    var active: Boolean = true,
    var isEraser:Boolean = false,
    var index: Int = -1,
    val transformations: MutableList<Transform>? = null
) {
    fun map(offset: Offset): Offset {
        return offset
    }

    val bestColor: Color
    get(){
        return when(drawStyleType){
            DrawStyleType.STROKE -> strokeColor
            else -> fillColor
        }
    }

    val clone: DrawingPath
    get(){
        return DrawingPath(
            displayName = this.displayName,
            strokeColor = this.strokeColor.copy(),
            strokeWidth = this.strokeWidth,
            points = this.points.clone(),
            alpha = this.alpha,
            colorFilter = this.colorFilter,
            blendMode = this.blendMode,
            type = this.type,
            twoPointData = this.twoPointData.clone(),
            pathEffect = this.pathEffect,
            cap = this.cap,
            forwardArrowHead = this.forwardArrowHead?.clone(),
            backArrowHead = this.backArrowHead?.clone(),
            drawStyle = this.drawStyle,
            fillColor = this.fillColor.copy(),
            drawStyleType = this.drawStyleType,
            text = this.text,
            paint = if(this.paint==null) null else Paint(this.paint),
            sweepShortest = this.sweepShortest,
            withCenter = this.withCenter,
            active = this.active,
            isEraser = this.isEraser,
            index = this.index
        )
    }
}

fun Offset.clone():Offset{
    return Offset(x,y)
}
fun Pair<Offset,Offset>.clone():Pair<Offset,Offset>{
    return Pair(first.clone(),second.clone())
}
fun MutableList<Offset>.clone():MutableList<Offset>{
    return this.map {
        it.clone()
    }.toMutableList()
}
fun ArrowHead.clone():ArrowHead{
    return ArrowHead(
        center.clone(),
        target.clone()
    )
}