package com.vxplore.whitebox

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Gesture
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ColumnScope.ToolsSection(vm: WhiteBoxViewModel) {
    LazyColumn(
        modifier = Modifier
            .weight(1f)
            .fillMaxSize()
    ){
        item{
            IconButton(onClick = {
                vm.switchGrid()
            }) {
                Image(
                    painter = when(vm.gridMode.value){
                        GridMode.XY -> painterResource(id = R.drawable.ic_grid_xy)
                        GridMode.X -> painterResource(id = R.drawable.ic_grid_x)
                        GridMode.Y -> painterResource(id = R.drawable.ic_grid_y)
                        GridMode.NONE -> painterResource(id = R.drawable.ic_grid_none)
                    },
                    contentDescription = "Grid",
                    modifier = Modifier.size(Constants.toolIconSize.dp)
                )

            }
        }
        items(Tool.values()) {
            IconButton(onClick = {
                vm.setTool(it)
            }) {
                val icon = getIcon(vm,it)
                if(icon is ImageVector){
                    Icon(
                        imageVector = icon,
                        contentDescription = "Tool: ${it.name}",
                        tint = toolTint(vm,it),
                        modifier = Modifier.size(Constants.toolIconSize.dp)
                    )
                }
                else if(icon is Painter){
                    Icon(
                        painter = icon,
                        contentDescription = "Tool: ${it.name}",
                        tint = toolTint(vm,it),
                        modifier = Modifier.size(Constants.toolIconSize.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun getIcon(vm: WhiteBoxViewModel, tool: Tool): Any {
    return when(tool){
        Tool.MOVE -> Icons.Default.PanTool
        Tool.FREE_HAND -> Icons.Default.Gesture
        Tool.ERASER -> Constants.eraserIcon
        Tool.CLEAN -> Icons.Default.CleaningServices
        Tool.HIGHLIGHTER -> Constants.highlighterIcon
        Tool.HORIZONTAL_LINE -> Constants.horizontalLineIcon
        Tool.VERTICAL_LINE -> Constants.verticalLineIcon
        Tool.LINE -> Constants.lineIcon
        Tool.RECTANGLE -> Icons.Outlined.CheckBoxOutlineBlank
        Tool.OVAL -> painterResource(id = R.drawable.ic_oval)
        Tool.CIRCLE_WITH_CENTER_AND_RADIUS -> painterResource(id = R.drawable.ic_center_radius_circle)
        Tool.CIRCLE_WITH_TWO_POINTS -> painterResource(id = R.drawable.ic_2_point_circle)
        Tool.PEN -> painterResource(id = R.drawable.ic_pen_svgrepo_com)
        Tool.DOT -> painterResource(id = R.drawable.ic_dot)
        Tool.TEXT -> painterResource(id = R.drawable.ic_text)
        Tool.ARC -> {
            if(vm.sweepShortest.value){
                painterResource(id = R.drawable.ic_arc)
            }
            else{
                painterResource(id = R.drawable.ic_arc_max)
            }
        }
        Tool.ARC_WITH_CENTER -> {
            if(vm.sweepShortestWithCenter.value){
                painterResource(id = R.drawable.ic_arc_with_center)
            }
            else{
                painterResource(id = R.drawable.arc_max_with_center)
            }
        }
        Tool.TRANSFORM -> painterResource(id = R.drawable.ic_pointer)
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