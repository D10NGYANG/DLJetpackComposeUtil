package com.d10ng.compose.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import dljetpackcomposeutil.library.generated.resources.Res
import dljetpackcomposeutil.library.generated.resources.ic_baseline_visibility_24
import dljetpackcomposeutil.library.generated.resources.ic_baseline_visibility_off_24
import org.jetbrains.compose.resources.painterResource

/**
 * 基础输入框
 * @param value String  输入内容
 * @param onValueChange Function1<String, Unit>  输入改变回调
 * @param modifier Modifier  样式
 * @param enabled Boolean  是否允许输入
 * @param readOnly Boolean  是否为只读
 * @param textStyle TextStyle  字体样式
 * @param placeholder String  占位文本
 * @param placeholderStyle TextStyle  占位文本的字体样式
 * @param keyboardOptions KeyboardOptions  键盘类型
 * @param keyboardActions KeyboardActions  键盘事件
 * @param singleLine Boolean  是否为单行输入
 * @param maxLines Int  最大行数
 * @param cursorColor Color  焦点颜色
 * @param isFocus Boolean  是否获取到焦点
 */
@Composable
fun Input(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = AppText.Normal.Body.default,
    placeholder: String = "",
    placeholderStyle: TextStyle = AppText.Normal.Hint.default,
    textAlign: TextAlign = TextAlign.Start,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    cursorColor: Color = AppColor.Main.primary,
    isFocus: Boolean = false
) {
    val focusRequester = remember { FocusRequester() }
    BasicTextField(
        value = value,
        onValueChange = {
            onValueChange.invoke(it)
        },
        modifier = modifier.focusRequester(focusRequester),
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle.copy(textAlign = textAlign),
        decorationBox = { innerTextField ->
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = placeholderStyle.copy(textAlign = textAlign),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            innerTextField()
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        cursorBrush = SolidColor(cursorColor)
    )
    SideEffect {
        if (isFocus) {
            focusRequester.requestFocus()
        }
    }
}

@Composable
fun MenuInput(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = AppText.Normal.Body.v14,
    placeholder: String = "",
    placeholderStyle: TextStyle = AppText.Normal.Hint.v14,
    prefix: String = "",
    suffix: String = "",
    error: String = "",
    menus: List<ListItemMenu> = emptyList(),
    noMatchMenuTips: String = "无符合选项",
    onMenuSelect: (ListItemMenu) -> Unit = {},
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    isFocus: Boolean = false,
    maxLines: Int = 1,
    inputWidth: Dp? = null,
    inputBackgroundColor: Color = AppColor.Neutral.line,
    inputBackgroundShape: Shape = AppShape.RC.v4,
    onClickNext: () -> Unit = {},
    onFocus: () -> Unit = {},
    onNoFocus: () -> Unit = {}
) {
    var isPasswordVisible by remember {
        mutableStateOf(false)
    }
    val visualTransformation = remember(isPassword, isPasswordVisible) {
        if (isPasswordVisible || !isPassword) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        }
    }
    // 计算合适的数据
    val menuList = remember(value, menus) {
        val list = menus.filter { it.show.contains(value) }
        list.subList(0, list.size.coerceAtMost(20))
    }
    val inputModifier = remember(inputWidth) {
        if (inputWidth == null) Modifier.fillMaxWidth()
        else Modifier.width(inputWidth)
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Column(
            modifier = inputModifier
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(inputBackgroundColor, inputBackgroundShape)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (prefix.isNotEmpty() && value.isNotEmpty()) {
                    Text(text = prefix, style = textStyle)
                }
                Input(
                    value = value,
                    onValueChange = {
                        if (it.contains("\n")) {
                            onValueChange.invoke(it.replace("\n", ""))
                            onClickNext.invoke()
                        } else {
                            onValueChange.invoke(it)
                        }
                    },
                    textStyle = textStyle,
                    placeholder = placeholder,
                    placeholderStyle = placeholderStyle,
                    keyboardOptions = KeyboardOptions().copy(
                        keyboardType = keyboardType, imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions { onClickNext.invoke() },
                    visualTransformation = visualTransformation,
                    singleLine = maxLines == 1,
                    maxLines = maxLines,
                    isFocus = isFocus,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .onFocusChanged { state ->
                            if (state.isFocused) {
                                onFocus.invoke()
                            }
                        }
                )
                if (suffix.isNotEmpty() && value.isNotEmpty()) {
                    Text(text = suffix, style = textStyle)
                }
                if (isPassword) {
                    Icon(
                        painter = painterResource(resource = if (isPasswordVisible) Res.drawable.ic_baseline_visibility_24 else Res.drawable.ic_baseline_visibility_off_24),
                        contentDescription = if (isPasswordVisible) "点击隐藏" else "点击显示",
                        tint = textStyle.color,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable { isPasswordVisible = !isPasswordVisible }
                    )
                }
            }
            if (menus.isNotEmpty()) {
                DropdownMenu(
                    expanded = isFocus,
                    onDismissRequest = { onNoFocus.invoke() },
                    properties = PopupProperties(
                        dismissOnBackPress = false,
                        dismissOnClickOutside = false,
                        clippingEnabled = false
                    ),
                    modifier = Modifier.heightIn(max = 200.dp)
                ) {
                    if (menuList.isEmpty()) {
                        DropdownMenuItem(onClick = {}, text = {
                            Text(noMatchMenuTips, style = textStyle)
                        })
                    } else {
                        menuList.forEach { item ->
                            DropdownMenuItem(onClick = {
                                onMenuSelect.invoke(item)
                            }, text = {
                                Text(item.show, style = textStyle)
                            })
                        }
                    }
                }
            }
            if (error.isNotEmpty()) {
                Text(text = error, style = AppText.Normal.Error.v12)
            }
        }
    }
}