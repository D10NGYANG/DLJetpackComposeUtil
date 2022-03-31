package com.d10ng.basicjetpackcomposeapp.dialog.builder

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.d10ng.basicjetpackcomposeapp.view.TimePicker

data class TimePickerDialogBuilder(
    var title: String = "提示",
    var message: String,
    var hour: Int,
    var minute: Int,
    var second: Int,
    var isShowSecond: Boolean = true,
    var sureButton: String = "确定",
    var cancelButton: String = "取消",
    var onClickSure: (Int,Int,Int) -> Unit,
    var onClickCancel: () -> Unit,
): DialogBuilder() {
    
    @Composable
    override fun Build() {
        var hour by remember(this) {
            mutableStateOf(hour)
        }
        var minute by remember(this) {
            mutableStateOf(minute)
        }
        var second by remember(this) {
            mutableStateOf(second)
        }
        BaseDialogBuilder(
            title = title,
            message = message,
            sureButton = sureButton,
            cancelButton = cancelButton,
            onClickSure = {
                onClickSure.invoke(hour, minute, second)
            },
            onClickCancel = onClickCancel
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            TimePicker(
                hour = hour,
                minute = minute,
                second = second,
                isShowSecond = isShowSecond,
                onValueChange = { h,m,s ->
                    hour = h
                    minute = m
                    second = s
                }
            )
        }.Build()
    }

}
