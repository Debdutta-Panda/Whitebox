package com.vxplore.whitebox

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Layers
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BoxScope.LayersButton(vm: WhiteBoxViewModel) {
    IconButton(
        onClick = {
            vm.onLayersClick()
        },
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(bottom = 60.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Layers,
            contentDescription ="Layers",
            tint = vm.toolColor.value
        )
    }
}