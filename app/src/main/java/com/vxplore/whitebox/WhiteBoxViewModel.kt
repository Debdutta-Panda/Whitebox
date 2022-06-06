package com.vxplore.whitebox

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawStyle
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
    val color = mutableStateOf(Color.Black)
    val stroke = mutableStateOf(Constants.strokeWidth)
    val pathUpdated = mutableStateOf(0L)

    val alpha = mutableStateOf(Constants.alpha)
    val colorFilter = mutableStateOf(Constants.colorFilter)
    val blendMode = mutableStateOf(Constants.penBlendMode)

    val paths = mutableStateListOf<DrawingPath>()
    //////////////////////////

    fun drag(dragAmount: Offset) {
        try {
            when(tool.value){
                Tool.MOVE -> handleMoveDrag(dragAmount)
                Tool.PEN -> handlePenDrag(dragAmount)
                Tool.ERASER -> handleEraserDrag(dragAmount)
                Tool.CLEAN -> {}
                Tool.HIGHLIGHTER -> handleHighlighterDrag(dragAmount)
                Tool.HORIZONTAL_LINE -> handleHLineDrag(dragAmount)
                Tool.VERTICAL_LINE -> handleVerticalDrag(dragAmount)
                Tool.LINE -> handleLineDrag(dragAmount)
                Tool.RECTANGLE -> handleDragRactangle(dragAmount)
            }
        } catch (e: Exception) {
        }
    }

    private fun handleDragRactangle(dragAmount: Offset) {
        var line = paths.last().lineData
        line = Pair(line.first,line.second+dragAmount)
        paths.last().lineData = line
        pathUpdated.value = System.currentTimeMillis()
    }

    private fun handleLineDrag(dragAmount: Offset) {
        var line = paths.last().lineData
        line = Pair(line.first,line.second+dragAmount)
        paths.last().lineData = line
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
                return Pair(lastPath.lineData.second,lastPath.lineData.first)
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
                return Pair(lastPath.lineData.first,lastPath.lineData.second)
            }
            ShapeType.LINE_SEGMENT -> TODO()
            ShapeType.RECTANGLE -> TODO()
            ShapeType.OVAL -> TODO()
        }
        return null
    }

    private fun handleVerticalDrag(dragAmount: Offset) {
        var line = paths.last().lineData
        line = Pair(line.first,Offset(line.second.x,line.second.y+dragAmount.y))
        paths.last().lineData = line
        updateArrowHeads()
        pathUpdated.value = System.currentTimeMillis()
    }

    private fun handleHLineDrag(dragAmount: Offset) {
        var line = paths.last().lineData
        line = Pair(line.first,Offset(line.second.x+dragAmount.x,line.second.y))
        paths.last().lineData = line
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

    private fun handlePenDrag(dragAmount: Offset) {
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
            return
        }
        tool.value = newTool
    }

    fun dragStart(offset: Offset) {
        when(tool.value){
            Tool.MOVE -> handleMoveDragStart(offset)
            Tool.PEN -> handlePenDragStart(offset)
            Tool.ERASER -> handleEraserDragStart(offset)
            Tool.CLEAN -> {}
            Tool.HIGHLIGHTER -> handleHighlighterDragStart(offset)
            Tool.HORIZONTAL_LINE -> handleHLineDragStart(offset)
            Tool.VERTICAL_LINE -> handleVerticalDragStart(offset)
            Tool.LINE -> handleLineDragStart(offset)
            Tool.RECTANGLE -> handleRectangleDragStart(offset)
        }
    }

    private fun handleRectangleDragStart(offset: Offset) {
        val path = DrawingPath(
            strokeColor = color.value,
            strokeWidth = stroke.value,
            colorFilter = null,
            blendMode = Constants.penBlendMode,
            type = ShapeType.RECTANGLE,
            pathEffect = currentPathEffect,
            cap = capType.value,
            drawStyle = getDrawStyle()
        )
        path.lineData = Pair(offset-canvasOffset.value,offset-canvasOffset.value)
        paths.add(path)
    }

    private fun getDrawStyle(): DrawStyle? {
        if(drawStyleType.value==DrawStyleType.STROKE){
            return Stroke(
                width = stroke.value,
                miter = miter.value,
                cap = capType.value.strokeType,
                join = join.value.stokeJoin,
                pathEffect = currentPathEffect
            )
        }
        return null
    }

    private fun handleLineDragStart(offset: Offset) {
        val path = DrawingPath(
            strokeColor = color.value,
            strokeWidth = stroke.value,
            colorFilter = null,
            blendMode = Constants.penBlendMode,
            type = ShapeType.LINE,
            pathEffect = currentPathEffect,
            cap = capType.value
        )
        path.lineData = Pair(offset-canvasOffset.value,offset-canvasOffset.value)
        paths.add(path)
    }

    private fun handleVerticalDragStart(offset: Offset) {
        val path = DrawingPath(
            strokeColor = color.value,
            strokeWidth = stroke.value,
            colorFilter = null,
            blendMode = Constants.penBlendMode,
            type = ShapeType.LINE,
            pathEffect = currentPathEffect,
            cap = capType.value
        )
        path.lineData = Pair(offset-canvasOffset.value,offset-canvasOffset.value)
        paths.add(path)
    }

    private fun handleHLineDragStart(offset: Offset) {
        val path = DrawingPath(
            strokeColor = color.value,
            strokeWidth = stroke.value,
            colorFilter = null,
            blendMode = Constants.penBlendMode,
            type = ShapeType.LINE,
            pathEffect = currentPathEffect,
            cap = capType.value
        )
        path.lineData = Pair(offset-canvasOffset.value,offset-canvasOffset.value)
        paths.add(path)
    }

    private fun handleHighlighterDragStart(offset: Offset) {
        val path = DrawingPath(
            strokeColor = color.value,
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
            strokeWidth = stroke.value,
            colorFilter = null,
            blendMode = Constants.eraserBlendMode,
            pathEffect = currentPathEffect,
            cap = capType.value
        )
        path.points.add(offset-canvasOffset.value)
        paths.add(path)
    }

    private fun handleMoveDragStart(offset: Offset) {

    }

    private fun handlePenDragStart(offset: Offset) {
        val path = DrawingPath(
            strokeColor = color.value,
            strokeWidth = stroke.value,
            alpha = alpha.value,
            colorFilter = colorFilter.value,
            blendMode = blendMode.value,
            pathEffect = currentPathEffect,
            cap = capType.value
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
        color.value = it
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
}


