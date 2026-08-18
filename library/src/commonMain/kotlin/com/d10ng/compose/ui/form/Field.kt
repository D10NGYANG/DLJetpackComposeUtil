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
 * 标签对齐方式
 * LEFT / CENTER / RIGHT 为标签与输入框水平排列，TOP 为标签单独一行显示在输入框上方
 */
enum class FieldLabelAlign(val align: Arrangement.Horizontal) {
    // 左对齐（标签与输入框同行，标签靠左）
    LEFT(Arrangement.Start),

    // 居中（标签与输入框同行，标签文字居中）
    CENTER(Arrangement.Center),

    // 右对齐（标签与输入框同行，标签文字靠右）
    RIGHT(Arrangement.End),

    // 顶部（标签单独一行置于输入框上方）
    TOP(Arrangement.Start),
}

/**
 * 表单输入框
 * 包含左侧标签区和右侧输入内容区，支持只读、禁用、密码、清除、错误提示等常用场景
 * 当 [type] 为 [KeyboardType.Password] 或 [KeyboardType.NumberPassword] 时，自动显示密码可见性切换图标
 * @param modifier Modifier 修饰符
 * @param value String 当前输入的文本内容
 * @param onValueChange (String) -> Unit 文本内容变更回调
 * @param label String 标签文字，同时作为占位文字的默认来源（「请输入{label}」）
 * @param placeholder String 自定义占位文字，为空时使用「请输入{label}」，默认为空
 * @param type KeyboardType 键盘类型，影响软键盘样式，为密码类型时自动启用密码掩码，默认 [KeyboardType.Text]
 * @param readonly Boolean 是否只读，只读时不弹出键盘但可选中文本，默认 false
 * @param disabled Boolean 是否禁用，禁用时文字变灰且不可交互，默认 false
 * @param canClear Boolean 是否显示一键清除按钮，仅在输入框获得焦点且有内容时显示，默认 false
 * @param required Boolean 是否在标签左侧显示红色必填星号 (*)，默认 false
 * @param border Boolean 是否在底部显示分割线，默认 true
 * @param autoSize Boolean 是否允许输入框随内容自动增高（多行模式），为 false 时高度固定为 [minLines]，默认 false
 * @param error String 错误提示文字；有值且输入内容不为空时在输入框下方以红色小字显示，为空时占位文字也会以红色提示，默认为空
 * @param labelWidth Dp 标签区域的固定宽度，默认 100.dp
 * @param labelAlign FieldLabelAlign 标签对齐方式，默认 [FieldLabelAlign.LEFT]
 * @param leftIconResource DrawableResource? 标签左侧图标资源，为 null 时不显示，默认 null
 * @param leftIconTint Color 标签左侧图标的着色，默认 [AppColor.Neutral.title]
 * @param maxLines Int 最大行数，[autoSize] 为 true 时限制最大高度，默认不限制
 * @param minLines Int 最小行数（即默认显示高度），默认 1
 * @param onSizeChange (IntSize, IntSize) -> Unit 组件整体尺寸变化回调，第一个参数为新尺寸，第二个为与上次的差值，默认无操作
 */
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