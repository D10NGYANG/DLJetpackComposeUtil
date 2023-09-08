package com.d10ng.compose.ui.sheet.builder

import androidx.compose.runtime.Composable
import com.d10ng.compose.model.UiViewModelManager

/**
 * 底部弹窗构建器
 * @Author d10ng
 * @Date 2023/9/8 18:01
 */
abstract class SheetBuilder(
    // 是否允许点击外部隐藏弹窗
    var clickOutsideDismiss: Boolean = false,
) {

    companion object {
        /**
         * 隐藏弹窗
         */
        fun dismiss() {
            UiViewModelManager.hideSheet()
        }
    }

    /**
     * 构建弹窗内容
     */
    @Composable
    abstract fun Build()
}