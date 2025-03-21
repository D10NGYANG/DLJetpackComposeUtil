package com.d10ng.compose.ui.dialog.builder

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.d10ng.compose.model.UiViewModelManager

/**
 * 弹窗构建器
 * @Author d10ng
 * @Date 2023/9/7 11:53
 */
abstract class DialogBuilder(
    // 是否允许点击外部隐藏弹窗
    var clickOutsideDismiss: Boolean = false,
    // 弹窗内容所在位置
    var contentAlignment: Alignment = Alignment.Center
) {

    companion object {
        /**
         * 隐藏弹窗
         * @param id String 弹窗唯一标识
         */
        fun dismiss(id: String) {
            UiViewModelManager.hideDialog(id)
        }
    }

    /**
     * 构建弹窗内容
     * @param id String 弹窗唯一标识
     */
    @Composable
    abstract fun Build(id: String)
}