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
fun ToolsAlphaSlider(vm: WhiteBoxViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Constants.bottomSheetSliderPadding),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            "Tools alpha",
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(12.dp))
        Slider(
            value = vm.toolAlpha.value,
            onValueChange = {
                vm.toolAlpha.value = it
            },
            modifier = Modifier
                .padding(Constants.bottomSheetSliderPadding)
                .fillMaxWidth()
        )
    }
}