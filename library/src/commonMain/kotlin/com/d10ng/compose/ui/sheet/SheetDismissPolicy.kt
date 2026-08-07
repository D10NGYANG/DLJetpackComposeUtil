package com.d10ng.compose.ui.sheet

/**
 * Sheet 在不同动画阶段的用户关闭入口。
 *
 * 进入和退出动画的边界由兼容返回处理器接管，避免官方处理器尚未启用或已经停用时，
 * 返回事件穿透到底层页面。
 */
internal data class SheetDismissPolicy(
    val dismissOnScrimClick: Boolean,
    val gesturesEnabled: Boolean,
    val officialBackHandlerEnabled: Boolean,
    val compatibilityBackHandlerEnabled: Boolean,
    val dismissOnCompatibilityBack: Boolean,
)

internal fun resolveSheetDismissPolicy(
    userDismissEnabled: Boolean,
    visible: Boolean,
    sheetStateVisible: Boolean,
): SheetDismissPolicy {
    val officialBackHandlerEnabled = userDismissEnabled && visible && sheetStateVisible
    return SheetDismissPolicy(
        dismissOnScrimClick = userDismissEnabled && visible,
        gesturesEnabled = userDismissEnabled && visible,
        officialBackHandlerEnabled = officialBackHandlerEnabled,
        compatibilityBackHandlerEnabled =
            !officialBackHandlerEnabled && (visible || sheetStateVisible),
        dismissOnCompatibilityBack =
            userDismissEnabled && visible && !officialBackHandlerEnabled,
    )
}
