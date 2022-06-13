package com.vxplore.whitebox

import androidx.compose.foundation.layout.*
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GridGapSlider(vm: WhiteBoxViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Constants.bottomSheetSliderPadding),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            "Grid Gap",
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(12.dp))
        Slider(
            value = vm.gridGap.value.map(Pair(10f,1000f),Pair(0f,1f)),
            onValueChange = {
                vm.gridGap.value = it.map(Pair(0f,1f),Pair(10f,1000f))
            },
            modifier = Modifier
                .padding(Constants.bottomSheetSliderPadding)
                .fillMaxWidth()
        )
    }
}