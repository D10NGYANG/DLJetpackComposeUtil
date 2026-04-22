package com.d10ng.compose.ui.show

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText

/**
 * 头像
 * @Author d10ng
 * @Date 2023/9/12 15:22
 */

/**
 * 字符头像
 *
 * 以单个字符作为头像内容，自动将字符转为大写并居中显示在带背景色的圆形（或自定义形状）容器内。
 * 支持通过 [content] 插槽在字符文本下层叠加自定义内容（如图片、图标等）。
 *
 * @param char Char 用于显示的头像字符，会自动转换为大写
 * @param size Dp 头像整体尺寸（宽高相同），默认为 56.dp
 * @param backgroundColor Color 头像背景色，默认为 [Color.LightGray]
 * @param contentColor Color 字符文本颜色，默认为 [Color.White]
 * @param shape RoundedCornerShape 头像形状，默认为 [AppShape.RC.Cycle]（圆形）
 * @param fontSize TextUnit 字符文本的字体大小，默认为 26.sp
 * @param content (@Composable () -> Unit) 叠加在字符文本下层的自定义内容插槽，默认为空
 */
@Composable
fun Avatar(
    char: Char,
    size: Dp = 56.dp,
    backgroundColor: Color = Color.LightGray,
    contentColor: Color = Color.White,
    shape: RoundedCornerShape = AppShape.RC.Cycle,
    fontSize: TextUnit = 26.sp,
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .size(size)
            .background(backgroundColor, shape),
        contentAlignment = Alignment.Center
    ) {
        content()
        Text(
            text = char.toString().uppercase(),
            style = AppText.Bold.Surface.v26,
            color = contentColor,
            fontSize = fontSize
        )
    }
}

@Preview
@Composable
fun PreviewAvatar() {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 默认圆形头像
        Avatar(char = 'A')
        // 自定义背景色
        Avatar(char = 'B', backgroundColor = Color(0xFF4CAF50))
        // 自定义尺寸
        Avatar(char = 'C', size = 40.dp, fontSize = 18.sp)
        // 方形头像
        Avatar(
            char = 'D',
            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
            backgroundColor = Color(0xFF2196F3)
        )
    }
}

