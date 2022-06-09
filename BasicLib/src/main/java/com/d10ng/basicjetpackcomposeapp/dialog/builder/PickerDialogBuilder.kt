package com.d10ng.basicjetpackcomposeapp.dialog.builder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.compose.AppText
import com.d10ng.basicjetpackcomposeapp.view.ListItemPicker

data class PickerDialogBuilder<T>(
    var title: String = "提示",
    var titleAlign: Alignment.Horizontal = Alignment.CenterHorizontally,
    var titleColor: Color = AppColor.Text.title,
    var message: String,
    var messageAlign: Alignment.Horizontal = Alignment.CenterHorizontally,
    var messageColor: Color = AppColor.Text.body,
    var label: (T) -> String = { it.toString() },
    var value: T,
    var list: List<T>,
    var dividersColor: Color = Color.Transparent,
    var textStyle: TextStyle = AppText.Normal.Title.v16,
    var sureButton: String = "确定",
    var sureButtonTextColor: Color = AppColor.On.secondary,
    var sureButtonBackgroundColor: Color = AppColor.System.secondary,
    var cancelButton: String = "取消",
    var cancelButtonTextColor: Color = AppColor.Text.body,
    var onClickSure: (T) -> Unit,
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
                ListItemPicker(
                    label = label,
                    value = pick,
                    onValueChange = {
                        pick = it
                    },
                    dividersColor = dividersColor,
                    list = list,
                    textStyle = textStyle
                )
            }
        }.Build()
    }
}
