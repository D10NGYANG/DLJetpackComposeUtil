package com.d10ng.compose.ui.show

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText

/**
 * 标签
 * @Author d10ng
 * @Date 2023/9/7 10:16
 */

/**
 * 标签类型枚举
 *
 * 定义标签的语义颜色，每种类型对应一个预设的主题色。
 */
enum class TagType(val color: Color) {
    /** 主要：使用主色调 [AppColor.Main.primary] */
    Primary(AppColor.Main.primary),
    /** 成功：使用成功色 [AppColor.Func.success] */
    Success(AppColor.Func.success),
    /** 警告：使用辅助色 [AppColor.Func.assist] */
    Warning(AppColor.Func.assist),
    /** 危险：使用错误色 [AppColor.Func.error] */
    Danger(AppColor.Func.error),
}

/**
 * 标签样式枚举
 *
 * 定义标签的外观形状与填充方式。
 */
enum class TagStyle(val shape: Shape) {
    /** 填充样式：小圆角矩形背景，白色文字 */
    Fill(AppShape.RC.v2),
    /** 边框样式：透明背景 + 1dp 彩色边框，文字颜色与边框色一致 */
    Outline(AppShape.RC.v2),
    /** 圆角样式：完整圆角（胶囊形）彩色背景，白色文字 */
    Round(AppShape.RC.Cycle),
    /** 标记样式：右侧半圆弧形背景，白色文字，适合用作角标 */
    Mark(RoundedCornerShape(topEndPercent = 100, bottomEndPercent = 100)),
}

/**
 * 标签
 *
 * 用于展示状态、分类或属性的小型标记组件，支持多种语义类型和外观样式。
 * 可通过 [color] 和 [contentColor] 覆盖类型预设颜色，传入 [onClick] 时标签可点击。
 *
 * @param text String 标签显示的文本内容
 * @param modifier Modifier 修饰符，默认为 [Modifier]
 * @param type TagType 标签语义类型，决定预设颜色，默认为 [TagType.Primary]
 * @param style TagStyle 标签外观样式，决定形状和填充方式，默认为 [TagStyle.Fill]
 * @param color Color? 自定义标签主色（背景色或边框色），为 null 时使用 [type] 的预设颜色，默认为 null
 * @param contentColor Color? 自定义文字颜色，为 null 时根据 [style] 自动计算，默认为 null
 * @param onClick (() -> Unit)? 点击回调；为 null 时标签不可点击，默认为 null
 */
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
            style = AppText.Bold.Surface.mini,
            color = textColor,
            modifier = Modifier
                .padding(vertical = 3.dp, horizontal = 6.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTagTypes() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Fill 样式 - 各类型
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Tag(text = "主要", type = TagType.Primary, style = TagStyle.Fill)
            Tag(text = "成功", type = TagType.Success, style = TagStyle.Fill)
            Tag(text = "警告", type = TagType.Warning, style = TagStyle.Fill)
            Tag(text = "危险", type = TagType.Danger, style = TagStyle.Fill)
        }
        // Outline 样式 - 各类型
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Tag(text = "主要", type = TagType.Primary, style = TagStyle.Outline)
            Tag(text = "成功", type = TagType.Success, style = TagStyle.Outline)
            Tag(text = "警告", type = TagType.Warning, style = TagStyle.Outline)
            Tag(text = "危险", type = TagType.Danger, style = TagStyle.Outline)
        }
        // Round 样式 - 各类型
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Tag(text = "主要", type = TagType.Primary, style = TagStyle.Round)
            Tag(text = "成功", type = TagType.Success, style = TagStyle.Round)
            Tag(text = "警告", type = TagType.Warning, style = TagStyle.Round)
            Tag(text = "危险", type = TagType.Danger, style = TagStyle.Round)
        }
        // Mark 样式 - 各类型
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Tag(text = "主要", type = TagType.Primary, style = TagStyle.Mark)
            Tag(text = "成功", type = TagType.Success, style = TagStyle.Mark)
            Tag(text = "警告", type = TagType.Warning, style = TagStyle.Mark)
            Tag(text = "危险", type = TagType.Danger, style = TagStyle.Mark)
        }
        // 自定义颜色
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Tag(text = "自定义", color = Color(0xFF9C27B0))
            Tag(text = "自定义边框", style = TagStyle.Outline, color = Color(0xFF9C27B0))
        }
    }
}
