package com.d10ng.compose.ui.sheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
                if (builder!!.clickOutsideDismiss) SheetBuilder.dismiss()
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
    content: @Composable () -> Unit
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = screenHeight * 0.3f, max = screenHeight * 0.7f)
            .background(color, shape)
            .clip(shape),
    ) {
        content()
    }
}

@Composable
fun SheetColumn(
    color: Color = Color.White,
    shape: RoundedCornerShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
    content: @Composable () -> Unit
) {
    SheetBox(
        color = color,
        shape = shape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            content()
        }
    }
}