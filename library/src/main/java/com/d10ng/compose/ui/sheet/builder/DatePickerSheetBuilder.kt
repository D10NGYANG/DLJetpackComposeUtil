package com.d10ng.compose.ui.sheet.builder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.form.DatePicker
import com.d10ng.compose.ui.form.DatePickerMode
import com.d10ng.compose.ui.sheet.SheetColumn
import com.d10ng.datelib.curTime
import kotlinx.coroutines.CoroutineScope

/**
 * 日期选择器构建器
 * @Author d10ng
 * @Date 2023/9/11 17:44
 */
class DatePickerSheetBuilder(
    // 标题
    private val title: String = "请选择",
    // 选中的日期
    private val value: Long,
    // 开始日期
    private val start: Long = 0,
    // 结束日期，包含
    private val endInclude: Long = curTime,
    // 文本样式
    private val textStyle: TextStyle = AppText.Normal.Title.default,
    // 选择器模式
    private val mode: DatePickerMode = DatePickerMode.YMD,
    // 选项文本
    private val itemText: (Int, String) -> String = { _, item -> item },
    // 取消文本
    private val cancelText: String = "取消",
    // 确定文本
    private val confirmText: String = "确定",
    // 取消按钮点击事件，返回true则隐藏弹窗
    private val onCancelClick: suspend CoroutineScope.() -> Boolean = { true },
    // 确定按钮点击事件，返回true则隐藏弹窗
    private val onConfirmClick: suspend CoroutineScope.(Long) -> Boolean = { true },
): SheetBuilder() {
    @Composable
    override fun Build() {
        var selected by remember(value) {
            mutableLongStateOf(value)
        }
        SheetColumn {
            // 标题栏
            RadioSheetBuilder.TitleBar(
                title = title,
                cancelText = cancelText,
                confirmText = confirmText,
                onCancelClick = onCancelClick,
                onConfirmClick = { onConfirmClick(selected) }
            )
            // 选项
            DatePicker(
                value = selected,
                onValueChange = { selected = it },
                start = start,
                endInclude = endInclude,
                textStyle = textStyle,
                mode = mode,
                itemText = itemText
            )
        }
    }
}