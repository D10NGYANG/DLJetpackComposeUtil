package com.d10ng.basicjetpackcomposeapp.bean

data class ProgressDialogBuilder(
    var title: String = "进度",
    var message: String = "",
    var progress: Long = 0,
    var max: Long = 100,
    var isShowProgressText: Boolean = true,
    var progressTextStyle: ProgressTextStyle = ProgressTextStyle.PERCENTAGE
) {
    enum class ProgressTextStyle {
        PROGRESS_AND_MAX,
        PERCENTAGE
    }
}
