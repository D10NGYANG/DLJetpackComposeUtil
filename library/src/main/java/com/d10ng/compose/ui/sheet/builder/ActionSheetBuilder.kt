package com.d10ng.compose.ui.sheet.builder

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.sheet.SheetBox

/**
 * 动作面板构建器
 * @Author d10ng
 * @Date 2023/9/8 18:07
 */
class ActionSheetBuilder<T>(
    // 选项
    private val items: Set<T>,
    // 选项文本
    private val itemText: (T) -> String = { it.toString() },
    // 取消文本
    private val cancelText: String = "取消",
    // 选项点击事件
    private val onItemClick: (T) -> Unit = {},
): SheetBuilder() {
    @Composable
    override fun Build() {
        SheetBox {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColor.Neutral.card)
                    .padding(top = 16.dp)
                    .navigationBarsPadding()
            ) {
                // 选项
                items.forEach { item ->
                    ItemView(
                        text = itemText(item)
                    ) {
                        onItemClick(item)
                        dismiss()
                    }
                }
                // 间隔
                Box(modifier = Modifier.height(8.dp))
                // 取消按钮
                ItemView(
                    text = cancelText
                ) {
                    dismiss()
                }
            }
        }
    }

    @Composable
    private fun ItemView(
        text: String,
        onClick: () -> Unit
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = 1.dp)
                .fillMaxWidth()
                .height(48.dp)
                .background(Color.White)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = AppText.Normal.Title.default
            )
        }
    }
}