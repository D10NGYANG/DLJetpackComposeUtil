package com.d10ng.compose.ui.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppShape

/**
 * 轻提示组件
 * @Author d10ng
 * @Date 2023/9/4 10:07
 */

enum class ToastPosition(val contentAlignment: Alignment) {
    Top(Alignment.TopCenter),
    Center(Alignment.Center),
    Bottom(Alignment.BottomCenter)
}

@Composable
fun Toast(
    text: String,
    position: ToastPosition = ToastPosition.Center
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(0.7f),
            contentAlignment = position.contentAlignment
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.7f), shape = AppShape.RC.v8)
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Text(text = text, color = Color.White)
            }
        }
    }
}

@Composable
fun LoadingToast(
    text: String? = null,
) {

}