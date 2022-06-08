package com.vxplore.whitebox

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

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
        FillColorBox(vm)
        DrawStyleBox(vm)
    }
}




