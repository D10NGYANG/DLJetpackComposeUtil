package com.d10ng.compose.ui.dialog.builder

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.dialog.DialogColumn
import com.d10ng.compose.view.Input
import kotlinx.coroutines.CoroutineScope

/**
 * 输入弹窗构建器
 * @Author d10ng
 * @Date 2023/9/8 09:25
 */
class InputDialogBuilder(
    // 标题
    private val title: String = "提示",
    // 输入框列表
    private val inputs: List<Input>,
    // 确定按钮文字
    private val confirmText: String = "确定",
    // 取消按钮文字
    private val cancelText: String = "取消",
    // 确定按钮点击事件，返回true则隐藏弹窗
    private val onConfirmClick: suspend CoroutineScope.(List<String>) -> Boolean = { true },
    // 取消按钮点击事件，返回true则隐藏弹窗
    private val onCancelClick: suspend CoroutineScope.() -> Boolean = { true }
): DialogBuilder() {

    data class Input(
        // 初始值
        var initValue: String = "",
        // 输入提示语句
        var placeholder: String = "请输入",
        // 标题
        var label: String,
        // 键盘类型
        var keyboardType: KeyboardType = KeyboardType.Text,
        // 单行输入
        var singleLine: Boolean = true,
        // 输入验证
        var verify: (String) -> Verify = { Verify() }
    )

    data class Verify(
        // 是否通过验证
        var pass: Boolean = true,
        // 错误提示语句
        var errorText: String = ""
    )

    @Composable
    override fun Build(id: Int) {
        val inputValues = remember(this) {
            mutableStateListOf<String>().apply {
                inputs.forEach { item ->
                    this.add(item.initValue)
                }
            }
        }
        val errorTexts = remember(this) {
            mutableStateListOf<String>().apply {
                inputs.forEach { _ ->
                    this.add("")
                }
            }
        }
        DialogColumn {
            // 标题
            if (title.isNotEmpty()) {
                TipsDialogBuilder.TitleText(text = title)
            }
            // 内容
            inputs.forEachIndexed { index, input ->
                InputView(
                    value = inputValues[index],
                    onValueChange = {inputValues[index] = it },
                    conf = input,
                    error = errorTexts[index]
                )
            }
            // 间隔
            Box(modifier = Modifier.height(16.dp))
            // 按钮组
            ConfirmDialogBuilder.ButtonRow(
                id = id,
                cancelText = cancelText,
                confirmText = confirmText,
                onCancelClick = onCancelClick,
                onConfirmClick = {
                    val results = inputs.mapIndexed { index, input ->
                        input.verify(inputValues[index])
                    }
                    results.forEachIndexed { index, verify ->
                        errorTexts[index] = verify.errorText
                    }
                    if (results.find { it.pass.not() } == null) {
                        onConfirmClick(inputValues)
                    } else false
                }
            )
        }
    }

    companion object {
        @Composable
        fun InputView(
            value: String,
            onValueChange: (String) -> Unit,
            conf: Input,
            error: String
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                // label
                Text(text = conf.label, style = AppText.Normal.Body.default)
                // 输入框
                Box(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .border(
                            0.5.dp,
                            if (error.isEmpty()) AppColor.Neutral.hint else AppColor.Func.error,
                            AppShape.RC.v6
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Input(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = value,
                        onValueChange = onValueChange,
                        textStyle = AppText.Normal.Body.default,
                        placeholder = conf.placeholder,
                        placeholderStyle = AppText.Normal.Hint.default,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = conf.keyboardType),
                        singleLine = conf.singleLine
                    )
                }
                // 错误提示
                Text(text = error, style = AppText.Normal.Error.small)
            }
        }
    }
}