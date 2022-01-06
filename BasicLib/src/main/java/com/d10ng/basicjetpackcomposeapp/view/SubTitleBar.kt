package com.d10ng.basicjetpackcomposeapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.d10ng.basicjetpackcomposeapp.compose.AppText

@Composable
fun SubTitleBar(
    text: String,
    background: Color = Color(0xFFEBEEF0)
) {
    Text(
        text = text,
        style = AppText.Normal.Hint.v14,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(background)
            .padding(16.dp, 8.dp)
    )
}