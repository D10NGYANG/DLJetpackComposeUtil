package com.d10ng.compose.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText

@Composable
fun BadgeText(
    modifier: Modifier = Modifier,
    number: Int,
    size: Dp = 20.dp,
    background: Color = AppColor.Main.primary,
    textStyle: TextStyle = AppText.Bold.White.v12
) {
    if (number == 0) return
    val text = remember(number) {
        if (number > 99) {
            "99+"
        } else {
            "$number"
        }
    }
    Box(
        modifier = modifier
            .size(size)
            .background(background, AppShape.RC.Cycle),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = textStyle)
    }
}

@Preview
@Composable
private fun BadgeText_Test() {
    BadgeText(number = 1)
    BadgeText(number = 50)
    BadgeText(number = 100)
}