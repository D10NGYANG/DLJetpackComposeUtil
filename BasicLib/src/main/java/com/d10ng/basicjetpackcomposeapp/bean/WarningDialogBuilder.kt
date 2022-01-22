package com.d10ng.basicjetpackcomposeapp.bean

data class WarningDialogBuilder(
    var title: String = "警告",
    var message: String,
    var buttonText: String = "确定",
    var onClickButton: (() -> Unit)? = null
)
