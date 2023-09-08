package com.d10ng.compose.ui.dialog.builder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.base.Button
import com.d10ng.compose.ui.base.ButtonType
import com.d10ng.compose.ui.dialog.DialogBox
import com.d10ng.compose.ui.dialog.DialogColumn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * 确认弹窗构建器
 * @Author d10ng
 * @Date 2023/9/7 17:19
 */
class ConfirmDialogBuilder(
    // 标题
    private val title: String = "提示",
    // 内容
    private val content: String,
    // 内容插槽
    private val contentSlot: @Composable () -> Unit = {},
    // 确定按钮文字
    private val confirmText: String = "确定",
    // 取消按钮文字
    private val cancelText: String = "取消",
    // 弹窗类型
    private val type: Type = Type.Default,
    // 确定按钮点击事件，返回true则隐藏弹窗
    private val onConfirmClick: suspend CoroutineScope.() -> Boolean = { true },
    // 取消按钮点击事件，返回true则隐藏弹窗
    private val onCancelClick: suspend CoroutineScope.() -> Boolean = { true }
): DialogBuilder() {

    enum class Type(val titleStyle: TextStyle, val contentStyle: TextStyle, val confirmButtonType: ButtonType, val cancelButtonType: ButtonType) {
        Default(AppText.Bold.Title.large, AppText.Normal.Body.default, ButtonType.PRIMARY, ButtonType.DEFAULT),
        Success(AppText.Bold.Success.large, AppText.Normal.Body.default, ButtonType.SUCCESS, ButtonType.DEFAULT),
        Warning(AppText.Bold.Assist.large, AppText.Normal.Body.default, ButtonType.WARNING, ButtonType.DEFAULT),
        Danger(AppText.Bold.Error.large, AppText.Normal.Body.default, ButtonType.DANGER, ButtonType.DEFAULT),
    }

    @Composable
    override fun Build(id: Int) {
        DialogColumn {
            // 标题
            if (title.isNotEmpty()) {
                TipsDialogBuilder.TitleText(text = title, style = type.titleStyle)
            }
            // 内容
            contentSlot()
            if (content.isNotEmpty()) {
                TipsDialogBuilder.ContentText(text = content, style = type.contentStyle)
            }
            // 按钮组
            ButtonRow(
                id = id,
                cancelText = cancelText,
                confirmText = confirmText,
                cancelButtonType = type.cancelButtonType,
                confirmButtonType = type.confirmButtonType,
                onCancelClick = onCancelClick,
                onConfirmClick = onConfirmClick
            )
        }
    }

    companion object {
        @Composable
        fun ButtonRow(
            id: Int,
            cancelText: String = "取消",
            confirmText: String = "确定",
            cancelButtonType: ButtonType = ButtonType.DEFAULT,
            confirmButtonType: ButtonType = ButtonType.PRIMARY,
            onCancelClick: suspend CoroutineScope.() -> Boolean = { true },
            onConfirmClick: suspend CoroutineScope.() -> Boolean = { true }
        ) {
            val scope = rememberCoroutineScope()
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    text = cancelText,
                    modifier = Modifier
                        .weight(1f),
                    shape = AppShape.RC.Cycle,
                    type = cancelButtonType
                ) {
                    scope.launch {
                        if (onCancelClick(this)) dismiss(id)
                    }
                }
                Box(modifier = Modifier.width(32.dp))
                Button(
                    text = confirmText,
                    modifier = Modifier
                        .weight(1f),
                    shape = AppShape.RC.Cycle,
                    type = confirmButtonType
                ) {
                    scope.launch {
                        if (onConfirmClick(this)) dismiss(id)
                    }
                }
            }
        }
    }
}