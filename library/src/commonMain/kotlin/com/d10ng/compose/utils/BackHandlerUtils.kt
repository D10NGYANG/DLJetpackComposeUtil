package com.d10ng.compose.utils

import androidx.compose.runtime.Composable
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState

/**
 * 返回键处理
 * @Author d10ng
 * @Date 2024/9/12 11:26
 */

@Composable
@Deprecated(
    message = "已废弃"
)
fun BackHandler(enabled: Boolean = true, onBack: () -> Unit) {
    val state = rememberNavigationEventState(NavigationEventInfo.None)
    NavigationBackHandler(state, enabled, onBackCompleted = onBack)
}