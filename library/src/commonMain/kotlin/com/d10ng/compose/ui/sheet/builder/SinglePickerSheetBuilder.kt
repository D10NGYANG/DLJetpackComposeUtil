package com.d10ng.compose.ui.sheet.builder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.form.Picker
import com.d10ng.compose.ui.sheet.SheetColumn
import kotlinx.coroutines.CoroutineScope

/**
 * 单列滚轮选择器构建器
 * @Author d10ng
 * @Date 2023/9/11 15:38
 */
class SinglePickerSheetBuilder<T>(
    // 标题
    private val title: String = "请选择",
    // 选项
    private val items: Set<T>,
    // 选项文本
    private val itemText: (T) -> String = { it.toString() },
    // 文本样式
    private val textStyle: TextStyle = AppText.Normal.Title.default,
    // 选中的项
    private val selectedItem: T = items.first(),
    // 取消文本
    private val cancelText: String = "取消",
    // 确定文本
    private val confirmText: String = "确定",
    // 取消按钮点击事件，返回true则隐藏弹窗
    private val onCancelClick: suspend CoroutineScope.() -> Boolean = { true },
    // 确定按钮点击事件，返回true则隐藏弹窗
    private val onConfirmClick: suspend CoroutineScope.(T) -> Boolean = { true },
): SheetBuilder() {
    @Composable
    override fun Build() {
        var selected by remember(selectedItem) {
            mutableStateOf(selectedItem)
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