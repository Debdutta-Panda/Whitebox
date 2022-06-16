package com.vxplore.whitebox

import android.graphics.Paint
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import kotlin.math.abs
import kotlin.math.min


fun DrawScope.Drawing(vm: WhiteBoxViewModel) {
    if(vm.pathUpdated.value>0L){
        with(drawContext.canvas.nativeCanvas) {
            val checkPoint = saveLayer(null, null)
            vm.paths.forEach { it ->
                if(!it.active){
                    return@forEach
                }
                withTransform({
                    it.myMatrix?.let {
                        transform(it)
                    }
                }){
                    when(it.type){
                        ShapeType.PATH -> drawPath(vm,it)
                        ShapeType.LINE -> drawLine(vm,it)
                        ShapeType.LINE_SEGMENT -> TODO()
                        ShapeType.RECTANGLE -> drawRectangle(vm,it)
                        ShapeType.OVAL -> drawOval(vm,it)
                        ShapeType.CIRCLE_WITH_CENTER_AND_RADIUS -> drawCircle(vm,it)
                        ShapeType.CIRCLE_WITH_2_POINT -> drawCircleWith2Point(vm,it)
                        ShapeType.TEXT -> drawText(vm,it)
                        ShapeType.ARC -> drawArc(vm,it)
                    }
                }
            }
            drawEraserIndicator(vm)
            //TestDrawing()
            restoreToCount(checkPoint)
        }
    }
}

fun DrawScope.TestDrawing() {
    val m = Matrix().apply {
        translate(300f,300f)
        rotateZ(145f)
        translate(-300f,-300f)
    }
    withTransform({
        transform(m)
    }){
        drawLine(
            color = Color.Black,
            strokeWidth = 5f,
            start = Offset(200f,200f),
            end = Offset(400f,400f)
        )
    }
    drawLine(
        color = Color.Red,
        strokeWidth = 2f,
        start = m.map(Offset(200f,200f)),
        end = m.map(Offset(400f,400f))
    )
}

fun DrawScope.drawArc(vm: WhiteBoxViewModel,path: DrawingPath){
    val points = path.points
    val count = points.size




    when(count){
        1->{
            drawCircle(
                color = path.bestColor,
                radius = 5f,
                center = points.last()+vm.canvasOffset.value,
                style = Fill
            )
        }
        2->{
            drawCircle(
                color = path.bestColor,
                radius = 5f,
                center = points.first()+vm.canvasOffset.value,
                style = Fill
            )
            drawLine(
                color = path.bestColor,
                start = points.first()+vm.canvasOffset.value,
                end = points.last()+vm.canvasOffset.value,
                strokeWidth = 2f,
            )
        }
        3->{
            val A = points[0]
            val B = points[1]
            val C = points[2]
            val c = distance(A,B)

            val size = 2*c
            val tl = A - Offset(c,c)

            val angle1 = B.angle(A)
            val angle2 = C.angle(A)
            when(path.drawStyleType){
                DrawStyleType.STROKE -> {
                    drawArc(
                        color = path.strokeColor,
                        startAngle = angle1,
                        sweepAngle = intendedSweep(path.sweepShortest,angle2,angle1),
                        useCenter = path.withCenter,
                        topLeft = tl+vm.canvasOffset.value,
                        size = Size(size,size),
                        alpha = path.alpha,
                        style = path.drawStyle?:Fill,
                        colorFilter = path.colorFilter,
                        blendMode = path.blendMode
                    )
                }
                DrawStyleType.FILL -> {
                    drawArc(
                        color = path.fillColor,
                        startAngle = angle1,
                        sweepAngle = intendedSweep(path.sweepShortest,angle2,angle1),
                        useCenter = path.withCenter,
                        topLeft = tl+vm.canvasOffset.value,
                        size = Size(size,size),
                        alpha = path.alpha,
                        style = Fill,
                        colorFilter = path.colorFilter,
                        blendMode = path.blendMode
                    )
                }
                DrawStyleType.BOTH -> {
                    drawArc(
                        color = path.fillColor,
                        startAngle = angle1,
                        sweepAngle = intendedSweep(path.sweepShortest,angle2,angle1),
                        useCenter = path.withCenter,
                        topLeft = tl+vm.canvasOffset.value,
                        size = Size(size,size),
                        alpha = path.alpha,
                        style = Fill,
                        colorFilter = path.colorFilter,
                        blendMode = path.blendMode
                    )
                    drawArc(
                        color = path.strokeColor,
                        startAngle = angle1,
                        sweepAngle = intendedSweep(path.sweepShortest,angle2,angle1),
                        useCenter = path.withCenter,
                        topLeft = tl+vm.canvasOffset.value,
                        size = Size(size,size),
                        alpha = path.alpha,
                        style = path.drawStyle?:Fill,
                        colorFilter = path.colorFilter,
                        blendMode = path.blendMode
                    )
                }
            }
            if(vm.selectedPath.value==path.hashCode()){
                drawArc(
                    color = Color.Black,
                    startAngle = angle1,
                    sweepAngle = intendedSweep(path.sweepShortest,angle2,angle1),
                    useCenter = path.withCenter,
                    topLeft = tl+vm.canvasOffset.value,
                    size = Size(size,size),
                    style = Stroke(width = 4f)
                )
                drawArc(
                    color = Color.Green,
                    startAngle = angle1,
                    sweepAngle = intendedSweep(path.sweepShortest,angle2,angle1),
                    useCenter = path.withCenter,
                    topLeft = tl+vm.canvasOffset.value,
                    size = Size(size,size),
                    style = Stroke(width = 2f)
                )
            }
        }
    }
}

