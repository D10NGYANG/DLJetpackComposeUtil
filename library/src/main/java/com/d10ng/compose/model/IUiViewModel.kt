package com.d10ng.compose.model

import com.d10ng.compose.ui.base.ToastPosition
import com.d10ng.compose.ui.feedback.NotifyType
import com.d10ng.compose.ui.sheet.builder.SheetBuilder

/**
 *
 * @Author d10ng
 * @Date 2023/9/4 15:19
 */
interface IUiViewModel {

    /**
     * 显示toast
     * @param msg String 消息
     * @param duration Long 显示时长, 默认1500毫秒
     * @param position ToastPosition 显示位置, 默认居中
     */
    fun showToast(
        msg: String,
        duration: Long = 1500,
        position: ToastPosition = ToastPosition.Center
    )

    /**
     * 显示成功的toast
     * @param msg String 消息
     * @param duration Long 显示时长, 默认1500毫秒
     */
    fun showSuccessToast(msg: String, duration: Long = 1500)

    /**
     * 显示失败的toast
     * @param msg String 消息
     * @param duration Long 显示时长, 默认1500毫秒
     */
    fun showFailToast(msg: String, duration: Long = 1500)

    /**
     * 显示loading
     * @param text String
     */
    fun showLoading(text: String = "")

    /**
     * 隐藏loading
     */
    fun hideLoading()

    /**
     * 显示提示
     * @param type NotifyType
     * @param text String
     * @param duration Long
     */
    fun showNotify(type: NotifyType, text: String, duration: Long = 1500)

    /**
     * 显示错误提示
     * @param text String
     * @param duration Long
     */
    fun showErrorNotify(text: String, duration: Long = 1500)

    /**
     * 显示底部弹窗
     * @param builder SheetBuilder
     */
    fun showSheet(builder: SheetBuilder)

    /**
     * 隐藏底部弹窗
     */
    fun hideSheet()
}