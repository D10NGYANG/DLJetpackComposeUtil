package com.d10ng.compose.ui.show

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Divider
 * @Author d10ng
 * @Date 2023/9/5 11:51
 */

/**
 * 横向分割线
 * @param modifier Modifier 修饰符
 * @param color Color 分割线颜色
 * @param thickness Dp 分割线高度
 * @param paddingStart Dp 左边距
 * @param paddingEnd Dp 右边距
 */
@Composable
fun HorizontalDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.outlineVariant,
    thickness: Dp = 0.5.dp,
    paddingStart: Dp = 0.dp,
    paddingEnd: Dp = 0.dp,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(thickness)
            .padding(start = paddingStart, end = paddingEnd)
            .background(color)
    )
}

/**
 * 纵向分割线
 * @param modifier Modifier 修饰符
 * @param color Color 分割线颜色
 * @param thickness Dp 分割线高度
 * @param paddingTop Dp 上边距
 * @param paddingBottom Dp 下边距
 */
@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.outlineVariant,
    thickness: Dp = 0.5.dp,
    paddingTop: Dp = 0.dp,
    paddingBottom: Dp = 0.dp,
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(thickness)
            .padding(top = paddingTop, bottom = paddingBottom)
            .background(color)
    )
}