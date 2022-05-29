package com.vxplore.whitebox

import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector

object Constants {
    private const val horizontalLineIconPathData = "M0,7h16v1h-16v-1z"
    val highlighterBlendMode: BlendMode = BlendMode.Multiply
    val eraserBlendMode: BlendMode = BlendMode.Clear
    val penBlendMode = DrawScope.DefaultBlendMode
    val colorFilter: ColorFilter? = null
    const val alpha = 1f
    const val xGridStroke = 1f
    const val strokeWidth = 1f
    const val yGridColor = 0xffe0e0e0
    const val xGridColor = 0xffe0e0e0
    const val yGridStroke = 1f
    const val yGridGap = 100
    const val selectedToolColor = 0xff9cccff
    const val toolColor = 0xffe3e3e3
    const val toolAlpha = 0.5f
    const val xGridGap = 100
    const val highlighterIconPathData = "M398.79,22.31a31.76,31.76 0,0 0,-22.77 -9.52L376,12.79a31.77,31.77 0,0 0,-22.78 9.55L87.53,292.23a32.09,32.09 0,0 0,0.18 45.08l14.7,14.7L16,439.43L16,478L122.8,478l52.8,-52.8 12.48,12.48a32,32 0,0 0,46 -0.77L492.31,160.77a31.91,31.91 0,0 0,-0.6 -44.34ZM109.55,446L54.5,446l46.55,-47.1 27.8,27.8ZM210.71,415.05L175.6,379.95l-24.13,24.13 -27.93,-27.93 23.99,-24.27 -37.19,-37.19 48.34,-49.1L257.8,364.7ZM279.67,341.31 L181.13,242.77L376.02,44.79l92.92,94.12Z"
    const val eraserIconPathData = "M315,285H201.21l124.39,-124.39c5.86,-5.86 5.86,-15.35 0,-21.21l-120,-120c-5.86,-5.86 -15.35,-5.86 -21.21,0l-180,180C1.58,202.21 0,206.02 0,210s1.58,7.79 4.39,10.61l90,90c2.81,2.81 6.63,4.39 10.61,4.39L165,315c0.01,0 0.01,-0 0.02,-0L315,315c8.28,0 15,-6.72 15,-15C330,291.72 323.28,285 315,285zM195,51.21L293.79,150L207,236.79L108.21,138L195,51.21z"
    const val toolIconSize = 32
    val selectedColorIndicatorColor = Color.White
    const val colorBoxHeight = 40f
    const val strokeSliderEndPadding = 12
    const val strokeSliderStartPadding = 40
    const val strokeSliderAlpha: Float = 0.5f
    const val maxStroke = 100f
    val colors = arrayOf(
        Color.Black,
        Color.Red,
        Color.Green,
        Color.Blue,
        Color.Gray,
        Color.Magenta,
        Color.Yellow,
    )


    val eraserIcon: ImageVector = createImageVector(
        eraserIconPathData,
        toolIconSize,
        toolIconSize,
        330f,
        330f
    )
    val highlighterIcon = createImageVector(
        highlighterIconPathData,
        toolIconSize,
        toolIconSize,
        512f,
        512f
    )


    val horizontalLineIcon = createImageVector(
        horizontalLineIconPathData,
        (toolIconSize*2).toInt(),
        (toolIconSize*2).toInt(),
        32f,
        32f
    )
}