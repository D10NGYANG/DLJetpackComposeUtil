package com.d10ng.compose.ui.sheet.builder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.form.MultiPicker
import com.d10ng.compose.ui.sheet.SheetColumn
import kotlinx.coroutines.CoroutineScope

/**
 * 多列滚轮选择器构建器
 * @Author d10ng
 * @Date 2023/9/11 15:52
 */
class MultiPickerSheetBuilder<T>(
    // 标题
    private val title: String = "请选择",
    // 选项
    private val items: List<Set<T>>,
    // 选项文本
    private val itemText: (Int, T) -> String = { _, item -> item.toString() },
    // 文本样式
    private val textStyle: TextStyle = AppText.Normal.Title.default,
    // 选中的项
    private val selectedItems: List<T> = items.map { it.first() },
    // 取消文本
    private val cancelText: String = "取消",
    // 确定文本
    private val confirmText: String = "确定",
    // 取消按钮点击事件，返回true则隐藏弹窗
    private val onCancelClick: suspend CoroutineScope.() -> Boolean = { true },
    // 确定按钮点击事件，返回true则隐藏弹窗
    private val onConfirmClick: suspend CoroutineScope.(List<T>) -> Boolean = { true },
): SheetBuilder() {
    @Composable
    override fun Build() {
        var selected by remember(selectedItems) {
            mutableStateOf(selectedItems)
        }
        SheetColumn {
            // 标题栏
            TitleBar(
                title = title,
                cancelText = cancelText,
                confirmText = confirmText,
                onCancelClick = onCancelClick,
                onConfirmClick = { onConfirmClick(selected) }
            )
            // 选项
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