fun intendedSweep(shortest: Boolean, angle2: Float, angle1: Float): Float {
    val s = shortestSweep(angle2,angle1)
    return if(shortest){
        s
    }
    else{
        when (s) {
            in 0f..180f -> s-360
            else -> s+360
        }
    }
}

fun shortestSweep(angle2: Float, angle1: Float): Float {
    val dif = angle2 - angle1
    val absDif = abs(dif)
    val other = 360 - absDif
    val min = min(absDif,other)
    val r = if(min == absDif){
        dif
    }
    else{
        val t = 360 + dif
        if(t>360){
            dif - 360

        }
        else{
            if(dif < -180){
                dif+360
            }
            else{
                dif
            }
        }
    }
    Log.d("fljflsfsdlj","$dif,$r")
    return r
}

fun NativeCanvas.drawText(vm: WhiteBoxViewModel, path: DrawingPath){
    val point = path.points.last()
    val cp = vm.canvasOffset.value
    drawText(path.text?:"",point.x+cp.x,point.y+cp.y,path.paint?:return)
    if(vm.selectedPath.value==path.hashCode()){
        drawText(path.text?:"",point.x+cp.x,point.y+cp.y,Paint(path.paint).apply {
            strokeWidth = 4f
            style = Paint.Style.STROKE
            color = android.graphics.Color.BLACK
        })
        drawText(path.text?:"",point.x+cp.x,point.y+cp.y,Paint(path.paint).apply {
            strokeWidth = 2f
            style = Paint.Style.STROKE
            color = android.graphics.Color.GREEN
        })
    }
}

fun DrawScope.drawCircleWith2Point(vm: WhiteBoxViewModel, path: DrawingPath) {
    val points = path.twoPointData
    val first = points.first
    val second = points.second
    val mid = (first + second)/2f
    when(path.drawStyleType){
        DrawStyleType.STROKE -> {
            drawCircle(
                color = path.strokeColor,
                center = mid+vm.canvasOffset.value,
                radius = distance(mid,path.twoPointData.second),
                colorFilter = path.colorFilter,
                blendMode = path.blendMode,
                alpha = path.alpha,
                style = path.drawStyle?:Fill
            )
        }
        DrawStyleType.FILL -> {
            drawCircle(
                color = path.fillColor,
                center = mid+vm.canvasOffset.value,
                radius = distance(mid,path.twoPointData.second),
                colorFilter = path.colorFilter,
                blendMode = path.blendMode,
                alpha = path.alpha,
                style = Fill
            )
        }
        DrawStyleType.BOTH -> {
            drawCircle(
                color = path.fillColor,
                center = mid+vm.canvasOffset.value,
                radius = distance(mid,path.twoPointData.second),
                colorFilter = path.colorFilter,
                blendMode = path.blendMode,
                alpha = path.alpha,
                style = Fill
            )
            drawCircle(
                color = path.strokeColor,
                center = mid+vm.canvasOffset.value,
                radius = distance(mid,path.twoPointData.second),
                colorFilter = path.colorFilter,
                blendMode = path.blendMode,
                alpha = path.alpha,
                style = path.drawStyle?:Fill
            )
        }
    }
    if(vm.selectedPath.value==path.hashCode()){
        drawCircle(
            color = Color.Black,
            center = mid+vm.canvasOffset.value,
            radius = distance(mid,path.twoPointData.second),
            style = Stroke(
                width = 4f
            )
        )
        drawCircle(
            color = Color.Green,
            center = mid+vm.canvasOffset.value,
            radius = distance(mid,path.twoPointData.second),
            style = Stroke(
                width = 2f
            )
        )
    }
}

