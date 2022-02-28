package com.d10ng.basicjetpackcomposeapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.compose.AppText

@Composable
fun BadgeText(
    modifier: Modifier = Modifier,
    badgeNum: Int,
    textStyle: TextStyle = AppText.Bold.OnError.v14,
    backgroundColor: Color = AppColor.System.error,
    defaultMinSize: Dp = 28.dp,
    padding: Dp = 3.dp,
) {
    val text = remember(badgeNum) {
        if (badgeNum > 99) {
            "99+"
        } else {
            "$badgeNum"
        }
    }
    Text(
        text = text,
        style = textStyle,
        textAlign = TextAlign.Center,
        modifier = modifier
            .defaultMinSize(defaultMinSize, defaultMinSize)
            .background(backgroundColor, RoundedCornerShape(100))
            .padding(padding)
    )
}