package com.d10ng.compose.ui.show

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
 * 头像
 * @param char Char
 * @param size Dp
 * @param backgroundColor Color
 * @param contentColor Color
 * @param shape RoundedCornerShape
 * @param fontSize TextUnit
 * @param content [@androidx.compose.runtime.Composable] Function0<Unit>
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