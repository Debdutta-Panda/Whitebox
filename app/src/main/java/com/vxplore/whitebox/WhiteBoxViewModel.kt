package com.vxplore.whitebox

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

class WhiteBoxViewModel: ViewModel() {
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
        }
    }

    private fun handleHLineDrag(dragAmount: Offset) {
        var line = paths.last().lineData
        line = Pair(line.first,line.second+dragAmount)
        paths.last().lineData = line
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
        }
    }

    private fun handleHLineDragStart(offset: Offset) {
        val path = DrawingPath(
            strokeColor = color.value,
            strokeWidth = stroke.value,
            colorFilter = null,
            blendMode = Constants.penBlendMode,
            type = ShapeType.LINE
        )
        path.lineData = Pair(offset-canvasOffset.value,offset-canvasOffset.value)
        paths.add(path)
    }

    private fun handleHighlighterDragStart(offset: Offset) {
        val path = DrawingPath(
            strokeColor = color.value,
            strokeWidth = stroke.value,
            colorFilter = null,
            blendMode = Constants.highlighterBlendMode
        )
        path.points.add(offset-canvasOffset.value)
        paths.add(path)
    }

    private fun handleEraserDragStart(offset: Offset) {
        val path = DrawingPath(
            strokeColor = eraserColor.value,
            strokeWidth = stroke.value,
            colorFilter = null,
            blendMode = Constants.eraserBlendMode
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
            blendMode = blendMode.value
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
}