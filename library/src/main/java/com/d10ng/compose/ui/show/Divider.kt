package com.d10ng.compose.ui.show

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor

/**
 * Divider
 * @Author d10ng
 * @Date 2023/9/5 11:51
 */

/**
 * 横向分割线
 * @param color Color 分割线颜色
 * @param thickness Dp 分割线高度
 * @param paddingStart Dp 左边距
 * @param paddingEnd Dp 右边距
 */
@Composable
fun HorizontalDivider(
    color: Color = AppColor.Neutral.line,
    thickness: Dp = 0.5.dp,
    paddingStart: Dp = 0.dp,
    paddingEnd: Dp = 0.dp,
) {
    Divider(
        color = color,
        thickness = thickness,
        modifier = Modifier.padding(start = paddingStart, end = paddingEnd)
    )
}

/**
 * 纵向分割线
 * @param color Color 分割线颜色
 * @param thickness Dp 分割线高度
 * @param paddingTop Dp 上边距
 * @param paddingBottom Dp 下边距
 */
@Composable
fun VerticalDivider(
    color: Color = AppColor.Neutral.line,
    thickness: Dp = 0.5.dp,
    paddingTop: Dp = 0.dp,
    paddingBottom: Dp = 0.dp,
) {
    Box(modifier = Modifier
        .fillMaxHeight()
        .width(thickness)
        .padding(top = paddingTop, bottom = paddingBottom)
        .background(color))
}