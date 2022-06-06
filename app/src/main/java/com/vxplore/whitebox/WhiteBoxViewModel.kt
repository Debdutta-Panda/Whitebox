package com.vxplore.whitebox

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.lifecycle.ViewModel

class WhiteBoxViewModel: ViewModel() {
    val forwardArrowHead = mutableStateOf(false)
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
        when(tool.value){
            Tool.MOVE -> handleMoveDrag(dragAmount)
            Tool.PEN -> handlePenDrag(dragAmount)
            Tool.ERASER -> handleEraserDrag(dragAmount)
            Tool.CLEAN -> {}
            Tool.HIGHLIGHTER -> handleHighlighterDrag(dragAmount)
            Tool.HORIZONTAL_LINE -> handleHLineDrag(dragAmount)
            Tool.VERTICAL_LINE -> handleVerticalDrag(dragAmount)
            Tool.LINE -> handleLineDrag(dragAmount)
        }
    }

    private fun handleLineDrag(dragAmount: Offset) {
        var line = paths.last().lineData
        line = Pair(line.first,line.second+dragAmount)
        paths.last().lineData = line
        updateArrowHeads()
        pathUpdated.value = System.currentTimeMillis()
    }

    private fun updateArrowHeads() {
        val lastTwoPoints = getLastTwoPointsOfLastPath()
    }

    private fun getLastTwoPointsOfLastPath(): Pair<Offset, Offset>? {
        val lastPath = paths.last()
        when(lastPath.type){
            ShapeType.PATH -> {
                val size = lastPath.points.size
                if(size>1){
                    return Pair(
                        lastPath.points[size-1],
                        lastPath.points[size-2]
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
        }
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

    }

    fun dragCancel() {

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

    fun toggleEndArrowHead() {
        forwardArrowHead.toggle()
    }
}


