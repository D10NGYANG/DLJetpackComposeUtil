package com.d10ng.compose.dialog.builder

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

abstract class DialogBuilder(
    // 是否允许点击外部隐藏弹窗
    var isClickOutsideDismiss: Boolean = false,
    // 弹窗内容所在位置
    var contentAlignment: Alignment = Alignment.Center
) {
    // 构建弹窗内容
    @Composable
    abstract fun Build()
}