package com.vxplore.whitebox

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun WhiteBox(vm: WhiteBoxViewModel) {
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = {
                                vm.dragStart(it)
                            },
                            onDragEnd = {
                                vm.dragEnd()
                            },
                            onDragCancel = {
                                vm.dragCancel()
                            }
                        ) { change, dragAmount ->
                            vm.drag(dragAmount)
                        }
                }
        ){
            GraphPaper(vm)
            Drawing(vm)
        }
        ToolBox(vm)
        StrokeSizeSlider(vm)
        ColorBox(vm)
    }
}






