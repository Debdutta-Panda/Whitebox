package com.vxplore.whitebox

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope

fun DrawScope.GraphPaper(vm: WhiteBoxViewModel) {
    val canvasWidth = size.width
    val canvasHeight = size.height

    val offsetX = vm.canvasOffset.value.x
    val xGridGap = vm.xGridGap.value
    var gridY = 0f
    ///////////////////////////
    val offsetY = vm.canvasOffset.value.y
    val yGridGap = vm.yGridGap.value
    var gridX = 0f
    while(gridX<=canvasWidth+xGridGap){
        drawLine(
            start = Offset(x = gridX+offsetX % xGridGap, y = 0f),
            end = Offset(x = gridX+offsetX % xGridGap, y = canvasHeight),
            color = vm.yGridColor.value,
            strokeWidth = vm.yGridStroke.value
        )
        gridX += xGridGap
    }

    while(gridY<=canvasHeight+yGridGap){
        drawLine(
            start = Offset(x = 0f, y = gridY+offsetY % yGridGap),
            end = Offset(x = canvasWidth, y = gridY+offsetY % yGridGap),
            color = vm.yGridColor.value,
            strokeWidth = vm.yGridStroke.value
        )
        gridY += yGridGap
    }
}