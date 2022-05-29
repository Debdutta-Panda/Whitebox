package com.vxplore.whitebox

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Slider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

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

@Composable
fun BoxScope.ColorBox(vm: WhiteBoxViewModel) {
    LazyRow(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .height(Constants.colorBoxHeight.dp)
    ){
        items(Constants.colors){
            Button(
                onClick = {
                    vm.setColor(it)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = it
                )
            ) {

            }
        }
    }
}

@Composable
fun BoxScope.StrokeSizeSlider(vm: WhiteBoxViewModel) {
    Slider(
        value = vm.stroke.value/Constants.maxStroke,
        onValueChange = {
            vm.stroke.value = it*Constants.maxStroke
        },
        modifier = Modifier
            .padding(
                start = Constants
                    .strokeSliderStartPadding
                    .dp,
                end = Constants.strokeSliderEndPadding.dp
            )
            .fillMaxWidth()
            .alpha(
                Constants
                    .strokeSliderAlpha
            )
    )
}




