package com.d10ng.compose.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText

@Preview(showBackground = true)
@Composable
private fun NameAvatar_Test() {
    Box(modifier = Modifier.fillMaxWidth()) {
        NameAvatar(name = "氧")
    }
}

@Composable
fun NameAvatar(
    name: String,
    nameStyle: TextStyle = AppText.Bold.Title.v18,
    backgroundColor: Color = Color(0xFFEBEEF0),
    backgroundShape: Shape = AppShape.RC.Cycle,
    size: Dp = 50.dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .background(backgroundColor, backgroundShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (name.isEmpty()) "无" else name.substring(0, 1).uppercase(),
            style = nameStyle
        )
    }
}