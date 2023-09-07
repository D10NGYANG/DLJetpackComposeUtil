package com.d10ng.compose.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.dialog.builder.DialogBuilder
import com.d10ng.compose.ui.feedback.Overlay

/**
 * 弹窗
 * @Author d10ng
 * @Date 2023/9/7 11:33
 */

@Composable
fun Dialog(
    builder: DialogBuilder,
    id: Int
) {
    Overlay(
        onDismiss = {
            if (builder.clickOutsideDismiss) builder.dismiss(id)
        },
        contentAlignment = builder.contentAlignment
    ) {
        builder.Build(id)
    }
}

@Composable
fun DialogBox(
    color: Color = Color.White,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    margin: Dp = 22.dp,
    padding: Dp = 25.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(margin)
            .background(color, shape)
            .padding(padding)
    ) {
        content()
    }
}