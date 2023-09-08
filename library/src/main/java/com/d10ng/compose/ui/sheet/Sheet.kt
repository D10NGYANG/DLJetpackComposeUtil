package com.d10ng.compose.ui.sheet

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.d10ng.compose.ui.feedback.Overlay
import com.d10ng.compose.ui.sheet.builder.SheetBuilder

/**
 * 底部弹窗
 * @Author d10ng
 * @Date 2023/9/8 18:00
 */

@Composable
fun Sheet(
    builder: SheetBuilder
) {
    Overlay(
        onDismiss = {
            if (builder.clickOutsideDismiss) SheetBuilder.dismiss()
        },
        contentAlignment = Alignment.BottomCenter
    ) {
        builder.Build()
    }
}