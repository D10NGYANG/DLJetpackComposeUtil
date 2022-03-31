package com.d10ng.basicjetpackcomposeapp.dialog.builder

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.d10ng.basicjetpackcomposeapp.view.DialogInput

data class InputDialogBuilder(
    var title: String = "提示",
    var message: String,
    var inputs: List<InputDialogBuilder.Input>,
    var sureButton: String = "确定",
    var cancelButton: String = "取消",
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
            message = message,
            sureButton = sureButton,
            cancelButton = cancelButton,
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
                    onValueChange = {
                        inputValues[index] = it
                    },
                    errorText = errorTexts[index]
                )
            }
        }.Build()
    }
}
