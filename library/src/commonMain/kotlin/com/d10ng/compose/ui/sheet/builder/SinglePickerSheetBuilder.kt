package com.d10ng.compose.ui.sheet.builder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.form.Picker
import com.d10ng.compose.ui.sheet.SheetColumn
import kotlinx.coroutines.CoroutineScope

/**
 * 单列滚轮选择器底部弹窗构建器
 *
 * 从底部弹出的单列滚轮选择面板，包含标题栏（取消/确定按钮）和单列滚轮选择器。
 * 适用于从一组选项中选择单个值的场景。
 * 确认后通过 [onConfirmClick] 回调返回选中项。
 *
 * @param T 选项数据类型
 * @param title 标题文字，默认 "请选择"
 * @param items 可选项集合，要求非空
 * @param itemText 选项文本转换函数，默认调用 toString()
 * @param textStyle 选项文字样式，默认 `AppText.Normal.Title.default`
 * @param selectedItem 当前选中的项，默认为第一项
 * @param cancelText 取消按钮文字，默认 "取消"
 * @param confirmText 确定按钮文字，默认 "确定"
 * @param onCancelClick 取消按钮点击回调，返回 true 则自动关闭弹窗
 * @param onConfirmClick 确定按钮点击回调，参数为选中项，返回 true 则自动关闭弹窗
 * @Author d10ng
 * @Date 2023/9/11 15:38
 */
class SinglePickerSheetBuilder<T>(
    private val title: String = "请选择",
    private val items: Set<T>,
    private val itemText: (T) -> String = { it.toString() },
    private val textStyle: TextStyle = AppText.Normal.Title.default,
    private val selectedItem: T = items.first(),
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
            TitleBar(
                title = title,
                cancelText = cancelText,
                confirmText = confirmText,
                onCancelClick = onCancelClick,
                onConfirmClick = { onConfirmClick(selected) }
            )
            Picker(
                items = items,
                itemText = itemText,
                textStyle = textStyle,
                selectedItem = selected,
                onValueChange = { selected = it }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSinglePickerSheet() {
    SinglePickerSheetBuilder(
        title = "选择颜色",
        items = linkedSetOf("红色", "橙色", "黄色", "绿色", "蓝色", "紫色"),
        selectedItem = "绿色",
    ).Build()
}