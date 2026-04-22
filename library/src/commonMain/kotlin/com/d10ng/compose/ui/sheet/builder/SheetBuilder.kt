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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * 底部弹窗构建器
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
        private val scope = CoroutineScope(Dispatchers.Default)
    }

    val visibleFlow = MutableStateFlow(false)

    /**
     * 隐藏弹窗
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
     */
    @Composable
    abstract fun Build()
}

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
