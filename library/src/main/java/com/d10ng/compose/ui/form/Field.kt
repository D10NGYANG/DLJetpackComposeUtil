package com.d10ng.compose.ui.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.d10ng.compose.R
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
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
    autoSize: Boolean = false,
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
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
            FieldInput(
                value,
                onValueChange,
                label,
                placeholder,
                type,
                readonly,
                disabled,
                canClear,
                autoSize,
                maxLines,
                minLines
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
        ConstraintLayout {
            val (leftIcon, requiredText, labelText) = createRefs()
            // 左侧图标
            if (leftIconId > 0) {
                Icon(
                    painter = painterResource(id = leftIconId),
                    contentDescription = "",
                    tint = iconColor,
                    modifier = Modifier
                        .constrainAs(leftIcon) {
                            start.linkTo(parent.start)
                            top.linkTo(labelText.top)
                            bottom.linkTo(labelText.bottom)
                        }
                        .size(20.dp)
                )
            }
            // 必填标记
            if (required) {
                Text(
                    text = "*",
                    color = requiredColor,
                    style = AppText.Normal.White.default,
                    modifier = Modifier
                        .constrainAs(requiredText) {
                            start.linkTo(if (leftIconId > 0) leftIcon.end else parent.start)
                            top.linkTo(labelText.top)
                            bottom.linkTo(labelText.bottom)
                        }
                )
            }
            // 标题
            Text(
                text = label,
                color = labelColor,
                style = AppText.Normal.White.default,
                modifier = Modifier
                    .constrainAs(labelText) {
                        val startLink =
                            if (required) requiredText.end else if (leftIconId > 0) leftIcon.end else parent.start
                        val startMargin = if (!required && leftIconId > 0) 8.dp else 0.dp
                        start.linkTo(startLink, margin = startMargin)
                        top.linkTo(parent.top, margin = 16.dp)
                        end.linkTo(parent.end)
                    }
            )
        }
    }
}

@Composable
private fun RowScope.FieldInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    type: KeyboardType = KeyboardType.Text,
    readonly: Boolean = false,
    disabled: Boolean = false,
    canClear: Boolean = false,
    autoSize: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
) {
    var isPasswordVisible by remember {
        mutableStateOf(false)
    }
    val isPassword = remember(type) {
        type == KeyboardType.Password || type == KeyboardType.NumberPassword
    }
    val visualTransformation = remember(type, isPasswordVisible) {
        if (isPasswordVisible || !isPassword) VisualTransformation.None
        else PasswordVisualTransformation('*')
    }
    val textStyle = remember(disabled) {
        if (disabled) AppText.Normal.Hint.default else AppText.Normal.Body.default
    }
    // 输入框
    Input(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .weight(1f),
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder.ifEmpty { "请输入${label}" },
        textStyle = textStyle,
        enabled = !disabled,
        readOnly = readonly,
        singleLine = !autoSize,
        maxLines = maxLines,
        minLines = minLines,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = type),
        visualTransformation = visualTransformation,
    )
    ConstraintLayout(
        modifier = Modifier
            .padding(top = 16.dp)
            .wrapContentWidth()
    ) {
        val (spaceText, clearIcon, passwordIcon) = createRefs()
        // 占位文本
        Text(
            text = "",
            style = AppText.Normal.Body.default,
            modifier = Modifier.constrainAs(spaceText) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(if (canClear) clearIcon.start else passwordIcon.start)
                bottom.linkTo(parent.bottom)
            })
        // 清除图标
        if (canClear) {
            Icon(
                painter = painterResource(id = R.drawable.ic_round_cancel_24),
                contentDescription = "清除",
                tint = AppColor.Neutral.hint,
                modifier = Modifier
                    .constrainAs(clearIcon) {
                        start.linkTo(spaceText.end)
                        end.linkTo(if (isPassword) passwordIcon.start else parent.end)
                        top.linkTo(spaceText.top)
                        bottom.linkTo(spaceText.bottom)
                    }
                    .size(20.dp)
                    .clip(AppShape.RC.Cycle)
                    .clickable { onValueChange("") }
            )
        }
        // 密码图标
        if (isPassword) {
            Icon(
                painter = painterResource(id = if (isPasswordVisible) R.drawable.ic_baseline_visibility_24 else R.drawable.ic_baseline_visibility_off_24),
                contentDescription = if (isPasswordVisible) "点击隐藏" else "点击显示",
                tint = AppColor.Neutral.hint,
                modifier = Modifier
                    .constrainAs(passwordIcon) {
                        start.linkTo(if (canClear) clearIcon.end else spaceText.end)
                        end.linkTo(parent.end)
                        top.linkTo(spaceText.top)
                        bottom.linkTo(spaceText.bottom)
                    }
                    .size(20.dp)
                    .clip(AppShape.RC.Cycle)
                    .clickable { isPasswordVisible = !isPasswordVisible }
            )
        }
    }
}