package com.vxplore.whitebox

import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.KeyEvent
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.core.math.MathUtils.clamp
import androidx.lifecycle.ViewModel
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

val StrokeJoinType.stokeJoin: StrokeJoin
    get() {
        return when(this){
            StrokeJoinType.BEVEL -> StrokeJoin.Bevel
            StrokeJoinType.MITER -> StrokeJoin.Miter
            StrokeJoinType.ROUND -> StrokeJoin.Round
        }
    }

class WhiteBoxViewModel: ViewModel() {
    private val touchRadius = mutableStateOf(20f)
    val layerDeleteDialogOpen = mutableStateOf(false)
    val editingPath = mutableStateOf<DrawingPath?>(null)
    val layersWindowOpen = mutableStateOf(false)
    val selectedPath = mutableStateOf(0)
    private var arcPressCount = 0
    private var arcWithCenterPressCount = 0
    val sweepShortestWithCenter = mutableStateOf(true)
    val sweepShortest = mutableStateOf(true)
    private var lineSelected = 0L
    val snapToGrid = mutableStateOf(false)
    private val keyboardEnabled = mutableStateOf(true)
    private val textPos = mutableStateOf(Offset.Zero)
    val textDialog = mutableStateOf(false)
    private val dotSize = mutableStateOf(10f)
    val penPathAdded = mutableStateOf(false)
    val fillAlpha = mutableStateOf(1f)
    val strokeAlpha = mutableStateOf(1f)
    val fillColor = mutableStateOf(Color.Black)
    val sheetColor = mutableStateOf(Color.White)
    val eraserIndicatorAlpha = mutableStateOf(1f)
    val eraserIndicatorColor = mutableStateOf(Color.LightGray)
    val eraserPos = mutableStateOf(Offset.Zero)
    val showEraser = mutableStateOf(false)
    val join = mutableStateOf(StrokeJoinType.MITER)
    val miter = mutableStateOf(0f)
    val drawStyleType = mutableStateOf(DrawStyleType.STROKE)
    val forwardArrowHead = mutableStateOf(false)
    val backwardArrowHead = mutableStateOf(false)
    val capType = mutableStateOf(CapType.ROUND)
    val lineType = mutableStateOf(LineType.CONTINUOUS)
    val dashedIntervals = mutableStateOf(Constants.dashedIntervals)
    val dashedPhase = mutableStateOf(Constants.dashedPhase)
    private val currentPathEffect: PathEffect?
    get(){
        return when(lineType.value){
            LineType.CONTINUOUS -> null
            LineType.DASHED -> PathEffect.dashPathEffect(dashedIntervals.value,dashedPhase.value)
        }
    }
    ///////////////////////////////////////////
    val gridMode = mutableStateOf(GridMode.XY)
    //////////////////////////////
    private val eraserColor = mutableStateOf(Color.Transparent)
    //////////////////////////////
    val toolAlpha = mutableStateOf(Constants.toolAlpha)
    val toolColor = mutableStateOf(Color(Constants.toolColor))
    val selectedToolColor = mutableStateOf(Color(Constants.selectedToolColor))
    ///////////////////////////
    val canvasOffset = mutableStateOf(Offset.Zero)
    ////////////////////////
    val gridGap = mutableStateOf(Constants.gridGap)
    ////////////////////////////
    val xGridStroke = mutableStateOf(Constants.xGridStroke)
    val yGridStroke = mutableStateOf(Constants.yGridStroke)
    //////////////////////////////
    val yGridColor = mutableStateOf(Color(Constants.yGridColor))
    val xGridColor = mutableStateOf(Color(Constants.xGridColor))
    //////////////////////////////
    val tool = mutableStateOf(Tool.MOVE)
    val strokeColor = mutableStateOf(Color.Black)
    val stroke = mutableStateOf(Constants.strokeWidth)
    val pathUpdated = mutableStateOf(0L)

    val alpha = mutableStateOf(Constants.alpha)
    val colorFilter = mutableStateOf(Constants.colorFilter)
    val blendMode = mutableStateOf(Constants.penBlendMode)

    val paths = mutableStateListOf<DrawingPath>()
    //////////////////////////

    fun drag(dragAmount: Offset, offset: Offset) {
        try {
            when(tool.value){
                Tool.MOVE -> handleMoveDrag(dragAmount)
                Tool.FREE_HAND -> handleFreeHandDrag(dragAmount)
                Tool.ERASER -> handleEraserDrag(dragAmount)
                Tool.CLEAN -> {}
                Tool.HIGHLIGHTER -> handleHighlighterDrag(dragAmount)
                Tool.HORIZONTAL_LINE -> handleHLineDrag(dragAmount)
                Tool.VERTICAL_LINE -> handleVerticalDrag(dragAmount)
                Tool.LINE -> handleLineDrag(dragAmount)
                Tool.RECTANGLE -> handleDragRectangle(dragAmount)
                Tool.OVAL -> handleDragOval(dragAmount)
                Tool.CIRCLE_WITH_CENTER_AND_RADIUS -> handleDragCircle(dragAmount)
                Tool.CIRCLE_WITH_TWO_POINTS -> handleDragCircle2Point(dragAmount)
                Tool.PEN -> handlePenDrag(offset)
            }
        } catch (e: Exception) {
        }
    }

