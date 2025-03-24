package com.d10ng.compose.ui.base

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.d10ng.compose.utils.isDark
import com.d10ng.compose.utils.next

/**
 * 按钮组件
 * @Author d10ng
 * @Date 2023/9/4 10:07
 */

interface ButtonType {
    val color: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.surfaceContainerLowest
    val border: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.outline

    companion object {
        // 默认
        val DEFAULT = object : ButtonType {
            override val color: Color
                @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.surfaceContainerLowest
            override val border: Color
                @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.outline
        }
        // 主要
        val PRIMARY = object : ButtonType {
            override val color: Color
                @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.primary
            override val border: Color
                @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.primary
        }
        // 成功
        val SUCCESS = object : ButtonType {
            override val color: Color
                @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.secondary
            override val border: Color
                @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.secondary
        }
        // 警告
        val WARNING = object : ButtonType {
            override val color: Color
                @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.tertiary
            override val border: Color
                @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.tertiary
        }
        // 危险
        val DANGER = object : ButtonType {
            override val color: Color
                @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.error
            override val border: Color
                @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.error
        }
    }
}

interface ButtonSize {
    val textSize: TextUnit
        @Composable @ReadOnlyComposable get() = MaterialTheme.typography.bodyMedium.fontSize
    val paddingValues: PaddingValues
    val height: Dp

    companion object {
        // 迷你
        val MINI = object : ButtonSize {
            override val textSize: TextUnit
                @Composable @ReadOnlyComposable get() = MaterialTheme.typography.labelMedium.fontSize
            override val paddingValues: PaddingValues
                get() = PaddingValues(6.dp, 2.dp)
            override val height: Dp
                get() = 28.dp
        }
        // 小
        val SMALL = object : ButtonSize {
            override val textSize: TextUnit
                @Composable @ReadOnlyComposable get() = MaterialTheme.typography.bodySmall.fontSize
            override val paddingValues: PaddingValues
                get() = PaddingValues(12.dp, 4.dp)
            override val height: Dp
                get() = 36.dp
        }
        // 默认
        val NORMAL = object : ButtonSize {
            override val textSize: TextUnit
                @Composable @ReadOnlyComposable get() = MaterialTheme.typography.bodyMedium.fontSize
            override val paddingValues: PaddingValues
                get() = ButtonDefaults.ContentPadding
            override val height: Dp
                get() = 40.dp
        }
        // 大
        val BIG = object : ButtonSize {
            override val textSize: TextUnit
                @Composable @ReadOnlyComposable get() = MaterialTheme.typography.bodyLarge.fontSize
            override val paddingValues: PaddingValues
                get() = PaddingValues(21.dp, 7.dp)
            override val height
                get() = 44.dp
        }
        // 巨大
        val LARGE = object : ButtonSize {
            override val textSize: TextUnit
                @Composable @ReadOnlyComposable get() = MaterialTheme.typography.titleMedium.fontSize
            override val paddingValues: PaddingValues
                get() = PaddingValues(36.dp, 12.dp)
            override val height
                get() = 64.dp
        }
    }
}

/**
 * 按钮
 * @param text String 按钮文字
 * @param modifier Modifier 按钮样式
 * @param icon [@androidx.compose.runtime.Composable] Function0<Unit>? 按钮图标
 * @param type ButtonType 按钮类型
 * @param size ButtonSize 按钮尺寸
 * @param plain Boolean 是否朴素按钮
 * @param hairline Boolean 是否细边框
 * @param disabled Boolean 是否禁用
 * @param loading Boolean 是否加载中
 * @param shape CornerBasedShape 按钮形状
 * @param color Color? 按钮颜色
 * @param onClick Function0<Unit> 点击事件
 */
@Composable
fun Button(
    text: String,
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    type: ButtonType = ButtonType.DEFAULT,
    size: ButtonSize = ButtonSize.NORMAL,
    plain: Boolean = false,
    hairline: Boolean = false,
    disabled: Boolean = false,
    loading: Boolean = false,
    shape: CornerBasedShape = MaterialTheme.shapes.extraSmall,
    color: Color? = null,
    onClick: () -> Unit
) {
    // 主要颜色
    val mainColor = color ?: type.color
    // 背景颜色
    val bgColor = if (plain) Color.White else mainColor
    // 内容颜色
    val contentColor = if (plain) {
        if (mainColor.isDark()) mainColor else MaterialTheme.colorScheme.onSurface
    } else if (mainColor.isDark()) Color.White else MaterialTheme.colorScheme.onSurface
    // 边框颜色
    val borderColor = color ?: type.border
    val thisModifier = remember {
        if (size == ButtonSize.MINI || size == ButtonSize.SMALL) {
            modifier.height(size.height)
        } else {
            modifier
        }
    }
    Button(
        onClick = {
            if (!disabled && !loading) onClick()
        },
        modifier = thisModifier,
        enabled = !disabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = bgColor,
            disabledContainerColor = bgColor.next(0.5),
            contentColor = contentColor,
            disabledContentColor = contentColor.next(0.5),
        ),
        border = BorderStroke(
            width = if (hairline) 0.3.dp else 1.dp,
            color = if (disabled) borderColor.next(0.5) else borderColor
        ),
        contentPadding = size.paddingValues
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(14.dp),
                color = contentColor,
                strokeWidth = 2.dp,
                strokeCap = StrokeCap.Round
            )
        } else {
            if (icon != null) {
                icon()
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(text = text, fontSize = size.textSize)
        }
    }
}