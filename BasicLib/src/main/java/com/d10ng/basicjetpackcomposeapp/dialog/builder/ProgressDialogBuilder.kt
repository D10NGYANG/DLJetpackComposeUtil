package com.d10ng.basicjetpackcomposeapp.dialog.builder

import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.compose.AppText

data class ProgressDialogBuilder(
    var title: String = "进度",
    var message: String = "",
    var progress: Long = 0,
    var max: Long = 100,
    var isShowProgressText: Boolean = true,
    var progressTextStyle: ProgressTextStyle = ProgressTextStyle.PERCENTAGE
): DialogBuilder() {
    enum class ProgressTextStyle {
        PROGRESS_AND_MAX,
        PERCENTAGE
    }

    @Composable
    override fun Build() {
        BaseDialogBuilder(
            title = title,
            titleAlign = Alignment.CenterHorizontally,
            message = message,
            messageAlign = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            if (isShowProgressText) {
                Text(
                    text = when(progressTextStyle) {
                        ProgressTextStyle.PROGRESS_AND_MAX -> {
                            "${progress}/${max}"
                        }
                        ProgressTextStyle.PERCENTAGE -> {
                            "${((progress * 100.0) / max).toInt()}%"
                        }
                    },
                    style = AppText.Normal.Body.v16,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            LinearProgressIndicator(
                progress = (progress * 1.0f) / max,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 16.dp),
                color = AppColor.System.secondary,
                backgroundColor = AppColor.System.divider
            )
        }.Build()
    }
}
