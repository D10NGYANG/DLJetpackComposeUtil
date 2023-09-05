package com.d10ng.compose.ui.base

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.d10ng.compose.R
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText

/**
 * 单元格组件
 * @Author d10ng
 * @Date 2023/9/4 10:07
 */

/**
 * 单元格组
 * @param modifier Modifier 修饰符
 * @param title String 标题
 * @param inset Boolean 是否为圆角卡片风格
 * @param border Boolean 是否显示边框
 * @param bgColor Color 背景色
 * @param content [@androidx.compose.runtime.Composable] [@kotlin.ExtensionFunctionType] Function1<ColumnScope, Unit>
 */
@Composable
fun CellGroup(
    modifier: Modifier = Modifier,
    title: String = "",
    inset: Boolean = false,
    border: Boolean = true,
    bgColor: Color = Color.White,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        if (title.isNotEmpty()) {
            Text(
                text = title,
                style = AppText.Normal.Tips.default,
                modifier = Modifier.padding(16.dp)
            )
        }
        val shape = if (inset) AppShape.RC.v8 else AppShape.RC.v0
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (inset) 16.dp else 0.dp)
                .background(bgColor, shape)
        ) {
            if (border && !inset) Border()
            content()
            if (border && !inset) Border()
        }
    }
}

enum class CellArrowDirection(val degrees: Float) {
    LEFT(180f),
    RIGHT(0f),
    UP(-90f),
    DOWN(90f)
}

/**
 * 单元格
 * @param modifier Modifier 修饰符
 * @param title String 标题
 * @param value String 内容
 * @param label String 描述信息
 * @param icon [@androidx.compose.runtime.Composable] Function0<Unit>? 图标
 * @param border Boolean 是否显示边框
 * @param link Boolean 是否展示右侧箭头并开启点击反馈
 * @param required Boolean 是否显示必填标记
 * @param arrowDirection CellArrowDirection 箭头方向
 * @param onClick Function0<Unit>? 点击事件
 * @param afterTitle [@androidx.compose.runtime.Composable] Function0<Unit>? 标题后面的内容
 * @param afterValue [@androidx.compose.runtime.Composable] Function0<Unit>? 内容后面的内容
 */
@Composable
fun Cell(
    modifier: Modifier = Modifier,
    title: String,
    value: String = "",
    label: String = "",
    icon: @Composable (() -> Unit)? = null,
    border: Boolean = true,
    link: Boolean = false,
    required: Boolean = false,
    arrowDirection: CellArrowDirection = CellArrowDirection.RIGHT,
    onClick: (() -> Unit)? = null,
    afterTitle: @Composable (() -> Unit)? = null,
    afterValue: @Composable (() -> Unit)? = null
) {
    BaseCell(
        modifier = modifier,
        border = border,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.invoke()
            if (required) {
                Text(
                    text = "*",
                    style = AppText.Normal.Error.default,
                    modifier = Modifier.padding(start = if (icon != null) 8.dp else 0.dp)
                )
            }
            Text(
                text = title,
                style = AppText.Normal.Title.default,
                modifier = Modifier
                    .padding(start = if (icon != null && !required) 8.dp else 0.dp)
            )
            afterTitle?.invoke()
            Spacer(modifier = Modifier.weight(1f))
            if (value.isNotEmpty()) {
                Text(
                    text = value,
                    style = AppText.Normal.Body.default
                )
            }
            afterValue?.invoke()
            if (link) Icon(
                painter = painterResource(id = R.drawable.ic_round_arrow_forward_ios_24),
                contentDescription = "箭头",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(18.dp)
                    .rotate(arrowDirection.degrees),
                tint = AppColor.Neutral.body
            )
        }
        if (label.isNotEmpty()) {
            Text(
                text = label,
                style = AppText.Normal.Tips.small,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

/**
 * 基础单元格
 * @param modifier Modifier
 * @param border Boolean
 * @param onClick Function0<Unit>?
 * @param content [@androidx.compose.runtime.Composable] [@kotlin.ExtensionFunctionType] Function1<ColumnScope, Unit>
 */
@Composable
fun BaseCell(
    modifier: Modifier = Modifier,
    border: Boolean = true,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = onClick != null,
                role = Role.Button
            ) {
                onClick?.invoke()
            }
            .padding(horizontal = 16.dp),
    ) {
        content()
        if (border) Border()
    }
}