package com.vxplore.whitebox

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Slider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.math.MathUtils.clamp

@Composable
fun BottomSheetContent(vm: WhiteBoxViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.HorizontalRule,
            contentDescription = "Sheet Indicator",
            modifier = Modifier.align(
                Alignment.CenterHorizontally
            )
        )
        StrokeColorBox(vm)
        StrokeAlphaBox(vm)
        FillColorBox(vm)
        FillAlphaBox(vm)
        DrawStyleBox(vm)
        GridGapSlider(vm)
        ToolsAlphaSlider(vm)
    }
}



fun Float.map(
    source: Pair<Float,Float>,
    destination: Pair<Float,Float>
): Float{
    val sa = source.first
    val sb = source.second

    val av = this - sa
    val ad = sb - sa
    val p = clamp(try {
        av/ad
    } catch (e: Exception) {
        0f
    },0f,1f)

    val da = destination.first
    val db = destination.second
    val r = da + (db-da)*p
    return r
}

@Composable
fun FillAlphaBox(vm: WhiteBoxViewModel) {
    Slider(
        value = vm.fillAlpha.value,
        onValueChange = {
            vm.fillAlpha.value = it
        },
        modifier = Modifier
            .padding(Constants.bottomSheetSliderPadding)
            .fillMaxWidth()
    )
}