    private fun handlePenDrag(offset: Offset) {
        paths.last().points[paths.last().points.size-1] = offset-canvasOffset.value
        updateArrowHeads()
        pathUpdated.value = System.currentTimeMillis()
    }

    private fun handleDragCircle2Point(dragAmount: Offset) {
        var line = paths.last().twoPointData
        line = Pair(line.first,line.second+dragAmount)
        paths.last().twoPointData = line
        pathUpdated.value = System.currentTimeMillis()
    }

    private fun handleDragCircle(dragAmount: Offset) {
        var line = paths.last().twoPointData
        line = Pair(line.first,line.second+dragAmount)
        paths.last().twoPointData = line
        pathUpdated.value = System.currentTimeMillis()
    }

    private fun handleDragOval(dragAmount: Offset) {
        var line = paths.last().twoPointData
        line = Pair(line.first,line.second+dragAmount)
        paths.last().twoPointData = line
        pathUpdated.value = System.currentTimeMillis()
    }

    private fun handleDragRectangle(dragAmount: Offset) {
        var line = paths.last().twoPointData
        line = Pair(line.first,line.second+dragAmount)
        paths.last().twoPointData = line
        pathUpdated.value = System.currentTimeMillis()
    }

    private fun handleLineDrag(dragAmount: Offset) {
        var line = paths.last().twoPointData
        line = Pair(line.first,line.second+dragAmount)
        paths.last().twoPointData = line
        updateArrowHeads()
        pathUpdated.value = System.currentTimeMillis()
    }

    private fun updateArrowHeads() {
        if(forwardArrowHead.value){
            val lastTwoPoints = getLastTwoPointsOfLastPath()
            lastTwoPoints?.let {
                val f = it.first
                val s = it.second
                val d = distance(f,s)
                val n = Constants.arrowDistance
                val m = d - n
                val x1 = f.x
                val y1 = f.y
                val x2 = s.x
                val y2= s.y
                val a = Offset((m*x2+n*x1)/d,(m*y2+n*y1)/d)
                paths.last().forwardArrowHead = ArrowHead(s,a)
            }
        }
        else{
            paths.last().forwardArrowHead = null
        }
        if(backwardArrowHead.value){
            val firstTwoPoints = getFirstTwoPointsOfLastPath()
            firstTwoPoints?.let {
                val f = it.first
                val s = it.second
                val d = distance(f,s)
                val n = Constants.arrowDistance
                val m = d - n
                val x1 = f.x
                val y1 = f.y
                val x2 = s.x
                val y2= s.y
                val a = Offset((m*x2+n*x1)/d,(m*y2+n*y1)/d)
                paths.last().backArrowHead = ArrowHead(s,a)
            }
        }
        else{
            paths.last().backArrowHead = null
        }
    }

    private fun getFirstTwoPointsOfLastPath(): Pair<Offset, Offset>? {
        val lastPath = paths.last()
        when(lastPath.type){
            ShapeType.PATH -> {
                val size = lastPath.points.size
                if(size>1){
                    return Pair(
                        lastPath.points[1],
                        lastPath.points[0]
                    )
                }
            }
            ShapeType.LINE -> {
                return Pair(lastPath.twoPointData.second,lastPath.twoPointData.first)
            }
            ShapeType.LINE_SEGMENT -> TODO()
            ShapeType.RECTANGLE -> TODO()
            ShapeType.OVAL -> TODO()
        }
        return null
    }


    private fun getLastTwoPointsOfLastPath(): Pair<Offset, Offset>? {
        val lastPath = paths.last()
        when(lastPath.type){
            ShapeType.PATH -> {
                val size = lastPath.points.size
                if(size>1){
                    return Pair(
                        lastPath.points[size-2],
                        lastPath.points[size-1]
                    )
                }
            }
            ShapeType.LINE -> {
                return Pair(lastPath.twoPointData.first,lastPath.twoPointData.second)
            }
            ShapeType.LINE_SEGMENT -> TODO()
            ShapeType.RECTANGLE -> TODO()
            ShapeType.OVAL -> TODO()
        }
        return null
    }

    private fun handleVerticalDrag(dragAmount: Offset) {
        var line = paths.last().twoPointData
        line = Pair(line.first,Offset(line.second.x,line.second.y+dragAmount.y))
        paths.last().twoPointData = line
        updateArrowHeads()
        pathUpdated.value = System.currentTimeMillis()
    }

    private fun handleHLineDrag(dragAmount: Offset) {
        var line = paths.last().twoPointData
        line = Pair(line.first,Offset(line.second.x+dragAmount.x,line.second.y))
        paths.last().twoPointData = line
        updateArrowHeads()
        pathUpdated.value = System.currentTimeMillis()
    }

