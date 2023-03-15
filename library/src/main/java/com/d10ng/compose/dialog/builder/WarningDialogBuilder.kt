package com.d10ng.compose.dialog.builder

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.d10ng.compose.ui.AppColor

data class WarningDialogBuilder(
    var title: String = "警告",
    var message: String,
    var buttonText: String = "确定",
    var onClickButton: () -> Unit = {}
): DialogBuilder() {

    @Composable
    override fun Build() {
        BaseDialogBuilder(
            title = title,
            titleAlign = Alignment.CenterHorizontally,
            titleColor = AppColor.System.error,
            message = message,
            messageAlign = Alignment.CenterHorizontally,
            sureButton = buttonText,
            sureButtonTextColor = AppColor.On.error,
            sureButtonBackgroundColor = AppColor.System.error,
            onClickSure = onClickButton
        ).Build()
    }
}
