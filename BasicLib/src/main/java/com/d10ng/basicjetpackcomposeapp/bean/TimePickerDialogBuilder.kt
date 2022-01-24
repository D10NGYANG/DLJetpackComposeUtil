package com.d10ng.basicjetpackcomposeapp.bean

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
)