    private fun handleHighlighterDrag(dragAmount: Offset) {
        val last = paths.last().points.last()
        val new = last + dragAmount
        paths.last().points.add(new)
        pathUpdated.value = System.currentTimeMillis()
    }

    private fun handleEraserDrag(dragAmount: Offset) {
        val last = paths.last().points.last()
        val new = last + dragAmount
        paths.last().points.add(new)
        eraserPos.value = new
        pathUpdated.value = System.currentTimeMillis()
    }

    private fun handleFreeHandDrag(dragAmount: Offset) {
        val last = paths.last().points.last()
        val new = last + dragAmount
        paths.last().points.add(new)
        updateArrowHeads()
        pathUpdated.value = System.currentTimeMillis()
    }

    private fun handleMoveDrag(dragAmount: Offset) {
        canvasOffset.value = Offset(canvasOffset.value.x+dragAmount.x,canvasOffset.value.y+dragAmount.y)
    }

    fun setTool(newTool: Tool) {
        if(newTool==Tool.ARC&&newTool==tool.value){
            sweepShortest.toggle()
            return
        }
        if(newTool==Tool.ARC_WITH_CENTER&&newTool==tool.value){
            sweepShortestWithCenter.toggle()
            return
        }
        if(newTool==Tool.CLEAN){
            paths.clear()
            pathUpdated.value = System.currentTimeMillis()
            penPathAdded.value = false
            arcPressCount = 0
            arcWithCenterPressCount = 0
            return
        }
        tool.value = newTool
        if(newTool==Tool.PEN){
            onPenToolSelected()
        }
        if(tool.value==Tool.LINE){
            lineSelected = System.currentTimeMillis()
        }
    }

    private fun onPenToolSelected() {
        penPathAdded.value = false
    }

    fun dragStart(offset: Offset) {
        when(tool.value){
            Tool.MOVE -> handleMoveDragStart(offset)
            Tool.FREE_HAND -> handleFreeHandDragStart(offset)
            Tool.ERASER -> handleEraserDragStart(offset)
            Tool.CLEAN -> {}
            Tool.HIGHLIGHTER -> handleHighlighterDragStart(offset)
            Tool.HORIZONTAL_LINE -> handleHLineDragStart(offset)
            Tool.VERTICAL_LINE -> handleVerticalDragStart(offset)
            Tool.LINE -> handleLineDragStart(offset)
            Tool.RECTANGLE -> handleRectangleDragStart(offset)
            Tool.OVAL -> handleOvalDragStart(offset)
            Tool.CIRCLE_WITH_CENTER_AND_RADIUS -> handleCircleDragStart(offset)
            Tool.CIRCLE_WITH_TWO_POINTS -> handle2PointCircleDragStart(offset)
            Tool.PEN -> handlePenDragStart(offset)
            Tool.DOT -> handleDot(offset)
            Tool.TEXT -> handleTextStart(offset)
            Tool.ARC -> handleArcStart(offset)
            Tool.ARC_WITH_CENTER -> handleArcWithCenterStart(offset)
            Tool.TRANSFORM -> handleTransformStart(offset)
        }
    }

    private fun handleTransformStart(offset: Offset) {
        val effectivePoint = offset - canvasOffset.value
        val size = paths.size
        var finalPath:DrawingPath? = null
        for(index in paths.indices){
            val path = paths[size - index - 1]
            val found: Boolean = hit(path,path.inverseMap(effectivePoint))
            if(found){
                finalPath = path
                break
            }
        }
        selectPath(finalPath)
    }

    private fun hit(path: DrawingPath, effectivePoint: Offset): Boolean {
        return when(path.type){
            ShapeType.PATH -> hitPath(effectivePoint,path)
            ShapeType.LINE -> hitLine(
                effectivePoint,
                path.twoPointData.first,
                path.twoPointData.second)
            ShapeType.LINE_SEGMENT -> false
            ShapeType.RECTANGLE -> hitRectangle(effectivePoint,path)
            ShapeType.OVAL -> hitOval(effectivePoint,path)
            ShapeType.CIRCLE_WITH_CENTER_AND_RADIUS -> hitCircle(effectivePoint,path)
            ShapeType.CIRCLE_WITH_2_POINT -> hitCircleWith2Point(effectivePoint,path)
            ShapeType.TEXT -> hitText(effectivePoint,path)
            ShapeType.ARC -> hitArc(effectivePoint,path)
        }
    }

    private fun hitText(ep: Offset, path: DrawingPath): Boolean {
        val bounds = Rect()
        path.paint?.getTextBounds(path.text, 0, path.text?.length?:0, bounds)

        val h = bounds.height()
        val w = bounds.width()

        val p = path.points.last()
        val point = p - Offset(0f,h.toFloat())

        return ep.x>=point.x-touchRadius.value&&ep.x<=point.x+w+touchRadius.value&&ep.y>=point.y-touchRadius.value&&ep.y<=point.y+h+touchRadius.value
    }

