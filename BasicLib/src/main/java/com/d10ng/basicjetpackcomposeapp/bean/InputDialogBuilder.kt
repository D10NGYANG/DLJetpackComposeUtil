package com.d10ng.basicjetpackcomposeapp.bean

import androidx.compose.ui.text.input.KeyboardType

data class InputDialogBuilder(
    var title: String = "提示",
    var message: String,
    var inputs: List<Input>,
    var sureButton: String = "确定",
    var cancelButton: String = "取消",
    var onClickSure: (List<String>) -> Unit,
    var onClickCancel: () -> Unit,
) {
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
}
