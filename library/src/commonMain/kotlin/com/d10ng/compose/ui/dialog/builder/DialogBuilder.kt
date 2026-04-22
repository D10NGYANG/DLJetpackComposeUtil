package com.d10ng.compose.ui.dialog.builder

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.dialog.builder.DialogBuilder.Companion.dismiss

/**
 * 弹窗构建器基类
 * 所有弹窗 Builder 均继承此类，由 [com.d10ng.compose.ui.dialog.Dialog] 负责渲染
 * @Author d10ng
 * @Date 2023/9/7 11:53
 */
abstract class DialogBuilder(
    /** 是否允许点击遮罩区域关闭弹窗，默认 false */
    var clickOutsideDismiss: Boolean = false,
    /** 弹窗内容在遮罩层内的对齐位置，默认居中 [Alignment.Center] */
    var contentAlignment: Alignment = Alignment.Center
) {

    companion object {
        /**
         * 关闭指定弹窗
         * @param id String 弹窗的唯一标识，由 [com.d10ng.compose.model.UiViewModelManager.showDialog] 返回
         */
        fun dismiss(id: String) {
            UiViewModelManager.hideDialog(id)
        }
    }

    /**
     * 构建弹窗内容
     * 子类在此函数中声明弹窗的 UI 结构
     * @param id String 弹窗唯一标识，关闭弹窗时需透传给 [dismiss]
     */
    @Composable
    abstract fun Build(id: String)
}