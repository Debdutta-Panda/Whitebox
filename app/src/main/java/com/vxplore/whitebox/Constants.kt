package com.vxplore.whitebox

import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

object Constants {
    object displayNames{
        const val text: String = "Text"
        const val freeHand: String = "Freehand"
        const val eraser: String = "Eraser"
        const val horizontalLine: String = "Horizontal line"
        const val highlighter: String = "Highlighter"
        const val verticalLine: String = "Vertical line"
        const val line: String = "Line"
        const val rectangle: String = "Rectangle"
        const val oval: String = "Oval"
        const val circle: String = "Circle"
        const val _2PointCircle: String = "2 point circle"
        const val pen: String = "Pen"
        const val dot: String = "Dot"
        const val arc: String = "Arc"
        const val arcWithCenter = "Arc with center"
    }
    val maxGridGap = 1000f
    val lineTypeDebounceMillis = 2000L
    val bottomSheetSliderPadding = 12.dp
    val arrowDistance = 40f
    val capType = CapType.ROUND
    const val dashedPhase = 20f
    val dashedIntervals = floatArrayOf(20f, 20f)
    private const val dashedLineIconPathData = "M0,8h4v1h-4v-1zM6.5,9h4v-1h-4v1zM13,8v1h4v-1h-4z"
    private const val ovalIconPathData = "M1,32a31,20 0,1 0,62 0a31,20 0,1 0,-62 0z"
    private const val circleIconPathData = "M256,23.05C127.5,23.05 23.05,127.5 23.05,256S127.5,488.9 256,488.9 488.9,384.5 488.9,256 384.5,23.05 256,23.05zM256,40.95c118.9,0 215.1,96.15 215.1,215.05S374.9,471.1 256,471.1c-118.9,0 -215.05,-96.2 -215.05,-215.1C40.95,137.1 137.1,40.95 256,40.95z"
    const val toolSectionsWidth = 40
    val toolsSectionsDividerColor = Color.DarkGray
    private const val lineIconPathData = "M21.71,3.29a1,1 0,0 0,-1.42 0l-18,18a1,1 0,0 0,0 1.42,1 1,0 0,0 1.42,0l18,-18A1,1 0,0 0,21.71 3.29Z"
    private const val verticalLineIconPathData = "M8,0h1v16h-1v-16z"
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
    const val selectedToolColor = 0xFF2F87E4
    const val toolColor = 0xFF313131
    const val toolAlpha = 0.5f
    const val gridGap = 100f
    const val highlighterIconPathData = "M398.79,22.31a31.76,31.76 0,0 0,-22.77 -9.52L376,12.79a31.77,31.77 0,0 0,-22.78 9.55L87.53,292.23a32.09,32.09 0,0 0,0.18 45.08l14.7,14.7L16,439.43L16,478L122.8,478l52.8,-52.8 12.48,12.48a32,32 0,0 0,46 -0.77L492.31,160.77a31.91,31.91 0,0 0,-0.6 -44.34ZM109.55,446L54.5,446l46.55,-47.1 27.8,27.8ZM210.71,415.05L175.6,379.95l-24.13,24.13 -27.93,-27.93 23.99,-24.27 -37.19,-37.19 48.34,-49.1L257.8,364.7ZM279.67,341.31 L181.13,242.77L376.02,44.79l92.92,94.12Z"
    const val eraserIconPathData = "M315,285H201.21l124.39,-124.39c5.86,-5.86 5.86,-15.35 0,-21.21l-120,-120c-5.86,-5.86 -15.35,-5.86 -21.21,0l-180,180C1.58,202.21 0,206.02 0,210s1.58,7.79 4.39,10.61l90,90c2.81,2.81 6.63,4.39 10.61,4.39L165,315c0.01,0 0.01,-0 0.02,-0L315,315c8.28,0 15,-6.72 15,-15C330,291.72 323.28,285 315,285zM195,51.21L293.79,150L207,236.79L108.21,138L195,51.21z"
    const val toolIconSize = 32
    val selectedColorIndicatorColor = Color.White
    const val colorBoxHeight = 40f
    const val strokeSliderEndPadding = 12
    const val sliderStartPadding = 40
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
        Color.Transparent,
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
        toolIconSize,
        toolIconSize,
        16f,
        16f
    )
    val verticalLineIcon = createImageVector(
        verticalLineIconPathData,
        toolIconSize,
        toolIconSize,
        16f,
        16f
    )


    val lineIcon = createImageVector(
        lineIconPathData,
        toolIconSize,
        toolIconSize,
        24f,
        24f
    )

    val dashedLineIcon = createImageVector(
        dashedLineIconPathData,
        toolIconSize,
        toolIconSize,
        17f,
        17f
    )

    val ovalIcon = createImageVector(
        ovalIconPathData,
        toolIconSize,
        toolIconSize,
        64f,
        64f
    )

    val circleIcon = createImageVector(
        circleIconPathData,
        toolIconSize,
        toolIconSize,
        512f,
        512f
    )
}