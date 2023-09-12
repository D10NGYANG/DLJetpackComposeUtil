package com.d10ng.compose.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import com.d10ng.compose.ui.AppText
import com.d10ng.datelib.curTime

/**
 * 时间选择器
 * @Author d10ng
 * @Date 2023/9/11 19:11
 */

enum class TimePickerMode(

) {

}

@Composable
fun TimePicker(
    value: Long,
    onValueChange: (Long) -> Unit,
    start: Long = 0,
    endInclude: Long = curTime,
    textStyle: TextStyle = AppText.Normal.Title.default,
    mode: DatePickerMode = DatePickerMode.YMD,
    itemText: (Int, String) -> String = { _, item -> item },
) {
    // TODO
}

