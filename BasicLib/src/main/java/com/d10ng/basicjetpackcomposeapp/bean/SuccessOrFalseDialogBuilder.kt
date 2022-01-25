package com.d10ng.basicjetpackcomposeapp.bean

data class SuccessOrFalseDialogBuilder(
    var title: String = "提示",
    var message: String = "",
    var isSuccess: Boolean = true,
    var buttonText: String = "确定",
    var onClickButton: () -> Unit = {}
)
