package com.d10ng.basicjetpackcomposeapp.bean

data class NormalDialogBuilder(
    var title: String = "",
    var message: String = "",
    var sureButton: String? = null,
    var cancelButton: String? = null,
    var onClickButton: ((which: Int) -> Unit)? = null,
)
