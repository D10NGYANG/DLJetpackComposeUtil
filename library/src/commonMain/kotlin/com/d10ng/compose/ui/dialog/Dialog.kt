package com.d10ng.compose.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.dialog.builder.DialogBuilder
import com.d10ng.compose.ui.feedback.Overlay

/**
 * 弹窗
 * @Author d10ng
 * @Date 2023/9/7 11:33
 */

/**
 * 弹窗根容器
 * 在 [Overlay] 遮罩上渲染指定 [DialogBuilder] 的内容，
 * 同时将 [id] 传递给 builder 以便在内部调用 dismiss
 * @param builder DialogBuilder 弹窗内容构建器
 * @param id String 弹窗唯一标识，用于关闭弹窗
 */
@Composable
fun Dialog(
    builder: DialogBuilder,
    id: String
) {
    Overlay(
        onDismiss = {
            if (builder.clickOutsideDismiss) DialogBuilder.dismiss(id)
        },
        contentAlignment = builder.contentAlignment
    ) {
        builder.Build(id)
    }
}

/**
 * 弹窗内容容器（Box 版本）
 * 提供统一的圆角白色背景、外边距和内边距，用于包裹自定义弹窗内容
 * @param color Color 背景颜色，默认白色
 * @param shape RoundedCornerShape 圆角形状，默认 12dp 圆角
 * @param margin Dp 距屏幕（或父容器）边缘的外边距，默认 22dp
 * @param padding Dp 内容与背景边缘的内边距，默认 25dp
 * @param content @Composable () -> Unit 弹窗内部内容
 */
@Composable
fun DialogBox(
    color: Color = Color.White,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    margin: Dp = 22.dp,
    padding: Dp = 25.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(margin)
            .background(color, shape)
            .padding(padding)
    ) {
        content()
    }
}

/**
 * 弹窗内容容器（Column 版本）
 * 在 [DialogBox] 基础上内部使用 Column 布局，子元素水平居中排列
 * @param color Color 背景颜色，默认白色
 * @param shape RoundedCornerShape 圆角形状，默认 12dp 圆角
 * @param margin Dp 距屏幕（或父容器）边缘的外边距，默认 22dp
 * @param padding Dp 内容与背景边缘的内边距，默认 25dp
 * @param content @Composable ColumnScope.() -> Unit 弹窗内部列式内容
 */
@Composable
fun DialogColumn(
    color: Color = Color.White,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    margin: Dp = 22.dp,
    padding: Dp = 25.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    DialogBox(
        color = color,
        shape = shape,
        margin = margin,
        padding = padding
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
