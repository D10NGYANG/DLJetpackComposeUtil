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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.dialog.DialogColumn
import com.d10ng.compose.view.Input
import kotlinx.coroutines.CoroutineScope

/**
 * 输入弹窗构建器
 * 适用于需要用户填写一个或多个文本字段的场景
 * 每个输入项通过 [InputField] 配置，支持占位符、标签、键盘类型和自定义验证规则
 * @Author d10ng
 * @Date 2023/9/8 09:25
 */
class InputDialogBuilder(
    /** 弹窗标题，为空时不渲染标题行，默认"提示" */
    private val title: String = "提示",
    /** 输入项配置列表，至少包含一项 */
    private val inputs: List<InputField>,
    /** 确定按钮文字，默认"确定" */
    private val confirmText: String = "确定",
    /** 取消按钮文字，默认"取消" */
    private val cancelText: String = "取消",
    /** 确定按钮点击回调，参数为各输入框当前值列表（顺序与 [inputs] 一致），返回 true 则自动关闭弹窗 */
    private val onConfirmClick: suspend CoroutineScope.(List<String>) -> Boolean = { true },
    /** 取消按钮点击回调，返回 true 则自动关闭弹窗，默认直接关闭 */
    private val onCancelClick: suspend CoroutineScope.() -> Boolean = { true }
): DialogBuilder() {

    /**
     * 单个输入项配置
     * @property initValue 初始值，默认空字符串
     * @property placeholder 占位提示文字，默认"请输入"
     * @property label 输入框上方标签文字
     * @property keyboardType 键盘类型，默认文本键盘
     * @property singleLine 是否单行输入，默认 true
     * @property maxLines 最大行数（[singleLine] 为 false 时生效），默认 3
     * @property minLines 最小行数，默认 1
     * @property verify 输入验证函数，入参为当前输入值，返回 [VerifyResult]，默认不验证
     */
    data class InputField(
        var initValue: String = "",
        var placeholder: String = "请输入",
        var label: String,
        var keyboardType: KeyboardType = KeyboardType.Text,
        var singleLine: Boolean = true,
        var maxLines: Int = 3,
        var minLines: Int = 1,
        var verify: (String) -> VerifyResult = { VerifyResult() }
    )

    /**
     * 输入验证结果
     * @property pass 是否通过验证，默认 true
     * @property errorText 验证失败时的错误提示文字，默认空字符串
     */
    data class VerifyResult(
        var pass: Boolean = true,
        var errorText: String = ""
    )

    @Composable
    override fun Build(id: String) {
        val inputValues = remember(this) {
            mutableStateListOf<String>().apply {
                inputs.forEach { item -> add(item.initValue) }
            }
        }
        val errorTexts = remember(this) {
            mutableStateListOf<String>().apply {
                inputs.forEach { _ -> add("") }
            }
        }
        DialogColumn {
            // 标题
            if (title.isNotEmpty()) {
                TipsDialogBuilder.TitleText(text = title)
            }
            // 输入项列表
            inputs.forEachIndexed { index, input ->
                InputFieldView(
                    value = inputValues[index],
                    onValueChange = { inputValues[index] = it },
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
                    if (results.none { !it.pass }) {
                        onConfirmClick(inputValues)
                    } else false
                }
            )
        }
    }

    companion object {
        /**
         * 单个输入项视图
         * 渲染标签、带边框的输入框和错误提示文字
         * @param value String 当前输入值
         * @param onValueChange (String) -> Unit 输入值变化回调
         * @param conf InputField 输入项配置
         * @param error String 错误提示文字，为空时边框显示正常颜色
         */
        @Composable
        fun InputFieldView(
            value: String,
            onValueChange: (String) -> Unit,
            conf: InputField,
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
                        modifier = Modifier.fillMaxWidth(),
                        value = value,
                        onValueChange = onValueChange,
                        textStyle = AppText.Normal.Body.default,
                        placeholder = conf.placeholder,
                        placeholderStyle = AppText.Normal.Hint.default,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = conf.keyboardType),
                        singleLine = conf.singleLine,
                        maxLines = conf.maxLines,
                        minLines = conf.minLines,
                    )
                }
                // 错误提示
                Text(text = error, style = AppText.Normal.Error.small)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF888888)
@Composable
private fun PreviewInputDialogSingle() {
    InputDialogBuilder(
        title = "请输入姓名",
        inputs = listOf(
            InputDialogBuilder.InputField(label = "姓名", placeholder = "请输入您的姓名")
        )
    ).Build("preview")
}

@Preview(showBackground = true, backgroundColor = 0xFF888888)
@Composable
private fun PreviewInputDialogMultiple() {
    InputDialogBuilder(
        title = "账号信息",
        inputs = listOf(
            InputDialogBuilder.InputField(label = "用户名", placeholder = "请输入用户名"),
            InputDialogBuilder.InputField(
                label = "手机号",
                placeholder = "请输入手机号",
                keyboardType = KeyboardType.Phone
            )
        )
    ).Build("preview")
}

@Preview(showBackground = true, backgroundColor = 0xFF888888)
@Composable
private fun PreviewInputDialogWithError() {
    // 模拟验证失败状态的预览
    val builder = InputDialogBuilder(
        title = "设置密码",
        inputs = listOf(
            InputDialogBuilder.InputField(
                label = "密码",
                placeholder = "请输入密码",
                keyboardType = KeyboardType.Password,
                verify = { if (it.length < 6) InputDialogBuilder.VerifyResult(false, "密码长度不能少于6位") else InputDialogBuilder.VerifyResult() }
            )
        )
    )
    builder.Build("preview")
}
