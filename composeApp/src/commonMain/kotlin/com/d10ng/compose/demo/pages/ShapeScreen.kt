package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.d10ng.compose.demo.Nav
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.navigation.NavBar

/**
 * 容器形状
 * @Author d10ng
 * @Date 2025/3/24 11:59
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ShapeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "Shape", onClickBack = { Nav.instant().popBackStack() })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            MaterialTheme.shapes.apply {
                ShapeBox(extraSmall, "extraSmall")
                ShapeBox(small, "small")
                ShapeBox(medium, "medium")
                ShapeBox(large, "large")
                ShapeBox(extraLarge, "extraLarge")
            }
        }
    }
}

@Composable
private fun ShapeBox(
    shape: Shape,
    name: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(60.dp)
                .background(Color.Blue, shape)
                .border(1.dp, Color.Gray)
        )
        Text(
            text = name,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )
    }
}