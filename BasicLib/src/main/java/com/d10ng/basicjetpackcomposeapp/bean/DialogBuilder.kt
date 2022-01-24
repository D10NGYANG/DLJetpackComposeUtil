package com.d10ng.basicjetpackcomposeapp.bean

import androidx.compose.ui.Alignment

data class DialogBuilder(
    var title: String = "提示",
    var titleAlign: Alignment.Horizontal = Alignment.Start,
    var message: String,
    var messageAlign: Alignment.Horizontal = Alignment.Start,
    var sureButton: String = "确定",
    var cancelButton: String = "取消",
    var onClickSure: (() -> Unit)? = null,
    var onClickCancel: (() -> Unit)? = null,
)
