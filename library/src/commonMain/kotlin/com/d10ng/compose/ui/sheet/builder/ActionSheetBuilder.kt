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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.sheet.SheetBox

/**
 * 动作面板构建器
 *
 * 从底部弹出的动作面板，展示一组可点击的操作选项和底部取消按钮。
 * 适用于提供多个操作选项供用户选择的场景（如分享、导出、删除等）。
 * 点击选项后会触发 [onItemClick] 回调并自动关闭面板。
 *
 * @param T 选项数据类型
 * @param items Set<T> 选项数据集合，按顺序展示
 * @param itemText (T) -> String 选项文本转换函数，默认调用 toString()
 * @param itemStyle (T) -> TextStyle 选项文本样式，可根据选项动态设置（如危险操作设为红色），默认 `AppText.Normal.Title.default`
 * @param cancelText String 取消按钮文字，默认 "取消"
 * @param onItemClick (T) -> Unit 选项点击回调，默认无操作
 * @Author d10ng
 * @Date 2023/9/8 18:07
 */
class ActionSheetBuilder<T>(
    // 选项
    private val items: Set<T>,
    // 选项文本
    private val itemText: (T) -> String = { it.toString() },
    // 选项文本样式
    private val itemStyle: (T) -> TextStyle = { AppText.Normal.Title.default },
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
                        text = itemText(item),
                        style = itemStyle(item)
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
        style: TextStyle = AppText.Normal.Title.default,
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
                style = style
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewActionSheet() {
    ActionSheetBuilder(
        items = linkedSetOf("选项一", "选项二", "选项三"),
    ).Build()
}