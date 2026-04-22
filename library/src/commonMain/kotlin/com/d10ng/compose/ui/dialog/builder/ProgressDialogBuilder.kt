package com.d10ng.compose.ui.dialog.builder

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.dialog.DialogColumn

/**
 * 进度条弹窗构建器
 * 适用于文件下载、数据同步等有明确进度的耗时操作场景
 * 进度条不可被用户手动关闭（[clickOutsideDismiss] 默认 false），需由业务逻辑在完成后主动调用
 * [com.d10ng.compose.model.UiViewModelManager.hideDialog] 关闭
 * 推荐使用 [com.d10ng.compose.model.UiViewModelManager.updateDialog] 实时更新进度
 * @Author d10ng
 * @Date 2023/9/12 09:49
 */
data class ProgressDialogBuilder(
    /** 弹窗标题文字，为空时不渲染标题行，默认"请稍候..." */
    private val title: String = "请稍候...",
    /** 当前进度值，取值范围 [0, [max]]，默认 0 */
    private val progress: Long = 0,
    /** 最大进度值，必须大于 0，默认 100 */
    private val max: Long = 100,
    /** 进度文字显示类型，默认 [Type.PERCENTAGE] */
    private val type: Type = Type.PERCENTAGE,
) : DialogBuilder(clickOutsideDismiss = false) {

    /**
     * 进度文字的显示类型
     */
    enum class Type {
        /** 显示百分比，如"75%" */
        PERCENTAGE,
        /** 显示当前进度与最大值，如"75/100" */
        PROGRESS_AND_MAX,
        /** 不显示进度文字 */
        NONE
    }

    @Composable
    override fun Build(id: String) {
        // 防止 max 为 0 时发生除零异常
        val safMax = if (max <= 0) 1L else max
        val content = remember(type, progress, safMax) {
            when (type) {
                Type.PERCENTAGE -> "${((progress * 100.0) / safMax).toInt()}%"
                Type.PROGRESS_AND_MAX -> "$progress/$safMax"
                Type.NONE -> ""
            }
        }
        DialogColumn {
            // 标题
            if (title.isNotEmpty()) {
                TipsDialogBuilder.TitleText(text = title)
            }
            // 进度文字
            if (content.isNotEmpty()) {
                Text(text = content, style = AppText.Normal.Body.big)
            }
            // 进度条
            LinearProgressIndicator(
                progress = { (progress.coerceIn(0, safMax) * 1.0f) / safMax },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .height(12.dp),
                color = AppColor.Main.primary,
                trackColor = AppColor.Neutral.line,
                strokeCap = StrokeCap.Round,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF888888)
@Composable
private fun PreviewProgressDialogPercentage() {
    ProgressDialogBuilder(
        title = "正在下载...",
        progress = 63,
        max = 100,
        type = ProgressDialogBuilder.Type.PERCENTAGE
    ).Build("preview")
}

@Preview(showBackground = true, backgroundColor = 0xFF888888)
@Composable
private fun PreviewProgressDialogProgressAndMax() {
    ProgressDialogBuilder(
        title = "正在同步数据",
        progress = 128,
        max = 512,
        type = ProgressDialogBuilder.Type.PROGRESS_AND_MAX
    ).Build("preview")
}

@Preview(showBackground = true, backgroundColor = 0xFF888888)
@Composable
private fun PreviewProgressDialogNone() {
    ProgressDialogBuilder(
        title = "请稍候...",
        progress = 30,
        max = 100,
        type = ProgressDialogBuilder.Type.NONE
    ).Build("preview")
}
