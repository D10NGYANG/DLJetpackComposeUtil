package com.d10ng.compose.ui.show

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText

/**
 * 徽标容器
 *
 * 将 [content] 内容与 [badge] 徽标叠加显示，徽标固定对齐到内容区域的右上角。
 * 常用于图标、头像等元素上方叠加消息数量、小红点等提示信息。
 *
 * @param badge (@Composable () -> Unit) 徽标内容插槽，显示在右上角，通常传入 [Badge] 组件
 * @param content (@Composable () -> Unit) 主体内容插槽，徽标将叠加在其右上角
 */
@Composable
fun BadgeBox(
    badge: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
    ) {
        Box(modifier = Modifier.padding(4.dp)) {
            content()
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
        ) {
            badge()
        }
    }
}

/**
 * 文本徽标
 *
 * 显示带文字内容的徽标，文字超出最大宽度时裁剪。
 * 当 [content] 为空字符串时退化为小圆点样式（直径 12.dp）；
 * 有文字内容时显示胶囊形背景（最小尺寸 18×18.dp），文字超长时自动裁剪。
 *
 * @param content String 徽标显示的文本内容；为空字符串时显示小圆点，默认为空字符串
 * @param color Color 徽标背景色，默认为 [AppColor.Func.error]（红色）
 */
@Composable
fun Badge(
    content: String = "",
    color: Color = AppColor.Func.error
) {
    if (content.isEmpty()) {
        Box(
            modifier = Modifier
                .defaultMinSize(minWidth = 12.dp, minHeight = 12.dp)
                .background(color, AppShape.RC.Cycle)
        )
    } else {
        Box(
            modifier = Modifier
                .defaultMinSize(minWidth = 18.dp, minHeight = 18.dp)
                .background(color, AppShape.RC.v10)
                .padding(horizontal = 4.dp, vertical = 1.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = content,
                style = AppText.Bold.Surface.mini,
                modifier = Modifier.widthIn(max = 32.dp),
                maxLines = 1,
                overflow = TextOverflow.Clip
            )
        }
    }
}

/**
 * 数字徽标
 *
 * 显示数字类型的徽标，内部将数字转换为文本后委托给 [Badge] 文本徽标渲染。
 * 当 [num] 为 0 或负数时不渲染任何内容；超过 [max] 时显示为 "[max]+"。
 *
 * @param num Int 需要显示的数字；小于等于 0 时不显示徽标
 * @param max Int 显示的最大数值，超过此值时显示为 "[max]+"，默认为 99
 * @param color Color 徽标背景色，默认为 [AppColor.Func.error]（红色）
 */
@Composable
fun Badge(
    num: Int,
    max: Int = 99,
    color: Color = AppColor.Func.error
) {
    if (num > 0) {
        val content = remember(num, max) {
            if (num > max) "$max+" else num.toString()
        }
        Badge(content, color)
    }
}

@Preview
@Composable
fun PreviewBadge() {
    androidx.compose.foundation.layout.Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
    ) {
        androidx.compose.foundation.layout.Row(
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 小圆点（content 为空）
            Badge()
            // 文本徽标
            Badge(content = "新")
            // 数字徽标
            Badge(content = "8")
            // 数字徽标（超长裁剪）
            Badge(content = "999")
        }
        androidx.compose.foundation.layout.Row(
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 数字为 0，不显示
            Badge(num = 0)
            // 正常数字
            Badge(num = 5)
            // 超出最大值显示 99+
            Badge(num = 120)
            // 自定义最大值
            Badge(num = 120, max = 999)
        }
    }
}

@Preview
@Composable
fun PreviewBadgeBox() {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 小圆点徽标
        BadgeBox(badge = { Badge() }) {
            Box(
                modifier = Modifier
                    .defaultMinSize(minWidth = 40.dp, minHeight = 40.dp)
                    .background(Color.LightGray, AppShape.RC.v8)
            )
        }
        // 数字徽标
        BadgeBox(badge = { Badge(num = 9) }) {
            Box(
                modifier = Modifier
                    .defaultMinSize(minWidth = 40.dp, minHeight = 40.dp)
                    .background(Color.LightGray, AppShape.RC.v8)
            )
        }
        // 超出最大值
        BadgeBox(badge = { Badge(num = 100) }) {
            Box(
                modifier = Modifier
                    .defaultMinSize(minWidth = 40.dp, minHeight = 40.dp)
                    .background(Color.LightGray, AppShape.RC.v8)
            )
        }
    }
}