fun DrawScope.drawCircle(vm: WhiteBoxViewModel, path: DrawingPath) {
    when(path.drawStyleType){
        DrawStyleType.STROKE -> {
            drawCircle(
                color = path.strokeColor,
                center = path.twoPointData.first+vm.canvasOffset.value,
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
                center = path.twoPointData.first+vm.canvasOffset.value,
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
                center = path.twoPointData.first+vm.canvasOffset.value,
                radius = distance(path.twoPointData.first,path.twoPointData.second),
                colorFilter = path.colorFilter,
                blendMode = path.blendMode,
                alpha = path.alpha,
                style = Fill
            )
            drawCircle(
                color = path.strokeColor,
                center = path.twoPointData.first+vm.canvasOffset.value,
                radius = distance(path.twoPointData.first,path.twoPointData.second),
                colorFilter = path.colorFilter,
                blendMode = path.blendMode,
                alpha = path.alpha,
                style = path.drawStyle?:Fill
            )
        }
    }
    if(vm.selectedPath.value==path.hashCode()){
        drawCircle(
            color = Color.Black,
            center = path.twoPointData.first+vm.canvasOffset.value,
            radius = distance(path.twoPointData.first,path.twoPointData.second),
            style = Stroke(
                width = 4f
            )
        )
        drawCircle(
            color = Color.Green,
            center = path.twoPointData.first+vm.canvasOffset.value,
            radius = distance(path.twoPointData.first,path.twoPointData.second),
            style = Stroke(
                width = 2f
            )
        )
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
    if(vm.selectedPath.value==path.hashCode()){
        drawOval(
            color = Color.Black,
            topLeft = path.twoPointData.first+vm.canvasOffset.value,
            size = Size(gap.x,gap.y),
            style = Stroke(
                width = 4f
            )
        )
        drawOval(
            color = Color.Green,
            topLeft = path.twoPointData.first+vm.canvasOffset.value,
            size = Size(gap.x,gap.y),
            style = Stroke(
                width = 2f
            )
        )
    }
}

fun applyEraser(vm: WhiteBoxViewModel, it: DrawingPath) {

}

fun DrawScope.drawEraserIndicator(vm: WhiteBoxViewModel) {
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
    if(vm.selectedPath.value==path.hashCode()){
        drawRect(
            color = Color.Black,
            topLeft = path.twoPointData.first+vm.canvasOffset.value,
            size = Size(gap.x,gap.y),
            style = Stroke(
                width = 4f
            )
        )
        drawRect(
            color = Color.Green,
            topLeft = path.twoPointData.first+vm.canvasOffset.value,
            size = Size(gap.x,gap.y),
            style = Stroke(
                width = 2f
            )
        )
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
    if(path.hashCode()==vm.selectedPath.value){
        drawLine(
            start = path.twoPointData.first+vm.canvasOffset.value,
            end = path.twoPointData.second+vm.canvasOffset.value,
            color = Color.Black,
            strokeWidth = 4f,
        )
        drawLine(
            start = path.twoPointData.first+vm.canvasOffset.value,
            end = path.twoPointData.second+vm.canvasOffset.value,
            color = Color.Green,
            strokeWidth = 2f,
        )
    }
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
    if(vm.selectedPath.value==path.hashCode()/*&&!path.isEraser*/){
        drawPath(
            createNewPath(vm,path),
            color = Color.Black,
            style = Stroke(
                width = 4f
            )
        )
        drawPath(
            createNewPath(vm,path),
            color = Color.Green,
            style = Stroke(
                width = 2f
            )
        )
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

                if(path.hashCode()==vm.selectedPath.value){
                    drawLine(
                        start = it.center,
                        end = it.target,
                        color = Color.Black,
                        strokeWidth = 4f,
                    )
                    drawLine(
                        start = it.center,
                        end = it.target,
                        color = Color.Green,
                        strokeWidth = 2f,
                    )
                }
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
                if(path.hashCode()==vm.selectedPath.value){
                    drawLine(
                        start = it.center,
                        end = it.target,
                        color = Color.Black,
                        strokeWidth = 4f,
                    )
                    drawLine(
                        start = it.center,
                        end = it.target,
                        color = Color.Green,
                        strokeWidth = 2f,
                    )
                }
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
                if(path.hashCode()==vm.selectedPath.value){
                    drawLine(
                        start = it.center,
                        end = it.target,
                        color = Color.Black,
                        strokeWidth = 4f,
                    )
                    drawLine(
                        start = it.center,
                        end = it.target,
                        color = Color.Green,
                        strokeWidth = 2f,
                    )
                }
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
                if(path.hashCode()==vm.selectedPath.value){
                    drawLine(
                        start = it.center,
                        end = it.target,
                        color = Color.Black,
                        strokeWidth = 4f,
                    )
                    drawLine(
                        start = it.center,
                        end = it.target,
                        color = Color.Green,
                        strokeWidth = 2f,
                    )
                }
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