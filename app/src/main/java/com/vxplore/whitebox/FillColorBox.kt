package com.vxplore.whitebox

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FillColorBox(vm: WhiteBoxViewModel) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(Constants.colorBoxHeight.dp)
    ){
        items(Constants.colors){
            Button(
                onClick = {
                    vm.setFillColor(it)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = it
                )
            ) {
                if(vm.fillColor.value==it){
                    Icon(
                        imageVector = Icons.Default.Circle,
                        contentDescription ="Selected",
                        tint = Color.White
                    )
                }
            }
        }
    }
}