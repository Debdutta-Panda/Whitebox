package com.vxplore.whitebox

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope

fun DrawScope.GraphPaper(vm: WhiteBoxViewModel) {
    val canvasWidth = size.width
    val canvasHeight = size.height

    val offsetX = vm.canvasOffset.value.x
    val xGridGap = vm.gridGap.value
    var gridY = 0f
    ///////////////////////////
    val offsetY = vm.canvasOffset.value.y
    val yGridGap = vm.gridGap.value
    var gridX = 0f
    if(
        vm.gridMode.value==GridMode.Y
        ||vm.gridMode.value==GridMode.XY
    ){
        while(gridX<=canvasWidth+xGridGap){
            drawLine(
                start = Offset(x = gridX+offsetX % xGridGap, y = 0f),
                end = Offset(x = gridX+offsetX % xGridGap, y = canvasHeight),
                color = vm.yGridColor.value,
                strokeWidth = vm.yGridStroke.value,
            )
            gridX += xGridGap
        }
    }
    if(
        vm.gridMode.value==GridMode.X
        ||vm.gridMode.value==GridMode.XY
    ){
        while(gridY<=canvasHeight+yGridGap){
            drawLine(
                start = Offset(x = 0f, y = gridY+offsetY % yGridGap),
                end = Offset(x = canvasWidth, y = gridY+offsetY % yGridGap),
                color = vm.xGridColor.value,
                strokeWidth = vm.xGridStroke.value
            )
            gridY += yGridGap
        }
    }
}