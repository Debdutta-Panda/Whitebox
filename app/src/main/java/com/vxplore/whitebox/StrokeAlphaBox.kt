package com.vxplore.whitebox

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun StrokeAlphaBox(vm: WhiteBoxViewModel) {
    Slider(
        value = vm.strokeAlpha.value,
        onValueChange = {
            vm.strokeAlpha.value = it
        },
        modifier = Modifier
            .padding(Constants.alphaSliderPadding)
            .fillMaxWidth()
    )
}