package com.d10ng.compose.model

import com.d10ng.compose.ui.base.ToastPosition

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
}