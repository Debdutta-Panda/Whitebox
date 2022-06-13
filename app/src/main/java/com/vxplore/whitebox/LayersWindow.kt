package com.vxplore.whitebox

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BoxScope.LayersWindow(vm: WhiteBoxViewModel) {
    if(vm.layersWindowOpen.value){
        Card(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 60.dp)
                .fillMaxWidth(0.25f)
                .fillMaxHeight(),
            backgroundColor = Color(0xfffcfcfc),
            elevation = 4.dp,
            shape = RoundedCornerShape(12.dp)
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ){
                    Text(
                        "Layers",
                        color = Color.DarkGray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 4.dp)
                    )
                    IconButton(
                        onClick = {
                            vm.onLayersCloseClick()
                        },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription ="Close",
                            tint = Color(0xffff8787),
                        )
                    }
                }
                Divider(
                    color = Color.LightGray
                )
                if(vm.paths.size==0){
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            "No items yet",
                            color = Color.LightGray,
                            fontSize = 12.sp
                        )
                    }
                }
                else{
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(4.dp)
                    ){
                        itemsIndexed(vm.paths.reversed()){index,item->
                            Card(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .fillMaxWidth()
                            ){
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    Checkbox(
                                        checked = item.active,
                                        onCheckedChange = {
                                            vm.onPathActiveChanged(item,it)
                                        },
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        item.displayName+"(${vm.paths.size - index})",
                                        color = Color.Gray,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}