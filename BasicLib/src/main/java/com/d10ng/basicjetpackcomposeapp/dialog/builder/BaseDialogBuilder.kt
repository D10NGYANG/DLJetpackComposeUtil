package com.d10ng.basicjetpackcomposeapp.dialog.builder

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.dialog.DialogColumn
import com.d10ng.basicjetpackcomposeapp.dialog.DialogMessage
import com.d10ng.basicjetpackcomposeapp.dialog.DialogTitle
import com.d10ng.basicjetpackcomposeapp.view.DialogCancelButton
import com.d10ng.basicjetpackcomposeapp.view.DialogSureButton

data class BaseDialogBuilder(
    var title: String = "提示",
    var titleAlign: Alignment.Horizontal = Alignment.Start,
    var titleColor: Color = AppColor.Text.title,
    var message: String = "",
    var messageAlign: Alignment.Horizontal = Alignment.Start,
    var messageColor: Color = AppColor.Text.body,
    var sureButton: String = "确定",
    var sureButtonTextColor: Color = AppColor.On.secondary,
    var sureButtonBackgroundColor: Color = AppColor.System.secondary,
    var cancelButton: String = "取消",
    var cancelButtonTextColor: Color = AppColor.Text.body,
    var onClickSure: (() -> Unit)? = null,
    var onClickCancel: (() -> Unit)? = null,
    var columnContent: @Composable ColumnScope.() -> Unit = {}
): DialogBuilder() {

    @Composable
    override fun Build() {
        DialogColumn {
            DialogTitle(
                text = title,
                color = titleColor,
                modifier = Modifier
                    .align(titleAlign)
            )
            if (message.isNotEmpty()) {
                DialogMessage(
                    text = message,
                    color = messageColor,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(messageAlign)
                )
            }
            columnContent()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                onClickCancel?.let {
                    DialogCancelButton(
                        modifier = Modifier.weight(1f),
                        text = cancelButton,
                        color = cancelButtonTextColor,
                        onClick = it
                    )
                }
                if (onClickCancel != null && onClickSure != null) {
                    Spacer(modifier = Modifier.width(16.dp))
                }
                onClickSure?.let {
                    DialogSureButton(
                        modifier = Modifier.weight(1f),
                        text = sureButton,
                        color = sureButtonTextColor,
                        backgroundColor = sureButtonBackgroundColor,
                        onClick = it
                    )
                }
            }
        }
    }
}

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
 * @return BaseDialogBuilder
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
) = BaseDialogBuilder(
    title, titleAlign, AppColor.System.error, message, messageAlign, AppColor.Text.body, sureButton, AppColor.On.error, AppColor.System.error, cancelButton, AppColor.Text.body, onClickSure, onClickCancel
)