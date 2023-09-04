package com.d10ng.compose.dialog.builder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.view.DatePicker
import com.d10ng.datelib.curTime
import com.d10ng.datelib.getDateBy

data class DatePickerDialogBuilder(
    var title: String = "提示",
    var titleAlign: Alignment.Horizontal = Alignment.CenterHorizontally,
    var titleColor: Color = AppColor.Neutral.title,
    var message: String,
    var messageAlign: Alignment.Horizontal = Alignment.CenterHorizontally,
    var messageColor: Color = AppColor.Neutral.body,
    var initValue: Long,
    var start: Long = getDateBy(1900, 1, 1, 0, 0, 0, 0),
    var endInclude: Long = curTime,
    var dividersColor: Color = Color.Transparent,
    var textStyle: TextStyle = AppText.Normal.Title.v16,
    var sureButton: String = "确定",
    var sureButtonTextColor: Color = Color.White,
    var sureButtonBackgroundColor: Color = AppColor.Main.primary,
    var cancelButton: String = "取消",
    var cancelButtonTextColor: Color = AppColor.Neutral.body,
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
                onClickSure.invoke(value)
            },
            onClickCancel = onClickCancel
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                DatePicker(
                    value = value,
                    onValueChange = {
                        value = it
                    },
                    start = start,
                    endInclude = endInclude,
                    dividersColor = dividersColor,
                    textStyle = textStyle
                )
            }
        }.Build()
    }
}