    private fun hitCircleWith2Point(effectivePoint: Offset, path: DrawingPath): Boolean {
        val f = path.twoPointData.first
        val s = path.twoPointData.second
        val c = (f + s)/2f
        val r = distance(c,f)
        val d = distance(c,effectivePoint)
        return d<=r+touchRadius.value
    }

    private fun hitCircle(effectivePoint: Offset, path: DrawingPath): Boolean {
        val center = path.twoPointData.first
        val point = path.twoPointData.second
        val radius = distance(center,point)
        val distance = distance(center,effectivePoint)
        return distance<=radius+touchRadius.value
    }

    private fun hitArc(effectivePoint: Offset, path: DrawingPath): Boolean {
        val c = path.points.first()
        val start = path.points[1]
        val r = distance(c,start)
        val end = onDistance(path.points.last(),c,r)
        val angle1 = start.angle(c)
        val angle2 = end.angle(c)
        val angle = effectivePoint.angle(c)
        val d = distance(effectivePoint,c)
        val insideShortest = angle.inside(angle1,angle2)
        if(path.sweepShortest){
            if(path.withCenter){
                return d<=r && insideShortest
            }
            return (d in (r-touchRadius.value)..(r+touchRadius.value)) && insideShortest
        }
        else{
            if(path.withCenter){
                return d<=r && !insideShortest
            }
            return (d in (r-touchRadius.value)..(r+touchRadius.value)) && !insideShortest
        }
    }

    private fun onDistance(original: Offset, start: Offset, distance: Float): Offset {
        val m = distance
        val n = distance(original,start) - distance
        if(m+n==0f){
            return original
        }
        return (original*m + start*n)/(m+n)
    }

    private fun hitOval(effectivePoint: Offset, path: DrawingPath): Boolean {
        val extra = Offset(touchRadius.value,touchRadius.value)
        val tl = path.twoPointData.first-extra
        val br = path.twoPointData.second+extra
        val s = br - tl
        val c = (br+tl)/2f
        val w = abs(s.x)
        val h = abs(s.y)
        val a = w/2f
        val b = h/2f
        val x = effectivePoint.x - c.x
        val y = effectivePoint.y - c.y
        val aSquare = a.square()
        val bSquare = b.square()
        val left = x.square()*bSquare+y.square()*aSquare
        val right = aSquare*bSquare
        return left<=right
    }

    private fun hitRectangle(
        effectivePoint: Offset,
        path: DrawingPath
    ): Boolean {
        val offset = Offset(touchRadius.value,touchRadius.value)
        val end1 = path.twoPointData.first-offset
        val end2 = path.twoPointData.second+offset
        return effectivePoint.x>=end1.x
               &&effectivePoint.x<=end2.x
                &&effectivePoint.y>=end1.y
                &&effectivePoint.y<=end2.y
    }

    private fun hitPath(
        effectivePoint: Offset,
        path: DrawingPath,
    ): Boolean {
        val count = path.points.size
        for(index in 0..count-2){
            val end1 = path.points[index]
            val end2 = path.points[index+1]
            val touching = hitLine(effectivePoint,end1,end2)
            if(touching){
                return true
            }
        }
        return false
    }

    private fun hitLine(effectivePoint: Offset, end1: Offset, end2: Offset): Boolean {
        return isTouching(effectivePoint, end1, end2, touchRadius.value)
    }

    private fun isTouching(pointer: Offset, end1:Offset, end2:Offset, touchRadius: Float):Boolean{
        val a = distance(pointer,end1)
        val b = distance(pointer,end2)
        val c = distance(end2,end1)

        val c1 = (a.square()-b.square()+c.square())/(2*c)
        val c2 = c - c1

        val d = sqrt(a.square() - c1.square())
        return d<=touchRadius && c1<c && c2<c
    }

    private fun selectPath(path: DrawingPath?) {
        selectedPath.value = if(path==null) 0 else path.hashCode()
        pathUpdated.value = System.currentTimeMillis()
        /////////////////
        /*val points = floatArrayOf(0f,0f,100f,100f)
        val ePoints = FloatArray(4)
        val m = android.graphics.Matrix()
        m.setRotate(90f,50f,50f)
        m.mapPoints(ePoints, points)
        val a = ePoints.size
        Log.d("fdfdfd",a.toString())*/
    }

    private fun handleArcWithCenterStart(offset: Offset) {
        ++arcWithCenterPressCount
        if(arcWithCenterPressCount==1){
            val path = DrawingPath(
                Constants.displayNames.arcWithCenter,
                strokeColor = strokeColor.value.copy(alpha = strokeAlpha.value),
                strokeWidth = stroke.value,
                colorFilter = null,
                blendMode = Constants.penBlendMode,
                type = ShapeType.ARC,
                pathEffect = currentPathEffect,
                cap = capType.value,
                drawStyle = getDrawStyle(),
                fillColor = fillColor.value.copy(alpha = fillAlpha.value),
                drawStyleType = drawStyleType.value,
                sweepShortest = sweepShortestWithCenter.value,
                withCenter = true
            )
            path.points.add(offset-canvasOffset.value)
            paths.add(path)
        }
        if(arcWithCenterPressCount==2){
            paths.last().points.add(offset-canvasOffset.value)
        }
        if(arcWithCenterPressCount==3){
            paths.last().points.add(offset-canvasOffset.value)
            arcWithCenterPressCount = 0
        }
        pathUpdated.value = System.currentTimeMillis()
    }

