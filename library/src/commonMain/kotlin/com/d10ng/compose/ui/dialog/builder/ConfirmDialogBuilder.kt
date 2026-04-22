package com.d10ng.compose.ui.dialog.builder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.base.Button
import com.d10ng.compose.ui.base.ButtonType
import com.d10ng.compose.ui.dialog.DialogColumn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * 双按钮确认弹窗构建器
 * 适用于需要用户二次确认的操作场景，提供标题、内容文本，以及取消和确认两个按钮
 * 支持通过 [contentSlot] 插入自定义内容代替或补充纯文字内容
 * @Author d10ng
 * @Date 2023/9/7 17:19
 */
class ConfirmDialogBuilder(
    /** 弹窗标题，为空时不渲染标题行，默认"提示" */
    private val title: String = "提示",
    /** 标题文字对齐方式，默认居中 */
    private val titleAlign: TextAlign = TextAlign.Center,
    /** 内容文字，为空时不渲染内容行；与 [contentSlot] 可共用，默认空字符串 */
    private val content: String = "",
    /** 内容文字对齐方式，默认居中 */
    private val contentAlign: TextAlign = TextAlign.Center,
    /** 自定义内容插槽，渲染在标题之后、文字内容之前 */
    private val contentSlot: @Composable () -> Unit = {},
    /** 确定按钮文字，默认"确定" */
    private val confirmText: String = "确定",
    /** 取消按钮文字，默认"取消" */
    private val cancelText: String = "取消",
    /** 弹窗主题类型，控制标题颜色和按钮样式，默认 [Type.Default] */
    private val type: Type = Type.Default,
    /** 确定按钮点击回调，返回 true 则自动关闭弹窗，默认直接关闭 */
    private val onConfirmClick: suspend CoroutineScope.() -> Boolean = { true },
    /** 取消按钮点击回调，返回 true 则自动关闭弹窗，默认直接关闭 */
    private val onCancelClick: suspend CoroutineScope.() -> Boolean = { true }
): DialogBuilder() {

    /**
     * 弹窗主题类型，控制标题文字样式和按钮颜色
     * @property titleStyle 标题文字样式
     * @property contentStyle 内容文字样式
     * @property confirmButtonType 确定按钮类型
     * @property cancelButtonType 取消按钮类型
     */
    enum class Type(val titleStyle: TextStyle, val contentStyle: TextStyle, val confirmButtonType: ButtonType, val cancelButtonType: ButtonType) {
        /** 默认蓝色主题 */
        Default(AppText.Bold.Title.large, AppText.Normal.Body.default, ButtonType.PRIMARY, ButtonType.DEFAULT),
        /** 成功绿色主题 */
        Success(AppText.Bold.Success.large, AppText.Normal.Body.default, ButtonType.SUCCESS, ButtonType.DEFAULT),
        /** 警告橙色主题 */
        Warning(AppText.Bold.Assist.large, AppText.Normal.Body.default, ButtonType.WARNING, ButtonType.DEFAULT),
        /** 危险红色主题，适用于删除等不可逆操作 */
        Danger(AppText.Bold.Error.large, AppText.Normal.Body.default, ButtonType.DANGER, ButtonType.DEFAULT),
    }

    @Composable
    override fun Build(id: String) {
        DialogColumn {
            // 标题
            if (title.isNotEmpty()) {
                TipsDialogBuilder.TitleText(title, type.titleStyle, titleAlign)
            }
            // 内容
            contentSlot()
            if (content.isNotEmpty()) {
                TipsDialogBuilder.ContentText(content, type.contentStyle, contentAlign)
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
        /**
         * 双按钮行组件（取消 + 确定），供各 Builder 复用
         * 两个按钮等宽排列，中间间隔 32dp
         * @param id String 所在弹窗的唯一标识，用于回调内部关闭弹窗
         * @param cancelText String 取消按钮文字，默认"取消"
         * @param confirmText String 确定按钮文字，默认"确定"
         * @param cancelButtonType ButtonType 取消按钮样式，默认 [ButtonType.DEFAULT]
         * @param confirmButtonType ButtonType 确定按钮样式，默认 [ButtonType.PRIMARY]
         * @param onCancelClick 取消按钮点击回调，返回 true 则自动关闭弹窗
         * @param onConfirmClick 确定按钮点击回调，返回 true 则自动关闭弹窗
         */
        @Composable
        fun ButtonRow(
            id: String,
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

@Preview(showBackground = true, backgroundColor = 0xFF888888)
@Composable
private fun PreviewConfirmDialogDefault() {
    ConfirmDialogBuilder(
        title = "提示",
        content = "确认要执行此操作吗？",
        type = ConfirmDialogBuilder.Type.Default
    ).Build("preview")
}

@Preview(showBackground = true, backgroundColor = 0xFF888888)
@Composable
private fun PreviewConfirmDialogDanger() {
    ConfirmDialogBuilder(
        title = "删除确认",
        content = "删除后数据无法恢复，确认继续？",
        confirmText = "删除",
        type = ConfirmDialogBuilder.Type.Danger
    ).Build("preview")
}

@Preview(showBackground = true, backgroundColor = 0xFF888888)
@Composable
private fun PreviewConfirmDialogWarning() {
    ConfirmDialogBuilder(
        title = "注意",
        content = "此操作可能影响其他用户，请确认后继续。",
        type = ConfirmDialogBuilder.Type.Warning
    ).Build("preview")
}
