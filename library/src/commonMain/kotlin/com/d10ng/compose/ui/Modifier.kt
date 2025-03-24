package com.d10ng.compose.ui

import androidx.compose.foundation.clickable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 虚线边框
 * @Author d10ng
 * @Date 2023/9/6 16:09
 */

/**
 * 虚线边框
 * @receiver Modifier
 * @param color Color 边框颜色
 * @param width Dp 边框宽度
 * @param dashLength Dp 虚线长度
 * @param cornerRadiusDp Dp 圆角
 * @return Modifier
 */
fun Modifier.dashBorder(
    color: Color = Color.Black,
    width: Dp = 1.dp,
    dashLength:Dp = 5.dp,
    cornerRadiusDp: Dp = 0.dp
) = drawBehind {
    drawIntoCanvas {
        val paint = Paint()
            .apply {
                strokeWidth = width.toPx()
                this.color = color
                style = PaintingStyle.Stroke
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashLength.toPx(), dashLength.toPx()), 0f)
            }
        it.drawRoundRect(
            width.toPx(),
            width.toPx(),
            size.width - width.toPx(),
            size.height - width.toPx(),
            cornerRadiusDp.toPx(),
            cornerRadiusDp.toPx(),
            paint
        )
    }
}

/**
 * 防抖点击
 * @receiver Modifier
 * @param onClick Function0<Unit>
 * @param delayMillis Long
 * @return Modifier
 */
fun Modifier.debounceClick(
    onClick: () -> Unit,
    delayMillis: Long = 1000L
): Modifier = composed {
    val scope = rememberCoroutineScope()
    val clickEnabled = remember { mutableStateOf(true) }

    clickable {
        if (clickEnabled.value) {
            clickEnabled.value = false
            onClick()
            scope.launch {
                delay(delayMillis)
                clickEnabled.value = true
            }
        }
    }
}