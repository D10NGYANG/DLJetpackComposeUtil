package com.d10ng.basicjetpackcomposeapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.d10ng.basicjetpackcomposeapp.bean.NormalDialogBuilder
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.compose.AppText
import com.d10ng.basicjetpackcomposeapp.compose.Shapes

/**
 * 显示加载中弹窗
 * @param isShow Boolean
 * @param onDismiss Function0<Unit>
 */
@Composable
fun LoadingDialog (
    isShow: Boolean,
    background: Color = Color(0x97454545),
    onDismiss:() -> Unit = {}
) {
    if (isShow) {
        Dialog(
            onDismissRequest = { onDismiss.invoke() },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Box(
                contentAlignment= Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .background(background, shape = Shapes.medium)
            ) {
                CircularProgressIndicator(color = AppColor.System.secondary)
            }
        }
    }
}

/**
 * 普通弹窗
 * @param isShow Boolean
 * @param builder NormalDialogBuilder
 * @param onDismiss Function0<Unit>
 */
@Composable
fun NormalDialog(
    isShow: Boolean,
    builder: NormalDialogBuilder?,
    onDismiss:() -> Unit = {}
) {
    if (isShow && builder != null) {
        AlertDialog(
            onDismissRequest = { onDismiss.invoke() },
            title = {
                Text(text = builder.title, style = AppText.Bold.Title.v18)
            },
            text = {
                Text(text = builder.message, style = AppText.Medium.Body.v14)
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    if (builder.cancelButton != null) {
                        HollowButtonWithText(
                            text = builder.cancelButton!!,
                            onClick = { builder.onClickButton?.invoke(0) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(start = 8.dp, end = 8.dp, bottom = 24.dp)
                        )
                    }
                    if (builder.sureButton != null) {
                        SolidButtonWithText(
                            text = builder.sureButton!!,
                            onClick = { builder.onClickButton?.invoke(1) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(start = 8.dp, end = 8.dp, bottom = 24.dp)
                        )
                    }
                }
            },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        )
    }
}