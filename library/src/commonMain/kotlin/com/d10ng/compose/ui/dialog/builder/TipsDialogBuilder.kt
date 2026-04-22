package com.d10ng.compose.ui.dialog.builder

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
 * 单按钮提示弹窗构建器
 * 适用于仅需用户知晓并确认的通知类场景，提供标题、内容文本和一个确认按钮
 * 支持通过 [contentSlot] 插入自定义内容代替或补充纯文字内容
 * @Author d10ng
 * @Date 2023/9/7 13:38
 */
open class TipsDialogBuilder(
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
    /** 确认按钮文字，默认"确定" */
    private val buttonText: String = "确定",
    /** 弹窗主题类型，控制标题颜色和按钮样式，默认 [Type.Default] */
    private val type: Type = Type.Default,
    /** 按钮点击回调，返回 true 则自动关闭弹窗，默认直接关闭 */
    private val onButtonClick: suspend CoroutineScope.() -> Boolean = { true }
): DialogBuilder() {

    /**
     * 弹窗主题类型，控制标题文字样式和按钮颜色
     * @property titleStyle 标题文字样式
     * @property contentStyle 内容文字样式
     * @property buttonType 按钮类型
     */
    enum class Type(val titleStyle: TextStyle, val contentStyle: TextStyle, val buttonType: ButtonType) {
        /** 默认蓝色主题 */
        Default(AppText.Bold.Title.large, AppText.Normal.Body.default, ButtonType.PRIMARY),
        /** 成功绿色主题 */
        Success(AppText.Bold.Success.large, AppText.Normal.Body.default, ButtonType.SUCCESS),
        /** 警告橙色主题 */
        Warning(AppText.Bold.Assist.large, AppText.Normal.Body.default, ButtonType.WARNING),
        /** 危险红色主题 */
        Danger(AppText.Bold.Error.large, AppText.Normal.Body.default, ButtonType.DANGER),
    }

    @Composable
    override fun Build(id: String) {
        val scope = rememberCoroutineScope()
        DialogColumn {
            // 标题
            if (title.isNotEmpty()) {
                TitleText(title, type.titleStyle, titleAlign)
            }
            // 内容
            contentSlot()
            if (content.isNotEmpty()) {
                ContentText(content, type.contentStyle, contentAlign)
            }
            // 按钮
            Button(
                text = buttonText,
                modifier = Modifier
                    .fillMaxWidth(),
                shape = AppShape.RC.Cycle,
                type = type.buttonType
            ) {
                scope.launch {
                    if (onButtonClick(this)) dismiss(id)
                }
            }
        }
    }

    companion object {
        /**
         * 标题文字组件
         * 渲染一行带下边距的标题文本，供各 Builder 复用
         * @param text String 标题内容
         * @param style TextStyle 文字样式，默认粗体标题
         * @param textAlign TextAlign 文字对齐方式，默认居中
         */
        @Composable
        fun TitleText(
            text: String,
            style: TextStyle = AppText.Bold.Title.large,
            textAlign: TextAlign = TextAlign.Center
        ) {
            BasicText(text, style, textAlign)
        }

        /**
         * 内容文字组件
         * 渲染一行带下边距的内容文本，供各 Builder 复用
         * @param text String 内容文字
         * @param style TextStyle 文字样式，默认正文
         * @param textAlign TextAlign 文字对齐方式，默认居中
         */
        @Composable
        fun ContentText(
            text: String,
            style: TextStyle = AppText.Normal.Body.default,
            textAlign: TextAlign = TextAlign.Center
        ) {
            BasicText(text, style, textAlign)
        }

        @Composable
        private fun BasicText(
            text: String,
            style: TextStyle,
            textAlign: TextAlign = TextAlign.Center
        ) {
            Text(
                text = text,
                style = style,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp),
                textAlign = textAlign
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF888888)
@Composable
private fun PreviewTipsDialogDefault() {
    TipsDialogBuilder(
        title = "提示",
        content = "这是一条提示信息，请用户确认后继续操作。",
        type = TipsDialogBuilder.Type.Default
    ).Build("preview")
}

@Preview(showBackground = true, backgroundColor = 0xFF888888)
@Composable
private fun PreviewTipsDialogSuccess() {
    TipsDialogBuilder(
        title = "操作成功",
        content = "您的订单已提交成功，请耐心等待处理。",
        type = TipsDialogBuilder.Type.Success
    ).Build("preview")
}

@Preview(showBackground = true, backgroundColor = 0xFF888888)
@Composable
private fun PreviewTipsDialogDanger() {
    TipsDialogBuilder(
        title = "危险操作",
        content = "此操作将永久删除数据，无法恢复，请谨慎确认。",
        buttonText = "我已知晓",
        type = TipsDialogBuilder.Type.Danger
    ).Build("preview")
}
