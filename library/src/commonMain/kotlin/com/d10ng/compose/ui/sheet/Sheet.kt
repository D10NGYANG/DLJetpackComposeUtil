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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.sheet.builder.SheetBuilder

/**
 * 底部弹窗容器
 *
 * 全屏覆盖层，包含半透明黑色遮罩和从底部滑入的弹窗内容区域。
 * 遮罩和内容分别通过 [AnimatedVisibility] 实现淡入/淡出和滑入/滑出动画。
 * 点击弹窗外部区域（遮罩部分）可根据 [SheetBuilder.clickOutsideDismiss] 配置决定是否关闭弹窗。
 *
 * @param builder [SheetBuilder] 弹窗构建器实例，负责提供弹窗内容和控制弹窗的显隐状态
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

/**
 * 底部弹窗内容盒子
 *
 * 为弹窗内容提供统一的白色圆角背景容器，使用 [BoxScope] 布局。
 * 默认顶部左右圆角 12dp，适合作为底部弹窗最外层容器使用。
 *
 * @param color Color 容器背景色，默认白色
 * @param shape RoundedCornerShape 容器圆角形状，默认顶部左右 12dp 圆角
 * @param content [@Composable] BoxScope 内容插槽
 */
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

/**
 * 底部弹窗内容列布局
 *
 * 基于 [SheetBox] 的纵向布局容器，内部使用 [Column] 排列子组件，
 * 并自动应用 [navigationBarsPadding] 以适配系统导航栏。
 * 适用于弹窗内容需要纵向排列的场景（如标题栏 + 选择器）。
 *
 * @param color Color 容器背景色，默认白色
 * @param shape RoundedCornerShape 容器圆角形状，默认顶部左右 12dp 圆角
 * @param content [@Composable] ColumnScope 内容插槽
 */
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

@Preview(showBackground = true)
@Composable
private fun PreviewSheetBox() {
    Box(modifier = Modifier.background(Color(0xFFEEEEEE))) {
        SheetBox {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "SheetBox 内容区域", style = AppText.Normal.Title.default)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSheetColumn() {
    Box(modifier = Modifier.background(Color(0xFFEEEEEE))) {
        SheetColumn {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "标题", style = AppText.Bold.Title.large)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "SheetColumn 内容区域", style = AppText.Normal.Title.default)
            }
        }
    }
}