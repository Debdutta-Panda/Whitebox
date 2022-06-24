package com.vxplore.whitebox

import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.sqrt

inline fun <reified T: Enum<T>> T.next(): T {
    val values = enumValues<T>()
    val nextOrdinal = (ordinal + 1) % values.size
    return values[nextOrdinal]
}

fun MutableState<Boolean>.toggle() {
    this.value = !this.value
}

fun distance(first: Offset, second: Offset) : Float{
    return sqrt((first.x-second.x).square()+(first.y-second.y).square())
}

fun Float.square(): Float{
    return this*this
}

val CapType.strokeType: StrokeCap
    get() {
        return when(this){
            CapType.ROUND -> StrokeCap.Round
            CapType.BUTT -> StrokeCap.Butt
            CapType.SQUARE -> StrokeCap.Square
        }
    }

fun Offset.angle(center: Offset,makePositive: Boolean = false):Float{
    val A = center
    val B = this
    val dx = B.x-A.x
    val dy = B.y-A.y
    val tan = dy/dx
    val angle = atan(tan)
    val caseH = if(dy<0) 1 else 0
    val caseV = if(dx<0) 2 else 0
    val correction = when(caseH + caseV){
        0->0
        1->0
        2->180
        3->180
        else->0
    }
    val r = (angle*180/PI).toFloat() + correction
    return if(makePositive){
        if(r>=0f) r else 360f + r
    }
    else{
        r
    }
}

fun createImageVector(path: String, width: Int,height: Int, vWidth: Float,vHeight:Float): ImageVector {
    return ImageVector.Builder(
        defaultWidth = width.dp,
        defaultHeight = height.dp,
        viewportWidth = vWidth,
        viewportHeight = vHeight,
    ).run {
        addPath(
            pathData = addPathNodes(path),
            name = "",
            fill = SolidColor(Color.Black),
            stroke = SolidColor(Color.Black),
        )
        build()
    }
}

val Color.Companion.lightRed: Color get() = Color(0xffe64949)


fun intendedSweep(shortest: Boolean, angle2: Float, angle1: Float): Float {
    val s = shortestSweep(angle2,angle1)
    return if(shortest){
        s
    }
    else{
        when (s) {
            in 0f..180f -> s-360
            else -> s+360
        }
    }
}