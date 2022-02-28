package com.d10ng.basicjetpackcomposeapp.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.d10ng.basicjetpackcomposeapp.bean.InputDialogBuilder
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.compose.AppShape
import com.d10ng.basicjetpackcomposeapp.compose.AppText

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
    textStyle: TextStyle = AppText.Normal.Body.v14,
    placeholder: String = "",
    placeholderStyle: TextStyle = AppText.Normal.Hint.v14,
    textAlign: TextAlign = TextAlign.Start,
    contentAlignment: Alignment = Alignment.TopStart,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    cursorColor: Color = AppColor.System.secondary,
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
            Box (
                contentAlignment = contentAlignment
            ) {
                if (value.isEmpty()) {
                    Text(text = placeholder, style = placeholderStyle.copy(textAlign = textAlign))
                }
                innerTextField()
            }
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        singleLine = singleLine,
        maxLines = maxLines,
        cursorBrush = SolidColor(cursorColor)
    )
    SideEffect {
        if (isFocus) {
            focusRequester.requestFocus()
        }
    }
}

@Composable
fun ColumnScope.DialogInput(
    input: InputDialogBuilder.Input,
    value: String,
    errorText: String = "",
    onValueChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (input.singleLine) 30.dp else 90.dp)
            .border(
                1.dp,
                if (errorText.isEmpty()) AppColor.Text.body else AppColor.Text.error,
                AppShape.RC.v8
            )
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Input(
            value = value,
            onValueChange = onValueChange,
            textStyle = AppText.Normal.Body.v14,
            placeholder = input.placeholder,
            placeholderStyle = AppText.Normal.Hint.v14,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = input.keyboardType),
            singleLine = input.singleLine
        )
    }
    Text(text = errorText, style = AppText.Normal.Error.v12)
}