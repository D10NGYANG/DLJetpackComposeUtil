package com.d10ng.compose.ui.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.show.HorizontalDivider
import com.d10ng.compose.view.Input
import dljetpackcomposeutil.library.generated.resources.Res
import dljetpackcomposeutil.library.generated.resources.ic_baseline_visibility_24
import dljetpackcomposeutil.library.generated.resources.ic_baseline_visibility_off_24
import dljetpackcomposeutil.library.generated.resources.ic_round_cancel_24
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
            Box(modifier = Modifier.width(16.dp))
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
            if (leftIconResource != null) {
                Icon(
                    painter = painterResource(resource = leftIconResource),
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
                            start.linkTo(if (leftIconResource != null) leftIcon.end else parent.start)
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
                            if (required) requiredText.end else if (leftIconResource != null) leftIcon.end else parent.start
                        val startMargin = if (!required && leftIconResource != null) 8.dp else 0.dp
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
    error: String = "",
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
    // 错误提示
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

    ConstraintLayout(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .weight(1f)
    ) {
        val (input, errorText, spaceText, clearIcon, passwordIcon) = createRefs()
        // 输入框
        Input(
            modifier = Modifier
                .constrainAs(input) {
                    width = Dimension.fillToConstraints
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(spaceText.start)
                    bottom.linkTo(if (showError) errorText.top else parent.bottom)
                }
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
        // 错误信息
        if (showError) {
            Text(
                text = error,
                style = AppText.Normal.Error.small,
                modifier = Modifier
                    .constrainAs(errorText) {
                        width = Dimension.fillToConstraints
                        start.linkTo(input.start)
                        top.linkTo(input.bottom, 4.dp)
                        end.linkTo(input.end)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }
        // 占位文本
        Text(
            text = "",
            style = AppText.Normal.Body.default,
            modifier = Modifier.constrainAs(spaceText) {
                start.linkTo(input.end)
                top.linkTo(parent.top)
                val endLink = if (isShowClear) clearIcon.start
                else if (isPassword) passwordIcon.start else parent.end
                end.linkTo(endLink)
            })
        // 清除图标
        if (isShowClear) {
            Icon(
                painter = painterResource(resource = Res.drawable.ic_round_cancel_24),
                contentDescription = "清除",
                tint = AppColor.Neutral.hint,
                modifier = Modifier
                    .constrainAs(clearIcon) {
                        start.linkTo(spaceText.end, 8.dp)
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
                painter = painterResource(resource = if (isPasswordVisible) Res.drawable.ic_baseline_visibility_24 else Res.drawable.ic_baseline_visibility_off_24),
                contentDescription = if (isPasswordVisible) "点击隐藏" else "点击显示",
                tint = AppColor.Neutral.hint,
                modifier = Modifier
                    .constrainAs(passwordIcon) {
                        start.linkTo(if (isShowClear) clearIcon.end else spaceText.end, 8.dp)
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