    private fun handleArcStart(offset: Offset) {
        ++arcPressCount
        if(arcPressCount==1){
            val path = DrawingPath(
                Constants.displayNames.arc,
                strokeColor = strokeColor.value.copy(alpha = strokeAlpha.value),
                strokeWidth = stroke.value,
                colorFilter = null,
                blendMode = Constants.penBlendMode,
                type = ShapeType.ARC,
                pathEffect = currentPathEffect,
                cap = capType.value,
                drawStyle = getDrawStyle(),
                fillColor = fillColor.value.copy(alpha = fillAlpha.value),
                drawStyleType = drawStyleType.value,
                sweepShortest = sweepShortest.value
            )
            path.points.add(offset-canvasOffset.value)
            paths.add(path)
        }
        if(arcPressCount==2){
            paths.last().points.add(offset-canvasOffset.value)
        }
        if(arcPressCount==3){
            paths.last().points.add(offset-canvasOffset.value)
            arcPressCount = 0
        }
        pathUpdated.value = System.currentTimeMillis()
    }

    private fun handleTextStart(offset: Offset) {
        textPos.value = offset
        textDialog.value = true
    }

    private fun handleDot(offset: Offset) {
        val path = DrawingPath(
            Constants.displayNames.dot,
            strokeColor = strokeColor.value.copy(alpha = strokeAlpha.value),
            strokeWidth = stroke.value,
            colorFilter = null,
            blendMode = Constants.penBlendMode,
            type = ShapeType.CIRCLE_WITH_CENTER_AND_RADIUS,
            pathEffect = currentPathEffect,
            cap = capType.value,
            drawStyle = getDrawStyle(),
            fillColor = fillColor.value.copy(alpha = fillAlpha.value),
            drawStyleType = drawStyleType.value
        )
        path.twoPointData = Pair(offset-canvasOffset.value,offset-canvasOffset.value+Offset(dotSize.value,0f))
        paths.add(path)
        pathUpdated.value = System.currentTimeMillis()
    }

    private fun handlePenDragStart(offset: Offset) {
        if(!penPathAdded.value){
            val path = DrawingPath(
                Constants.displayNames.pen,
                strokeColor = strokeColor.value.copy(alpha = strokeAlpha.value),
                fillColor = fillColor.value.copy(alpha = fillAlpha.value),
                strokeWidth = stroke.value,
                alpha = alpha.value,
                colorFilter = colorFilter.value,
                blendMode = blendMode.value,
                pathEffect = currentPathEffect,
                cap = capType.value,
                drawStyle = getDrawStyle(),
                drawStyleType = drawStyleType.value
            )
            paths.add(path)
            pathUpdated.value = System.currentTimeMillis()
            penPathAdded.value = true
            paths.last().points.add(offset-canvasOffset.value)
        }
        paths.last().points.add(offset-canvasOffset.value)
        pathUpdated.value = System.currentTimeMillis()
    }

    private fun handle2PointCircleDragStart(offset: Offset) {
        val path = DrawingPath(
            Constants.displayNames._2PointCircle,
            strokeColor = strokeColor.value.copy(alpha = strokeAlpha.value),
            strokeWidth = stroke.value,
            colorFilter = null,
            blendMode = Constants.penBlendMode,
            type = ShapeType.CIRCLE_WITH_2_POINT,
            pathEffect = currentPathEffect,
            cap = capType.value,
            drawStyle = getDrawStyle(),
            fillColor = fillColor.value.copy(alpha = fillAlpha.value),
            drawStyleType = drawStyleType.value
        )
        path.twoPointData = Pair(offset-canvasOffset.value,offset-canvasOffset.value)
        paths.add(path)
    }

    private fun handleCircleDragStart(offset: Offset) {
        val path = DrawingPath(
            Constants.displayNames.circle,
            strokeColor = strokeColor.value.copy(alpha = strokeAlpha.value),
            strokeWidth = stroke.value,
            colorFilter = null,
            blendMode = Constants.penBlendMode,
            type = ShapeType.CIRCLE_WITH_CENTER_AND_RADIUS,
            pathEffect = currentPathEffect,
            cap = capType.value,
            drawStyle = getDrawStyle(),
            fillColor = fillColor.value.copy(alpha = fillAlpha.value),
            drawStyleType = drawStyleType.value
        )
        path.twoPointData = Pair(offset-canvasOffset.value,offset-canvasOffset.value)
        paths.add(path)
    }

