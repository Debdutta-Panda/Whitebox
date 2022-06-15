package com.vxplore.whitebox

import androidx.compose.ui.graphics.Matrix

data class Transform(
    val type: Int = 0,//transformation type 0 = none, 1 = translate, 2 = rotate, 3 = scale
    val value1: Float = 0f,//value1 rotateZ, scaleX, translateX
    val value2: Float = 0f,//value2 scaleX, translateX
    val continuous: Boolean = false
)

val List<Transform>.matrix : Matrix
get(){
    return Matrix()
        .apply {
            this@matrix.forEach {
                when(it.type){
                    1->translate(it.value1,it.value2)
                    2->rotateZ(it.value1)
                    3->scale(it.value1,it.value2)
                }
            }
        }
}
