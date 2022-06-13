package com.vxplore.whitebox

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vxplore.whitebox.ui.theme.WhiteboxTheme
import kotlinx.coroutines.launch

class MainActivity() : ComponentActivity(), KeyEvent.Callback {
    private var viewModel:WhiteBoxViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vm:WhiteBoxViewModel by viewModels()
        viewModel = vm
        setContent {
            WhiteboxTheme {
                WhiteBox(vm)
            }
        }
    }

    override fun onKeyDown(var1: Int, var2: KeyEvent?): Boolean{
        return viewModel?.onKeyDown(var1,var2)?:false
    }

    override fun onKeyLongPress(var1: Int, var2: KeyEvent?): Boolean{
        return viewModel?.onKeyLongPress(var1,var2)?:false
    }

    override fun onKeyUp(var1: Int, var2: KeyEvent?): Boolean{
        return viewModel?.onKeyUp(var1,var2)?:false
    }

    override fun onKeyMultiple(var1: Int, var2: Int, var3: KeyEvent?): Boolean{
        return viewModel?.onKeyMultiple(var1,var2,var3)?:false
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen() {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Text(text = "Hello from sheet")
            }
        }, sheetPeekHeight = 0.dp
    ) {
        Button(onClick = {
            coroutineScope.launch {

                if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                    bottomSheetScaffoldState.bottomSheetState.expand()
                } else {
                    bottomSheetScaffoldState.bottomSheetState.collapse()
                }
            }
        }) {
            Text(text = "Expand/Collapse Bottom Sheet")
        }
    }
}