    private fun handleOvalDragStart(offset: Offset) {
        val path = DrawingPath(
            Constants.displayNames.oval,
            strokeColor = strokeColor.value.copy(alpha = strokeAlpha.value),
            strokeWidth = stroke.value,
            colorFilter = null,
            blendMode = Constants.penBlendMode,
            type = ShapeType.OVAL,
            pathEffect = currentPathEffect,
            cap = capType.value,
            drawStyle = getDrawStyle(),
            fillColor = fillColor.value.copy(alpha = fillAlpha.value),
            drawStyleType = drawStyleType.value
        )
        path.twoPointData = Pair(offset-canvasOffset.value,offset-canvasOffset.value)
        paths.add(path)
    }

    private fun handleRectangleDragStart(offset: Offset) {
        val path = DrawingPath(
            Constants.displayNames.rectangle,
            strokeColor = strokeColor.value.copy(alpha = strokeAlpha.value),
            strokeWidth = stroke.value,
            colorFilter = null,
            blendMode = Constants.penBlendMode,
            type = ShapeType.RECTANGLE,
            pathEffect = currentPathEffect,
            cap = capType.value,
            drawStyle = getDrawStyle(),
            fillColor = fillColor.value.copy(alpha = fillAlpha.value),
            drawStyleType = drawStyleType.value
        )
        path.twoPointData = Pair(offset-canvasOffset.value,offset-canvasOffset.value)
        paths.add(path)
    }

    private fun getDrawStyle(): DrawStyle {
        return if(
            drawStyleType.value==DrawStyleType.STROKE
            ||drawStyleType.value==DrawStyleType.BOTH
                ){
            Stroke(
                width = stroke.value,
                miter = miter.value,
                cap = capType.value.strokeType,
                join = join.value.stokeJoin,
                pathEffect = currentPathEffect
            )
        } else{
            Fill
        }
    }

    private fun handleLineDragStart(offset: Offset) {
        val path = DrawingPath(
            Constants.displayNames.line,
            strokeColor = strokeColor.value.copy(alpha = strokeAlpha.value),
            strokeWidth = stroke.value,
            colorFilter = null,
            blendMode = Constants.penBlendMode,
            type = ShapeType.LINE,
            pathEffect = currentPathEffect,
            cap = capType.value
        )
        path.twoPointData = Pair(offset-canvasOffset.value,offset-canvasOffset.value)
        paths.add(path)
    }

    private fun handleVerticalDragStart(offset: Offset) {
        val path = DrawingPath(
            Constants.displayNames.verticalLine,
            strokeColor = strokeColor.value.copy(alpha = strokeAlpha.value),
            strokeWidth = stroke.value,
            colorFilter = null,
            blendMode = Constants.penBlendMode,
            type = ShapeType.LINE,
            pathEffect = currentPathEffect,
            cap = capType.value
        )
        path.twoPointData = Pair(offset-canvasOffset.value,offset-canvasOffset.value)
        paths.add(path)
    }

    private fun handleHLineDragStart(offset: Offset) {
        val path = DrawingPath(
            Constants.displayNames.horizontalLine,
            strokeColor = strokeColor.value.copy(alpha = strokeAlpha.value),
            strokeWidth = stroke.value,
            colorFilter = null,
            blendMode = Constants.penBlendMode,
            type = ShapeType.LINE,
            pathEffect = currentPathEffect,
            cap = capType.value
        )
        path.twoPointData = Pair(offset-canvasOffset.value,offset-canvasOffset.value)
        paths.add(path)
    }

    private fun handleHighlighterDragStart(offset: Offset) {
        val path = DrawingPath(
            Constants.displayNames.highlighter,
            strokeColor = strokeColor.value.copy(alpha = strokeAlpha.value),
            strokeWidth = stroke.value,
            colorFilter = null,
            blendMode = Constants.highlighterBlendMode,
            pathEffect = currentPathEffect,
            cap = capType.value,
            drawStyle = getDrawStyle(),
            drawStyleType = drawStyleType.value
        )
        path.points.add(offset-canvasOffset.value)
        paths.add(path)
    }

    private fun handleEraserDragStart(offset: Offset) {
        showEraser.value = true
        val path = DrawingPath(
            Constants.displayNames.eraser,
            strokeColor = eraserColor.value,
            fillColor = eraserColor.value,
            strokeWidth = stroke.value,
            colorFilter = null,
            blendMode = Constants.eraserBlendMode,
            pathEffect = currentPathEffect,
            cap = capType.value,
            drawStyle = getDrawStyle(),
            drawStyleType = drawStyleType.value,
            isEraser = true
        )
        eraserPos.value = offset-canvasOffset.value
        path.points.add(offset-canvasOffset.value)
        paths.add(path)
    }

    private fun handleMoveDragStart(offset: Offset) {

    }

    private fun handleFreeHandDragStart(offset: Offset) {
        val path = DrawingPath(
            Constants.displayNames.freeHand,
            strokeColor = strokeColor.value.copy(alpha = strokeAlpha.value),
            fillColor = fillColor.value.copy(alpha = fillAlpha.value),
            strokeWidth = stroke.value,
            alpha = alpha.value,
            colorFilter = colorFilter.value,
            blendMode = blendMode.value,
            pathEffect = currentPathEffect,
            cap = capType.value,
            drawStyle = getDrawStyle(),
            drawStyleType = drawStyleType.value
        )
        path.points.add(offset-canvasOffset.value)
        paths.add(path)
    }

