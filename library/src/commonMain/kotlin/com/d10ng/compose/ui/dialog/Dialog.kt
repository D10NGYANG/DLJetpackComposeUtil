package com.d10ng.compose.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.d10ng.compose.ui.dialog.builder.DialogBuilder
import androidx.compose.ui.window.Dialog as ComposeDialog

/**
 * 弹窗
 * @Author d10ng
 * @Date 2023/9/7 11:33
 */

/**
 * 弹窗根容器
 * 使用 Compose UI 官方 [ComposeDialog] 建立平台模态窗口，并渲染指定 [DialogBuilder] 的内容。
 * 兼容旧 Builder 的任意 [Alignment] 定位；关闭策略仍由 [DialogBuilder.clickOutsideDismiss] 统一控制。
 * @param builder DialogBuilder 弹窗内容构建器
 * @param id String 弹窗唯一标识，用于关闭弹窗
 */
@Composable
fun Dialog(
    builder: DialogBuilder,
    id: String
) {
    val dismissPolicy = resolveDialogDismissPolicy(builder.clickOutsideDismiss)
    val dismiss by rememberUpdatedState { DialogBuilder.dismiss(id) }
    ComposeDialog(
        onDismissRequest = dismiss,
        properties = DialogProperties(
            dismissOnBackPress = dismissPolicy.dismissOnBackPress,
            // 全屏对齐容器负责兼容旧 contentAlignment，因此由容器识别内容外点击。
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(dismissPolicy.dismissOnClickOutside) {
                    detectTapGestures {
                        if (dismissPolicy.dismissOnClickOutside) dismiss()
                    }
                },
            contentAlignment = builder.contentAlignment,
        ) {
            Box(
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures { }
                }
            ) {
                builder.Build(id)
            }
        }
    }
}

/**
 * 弹窗内容容器（Box 版本）
 * 提供统一的圆角白色背景、外边距和内边距，用于包裹自定义弹窗内容
 * @param color Color 背景颜色，默认白色
 * @param shape RoundedCornerShape 圆角形状，默认 12dp 圆角
 * @param margin Dp 距屏幕（或父容器）边缘的外边距，默认 22dp
 * @param padding Dp 内容与背景边缘的内边距，默认 25dp
 * @param maxWidth Dp 弹窗最大宽度，默认 560dp
 * @param content @Composable () -> Unit 弹窗内部内容
 */
@Composable
fun DialogBox(
    color: Color = Color.White,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    margin: Dp = 22.dp,
    padding: Dp = 25.dp,
    maxWidth: Dp = 560.dp,
    content: @Composable () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = margin),
        contentAlignment = Alignment.Center
    ) {
        val dialogWidth = this.maxWidth.coerceAtMost(maxWidth)

        Box(
            modifier = Modifier
                .width(dialogWidth)
                .background(color, shape)
                .padding(padding)
        ) {
            content()
        }
    }
}

/**
 * 弹窗内容容器（Column 版本）
 * 在 [DialogBox] 基础上内部使用 Column 布局，子元素水平居中排列
 * @param color Color 背景颜色，默认白色
 * @param shape RoundedCornerShape 圆角形状，默认 12dp 圆角
 * @param margin Dp 距屏幕（或父容器）边缘的外边距，默认 22dp
 * @param padding Dp 内容与背景边缘的内边距，默认 25dp
 * @param maxWidth Dp 弹窗最大宽度，默认 560dp
 * @param content @Composable ColumnScope.() -> Unit 弹窗内部列式内容
 */
@Composable
fun DialogColumn(
    color: Color = Color.White,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    margin: Dp = 22.dp,
    padding: Dp = 25.dp,
    maxWidth: Dp = 560.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    DialogBox(
        color = color,
        shape = shape,
        margin = margin,
        padding = padding,
        maxWidth = maxWidth
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF888888)
@Composable
private fun PreviewDialogBox() {
    DialogBox {
        Text(text = "DialogBox 内容区域")
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF888888)
@Composable
private fun PreviewDialogColumn() {
    DialogColumn {
        Text(text = "DialogColumn 内容区域")
        Text(text = "第二行内容", modifier = Modifier.padding(top = 8.dp))
    }
}
