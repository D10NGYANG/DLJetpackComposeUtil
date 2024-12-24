package com.d10ng.compose.ui.sheet.builder

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
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
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .height(56.dp)
    ) {
        val (cancelButton, titleText, confirmButton) = createRefs()
        // 取消按钮
        TextButton(
            onClick = {
                scope.launch {
                    if (onCancelClick()) dismiss()
                }
            },
            modifier = Modifier
                .constrainAs(cancelButton) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            Text(text = cancelText, style = AppText.Normal.Tips.default)
        }
        // 标题
        Text(
            text = title,
            style = AppText.Bold.Title.large,
            modifier = Modifier
                .constrainAs(titleText) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )
        // 确定按钮
        TextButton(
            onClick = {
                scope.launch {
                    if (onConfirmClick()) dismiss()
                }
            },
            modifier = Modifier
                .constrainAs(confirmButton) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            Text(text = confirmText, style = AppText.Normal.Title.default)
        }
    }
}