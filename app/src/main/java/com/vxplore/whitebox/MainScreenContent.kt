package com.vxplore.whitebox

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInteropFilter

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainScreenContent(vm: WhiteBoxViewModel) {
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        var lastOffset by remember { mutableStateOf(Offset.Zero) }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInteropFilter {
                    val x = it.x
                    val y = it.y
                    val offset = Offset(x, y)
                    when (it.action) {

                        MotionEvent.ACTION_DOWN -> {
                            vm.dragStart(offset)
                            lastOffset = offset
                        }
                        MotionEvent.ACTION_MOVE -> {
                            vm.drag(offset - lastOffset, offset)
                            lastOffset = offset
                        }
                        MotionEvent.ACTION_UP -> {
                            vm.dragEnd()
                        }
                        else -> false
                    }
                    return@pointerInteropFilter true
                }
            /*.pointerInput(Unit) {
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
            }*/
        ){
            GraphPaper(vm)
            Drawing(vm)
        }
        ToolBox(vm)
        if(vm.tool.value==Tool.PEN&&vm.penPathAdded.value){
            IconButton(onClick = {
                vm.onCloseCurrentPath()
            }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
        }
        else{
            StrokeSizeSlider(vm)
        }
    }
}