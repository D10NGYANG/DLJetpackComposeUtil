@file:OptIn(ExperimentalTime::class)

package com.d10ng.compose.ui.sheet.builder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.form.DatePicker
import com.d10ng.compose.ui.form.DatePickerMode
import com.d10ng.compose.ui.sheet.SheetColumn
import kotlinx.coroutines.CoroutineScope
import kotlin.time.ExperimentalTime

/**
 * 日期选择器底部弹窗构建器
 *
 * 从底部弹出的日期选择面板，包含标题栏（取消/确定按钮）和日期滚轮选择器。
 * 支持年月日多种选择模式，可配置日期范围。
 * 确认后通过 [onConfirmClick] 回调返回选中的日期时间戳（毫秒）。
 *
 * @param title 标题文字，默认 "请选择"
 * @param value 当前选中的日期时间戳（毫秒），必填
 * @param start 可选日期范围的起始时间戳（毫秒），默认 0
 * @param endInclude 可选日期范围的结束时间戳（毫秒，包含），默认当前系统时间
 * @param textStyle 选项文字样式，默认 `AppText.Normal.Title.default`
 * @param mode 选择器模式，默认 [DatePickerMode.YMD]
 * @param itemText 选项文字自定义函数，第一个参数为列索引，第二个为默认文字
 * @param cancelText 取消按钮文字，默认 "取消"
 * @param confirmText 确定按钮文字，默认 "确定"
 * @param onCancelClick 取消按钮点击回调，返回 true 则自动关闭弹窗
 * @param onConfirmClick 确定按钮点击回调，参数为选中的日期时间戳（毫秒），返回 true 则自动关闭弹窗
 * @Author d10ng
 * @Date 2023/9/11 17:44
 */
class DatePickerSheetBuilder(
    private val title: String = "请选择",
    private val value: Long,
    private val start: Long = 0,
    private val endInclude: Long = kotlin.time.Clock.System.now().toEpochMilliseconds(),
    private val textStyle: TextStyle = AppText.Normal.Title.default,
    private val mode: DatePickerMode = DatePickerMode.YMD,
    private val itemText: (Int, String) -> String = { _, item -> item },
    private val cancelText: String = "取消",
    private val confirmText: String = "确定",
    private val onCancelClick: suspend CoroutineScope.() -> Boolean = { true },
    private val onConfirmClick: suspend CoroutineScope.(Long) -> Boolean = { true },
): SheetBuilder() {
    @Composable
    override fun Build() {
        var selected by remember(value) {
            mutableLongStateOf(value)
        }
        SheetColumn {
            TitleBar(
                title = title,
                cancelText = cancelText,
                confirmText = confirmText,
                onCancelClick = onCancelClick,
                onConfirmClick = { onConfirmClick(selected) }
            )
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

@Preview(showBackground = true)
@Composable
private fun PreviewDatePickerSheet() {
    DatePickerSheetBuilder(
        title = "选择日期",
        value = 1713772800000L,
    ).Build()
}