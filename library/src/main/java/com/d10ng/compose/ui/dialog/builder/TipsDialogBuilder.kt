package com.d10ng.compose.ui.dialog.builder

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.base.Button
import com.d10ng.compose.ui.base.ButtonType
import com.d10ng.compose.ui.dialog.DialogColumn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * 提示弹窗构建器
 * @Author d10ng
 * @Date 2023/9/7 13:38
 */
open class TipsDialogBuilder(
    // 标题
    private val title: String = "提示",
    // 内容
    private val content: String,
    // 内容插槽
    private val contentSlot: @Composable () -> Unit = {},
    // 按钮文字
    private val buttonText: String = "确定",
    // 弹窗类型
    private val type: Type = Type.Default,
    // 按钮点击事件，返回true则隐藏弹窗
    private val onButtonClick: suspend CoroutineScope.() -> Boolean = { true }
): DialogBuilder() {

    enum class Type(val titleStyle: TextStyle, val contentStyle: TextStyle, val buttonType: ButtonType) {
        Default(AppText.Bold.Title.large, AppText.Normal.Body.default, ButtonType.PRIMARY),
        Success(AppText.Bold.Success.large, AppText.Normal.Body.default, ButtonType.SUCCESS),
        Warning(AppText.Bold.Assist.large, AppText.Normal.Body.default, ButtonType.WARNING),
        Danger(AppText.Bold.Error.large, AppText.Normal.Body.default, ButtonType.DANGER),
    }

    @Composable
    override fun Build(id: Int) {
        val scope = rememberCoroutineScope()
        DialogColumn {
            // 标题
            if (title.isNotEmpty()) {
                TitleText(text = title, style = type.titleStyle)
            }
            // 内容
            contentSlot()
            if (content.isNotEmpty()) {
                ContentText(text = content, style = type.contentStyle)
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
        @Composable
        fun TitleText(
            text: String,
            style: TextStyle = AppText.Bold.Title.large
        ) {
            BasicText(text, style)
        }

        @Composable
        fun ContentText(
            text: String,
            style: TextStyle = AppText.Normal.Body.default
        ) {
            BasicText(text, style)
        }

        @Composable
        private fun BasicText(
            text: String,
            style: TextStyle
        ) {
            Text(
                text = text,
                style = style,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}