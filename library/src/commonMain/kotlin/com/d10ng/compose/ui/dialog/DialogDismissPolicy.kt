package com.d10ng.compose.ui.dialog

/** 官方 Dialog 与旧 Builder 关闭契约之间的映射。 */
internal data class DialogDismissPolicy(
    val dismissOnBackPress: Boolean,
    val dismissOnClickOutside: Boolean,
)

internal fun resolveDialogDismissPolicy(
    clickOutsideDismiss: Boolean,
): DialogDismissPolicy = DialogDismissPolicy(
    // 旧 Overlay 对遮罩点击和返回键使用同一开关，迁移时保持这个可观察行为。
    dismissOnBackPress = clickOutsideDismiss,
    dismissOnClickOutside = clickOutsideDismiss,
)
