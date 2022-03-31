package com.d10ng.basicjetpackcomposeapp.dialog.builder

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.d10ng.basicjetpackcomposeapp.view.DatePicker
import com.d10ng.datelib.curTime
import com.d10ng.datelib.getDateBy

data class DatePickerDialogBuilder(
    var title: String = "提示",
    var message: String,
    var initValue: Long,
    var start: Long = getDateBy(1900, 1, 1, 0, 0, 0, 0),
    var endInclude: Long = curTime,
    var sureButton: String = "确定",
    var cancelButton: String = "取消",
    var onClickSure: (Long) -> Unit,
    var onClickCancel: () -> Unit,
): DialogBuilder() {
    
    @Composable
    override fun Build() {
        var value by remember(this) {
            mutableStateOf(initValue)
        }
        BaseDialogBuilder(
            title = title,
            message = message,
            sureButton = sureButton,
            cancelButton = cancelButton,
            onClickSure = {
                onClickSure.invoke(value)
            },
            onClickCancel = onClickCancel
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            DatePicker(
                value = value,
                onValueChange = {
                    value = it
                },
                start = start,
                endInclude = endInclude
            )
        }.Build()
    }
}
