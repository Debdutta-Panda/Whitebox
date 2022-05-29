package com.vxplore.whitebox

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

class WhiteBoxViewModel: ViewModel() {
    val toolAlpha = mutableStateOf(0.5f)
    val toolColor = mutableStateOf(Color(0xffe3e3e3))
    val selectedToolColor = mutableStateOf(Color(0xff9cccff))
    val canvasOffset = mutableStateOf(Offset.Zero)
    val xGridGap = mutableStateOf(100)
    val yGridGap = mutableStateOf(100)
    val yGridStroke = mutableStateOf(1f)
    val yGridColor = mutableStateOf(Color(0xffe0e0e0))
    val tool = mutableStateOf(Tool.MOVE)
    val color = mutableStateOf(Color.Black)
    val stroke = mutableStateOf(1f)
    val pathUpdated = mutableStateOf(0L)

    val paths = mutableStateListOf<DrawingPath>()

    fun drag(dragAmount: Offset) {
        when(tool.value){
            Tool.MOVE -> handleMoveDrag(dragAmount)
            Tool.PEN -> handlePenDrag(dragAmount)
        }
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
        tool.value = newTool
    }

    fun dragStart(offset: Offset) {
        val path = DrawingPath(
            color = color.value,
            strokeWidth = stroke.value
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
}