package com.d10ng.compose.ui.sheet.builder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.form.RadioCell
import com.d10ng.compose.ui.sheet.SheetColumn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * 单选面板底部弹窗构建器
 *
 * 从底部弹出的单选列表面板，展示一组带单选按钮的选项列表。
 * 支持两种交互模式：
 * - **带按钮模式**（[showButton] = true）：顶部显示标题栏（取消/确定按钮），
 *   用户选择后需点击确定才会触发回调
 * - **无按钮模式**（[showButton] = false）：仅显示标题，
 *   点击选项即刻触发 [onConfirmClick] 回调并关闭弹窗
 *
 * 选项列表超出可视区域时支持垂直滚动。
 *
 * @param T 选项数据类型
 * @param title 标题文字，默认 "请选择"
 * @param items 选项数据集合，要求非空
 * @param itemText 选项文本转换函数，默认调用 toString()
 * @param selectedItem 当前选中的项，默认为第一项
 * @param showButton 是否显示确定和取消按钮，默认 true
 * @param cancelText 取消按钮文字，默认 "取消"
 * @param confirmText 确定按钮文字，默认 "确定"
 * @param onCancelClick 取消按钮点击回调，返回 true 则自动关闭弹窗
 * @param onConfirmClick 确定按钮点击回调，参数为选中的项，返回 true 则自动关闭弹窗
 * @Author d10ng
 * @Date 2023/9/8 18:08
 */
class RadioSheetBuilder<T>(
    private val title: String = "请选择",
    private val items: Set<T>,
    private val itemText: (T) -> String = { it.toString() },
    private val selectedItem: T = items.first(),
    private val showButton: Boolean = true,
    private val cancelText: String = "取消",
    private val confirmText: String = "确定",
    private val onCancelClick: suspend CoroutineScope.() -> Boolean = { true },
    private val onConfirmClick: suspend CoroutineScope.(T) -> Boolean = { true },
): SheetBuilder() {

    @Composable
    override fun Build() {
        var selected by remember(selectedItem) {
            mutableStateOf(selectedItem)
        }
        SheetColumn {
            if (showButton) {
                TitleBar(
                    title = title,
                    cancelText = cancelText,
                    confirmText = confirmText,
                    onCancelClick = onCancelClick,
                    onConfirmClick = { onConfirmClick(selected) }
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        style = AppText.Bold.Title.large,
                        textAlign = TextAlign.Center
                    )
                }
            }
            val scope = rememberCoroutineScope()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
            ) {
                items.forEachIndexed { index, item ->
                    RadioCell(
                        label = itemText(item),
                        selected = selected == item,
                        onClick = {
                            selected = item
                            if (showButton.not()) {
                                scope.launch { if (onConfirmClick(item)) dismiss() }
                            }
                        },
                        border = index != items.size - 1
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRadioSheet() {
    RadioSheetBuilder(
        title = "请选择城市",
        items = linkedSetOf("北京", "上海", "广州", "深圳"),
        selectedItem = "上海",
    ).Build()
}

@Preview(showBackground = true)
@Composable
private fun PreviewRadioSheetNoButton() {
    RadioSheetBuilder(
        title = "请选择语言",
        items = linkedSetOf("中文", "English", "日本語"),
        selectedItem = "中文",
        showButton = false,
    ).Build()
}