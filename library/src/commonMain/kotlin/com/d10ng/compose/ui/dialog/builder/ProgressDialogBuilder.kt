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
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.dialog.DialogColumn

/**
 * 进度加载对话框构建器
 * @Author d10ng
 * @Date 2023/9/12 09:49
 */
data class ProgressDialogBuilder(
    // 标题
    private val title: String = "请稍候...",
    // 进度
    private val progress: Long = 0,
    // 最大值
    private val max: Long = 100,
    // 类型
    private val type: Type = Type.PERCENTAGE,
) : DialogBuilder(clickOutsideDismiss = false) {

    enum class Type {
        // 显示百分比
        PERCENTAGE,

        // 显示进度和最大值
        PROGRESS_AND_MAX,

        // 什么都不显示
        NONE
    }

    @Composable
    override fun Build(id: String) {
        // 根据类型换算文本内容
        val content = remember(type, progress, max) {
            when (type) {
                Type.PERCENTAGE -> "${((progress * 100.0) / max).toInt()}%"
                Type.PROGRESS_AND_MAX -> "${progress}/${max}"
                Type.NONE -> ""
            }
        }
        DialogColumn {
            // 标题
            if (title.isNotEmpty()) {
                TipsDialogBuilder.TitleText(text = title)
            }
            // 内容
            if (content.isNotEmpty()) {
                Text(text = content, style = AppText.Normal.Body.big)
            }
            // 进度
            LinearProgressIndicator(
                progress = { (progress * 1.0f) / max },
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