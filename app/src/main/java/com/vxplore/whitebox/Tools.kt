package com.vxplore.whitebox

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun BoxScope.ToolBox(vm: WhiteBoxViewModel) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(Constants.toolSectionsWidth.dp)
    ) {
        ToolsSection(vm)
        val density = LocalDensity.current
        Divider(
            color = Constants.toolsSectionsDividerColor,
            thickness = (vm.stroke.value/density.density).dp
        )
        ToolsSettingsSection(vm)
    }
}