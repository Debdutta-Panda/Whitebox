package com.vxplore.whitebox

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gesture
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ToolBox(vm: WhiteBoxViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .width(40.dp)
    ){
        item {
            IconButton(onClick = {
                vm.setTool(Tool.MOVE)
            }) {
                Icon(
                    imageVector = Icons.Default.PanTool,
                    contentDescription = "Pan Tool",
                    tint = toolTint(vm,Tool.MOVE)
                )
            }
        }
        item {
            IconButton(onClick = {
                vm.setTool(Tool.PEN)
            }) {
                Icon(
                    imageVector = Icons.Default.Gesture,
                    contentDescription = " Tool",
                    tint = toolTint(vm,Tool.PEN)
                )
            }
        }
    }
}

fun toolTint(vm: WhiteBoxViewModel, tool: Tool): Color {
    return (
            if(tool==vm.tool.value)
                vm.selectedToolColor.value
            else vm.toolColor.value
           )
        .copy(alpha = vm.toolAlpha.value)
}