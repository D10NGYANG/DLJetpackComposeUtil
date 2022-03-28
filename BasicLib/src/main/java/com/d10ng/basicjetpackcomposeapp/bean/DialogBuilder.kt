package com.d10ng.basicjetpackcomposeapp.bean

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.d10ng.basicjetpackcomposeapp.compose.AppColor

data class DialogBuilder(
    var title: String = "提示",
    var titleAlign: Alignment.Horizontal = Alignment.Start,
    var titleColor: Color = AppColor.Text.title,
    var message: String,
    var messageAlign: Alignment.Horizontal = Alignment.Start,
    var messageColor: Color = AppColor.Text.body,
    var sureButton: String = "确定",
    var sureButtonTextColor: Color = AppColor.On.secondary,
    var sureButtonBackgroundColor: Color = AppColor.System.secondary,
    var cancelButton: String = "取消",
    var cancelButtonTextColor: Color = AppColor.Text.body,
    var onClickSure: (() -> Unit)? = null,
    var onClickCancel: (() -> Unit)? = null,
)

/**
 * 错误弹窗
 * @param title String
 * @param titleAlign Horizontal
 * @param message String
 * @param messageAlign Horizontal
 * @param sureButton String
 * @param cancelButton String
 * @param onClickSure Function0<Unit>?
 * @param onClickCancel Function0<Unit>?
 * @return DialogBuilder
 */
fun getErrorDialogBuilder(
    title: String = "提示",
    titleAlign: Alignment.Horizontal = Alignment.Start,
    message: String,
    messageAlign: Alignment.Horizontal = Alignment.Start,
    sureButton: String = "确定",
    cancelButton: String = "取消",
    onClickSure: (() -> Unit)? = null,
    onClickCancel: (() -> Unit)? = null,
) = DialogBuilder(
    title, titleAlign, AppColor.System.error, message, messageAlign, AppColor.Text.body, sureButton, AppColor.On.error, AppColor.System.error, cancelButton, AppColor.Text.body, onClickSure, onClickCancel
)
