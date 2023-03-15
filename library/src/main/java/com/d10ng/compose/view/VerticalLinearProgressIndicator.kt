package com.d10ng.compose.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private val LinearIndicatorHeight = 240.dp
private val LinearIndicatorWidth = ProgressIndicatorDefaults.StrokeWidth

@Composable
fun VerticalLinearProgressIndicator(
    /*@FloatRange(from = 0.0, to = 1.0)*/
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    backgroundColor: Color = color.copy(alpha = ProgressIndicatorDefaults.IndicatorBackgroundOpacity)
) {
    Canvas(
        modifier
            .progressSemantics(progress)
            .size(LinearIndicatorWidth, LinearIndicatorHeight)
            .focusable()
    ) {
        val strokeWidth = size.width
        drawLinearIndicatorBackground(backgroundColor, strokeWidth)
        drawLinearIndicator(0f, progress, color, strokeWidth)
    }
}

private fun DrawScope.drawLinearIndicatorBackground(
    color: Color,
    strokeWidth: Float
) = drawLinearIndicator(0f, 1f, color, strokeWidth)

private fun DrawScope.drawLinearIndicator(
    startFraction: Float,
    endFraction: Float,
    color: Color,
    strokeWidth: Float
) {
    val width = size.width
    val height = size.height
    // Start drawing from the horizontal center of the stroke
    val xOffset = width / 2

    val barStart = height - startFraction * height
    val barEnd = height - endFraction * height

    // Progress line
    drawLine(color, Offset(xOffset, barStart), Offset(xOffset, barEnd), strokeWidth)
}

@Preview
@Composable
fun VerticalLinearProgressIndicator_Test() {
    Box(modifier = Modifier.background(Color.White)) {
        VerticalLinearProgressIndicator(0.4f)
    }
}