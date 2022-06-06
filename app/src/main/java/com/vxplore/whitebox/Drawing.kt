package com.vxplore.whitebox

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.graphics.nativeCanvas



fun DrawScope.Drawing(vm: WhiteBoxViewModel) {
    with(drawContext.canvas.nativeCanvas) {
        val checkPoint = saveLayer(null, null)
        vm.paths.forEach {
            when(it.type){
                ShapeType.PATH -> drawPath(vm,it)
                ShapeType.LINE -> drawLine(vm,it)
                ShapeType.LINE_SEGMENT -> TODO()
                ShapeType.RECTANGLE -> drawRectangle(vm,it)
                ShapeType.OVAL -> TODO()
            }
        }
        drawEraser(vm)
        restoreToCount(checkPoint)
    }

}

fun DrawScope.drawEraser(vm: WhiteBoxViewModel) {
    if(vm.showEraser.value){
        drawCircle(
            color = vm.eraserIndicatorColor.value,
            radius = vm.stroke.value/2,
            center = vm.eraserPos.value+vm.canvasOffset.value,
            alpha = vm.eraserIndicatorAlpha.value,
            style = Stroke(
                width = 2f
            )
        )
    }
}

fun DrawScope.drawRectangle(vm: WhiteBoxViewModel, path: DrawingPath) {
    if(vm.pathUpdated.value>0L){
        val gap = path.lineData.second - path.lineData.first
        drawRect(
            color = path.strokeColor,
            topLeft = path.lineData.first+vm.canvasOffset.value,
            size = Size(gap.x,gap.y),
            colorFilter = path.colorFilter,
            blendMode = path.blendMode,
            alpha = path.alpha,
            style = path.drawStyle?:Fill
        )
    }
}

fun DrawScope.drawLine(vm: WhiteBoxViewModel, path: DrawingPath){
    if(vm.pathUpdated.value>0L){
        drawLine(
            start = path.lineData.first+vm.canvasOffset.value,
            end = path.lineData.second+vm.canvasOffset.value,
            color = path.strokeColor,
            alpha = path.alpha,
            colorFilter = path.colorFilter,
            blendMode = path.blendMode,
            strokeWidth = path.strokeWidth,
            cap = path.cap.strokeType,
            pathEffect = path.pathEffect,
        )
        drawArrow(path,vm)
    }
}

fun DrawScope.drawPath(vm: WhiteBoxViewModel, path: DrawingPath) {
    drawPath(
        createNewPath(vm,path),
        color = path.strokeColor,
        style = Stroke(
            width = path.strokeWidth,
            cap = path.cap.strokeType,
            join = StrokeJoin.Round,
            pathEffect = path.pathEffect
        ),
        alpha = path.alpha,
        colorFilter = path.colorFilter,
        blendMode = path.blendMode
    )
    drawArrow(path,vm)
}

fun DrawScope.drawArrow(path: DrawingPath,vm: WhiteBoxViewModel) {
    path.forwardArrowHead?.let {
        translate(
            vm.canvasOffset.value.x,
            vm.canvasOffset.value.y,
        ) {
            rotate(
                45f,
                it.center
            ){
                drawLine(
                    start = it.center,
                    end = it.target,
                    color = path.strokeColor,
                    alpha = path.alpha,
                    colorFilter = path.colorFilter,
                    blendMode = path.blendMode,
                    strokeWidth = path.strokeWidth,
                    cap = path.cap.strokeType,
                    pathEffect = path.pathEffect,
                )
            }
            rotate(
                -45f,
                it.center
            ){
                drawLine(
                    start = it.center,
                    end = it.target,
                    color = path.strokeColor,
                    alpha = path.alpha,
                    colorFilter = path.colorFilter,
                    blendMode = path.blendMode,
                    strokeWidth = path.strokeWidth,
                    cap = path.cap.strokeType,
                    pathEffect = path.pathEffect,
                )
            }
        }
    }
    path.backArrowHead?.let {
        translate(
            vm.canvasOffset.value.x,
            vm.canvasOffset.value.y,
        ) {
            rotate(
                45f,
                it.center
            ){
                drawLine(
                    start = it.center,
                    end = it.target,
                    color = path.strokeColor,
                    alpha = path.alpha,
                    colorFilter = path.colorFilter,
                    blendMode = path.blendMode,
                    strokeWidth = path.strokeWidth,
                    cap = path.cap.strokeType,
                    pathEffect = path.pathEffect,
                )
            }
            rotate(
                -45f,
                it.center
            ){
                drawLine(
                    start = it.center,
                    end = it.target,
                    color = path.strokeColor,
                    alpha = path.alpha,
                    colorFilter = path.colorFilter,
                    blendMode = path.blendMode,
                    strokeWidth = path.strokeWidth,
                    cap = path.cap.strokeType,
                    pathEffect = path.pathEffect,
                )
            }
        }
    }
}

fun createNewPath(vm: WhiteBoxViewModel, path: DrawingPath): Path {
    return Path().apply {
        val points = path.points
        val count = points.size
        val offsetX = vm.canvasOffset.value.x
        val offsetY = vm.canvasOffset.value.y
        if(count>1&&(vm.pathUpdated.value>0L)){
            val first = points.first()
            moveTo(first.x+offsetX,first.y+offsetY)
            for(i in 1 until count){
                val point = points[i]
                lineTo(point.x+offsetX,point.y+offsetY)
            }
        }
    }
}