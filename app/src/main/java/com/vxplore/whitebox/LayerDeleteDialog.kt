package com.vxplore.whitebox

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun LayerDeleteDialog(vm: WhiteBoxViewModel) {
    Dialog(
        onDismissRequest = {
            vm.layerDeleteCancel()
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ){
                Text(
                    "Delete Layer",
                    color = Color.lightRed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Divider(
                    color = Color.LightGray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Are you sure to delete?",
                    color = Color.Black,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ){
                    Button(
                        onClick = {
                            vm.layerDeleteConfirm()
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.lightRed,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth().weight(1f)
                    ) {
                        Text("Yes")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            vm.layerDeleteCancel()
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White,
                            contentColor = Color.lightRed
                        ),
                        modifier = Modifier.fillMaxWidth().weight(1f)
                    ) {
                        Text("No")
                    }
                }
            }
        }
    }
}