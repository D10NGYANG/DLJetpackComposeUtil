package com.d10ng.compose.ui.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.d10ng.compose.utils.BackHandler

/**
 * 遮罩层
 * @Author d10ng
 * @Date 2023/9/7 11:36
 */

/**
 * 遮罩层
 * 全屏半透明黑色遮罩，通常用于弹窗、抽屉等浮层的背景
 * 点击遮罩区域或按下返回键均会触发 [onDismiss] 回调
 * 自动适配状态栏和输入法高度
 * @param onDismiss () -> Unit 点击遮罩或按下返回键时的关闭回调，默认无操作
 * @param contentAlignment Alignment 子内容在遮罩内的对齐方式，默认居中 [Alignment.Center]
 * @param content @Composable BoxScope.() -> Unit 遮罩上方叠加展示的内容
 */
@Composable
fun Overlay(
    onDismiss: () -> Unit = {},
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .pointerInput(Unit) {
                // 拦截外部的点击
                detectTapGestures { onDismiss() }
            }
            .statusBarsPadding()
            .imePadding(),
        content = content,
        contentAlignment = contentAlignment
    )
    BackHandler {
        onDismiss()
    }
}