package com.d10ng.compose.ui.show

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText

/**
 * 标签
 * @Author d10ng
 * @Date 2023/9/7 10:16
 */

enum class TagType(val color: Color) {
    Primary(AppColor.Main.primary),
    Success(AppColor.Func.success),
    Warning(AppColor.Func.assist),
    Danger(AppColor.Func.error),
}

enum class TagStyle(val shape: Shape) {
    // 填充
    Fill(AppShape.RC.v2),

    // 边框
    Outline(AppShape.RC.v2),

    // 圆角
    Round(AppShape.RC.Cycle),

    // 标记
    Mark(RoundedCornerShape(topEndPercent = 100, bottomEndPercent = 100)),
}

@Composable
fun Tag(
    text: String,
    modifier: Modifier = Modifier,
    type: TagType = TagType.Primary,
    style: TagStyle = TagStyle.Fill,
    color: Color? = null,
    contentColor: Color? = null,
    onClick: (() -> Unit)? = null
) {
    // 字体颜色
    val textColor = remember(style, type) {
        contentColor ?: when (style) {
            TagStyle.Outline -> color ?: type.color
            else -> Color.White
        }
    }
    // 背景颜色
    val bgColor = remember(style, type) {
        when (style) {
            TagStyle.Outline -> Color.Transparent
            else -> color ?: type.color
        }
    }
    // 边框颜色
    val borderColor = remember(style, type) {
        when (style) {
            TagStyle.Outline -> color ?: type.color
            else -> Color.Transparent
        }
    }
    // 背景样式
    val bgShape = style.shape
    Box(
        modifier = modifier
            .background(bgColor, bgShape)
            .then(
                when (style) {
                    TagStyle.Outline -> Modifier.border(1.dp, borderColor, bgShape)
                    else -> Modifier
                }
            )
            .clip(bgShape)
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = AppText.Bold.White.mini,
            color = textColor,
            modifier = Modifier
                .padding(vertical = 3.dp, horizontal = 6.dp)
        )
    }
}