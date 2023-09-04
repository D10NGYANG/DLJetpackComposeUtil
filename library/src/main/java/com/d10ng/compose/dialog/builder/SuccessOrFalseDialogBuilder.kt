package com.d10ng.compose.dialog.builder

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.d10ng.compose.R
import com.d10ng.compose.dialog.DialogColumn
import com.d10ng.compose.dialog.DialogMessage
import com.d10ng.compose.dialog.DialogTitle
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.view.DialogSureButton

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
                tint = if (isSuccess) AppColor.Main.primary else AppColor.Func.error
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
                color = if (isSuccess) Color.White else Color.White,
                containerColor = if (isSuccess) AppColor.Main.primary else AppColor.Func.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
} 