    fun dragEnd() {
        showEraser.value = false
    }

    fun dragCancel() {
        showEraser.value = false
    }

    fun setColor(it: Color) {
        strokeColor.value = it
    }

    fun switchGrid() {
        gridMode.value = gridMode.value.next()
    }

    fun switchLineType() {
        lineType.value = lineType.value.next()
    }

    fun switchCapType() {
        capType.value = capType.value.next()
    }

    fun toggleForwardArrowHead() {
        forwardArrowHead.toggle()
    }

    fun toggleBackwardArrowHead() {
        backwardArrowHead.toggle()
    }

    fun setFillColor(it: Color) {
        fillColor.value = it
    }

    fun setDrawStyleType(type: DrawStyleType) {
        drawStyleType.value = type
    }

    fun onCloseCurrentPath() {
        onPenToolSelected()
    }

    fun onTextDialogDismissRequest() {
        textPos.value = Offset.Zero
        textDialog.value = false
    }

    fun onTextDialogCancel() {
        onTextDialogDismissRequest()
    }

    fun onTextDialogDone(text: String) {
        textDialog.value = false
        if(text.isEmpty()){
            return
        }
        paths.add(
            DrawingPath(
                Constants.displayNames.text,
                strokeColor = strokeColor.value.copy(alpha = strokeAlpha.value),
                strokeWidth = stroke.value,
                points = mutableListOf(textPos.value-canvasOffset.value),
                alpha = alpha.value,
                colorFilter = colorFilter.value,
                blendMode = blendMode.value,
                type = ShapeType.TEXT,
                pathEffect = currentPathEffect,
                cap = capType.value,
                fillColor = fillColor.value.copy(alpha = fillAlpha.value),
                drawStyle = getDrawStyle(),
                drawStyleType = drawStyleType.value,
                text = text,
                paint = android.text.TextPaint().apply {
                    this.isAntiAlias = true
                    this.style = when(drawStyleType.value){
                        DrawStyleType.STROKE -> Paint.Style.STROKE
                        DrawStyleType.FILL -> Paint.Style.FILL
                        DrawStyleType.BOTH -> Paint.Style.FILL_AND_STROKE
                    }
                    this.textSize = stroke.value*10
                    val c = strokeColor.value.copy(alpha = strokeAlpha.value)
                    this.color = android.graphics.Color.parseColor("#"+getHexColorValue(c.alpha,c.red,c.green,c.blue))
                }
            )
        )
        pathUpdated.value = System.currentTimeMillis()
    }

    fun onKeyDown(var1: Int, var2: KeyEvent?): Boolean {
        Log.d("fsffsfs","$var1")
        if(!keyboardEnabled.value){
            return false
        }
        when(var1){
            KeyEvent.KEYCODE_NUMPAD_ADD->{
                stroke.value = clamp(stroke.value + 1,0f,Constants.maxStroke)
            }
            KeyEvent.KEYCODE_NUMPAD_SUBTRACT->{
                stroke.value = clamp(stroke.value - 1,0f,Constants.maxStroke)
            }
        }
        return true
    }

    fun onKeyLongPress(var1: Int, var2: KeyEvent?): Boolean {
        if(!keyboardEnabled.value){
            return false
        }
        return true
    }

    fun onKeyMultiple(var1: Int, var2: Int, var3: KeyEvent?): Boolean {
        if(!keyboardEnabled.value){
            return false
        }
        return true
    }


