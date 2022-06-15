package com.vxplore.whitebox

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LayerEditingContent(vm: WhiteBoxViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
    ){
        Text(
            "${vm.editingPath.value?.displayName}(${vm.editingPath.value?.index})",
            color = Color.DarkGray,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 4.dp)
        )
        IconButton(
            onClick = {
                vm.onLayerCloseClick()
            },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription ="Close",
                tint = Color(0xffff8787),
            )
        }
    }
    Divider(
        color = Color.LightGray
    )
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)
    ){
        item{
            OutlinedButton(
                onClick = {
                    vm.deleteLayer()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription ="Delete",
                    tint = Color.Red
                )
            }
        }
        item{
            Divider(
                color = Color.LightGray
            )
        }
        item{
            TransformationEditingContent(vm)
        }
    }
    if(vm.layerDeleteDialogOpen.value){
        LayerDeleteDialog(vm)
    }
}




