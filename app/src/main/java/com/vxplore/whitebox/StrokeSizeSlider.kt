package com.vxplore.whitebox

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp

@Composable
fun BoxScope.StrokeSizeSlider(vm: WhiteBoxViewModel) {
    Slider(
        value = vm.stroke.value/Constants.maxStroke,
        onValueChange = {
            vm.stroke.value = it*Constants.maxStroke
        },
        modifier = Modifier
            .padding(
                start = Constants
                    .sliderStartPadding
                    .dp,
                end = Constants.strokeSliderEndPadding.dp
            )
            .fillMaxWidth()
            .alpha(
                Constants
                    .strokeSliderAlpha
            )
    )
}