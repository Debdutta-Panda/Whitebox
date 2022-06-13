package com.vxplore.whitebox

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

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
                vm.toggleForwardArrowHead()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Forward Arrow Head",
                    tint = Color(if(vm.forwardArrowHead.value) Constants.selectedToolColor else Constants.toolColor),
                    modifier = Modifier.size(Constants.toolIconSize.dp)
                )
            }
        }
        item{
            IconButton(onClick = {
                vm.toggleBackwardArrowHead()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Forward Arrow Head",
                    tint = Color(if(vm.backwardArrowHead.value) Constants.selectedToolColor else Constants.toolColor),
                    modifier = Modifier.size(Constants.toolIconSize.dp)
                )
            }
        }
        item{
            IconButton(onClick = {
                vm.toggleGridSnap()
            }) {
                Image(
                    painter = painterResource(id = if(vm.snapToGrid.value) R.drawable.ic_grid_snap_on else R.drawable.ic_grid_snap_off),
                    contentDescription = "Snap to grid",
                    modifier = Modifier.size(Constants.toolIconSize.dp)
                )
            }
        }
        item{
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

fun getLineTypeIcon(vm: WhiteBoxViewModel): ImageVector {
    return when(vm.lineType.value){
        LineType.DASHED -> Constants.dashedLineIcon
        LineType.CONTINUOUS -> Constants.horizontalLineIcon
    }
}