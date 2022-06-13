package com.vxplore.whitebox

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun TextInputDialog(vm: WhiteBoxViewModel) {
    if(vm.textDialog.value){
        Dialog(
            onDismissRequest = {
                vm.onTextDialogDismissRequest()
            },
            properties = DialogProperties(
                dismissOnClickOutside = true,
                dismissOnBackPress = true
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                backgroundColor = Color.White,
                shape = RoundedCornerShape(12.dp)

            ){
                Box(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ){
                        Text(
                            "Text",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color.Black
                        )
                        Divider(
                            color = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        var text by remember { mutableStateOf("") }
                        val focusRequester = remember { FocusRequester() }
                        LaunchedEffect(key1 = Unit){
                            focusRequester.requestFocus()
                        }
                        OutlinedTextField(
                            value = text,
                            onValueChange = {
                                text = it
                            },
                            label = {
                                Text(
                                    "Value",
                                    color = Color.Gray
                                )
                            },
                            placeholder = {
                                Text(
                                    "Your text",
                                    color = Color.Gray
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ){
                            Button(
                                onClick = {
                                    vm.onTextDialogCancel()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color.Blue,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                Text("Cancel")
                            }
                            Button(
                                onClick = {
                                    vm.onTextDialogDone(text)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color.Blue,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                Text("Done")
                            }
                        }
                    }
                }
            }
        }
    }
}