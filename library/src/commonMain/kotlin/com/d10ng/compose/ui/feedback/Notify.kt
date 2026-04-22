package com.d10ng.compose.ui.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
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

/**
 * 消息提示类型
 * 控制通知条的背景颜色，通常在页面顶部展示
 */
enum class NotifyType(val color: Color) {
    // 主要通知（品牌主色蓝色）
    Primary(AppColor.Main.primary),
    // 成功通知（绿色）
    Success(AppColor.Func.success),
    // 警告通知（橙色）
    Warning(AppColor.Func.assist),
    // 错误通知（红色）
    Error(AppColor.Func.error),
}

/**
 * 消息通知条
 * 在页面顶部状态栏下方展示一条带圆角背景的通知文字，
 * 自动适配状态栏高度，文字最多显示 3 行，超出部分以省略号截断
 * @param type NotifyType 通知类型，控制背景颜色，默认 [NotifyType.Primary]
 * @param text String 通知内容文字
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

@Preview
@Composable
fun PreviewNotify() {
    Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFF5F5F5))) {
        Notify(type = NotifyType.Primary, text = "这是一条主要消息提示")
        Notify(type = NotifyType.Success, text = "操作成功，这是成功消息提示")
        Notify(type = NotifyType.Warning, text = "注意，这是警告消息提示")
        Notify(type = NotifyType.Error, text = "出错了，这是错误消息提示，内容可能会比较长，最多显示三行，超出部分将被截断显示省略号")
    }
}
