package com.d10ng.compose.ui.show

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor

/**
 * Border
 * @Author d10ng
 * @Date 2023/9/5 11:51
 */

@Composable
fun Border(
    color: Color = AppColor.Neutral.border
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(0.5.dp)
            .background(color)
    )
}