package com.vxplore.whitebox

import androidx.compose.runtime.MutableState

inline fun <reified T: Enum<T>> T.next(): T {
    val values = enumValues<T>()
    val nextOrdinal = (ordinal + 1) % values.size
    return values[nextOrdinal]
}

fun MutableState<Boolean>.toggle() {
    this.value = !this.value
}