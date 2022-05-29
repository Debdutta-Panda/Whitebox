package com.vxplore.whitebox

import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BoxScope.ColorBox(vm: WhiteBoxViewModel) {
    LazyRow(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .height(Constants.colorBoxHeight.dp)
    ){
        items(Constants.colors){
            Button(
                onClick = {
                    vm.setColor(it)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = it
                )
            ) {
                if(vm.color.value==it){
                    Icon(
                        imageVector = Icons.Default.Circle,
                        contentDescription ="Selected",
                        tint = Constants.selectedColorIndicatorColor
                    )
                }
            }
        }
    }
}