    fun onKeyUp(var1: Int, var2: KeyEvent?): Boolean {
        Log.d("ffljlfjdflj","$var1")
        if(!keyboardEnabled.value){
            return false
        }
        if(var1==KeyEvent.KEYCODE_3&&(var2?.isShiftPressed==true)){
            switchGrid()
            return true
        }
        if(var1==KeyEvent.KEYCODE_M){
            setTool(Tool.MOVE)
            return true
        }
        if(var1==KeyEvent.KEYCODE_F){
            setTool(Tool.FREE_HAND)
            return true
        }
        if(var1==KeyEvent.KEYCODE_T){
            setTool(Tool.TEXT)
            return true
        }
        if(var1==KeyEvent.KEYCODE_E){
            setTool(Tool.ERASER)
            return true
        }
        if(var1==KeyEvent.KEYCODE_X){
            setTool(Tool.CLEAN)
            return true
        }
        if(var1==KeyEvent.KEYCODE_H){
            if(tool.value==Tool.LINE&&(System.currentTimeMillis()-lineSelected)<=2000){
                setTool(Tool.HORIZONTAL_LINE)
                return true
            }
            setTool(Tool.HIGHLIGHTER)
            return true
        }
        if(var1==KeyEvent.KEYCODE_V){
            if(tool.value==Tool.LINE&&(System.currentTimeMillis()-lineSelected)<=Constants.lineTypeDebounceMillis){
                setTool(Tool.VERTICAL_LINE)
                return true
            }
            return true
        }
        if(var1==KeyEvent.KEYCODE_SLASH){
            setTool(Tool.LINE)
            return true
        }
        if(var1==KeyEvent.KEYCODE_R){
            setTool(Tool.RECTANGLE)
            return true
        }
        if(var1==KeyEvent.KEYCODE_O){
            setTool(Tool.OVAL)
            return true
        }
        if(var1==KeyEvent.KEYCODE_C&&(var2?.isShiftPressed==true)){
            setTool(Tool.CIRCLE_WITH_TWO_POINTS)
            return true
        }
        if(var1==KeyEvent.KEYCODE_C){
            setTool(Tool.CIRCLE_WITH_CENTER_AND_RADIUS)
            return true
        }
        if(var1==KeyEvent.KEYCODE_P){
            setTool(Tool.PEN)
            return true
        }
        if(var1==KeyEvent.KEYCODE_PERIOD){
            setTool(Tool.DOT)
            return true
        }
        if(var1==KeyEvent.KEYCODE_COMMA){
            switchCapType()
            return true
        }
        if(var1==KeyEvent.KEYCODE_L){
            switchLineType()
            return true
        }
        if(var1==KeyEvent.KEYCODE_NUMPAD_4){
            backwardArrowHead.toggle()
            return true
        }
        if(var1==KeyEvent.KEYCODE_NUMPAD_6){
            forwardArrowHead.toggle()
            return true
        }
        if(var1==KeyEvent.KEYCODE_ESCAPE){
            if(tool.value == Tool.PEN){
                onCloseCurrentPath()
                return true
            }
            return true
        }
        return true
    }

    fun toggleGridSnap() {
        snapToGrid.toggle()
    }

    fun filter(offset: Offset): Offset {
        if(tool.value == Tool.MOVE){
            return offset
        }
        if(!snapToGrid.value){
            return offset
        }
        ///////////////////////
        val gx = canvasOffset.value.x%gridGap.value
        val gy = canvasOffset.value.y%gridGap.value
        var g = gridGap.value
        val x = offset.x - gx
        val y = offset.y - gy

        var finalX = 0f
        var finalY = 0f
        val rx = x%g
        finalX = if(rx==0f){
            x
        } else{
            val px = x - rx
            val nx = px + g
            if(x-px<nx-x){
                px
            } else{
                nx
            }
        }
        val ry = y%g
        finalY = if(ry==0f){
            y
        } else{
            val py = y - ry
            val ny = py + g
            if(y-py<ny-y){
                py
            } else{
                ny
            }
        }
        ///////////////////////
        return Offset(finalX + gx,finalY + gy)
    }

    fun onLayersClick() {
        layersWindowOpen.value = true
    }

    fun onLayersCloseClick() {
        layersWindowOpen.value = false
    }

    fun togglePathActive(item: DrawingPath) {
        val index = paths.indexOf(item)
        item.active = !item.active
        paths.removeAt(index)
        paths.add(index,item)
        pathUpdated.value = System.currentTimeMillis()
    }

    fun onSelectPath(item: DrawingPath, index: Int) {
        if(selectedPath.value==item.hashCode()){
            openPathEditor(item,index)
            return
        }
        selectedPath.value = item.hashCode()
    }

    private fun openPathEditor(path: DrawingPath, index: Int) {
        editingPath.value = path.clone
        editingPath.value?.index = index
    }

    fun onLayerCloseClick() {
        editingPath.value?.index = -1
        editingPath.value = null
    }

    fun layerDeleteCancel() {
        layerDeleteDialogOpen.value = false
    }

    fun layerDeleteConfirm() {
        val index = editingPath.value?.index?:-1
        if(index > -1){
            try {
                paths.removeAt(index)
            } catch (e: Exception) {
            }
        }
        editingPath.value = null
    }

    fun deleteLayer() {
        layerDeleteDialogOpen.value = true
    }
}

private fun Float.inside(angle1: Float, angle2: Float): Boolean {
    val min = min(angle1,angle2)
    val max = max(angle1,angle2)
    val inside = this in min..max

    val a1 = if(angle1>=0f) angle1 else 360f + angle1
    val a2 = if(angle2>=0f) angle2 else 360f + angle2
    val a = if(this>=0f) this else 360f + this

    val inside1 = a in a1..a2
    return inside||inside1
}

val hexCodes = listOf(
    '0',
    '1',
    '2',
    '3',
    '4',
    '5',
    '6',
    '7',
    '8',
    '9',
    'A',
    'B',
    'C',
    'D',
    'E',
    'F'
)

val Number.hex: String
    get() {
        val i = this.toInt()
        val q = i / 16
        val r = i % 16
        val qc = hexCodes[q]
        val rc = hexCodes[r]
        return String(charArrayOf(qc, rc))
    }
fun getHexColorValue(alpha: Float,red: Float, green: Float, blue: Float): String {
    val hred = (red*255).hex
    val hgreen = (green*255).hex
    val hblue = (blue*255).hex
    val halpha = (alpha*255).hex
    return "${halpha}${hred}${hgreen}${hblue}"
}