package com.d10ng.compose.ui.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.d10ng.compose.resources.Res
import com.d10ng.compose.resources.ic_baseline_visibility_24
import com.d10ng.compose.resources.ic_baseline_visibility_off_24
import com.d10ng.compose.resources.ic_round_cancel_24
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.defaultPaddingSize
import com.d10ng.compose.ui.show.HorizontalDivider
import com.d10ng.compose.view.Input
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

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
    leftIconResource: DrawableResource? = null,
    leftIconTint: Color = AppColor.Neutral.title,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onSizeChange: (IntSize, IntSize) -> Unit = { _, _ -> },
) {
    var lastSize by remember {
        mutableStateOf<IntSize?>(null)
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 19.dp)
            .onSizeChanged {
                if (lastSize != null) {
                    onSizeChange(
                        it,
                        IntSize(it.width - lastSize!!.width, it.height - lastSize!!.height)
                    )
                }
                lastSize = it
            },
    ) {
        if (labelAlign == FieldLabelAlign.TOP) {
            FieldLeftContent(
                label,
                required,
                disabled,
                labelWidth,
                labelAlign,
                leftIconResource,
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
                    leftIconResource,
                    leftIconTint
                )
            }
            Box(modifier = Modifier.width(defaultPaddingSize))
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
                error,
                maxLines,
                minLines
            )
        }
        if (border) HorizontalDivider()
    }
}

@Composable
private fun FieldLeftContent(
    label: String,
    required: Boolean = false,
    disabled: Boolean = false,
    labelWidth: Dp = 100.dp,
    labelAlign: FieldLabelAlign = FieldLabelAlign.LEFT,
    leftIconResource: DrawableResource? = null,
    leftIconTint: Color = AppColor.Neutral.title,
) {
    val disabledColor = remember { AppColor.Neutral.tips }
    val iconColor = if (disabled) disabledColor else leftIconTint
    val labelColor = if (disabled) disabledColor else AppColor.Neutral.title
    val requiredColor = if (disabled) disabledColor else AppColor.Func.error
    Row(
        modifier = Modifier
            .width(labelWidth)
            .padding(top = defaultPaddingSize),
        horizontalArrangement = labelAlign.align,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leftIconResource != null) {
            Icon(
                painter = painterResource(resource = leftIconResource),
                contentDescription = "",
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }
        if (required) {
            Text(
                text = "*",
                color = requiredColor,
                style = AppText.Normal.Surface.default,
            )
        }
        if (!required && leftIconResource != null) {
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = label,
            color = labelColor,
            style = AppText.Normal.Surface.default,
        )
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
    error: String = "",
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
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
    val placeholderText = remember(value, error, label) {
        if (value.isEmpty() && error.isNotEmpty()) error else placeholder.ifEmpty { "请输入${label}" }
    }
    val placeholderStyle = remember(value, error) {
        if (value.isEmpty() && error.isNotEmpty()) AppText.Normal.Error.default else AppText.Normal.Hint.default
    }
    val showError = remember(value, error) {
        value.isNotEmpty() && error.isNotEmpty()
    }
    // 输入框是否获取焦点
    var isFocus by remember {
        mutableStateOf(false)
    }
    // 是否显示清除按钮
    val isShowClear = remember(value, isFocus, canClear) {
        canClear && isFocus && value.isNotEmpty()
    }

    Column(
        modifier = Modifier
            .padding(vertical = defaultPaddingSize)
            .weight(1f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Input(
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { isFocus = it.isFocused },
                value = value,
                onValueChange = onValueChange,
                placeholder = placeholderText,
                textStyle = textStyle,
                placeholderStyle = placeholderStyle,
                enabled = !disabled,
                readOnly = readonly,
                singleLine = !autoSize && maxLines == 1,
                maxLines = if (autoSize) maxLines else minLines,
                minLines = minLines,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = type),
                visualTransformation = visualTransformation,
            )
            // 占位空文本：与原 ConstraintLayout 的 spaceText 作用相同，
            // 用 Body 样式的行高撑起 Row 最小高度，并作为图标的垂直对齐基准
            Text(text = "", style = AppText.Normal.Body.default)
            if (isShowClear) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(resource = Res.drawable.ic_round_cancel_24),
                    contentDescription = "清除",
                    tint = AppColor.Neutral.hint,
                    modifier = Modifier
                        .size(20.dp)
                        .clip(AppShape.RC.Cycle)
                        .clickable { onValueChange("") }
                )
            }
            if (isPassword) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(
                        resource = if (isPasswordVisible) Res.drawable.ic_baseline_visibility_24
                        else Res.drawable.ic_baseline_visibility_off_24
                    ),
                    contentDescription = if (isPasswordVisible) "点击隐藏" else "点击显示",
                    tint = AppColor.Neutral.hint,
                    modifier = Modifier
                        .size(20.dp)
                        .clip(AppShape.RC.Cycle)
                        .clickable { isPasswordVisible = !isPasswordVisible }
                )
            }
        }
        if (showError) {
            Text(
                text = error,
                style = AppText.Normal.Error.small,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewField() {
    Column {
        Field(value = "", onValueChange = {}, label = "姓名")
        Field(value = "张三", onValueChange = {}, label = "姓名")
        Field(value = "", onValueChange = {}, label = "必填项", required = true)
        Field(value = "内容", onValueChange = {}, label = "带错误", error = "输入有误")
        Field(value = "", onValueChange = {}, label = "禁用", disabled = true)
        Field(value = "只读内容", onValueChange = {}, label = "只读", readonly = true)
    }
}

@Preview
@Composable
fun PreviewFieldLabelAlign() {
    Column {
        Field(value = "", onValueChange = {}, label = "左对齐", labelAlign = FieldLabelAlign.LEFT)
        Field(value = "", onValueChange = {}, label = "居中", labelAlign = FieldLabelAlign.CENTER)
        Field(value = "", onValueChange = {}, label = "右对齐", labelAlign = FieldLabelAlign.RIGHT)
        Field(value = "", onValueChange = {}, label = "顶部对齐", labelAlign = FieldLabelAlign.TOP)
    }
}

@Preview
@Composable
fun PreviewFieldPassword() {
    Column {
        Field(value = "123456", onValueChange = {}, label = "密码", type = KeyboardType.Password)
        Field(value = "123456", onValueChange = {}, label = "数字密码", type = KeyboardType.NumberPassword)
    }
}