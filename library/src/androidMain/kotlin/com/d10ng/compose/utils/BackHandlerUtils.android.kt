package com.d10ng.compose.utils

import androidx.compose.runtime.Composable

/**
 * 返回键处理
 * @Author d10ng
 * @Date 2024/9/12 11:26
 */
@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    androidx.activity.compose.BackHandler(enabled = enabled, onBack = onBack)
}