package com.vxplore.whitebox

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Gesture
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun BoxScope.ToolBox(vm: WhiteBoxViewModel) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(Constants.toolSectionsWidth.dp)
    ) {
        ToolsSection(vm)
        Divider(
            color = Constants.toolsSectionsDividerColor
        )
        ToolsSettingsSection(vm)
    }

}

@Composable
fun ColumnScope.ToolsSettingsSection(vm: WhiteBoxViewModel) {
    LazyColumn(
        modifier = Modifier
            .weight(1f)
            .fillMaxSize()
    ){
        item{
            IconButton(onClick = {
                vm.switchLineType()
            }) {
                Icon(
                    imageVector = getLineTypeIcon(vm),
                    contentDescription = "Line Type ${vm.lineType.value.name}",
                    tint = Color(Constants.selectedToolColor),
                    modifier = Modifier.size(Constants.toolIconSize.dp)
                )
            }
        }
        item{
            IconButton(onClick = {
                vm.switchCapType()
            }) {
                Image(
                    painter = when(vm.capType.value){
                        CapType.ROUND -> painterResource(id = R.drawable.ic_cap_round)
                        CapType.BUTT -> painterResource(id = R.drawable.ic_cap_square)
                        CapType.SQUARE -> painterResource(id = R.drawable.ic_cap_butt)
                    },
                    contentDescription = "Line Type ${vm.lineType.value.name}",
                    modifier = Modifier.size(Constants.toolIconSize.dp)
                )
            }
        }
        item{
            IconButton(onClick = {
                vm.toggleEndArrowHead()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Forward Arrow Head",
                    tint = Color(if(vm.forwardArrowHead.value) Constants.selectedToolColor else Constants.toolColor),
                    modifier = Modifier.size(Constants.toolIconSize.dp)
                )
            }
        }
    }
}


fun getLineTypeIcon(vm: WhiteBoxViewModel): ImageVector {
    return when(vm.lineType.value){
        LineType.DASHED -> Constants.dashedLineIcon
        LineType.CONTINUOUS -> Constants.horizontalLineIcon
    }
}

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
                Icon(
                    imageVector = getIcon(it),
                    contentDescription = "Tool: ${it.name}",
                    tint = toolTint(vm,it),
                    modifier = Modifier.size(Constants.toolIconSize.dp)
                )
            }
        }
    }
}

fun getIcon(tool: Tool): ImageVector {
    return when(tool){
        Tool.MOVE -> Icons.Default.PanTool
        Tool.PEN -> Icons.Default.Gesture
        Tool.ERASER -> Constants.eraserIcon
        Tool.CLEAN -> Icons.Default.CleaningServices
        Tool.HIGHLIGHTER -> Constants.highlighterIcon
        Tool.HORIZONTAL_LINE -> Constants.horizontalLineIcon
        Tool.VERTICAL_LINE -> Constants.verticalLineIcon
        Tool.LINE -> Constants.lineIcon
    }
}

fun createImageVector(path: String, width: Int,height: Int, vWidth: Float,vHeight:Float): ImageVector{
    return ImageVector.Builder(
        defaultWidth = width.dp,
        defaultHeight = height.dp,
        viewportWidth = vWidth,
        viewportHeight = vHeight,
    ).run {
        addPath(
            pathData = addPathNodes(path),
            name = "",
            fill = SolidColor(Color.Black),
            stroke = SolidColor(Color.Black),
        )
        build()
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