package com.d10ng.basicjetpackcomposeapp.bean

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
)
