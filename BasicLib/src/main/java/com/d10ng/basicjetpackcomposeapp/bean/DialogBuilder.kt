package com.d10ng.basicjetpackcomposeapp.bean

data class DialogBuilder(
    var title: String = "提示",
    var message: String,
    var sureButton: String = "确定",
    var cancelButton: String = "取消",
    var onClickSure: (() -> Unit)? = null,
    var onClickCancel: (() -> Unit)? = null,
)
