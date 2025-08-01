package com.d10ng.compose.ui.sheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.sheet.builder.SheetBuilder

/**
 * 底部弹窗
 * @Author d10ng
 * @Date 2023/9/8 18:00
 */

@Composable
fun Sheet(
    builder: SheetBuilder
) {
    val visible by builder.visibleFlow.collectAsState()
    LaunchedEffect(Unit) {
        builder.visibleFlow.value = true
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            )
        }
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(durationMillis = 300)
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(durationMillis = 300)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        // 拦截点击
                        detectTapGestures {  }
                    }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(builder.topMargin())
                        .pointerInput(Unit) {
                            // 拦截外部的点击
                            detectTapGestures {
                                if (builder.clickOutsideDismiss) builder.dismiss()
                            }
                        }
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .pointerInput(Unit) {
                            // 拦截外部的点击
                            detectTapGestures {
                                if (builder.clickOutsideDismiss) builder.dismiss()
                            }
                        }
                )
                Box {
                    builder.Build()
                }
            }
        }
    }
}

@Composable
fun SheetBox(
    color: Color = Color.White,
    shape: RoundedCornerShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
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
    content: @Composable ColumnScope.() -> Unit
) {
    SheetBox(
        color = color,
        shape = shape
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