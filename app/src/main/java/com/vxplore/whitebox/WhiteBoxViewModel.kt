package com.vxplore.whitebox

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.lifecycle.ViewModel

val StrokeJoinType.stokeJoin: StrokeJoin
    get() {
        return when(this){
            StrokeJoinType.BEVEL -> StrokeJoin.Bevel
            StrokeJoinType.MITER -> StrokeJoin.Miter
            StrokeJoinType.ROUND -> StrokeJoin.Round
        }
    }

class WhiteBoxViewModel: ViewModel() {
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
    val xGridGap = mutableStateOf(Constants.xGridGap)
    val yGridGap = mutableStateOf(Constants.yGridGap)
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
        if(newTool==Tool.CLEAN){
            paths.clear()
            pathUpdated.value = System.currentTimeMillis()
            penPathAdded.value = false
            return
        }
        tool.value = newTool
        if(newTool==Tool.PEN){
            onPenToolSelected()
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
        }
    }

    private fun handlePenDragStart(offset: Offset) {
        if(!penPathAdded.value){
            val path = DrawingPath(
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
        }
        paths.last().points.add(offset-canvasOffset.value)
        pathUpdated.value = System.currentTimeMillis()
    }

    private fun handle2PointCircleDragStart(offset: Offset) {
        val path = DrawingPath(
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
            strokeColor = strokeColor.value.copy(alpha = strokeAlpha.value),
            strokeWidth = stroke.value,
            colorFilter = null,
            blendMode = Constants.highlighterBlendMode,
            pathEffect = currentPathEffect,
            cap = capType.value
        )
        path.points.add(offset-canvasOffset.value)
        paths.add(path)
    }

    private fun handleEraserDragStart(offset: Offset) {
        showEraser.value = true
        val path = DrawingPath(
            strokeColor = eraserColor.value,
            fillColor = eraserColor.value,
            strokeWidth = stroke.value,
            colorFilter = null,
            blendMode = Constants.eraserBlendMode,
            pathEffect = currentPathEffect,
            cap = capType.value,
            drawStyle = getDrawStyle(),
            drawStyleType = drawStyleType.value
        )
        eraserPos.value = offset-canvasOffset.value
        path.points.add(offset-canvasOffset.value)
        paths.add(path)
    }

    private fun handleMoveDragStart(offset: Offset) {

    }

    private fun handleFreeHandDragStart(offset: Offset) {
        val path = DrawingPath(
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
}


