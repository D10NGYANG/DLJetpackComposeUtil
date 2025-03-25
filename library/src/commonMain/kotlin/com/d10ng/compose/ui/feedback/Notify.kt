package com.d10ng.compose.ui.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.defaultPaddingSize

/**
 * 消息提示
 * @Author d10ng
 * @Date 2023/9/12 18:11
 */

enum class NotifyType(val color: Color) {
    Primary(AppColor.Main.primary),
    Success(AppColor.Func.success),
    Warning(AppColor.Func.assist),
    Error(AppColor.Func.error),
}

/**
 * 消息提示
 * @param type NotifyType 消息类型
 * @param text String 消息内容
 */
@Composable
fun Notify(
    type: NotifyType = NotifyType.Primary,
    text: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = defaultPaddingSize, vertical = 8.dp)
            .statusBarsPadding()
            .background(type.color, AppShape.RC.v8)
            .padding(horizontal = defaultPaddingSize, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = AppText.Normal.Surface.default,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}