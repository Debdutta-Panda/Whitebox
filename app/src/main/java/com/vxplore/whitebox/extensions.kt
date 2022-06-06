package com.vxplore.whitebox

import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
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