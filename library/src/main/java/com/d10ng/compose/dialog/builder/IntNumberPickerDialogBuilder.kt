package com.d10ng.compose.dialog.builder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.view.IntNumberPicker

data class IntNumberPickerDialogBuilder(
    var title: String = "提示",
    var titleAlign: Alignment.Horizontal = Alignment.CenterHorizontally,
    var titleColor: Color = AppColor.Text.title,
    var message: String,
    var messageAlign: Alignment.Horizontal = Alignment.CenterHorizontally,
    var messageColor: Color = AppColor.Text.body,
    var label: (Int) -> String = { it.toString() },
    var value: Int,
    var start: Int = Int.MIN_VALUE,
    var endInclude: Int = Int.MAX_VALUE,
    var step: Int = 1,
    var dividersColor: Color = Color.Transparent,
    var textStyle: TextStyle = AppText.Normal.Title.v16,
    var sureButton: String = "确定",
    var sureButtonTextColor: Color = AppColor.On.secondary,
    var sureButtonBackgroundColor: Color = AppColor.System.secondary,
    var cancelButton: String = "取消",
    var cancelButtonTextColor: Color = AppColor.Text.body,
    var onClickSure: (Int) -> Unit,
    var onClickCancel: () -> Unit,
): DialogBuilder() {

    @Composable
    override fun Build() {
        var pick by remember(this) {
            mutableStateOf(value)
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
                onClickSure.invoke(pick)
            },
            onClickCancel = onClickCancel
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                IntNumberPicker(
                    label = label,
                    value = pick,
                    onValueChange = {
                        pick = it
                    },
                    dividersColor = dividersColor,
                    start = start,
                    endInclude = endInclude,
                    step = step,
                    textStyle = textStyle
                )
            }
        }.Build()
    }

}
