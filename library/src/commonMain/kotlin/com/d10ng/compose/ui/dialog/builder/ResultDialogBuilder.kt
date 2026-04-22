package com.d10ng.compose.ui.dialog.builder

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
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
 * 适用于异步操作完成后展示成功或失败结果的场景
 * 图标区域根据 [status] 自动切换，支持仅确认或确认+取消两种按钮布局
 * @Author d10ng
 * @Date 2023/11/21 14:10
 */
class ResultDialogBuilder(
    /** 弹窗标题，为空时不渲染标题行，默认"提示" */
    private val title: String = "提示",
    /** 结果描述文字 */
    private val content: String,
    /** 结果状态，控制中间图标，默认 [Status.SUCCESS] */
    private val status: Status = Status.SUCCESS,
    /** 确定按钮文字，默认"确定" */
    private val confirmText: String = "确定",
    /** 取消按钮文字，默认"取消" */
    private val cancelText: String = "取消",
    /** 是否显示取消按钮，false 时只显示确定按钮，默认 true */
    private val showCancel: Boolean = true,
    /** 确定按钮点击回调，返回 true 则自动关闭弹窗，默认直接关闭 */
    private val onConfirmClick: suspend CoroutineScope.() -> Boolean = { true },
    /** 取消按钮点击回调，返回 true 则自动关闭弹窗，默认直接关闭 */
    private val onCancelClick: suspend CoroutineScope.() -> Boolean = { true }
): DialogBuilder() {

    /**
     * 结果状态枚举，决定弹窗中间展示的图标
     * @property iconResource 对应的图标资源
     */
    enum class Status(val iconResource: DrawableResource) {
        /** 成功状态，展示绿色对勾图标 */
        SUCCESS(Res.drawable.ic_round_success_160),
        /** 失败/错误状态，展示红色叉号图标 */
        ERROR(Res.drawable.ic_round_error_160),
    }

    @Composable
    override fun Build(id: String) {
        DialogColumn {
            // 标题
            if (title.isNotEmpty()) {
                TipsDialogBuilder.TitleText(text = title)
            }
            // 图标
            Image(
                painter = painterResource(resource = status.iconResource),
                contentDescription = null,
                modifier = Modifier.padding(bottom = 12.dp).size(100.dp),
                contentScale = ContentScale.FillBounds
            )
            // 内容
            TipsDialogBuilder.ContentText(text = content)
            // 按钮组
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
                    modifier = Modifier.fillMaxWidth(),
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

@Preview(showBackground = true, backgroundColor = 0xFF888888)
@Composable
private fun PreviewResultDialogSuccess() {
    ResultDialogBuilder(
        title = "提交成功",
        content = "您的申请已成功提交，请等待审核。",
        status = ResultDialogBuilder.Status.SUCCESS,
        showCancel = false
    ).Build("preview")
}

@Preview(showBackground = true, backgroundColor = 0xFF888888)
@Composable
private fun PreviewResultDialogError() {
    ResultDialogBuilder(
        title = "操作失败",
        content = "网络异常，请稍后重试。",
        status = ResultDialogBuilder.Status.ERROR,
        confirmText = "重试",
        cancelText = "取消",
        showCancel = true
    ).Build("preview")
}
