package com.vxplore.whitebox

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas

private val CapType.strokeType: StrokeCap
    get() {
        return when(this){
            CapType.ROUND -> StrokeCap.Round
            CapType.BUTT -> StrokeCap.Butt
            CapType.SQUARE -> StrokeCap.Square
        }
    }

fun DrawScope.Drawing(vm: WhiteBoxViewModel) {
    with(drawContext.canvas.nativeCanvas) {
        val checkPoint = saveLayer(null, null)
        vm.paths.forEach {
            when(it.type){
                ShapeType.PATH -> drawPath(vm,it)
                ShapeType.LINE -> drawLine(vm,it)
                ShapeType.LINE_SEGMENT -> TODO()
                ShapeType.RECTANGLE -> TODO()
                ShapeType.OVAL -> TODO()
            }
        }
        restoreToCount(checkPoint)
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