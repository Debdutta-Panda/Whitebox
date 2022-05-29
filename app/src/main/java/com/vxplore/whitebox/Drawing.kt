package com.vxplore.whitebox

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

fun DrawScope.Drawing(vm: WhiteBoxViewModel) {
    vm.paths.forEach {
        drawPath(vm,it)
    }
}

fun DrawScope.drawPath(vm: WhiteBoxViewModel, path: DrawingPath) {
    drawPath(
        createNewPath(vm,path),
        color = path.color,
        style = Stroke(
            width = path.strokeWidth,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}

fun createNewPath(vm: WhiteBoxViewModel, path: DrawingPath): Path {
    return Path().apply {
        val points = path.points
        val count = points.size
        val offsetX = vm.canvasOffset.value.x
        val offsetY = vm.canvasOffset.value.y
        if(count>1&&vm.pathUpdated.value>0L){
            val first = points.first()
            moveTo(first.x+offsetX,first.y+offsetY)
            for(i in 1 until count){
                val point = points[i]
                lineTo(point.x+offsetX,point.y+offsetY)
            }
        }
    }
}