package com.d10ng.compose.ui.sheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.feedback.Overlay
import com.d10ng.compose.ui.sheet.builder.SheetBuilder

/**
 * 底部弹窗
 * @Author d10ng
 * @Date 2023/9/8 18:00
 */

@Composable
fun Sheet(
    builder: SheetBuilder? = null
) {
    AnimatedVisibility(
        visible = builder != null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Overlay(
            onDismiss = {
                if (builder!!.clickOutsideDismiss) builder.dismiss()
            },
            contentAlignment = Alignment.BottomCenter
        ) {
            AnimatedVisibility(
                visible = builder != null,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                builder?.Build()
            }
        }
    }
}

@Composable
fun SheetBox(
    color: Color = Color.White,
    shape: RoundedCornerShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
    minHeight: Float = 0.1f,
    maxHeight: Float = 0.7f,
    content: @Composable BoxScope.() -> Unit
) {
    var parentHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { layoutCoordinates ->
                // Get the height of the parent layout
                parentHeight = with(density) { layoutCoordinates.size.height.toFloat().toDp() }
            },
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = parentHeight * minHeight, max = parentHeight * maxHeight)
                .background(color, shape)
                .clip(shape),
        ) {
            content()
        }
    }

}

@Composable
fun SheetColumn(
    color: Color = Color.White,
    shape: RoundedCornerShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
    minHeight: Float = 0.1f,
    maxHeight: Float = 0.7f,
    content: @Composable ColumnScope.() -> Unit
) {
    SheetBox(
        color = color,
        shape = shape,
        minHeight = minHeight,
        maxHeight = maxHeight
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            content()
        }
    }
}