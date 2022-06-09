package com.vxplore.whitebox

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.graphics.nativeCanvas



fun DrawScope.Drawing(vm: WhiteBoxViewModel) {
    if(vm.pathUpdated.value>0L){
        with(drawContext.canvas.nativeCanvas) {
            val checkPoint = saveLayer(null, null)
            vm.paths.forEach {
                when(it.type){
                    ShapeType.PATH -> drawPath(vm,it)
                    ShapeType.LINE -> drawLine(vm,it)
                    ShapeType.LINE_SEGMENT -> TODO()
                    ShapeType.RECTANGLE -> drawRectangle(vm,it)
                    ShapeType.OVAL -> drawOval(vm,it)
                    ShapeType.CIRCLE -> drawCircle(vm,it)
                }
            }
            drawEraser(vm)
            restoreToCount(checkPoint)
        }
    }
}

fun DrawScope.drawCircle(vm: WhiteBoxViewModel, path: DrawingPath) {
    val gap = path.twoPointData.second - path.twoPointData.first
    when(path.drawStyleType){
        DrawStyleType.STROKE -> {
            drawCircle(
                color = path.strokeColor,
                center = path.twoPointData.first,
                radius = distance(path.twoPointData.first,path.twoPointData.second),
                colorFilter = path.colorFilter,
                blendMode = path.blendMode,
                alpha = path.alpha,
                style = path.drawStyle?:Fill
            )
        }
        DrawStyleType.FILL -> {
            drawCircle(
                color = path.fillColor,
                center = path.twoPointData.first,
                radius = distance(path.twoPointData.first,path.twoPointData.second),
                colorFilter = path.colorFilter,
                blendMode = path.blendMode,
                alpha = path.alpha,
                style = Fill
            )
        }
        DrawStyleType.BOTH -> {
            drawCircle(
                color = path.fillColor,
                center = path.twoPointData.first,
                radius = distance(path.twoPointData.first,path.twoPointData.second),
                colorFilter = path.colorFilter,
                blendMode = path.blendMode,
                alpha = path.alpha,
                style = Fill
            )
            drawCircle(
                color = path.strokeColor,
                center = path.twoPointData.first,
                radius = distance(path.twoPointData.first,path.twoPointData.second),
                colorFilter = path.colorFilter,
                blendMode = path.blendMode,
                alpha = path.alpha,
                style = path.drawStyle?:Fill
            )
        }
    }
}

fun DrawScope.drawOval(vm: WhiteBoxViewModel, path: DrawingPath) {
    val gap = path.twoPointData.second - path.twoPointData.first
    when(path.drawStyleType){
        DrawStyleType.STROKE -> {
            drawOval(
                color = path.strokeColor,
                topLeft = path.twoPointData.first+vm.canvasOffset.value,
                size = Size(gap.x,gap.y),
                colorFilter = path.colorFilter,
                blendMode = path.blendMode,
                alpha = path.alpha,
                style = path.drawStyle?:Fill
            )
        }
        DrawStyleType.FILL -> {
            drawOval(
                color = path.fillColor,
                topLeft = path.twoPointData.first+vm.canvasOffset.value,
                size = Size(gap.x,gap.y),
                colorFilter = path.colorFilter,
                blendMode = path.blendMode,
                alpha = path.alpha,
                style = Fill
            )
        }
        DrawStyleType.BOTH -> {
            drawOval(
                color = path.fillColor,
                topLeft = path.twoPointData.first+vm.canvasOffset.value,
                size = Size(gap.x,gap.y),
                colorFilter = path.colorFilter,
                blendMode = path.blendMode,
                alpha = path.alpha,
                style = Fill
            )
            drawOval(
                color = path.strokeColor,
                topLeft = path.twoPointData.first+vm.canvasOffset.value,
                size = Size(gap.x,gap.y),
                colorFilter = path.colorFilter,
                blendMode = path.blendMode,
                alpha = path.alpha,
                style = path.drawStyle?:Fill
            )
        }
    }
}

fun applyEraser(vm: WhiteBoxViewModel, it: DrawingPath) {

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
    val gap = path.twoPointData.second - path.twoPointData.first
    when(path.drawStyleType){
        DrawStyleType.STROKE -> {
            drawRect(
                color = path.strokeColor,
                topLeft = path.twoPointData.first+vm.canvasOffset.value,
                size = Size(gap.x,gap.y),
                colorFilter = path.colorFilter,
                blendMode = path.blendMode,
                alpha = path.alpha,
                style = path.drawStyle?:Fill
            )
        }
        DrawStyleType.FILL -> {
            drawRect(
                color = path.fillColor,
                topLeft = path.twoPointData.first+vm.canvasOffset.value,
                size = Size(gap.x,gap.y),
                colorFilter = path.colorFilter,
                blendMode = path.blendMode,
                alpha = path.alpha,
                style = Fill
            )
        }
        DrawStyleType.BOTH -> {
            drawRect(
                color = path.fillColor,
                topLeft = path.twoPointData.first+vm.canvasOffset.value,
                size = Size(gap.x,gap.y),
                colorFilter = path.colorFilter,
                blendMode = path.blendMode,
                alpha = path.alpha,
                style = Fill
            )
            drawRect(
                color = path.strokeColor,
                topLeft = path.twoPointData.first+vm.canvasOffset.value,
                size = Size(gap.x,gap.y),
                colorFilter = path.colorFilter,
                blendMode = path.blendMode,
                alpha = path.alpha,
                style = path.drawStyle?:Fill
            )
        }
    }
}

fun DrawScope.drawLine(vm: WhiteBoxViewModel, path: DrawingPath){
    drawLine(
        start = path.twoPointData.first+vm.canvasOffset.value,
        end = path.twoPointData.second+vm.canvasOffset.value,
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

fun DrawScope.drawPath(vm: WhiteBoxViewModel, path: DrawingPath) {
    when(path.drawStyleType){
        DrawStyleType.BOTH->{
            drawPath(
                createNewPath(vm,path),
                color = path.fillColor,
                style = Fill,
                alpha = path.alpha,
                colorFilter = path.colorFilter,
                blendMode = path.blendMode
            )
            drawPath(
                createNewPath(vm,path),
                color = path.strokeColor,
                style = path.drawStyle?:Fill,
                alpha = path.alpha,
                colorFilter = path.colorFilter,
                blendMode = path.blendMode
            )
        }
        DrawStyleType.STROKE->{
            drawPath(
                createNewPath(vm,path),
                color = path.strokeColor,
                style = path.drawStyle?:Fill,
                alpha = path.alpha,
                colorFilter = path.colorFilter,
                blendMode = path.blendMode
            )
        }
        DrawStyleType.FILL->{
            drawPath(
                createNewPath(vm,path),
                color = path.fillColor,
                style = Fill,
                alpha = path.alpha,
                colorFilter = path.colorFilter,
                blendMode = path.blendMode
            )
        }
    }
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