package com.d10ng.compose.ui.dialog.builder

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.d10ng.compose.resources.Res
import com.d10ng.compose.resources.ic_round_error_160
import com.d10ng.compose.resources.ic_round_success_160
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.base.Button
import com.d10ng.compose.ui.base.ButtonType
import com.d10ng.compose.ui.dialog.DialogColumn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * 结果弹窗构建器
 * @Author d10ng
 * @Date 2023/11/21 14:10
 */
class ResultDialogBuilder(
    // 标题
    private val title: String = "提示",
    // 内容
    private val content: String,
    // 状态
    private val status: Status = Status.SUCCESS,
    // 确定按钮文字
    private val confirmText: String = "确定",
    // 取消按钮文字
    private val cancelText: String = "取消",
    // 是否显示取消按钮
    private val showCancel: Boolean = true,
    // 确定按钮点击事件，返回true则隐藏弹窗
    private val onConfirmClick: suspend CoroutineScope.() -> Boolean = { true },
    // 取消按钮点击事件，返回true则隐藏弹窗
    private val onCancelClick: suspend CoroutineScope.() -> Boolean = { true }
): DialogBuilder() {

    enum class Status(val iconResource: DrawableResource) {
        SUCCESS(Res.drawable.ic_round_success_160),
        ERROR(Res.drawable.ic_round_error_160),
    }

    @Composable
    override fun Build(id: String) {
        DialogColumn {
            // 标题
            TipsDialogBuilder.TitleText(text = title)
            // 图标
            Image(
                painter = painterResource(resource = status.iconResource),
                contentDescription = null,
                modifier = Modifier.padding(bottom = 12.dp).size(100.dp),
                contentScale = ContentScale.FillBounds
            )
            // 内容
            TipsDialogBuilder.ContentText(text = content)
            if (showCancel) {
                ConfirmDialogBuilder.ButtonRow(
                    id = id,
                    cancelText = cancelText,
                    confirmText = confirmText,
                    onCancelClick = onCancelClick,
                    onConfirmClick = onConfirmClick
                )
            } else {
                val scope = rememberCoroutineScope()
                Button(
                    text = confirmText,
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = AppShape.RC.Cycle,
                    type = ButtonType.PRIMARY
                ) {
                    scope.launch {
                        if (onConfirmClick(this)) dismiss(id)
                    }
                }
            }
        }
    }

}
