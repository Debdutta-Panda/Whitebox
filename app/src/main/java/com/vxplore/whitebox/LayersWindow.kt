package com.vxplore.whitebox

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BoxScope.LayersWindow(vm: WhiteBoxViewModel) {
    if(vm.layersWindowOpen.value){

        Card(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 60.dp)
                .fillMaxWidth(0.25f)
                .fillMaxHeight(),
            backgroundColor = Color(0xfffcfcfc),
            elevation = 4.dp,
            shape = RoundedCornerShape(12.dp)
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ){
                if(vm.editingPath.value!=null){
                    LayerEditingContent(vm)
                }
                else{
                    LayersContent(vm)
                }
            }
        }
    }
}