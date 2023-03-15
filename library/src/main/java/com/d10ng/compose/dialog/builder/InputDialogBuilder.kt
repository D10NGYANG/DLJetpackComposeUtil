package com.d10ng.compose.dialog.builder

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.view.DialogInput

data class InputDialogBuilder(
    var title: String = "提示",
    var titleAlign: Alignment.Horizontal = Alignment.Start,
    var titleColor: Color = AppColor.Text.title,
    var message: String,
    var messageAlign: Alignment.Horizontal = Alignment.Start,
    var messageColor: Color = AppColor.Text.body,
    var inputs: List<Input>,
    var sureButton: String = "确定",
    var sureButtonTextColor: Color = AppColor.On.secondary,
    var sureButtonBackgroundColor: Color = AppColor.System.secondary,
    var cancelButton: String = "取消",
    var cancelButtonTextColor: Color = AppColor.Text.body,
    var onClickSure: (List<String>) -> Unit,
    var onClickCancel: () -> Unit,
): DialogBuilder() {
    data class Input(
        var initValue: String = "",
        var placeholder: String = "请输入",
        var keyboardType: KeyboardType = KeyboardType.Text,
        var singleLine: Boolean = true,
        var verify: (String) -> Verify = { Verify() }
    )

    data class Verify(
        var isOK: Boolean = true,
        var errorText: String = ""
    )

    @Composable
    override fun Build() {
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

        BaseDialogBuilder(
            title = title,
            titleAlign = titleAlign,
            titleColor = titleColor,
            message = message,
            messageAlign = messageAlign,
            messageColor = messageColor,
            sureButton = sureButton,
            sureButtonTextColor = sureButtonTextColor,
            sureButtonBackgroundColor = sureButtonBackgroundColor,
            cancelButton = cancelButton,
            cancelButtonTextColor = cancelButtonTextColor,
            onClickSure = {
                val results = inputs.mapIndexed { index, input ->
                    input.verify.invoke(inputValues[index])
                }
                results.forEachIndexed { index, verify ->
                    errorTexts[index] = verify.errorText
                }
                if (results.find { !it.isOK } == null) {
                    onClickSure.invoke(inputValues)
                }
            },
            onClickCancel = onClickCancel
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            inputs.forEachIndexed { index, input ->
                DialogInput(
                    input = input,
                    value = inputValues[index],
                    errorText = errorTexts[index]
                ) {
                    inputValues[index] = it
                }
            }
        }.Build()
    }
}
