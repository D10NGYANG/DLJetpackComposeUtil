package com.d10ng.compose.ui.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.base.Border
import com.d10ng.compose.view.Input

/**
 * 输入框
 * @Author d10ng
 * @Date 2023/9/5 09:51
 */

enum class FieldLabelAlign(val align: Arrangement.Horizontal) {
    // 左对齐
    LEFT(Arrangement.Start),

    // 居中
    CENTER(Arrangement.Center),

    // 右对齐
    RIGHT(Arrangement.End),

    // 顶部
    TOP(Arrangement.Start),
}

@Composable
fun Field(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    type: KeyboardType = KeyboardType.Text,
    readonly: Boolean = false,
    disabled: Boolean = false,
    canClear: Boolean = false,
    required: Boolean = false,
    border: Boolean = true,
    autosize: Boolean = false,
    error: String = "",
    labelWidth: Dp = 100.dp,
    labelAlign: FieldLabelAlign = FieldLabelAlign.LEFT,
    leftIconId: Int = 0,
    leftIconTint: Color = AppColor.Neutral.title,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        if (labelAlign == FieldLabelAlign.TOP) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
            ) {
                FieldLeftContent(
                    label,
                    required,
                    disabled,
                    labelWidth,
                    labelAlign,
                    leftIconId,
                    leftIconTint
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            if (labelAlign != FieldLabelAlign.TOP) {
                FieldLeftContent(
                    label,
                    required,
                    disabled,
                    labelWidth,
                    labelAlign,
                    leftIconId,
                    leftIconTint
                )
            }
            // 输入框
            Input(
                modifier = Modifier
                    .weight(1f),
                value = value,
                onValueChange = onValueChange,
                placeholder = placeholder.ifEmpty { "请输入${label}" },
                enabled = !disabled,
                readOnly = readonly,
                singleLine = maxLines == 1,
                maxLines = maxLines,
                minLines = minLines,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = type),
            )
        }
        if (border) Border()
    }
}

@Composable
private fun FieldLeftContent(
    label: String,
    required: Boolean = false,
    disabled: Boolean = false,
    labelWidth: Dp = 100.dp,
    labelAlign: FieldLabelAlign = FieldLabelAlign.LEFT,
    leftIconId: Int = 0,
    leftIconTint: Color = AppColor.Neutral.title,
) {
    val disabledColor = remember {
        AppColor.Neutral.tips
    }
    val iconColor = if (disabled) disabledColor else leftIconTint
    val labelColor = if (disabled) disabledColor else AppColor.Neutral.title
    val requiredColor = if (disabled) disabledColor else AppColor.Func.error
    Row(
        modifier = Modifier
            .width(labelWidth),
        horizontalArrangement = labelAlign.align
    ) {
        // 左侧图标
        if (leftIconId > 0) {
            Icon(
                painter = painterResource(id = leftIconId),
                contentDescription = "",
                tint = iconColor,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(18.dp)
            )
        }
        // 必填标记
        if (required) {
            Text(text = "*", color = requiredColor)
        }
        // 标题
        Text(text = label, color = labelColor)
    }
}