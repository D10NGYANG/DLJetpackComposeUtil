package com.d10ng.compose.ui.sheet.builder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.AppText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * 底部弹窗构建器基类
 *
 * 所有底部弹窗的抽象基类，提供弹窗显隐控制和基础配置。
 * 子类需实现 [Build] 方法以定义弹窗的具体 UI 内容。
 * 通过 [UiViewModelManager.showSheet] 展示弹窗，调用 [dismiss] 关闭弹窗。
 *
 * @param clickOutsideDismiss 是否允许点击弹窗外部区域关闭弹窗，默认 true
 * @param topMargin 弹窗距离屏幕顶部的间距，默认为状态栏高度 + 56dp
 * @Author d10ng
 * @Date 2023/9/8 18:01
 */
abstract class SheetBuilder(
    // 是否允许点击外部隐藏弹窗
    var clickOutsideDismiss: Boolean = true,
    // 屏幕顶部距离
    var topMargin: @Composable () -> Dp = { WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 56.dp },
) {
    companion object {
        private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    val visibleFlow = MutableStateFlow(false)

    /**
     * 隐藏弹窗
     *
     * 先将 [visibleFlow] 设为 false 触发退出动画，延迟 300ms 后从 [UiViewModelManager] 中移除。
     */
    fun dismiss() {
        visibleFlow.value = false
        scope.launch {
            delay(300)
            UiViewModelManager.hideSheet(this@SheetBuilder)
        }
    }

    /**
     * 构建弹窗内容
     *
     * 子类实现此方法以定义弹窗的具体 UI 布局和交互逻辑。
     * 通常应使用 [com.d10ng.compose.ui.sheet.SheetColumn] 或
     * [com.d10ng.compose.ui.sheet.SheetBox] 作为最外层容器。
     */
    @Composable
    abstract fun Build()
}

/**
 * 弹窗标题栏
 *
 * 通用的弹窗顶部标题栏组件，包含左侧取消按钮、居中标题和右侧确定按钮。
 * 按钮点击回调为挂起函数，返回 true 时自动调用 [SheetBuilder.dismiss] 关闭弹窗。
 *
 * @param title String 标题文字，默认 "请选择"
 * @param cancelText String 取消按钮文字，默认 "取消"
 * @param confirmText String 确定按钮文字，默认 "确定"
 * @param onCancelClick 取消按钮点击回调，在协程中执行，返回 true 则自动关闭弹窗，默认直接关闭
 * @param onConfirmClick 确定按钮点击回调，在协程中执行，返回 true 则自动关闭弹窗，默认直接关闭
 */
@Composable
fun SheetBuilder.TitleBar(
    title: String = "请选择",
    cancelText: String = "取消",
    confirmText: String = "确定",
    onCancelClick: suspend CoroutineScope.() -> Boolean = { true },
    onConfirmClick: suspend CoroutineScope.() -> Boolean = { true },
) {
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .height(56.dp)
    ) {
        // 取消按钮
        TextButton(
            onClick = {
                scope.launch {
                    if (onCancelClick()) dismiss()
                }
            },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Text(text = cancelText, style = AppText.Normal.Tips.default)
        }
        // 标题
        Text(
            text = title,
            style = AppText.Bold.Title.large,
            modifier = Modifier.align(Alignment.Center)
        )
        // 确定按钮
        TextButton(
            onClick = {
                scope.launch {
                    if (onConfirmClick()) dismiss()
                }
            },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Text(text = confirmText, style = AppText.Normal.Title.default)
        }
    }
}

@Preview
@Composable
private fun PreviewTitleBar() {
    // SheetBuilder 是抽象类，创建匿名实现用于预览
    val builder = object : SheetBuilder() {
        @Composable
        override fun Build() {}
    }
    Box(modifier = Modifier.background(Color.White)) {
        builder.TitleBar()
    }
}
