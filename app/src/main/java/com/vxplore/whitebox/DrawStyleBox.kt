package com.vxplore.whitebox

import androidx.compose.foundation.layout.*
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DrawStyleBox(vm: WhiteBoxViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text("Draw Style:")
        Spacer(modifier = Modifier.width(8.dp))
        Text("Stroke")
        RadioButton(
            selected = vm.drawStyleType.value==DrawStyleType.STROKE,
            onClick = {
                vm.setDrawStyleType(DrawStyleType.STROKE)
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("Fill")
        RadioButton(
            selected = vm.drawStyleType.value==DrawStyleType.FILL,
            onClick = {
                vm.setDrawStyleType(DrawStyleType.FILL)
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("Both")
        RadioButton(
            selected = vm.drawStyleType.value==DrawStyleType.BOTH,
            onClick = {
                vm.setDrawStyleType(DrawStyleType.BOTH)
            }
        )
    }
}