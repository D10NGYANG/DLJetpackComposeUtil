package com.d10ng.basicjetpackcomposeapp.dialog.builder

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.d10ng.basicjetpackcomposeapp.R
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.dialog.DialogColumn
import com.d10ng.basicjetpackcomposeapp.dialog.DialogMessage
import com.d10ng.basicjetpackcomposeapp.dialog.DialogTitle
import com.d10ng.basicjetpackcomposeapp.view.DialogSureButton

data class SuccessOrFalseDialogBuilder(
    var title: String = "提示",
    var message: String = "",
    var isSuccess: Boolean = true,
    var buttonText: String = "确定",
    var onClickButton: () -> Unit = {}
): DialogBuilder() {
    
    @Composable
    override fun Build() {
        DialogColumn {
            DialogTitle(
                text = title,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Icon(
                painter = painterResource(id = if (isSuccess) R.drawable.ic_success_102 else R.drawable.ic_false_102),
                contentDescription = if (isSuccess) "成功" else "失败",
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally),
                tint = if (isSuccess) AppColor.System.secondary else AppColor.System.error
            )
            if (message.isNotEmpty()) {
                DialogMessage(
                    text = message,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
            DialogSureButton(
                text = buttonText,
                onClick = onClickButton,
                color = if (isSuccess) AppColor.On.secondary else AppColor.On.error,
                backgroundColor = if (isSuccess) AppColor.System.secondary else AppColor.System.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
} 
