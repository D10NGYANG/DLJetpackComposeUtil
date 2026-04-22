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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Divider
 * @Author d10ng
 * @Date 2023/9/5 11:51
 */

/**
 * 横向分割线
 *
 * 横向铺满父容器宽度的分割线，支持自定义颜色、线条厚度及左右内边距。
 *
 * @param modifier Modifier 修饰符，默认为 [Modifier]
 * @param color Color 分割线颜色，默认为 `MaterialTheme.colorScheme.outlineVariant`
 * @param thickness Dp 分割线厚度（高度），默认为 0.5.dp
 * @param paddingStart Dp 分割线左侧内边距，默认为 0.dp
 * @param paddingEnd Dp 分割线右侧内边距，默认为 0.dp
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
 *
 * 纵向铺满父容器高度的分割线，支持自定义颜色、线条厚度及上下内边距。
 *
 * @param modifier Modifier 修饰符，默认为 [Modifier]
 * @param color Color 分割线颜色，默认为 `MaterialTheme.colorScheme.outlineVariant`
 * @param thickness Dp 分割线厚度（宽度），默认为 0.5.dp
 * @param paddingTop Dp 分割线顶部内边距，默认为 0.dp
 * @param paddingBottom Dp 分割线底部内边距，默认为 0.dp
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

@Preview(showBackground = true)
@Composable
fun PreviewHorizontalDivider() {
    androidx.compose.foundation.layout.Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
    ) {
        // 默认分割线
        HorizontalDivider()
        // 自定义颜色
        HorizontalDivider(color = Color.Red)
        // 自定义厚度
        HorizontalDivider(thickness = 2.dp)
        // 带左右内边距
        HorizontalDivider(paddingStart = 16.dp, paddingEnd = 16.dp)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewVerticalDivider() {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .padding(16.dp)
            .height(60.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
    ) {
        // 默认分割线
        VerticalDivider()
        // 自定义颜色
        VerticalDivider(color = Color.Red)
        // 自定义厚度
        VerticalDivider(thickness = 2.dp)
        // 带上下内边距
        VerticalDivider(paddingTop = 8.dp, paddingBottom = 8.dp)
    }
}
