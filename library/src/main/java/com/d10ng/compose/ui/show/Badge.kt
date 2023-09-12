package com.d10ng.compose.ui.show

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
 * 徽标
 * @Author d10ng
 * @Date 2023/9/12 13:43
 */

@Preview
@Composable
private fun BadgeTest() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val textList = listOf("", "1", "99", "999", "999+", "HOT")
        textList.forEach { item ->
            BadgeBox(badge = { Badge(item) }) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.LightGray, AppShape.RC.v8)
                )
            }
        }
    }
}

/**
 * 徽标盒子
 * @param badge [@androidx.compose.runtime.Composable] Function0<Unit>
 * @param content [@androidx.compose.runtime.Composable] Function0<Unit>
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
 * 徽标
 * @param content String 徽标内容，为空时显示小圆点
 * @param color Color 徽标颜色
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
                style = AppText.Bold.White.mini,
                modifier = Modifier.widthIn(max = 32.dp),
                maxLines = 1,
                overflow = TextOverflow.Clip
            )
        }
    }
}

/**
 * 徽标
 * @param num Int 徽标数字，为0时不显示
 * @param max Int 最大值，超过最大值显示最大值+，默认99
 * @param color Color 徽标颜色
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