package com.d10ng.compose.ui.sheet.builder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.form.MultiPicker
import com.d10ng.compose.ui.sheet.SheetColumn
import kotlinx.coroutines.CoroutineScope

/**
 * 多列滚轮选择器底部弹窗构建器
 *
 * 从底部弹出的多列滚轮选择面板，包含标题栏（取消/确定按钮）和多列并排的滚轮选择器。
 * 适用于需要多维度同时选择的场景（如省市区联动、年月日选择等）。
 * 确认后通过 [onConfirmClick] 回调返回各列选中项组成的列表。
 *
 * @param T 选项数据类型
 * @param title 标题文字，默认 "请选择"
 * @param items 每列的可选项集合列表，列表长度决定列数，每个 Set 不可为空
 * @param itemText 选项文字转换函数，第一个参数为列索引（从 0 起），第二个为选项值
 * @param textStyle 选项文字样式，默认 `AppText.Normal.Title.default`
 * @param selectedItems 各列当前选中项列表，长度须与 [items] 一致，默认每列取第一项
 * @param cancelText 取消按钮文字，默认 "取消"
 * @param confirmText 确定按钮文字，默认 "确定"
 * @param onCancelClick 取消按钮点击回调，返回 true 则自动关闭弹窗
 * @param onConfirmClick 确定按钮点击回调，参数为各列选中项列表，返回 true 则自动关闭弹窗
 * @Author d10ng
 * @Date 2023/9/11 15:52
 */
class MultiPickerSheetBuilder<T>(
    private val title: String = "请选择",
    private val items: List<Set<T>>,
    private val itemText: (Int, T) -> String = { _, item -> item.toString() },
    private val textStyle: TextStyle = AppText.Normal.Title.default,
    private val selectedItems: List<T> = items.map { it.first() },
    private val cancelText: String = "取消",
    private val confirmText: String = "确定",
    private val onCancelClick: suspend CoroutineScope.() -> Boolean = { true },
    private val onConfirmClick: suspend CoroutineScope.(List<T>) -> Boolean = { true },
): SheetBuilder() {
    @Composable
    override fun Build() {
        var selected by remember(selectedItems) {
            mutableStateOf(selectedItems)
        }
        SheetColumn {
            TitleBar(
                title = title,
                cancelText = cancelText,
                confirmText = confirmText,
                onCancelClick = onCancelClick,
                onConfirmClick = { onConfirmClick(selected) }
            )
            MultiPicker(
                items = items,
                itemText = itemText,
                textStyle = textStyle,
                selectedItems = selected,
                onValueChange = { selected = it }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMultiPickerSheet() {
    val col1 = linkedSetOf("A", "B", "C")
    val col2 = linkedSetOf("1", "2", "3")
    MultiPickerSheetBuilder(
        title = "多列选择",
        items = listOf(col1, col2),
        selectedItems = listOf("A", "2"),
    ).Build()
}