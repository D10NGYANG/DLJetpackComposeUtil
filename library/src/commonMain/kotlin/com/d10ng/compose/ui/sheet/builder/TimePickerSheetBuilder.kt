package com.d10ng.compose.ui.sheet.builder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.form.TimePicker
import com.d10ng.compose.ui.form.TimePickerMode
import com.d10ng.compose.ui.form.hour
import com.d10ng.compose.ui.form.minute
import com.d10ng.compose.ui.form.second
import com.d10ng.compose.ui.sheet.SheetColumn
import kotlinx.coroutines.CoroutineScope
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * 时间选择器底部弹窗构建器
 *
 * 从底部弹出的时间选择面板，包含标题栏（取消/确定按钮）和时间滚轮选择器。
 * 支持时分秒多种选择模式，可配置时间范围。
 * 时间值以一天内的秒数表示（0~86399）。
 * 确认后通过 [onConfirmClick] 回调返回选中的秒数值和各分量列表。
 *
 * 可在展示前通过 [setCurrentTime] 方法将选中时间设置为指定时间戳对应的时分秒。
 *
 * @param title 标题文字，默认 "请选择"
 * @param value 当前选中的时间，以一天内的秒数表示（0~86399），默认 0（即 00:00:00）
 * @param start 可选时间范围的起始秒数，默认 0
 * @param endInclude 可选时间范围的结束秒数（包含），默认 86399（即 23:59:59）
 * @param textStyle 选项文字样式，默认 `AppText.Normal.Title.default`
 * @param mode 选择器模式（时分秒组合），默认 [TimePickerMode.HMS]
 * @param itemText 选项文字自定义函数，第一个参数为列索引，第二个为默认文字
 * @param cancelText 取消按钮文字，默认 "取消"
 * @param confirmText 确定按钮文字，默认 "确定"
 * @param onCancelClick 取消按钮点击回调，返回 true 则自动关闭弹窗
 * @param onConfirmClick 确定按钮点击回调，第一个参数为选中的秒数值，第二个参数为时/分/秒分量列表，返回 true 则自动关闭弹窗
 * @Author d10ng
 * @Date 2023/9/12 11:23
 */
@ExperimentalTime
class TimePickerSheetBuilder(
    private val title: String = "请选择",
    private var value: Int = 0,
    private val start: Int = 0,
    private val endInclude: Int = 86399,
    private val textStyle: TextStyle = AppText.Normal.Title.default,
    private val mode: TimePickerMode = TimePickerMode.HMS,
    private val itemText: (Int, String) -> String = { _, item -> item },
    private val cancelText: String = "取消",
    private val confirmText: String = "确定",
    private val onCancelClick: suspend CoroutineScope.() -> Boolean = { true },
    private val onConfirmClick: suspend CoroutineScope.(Int, List<Int>) -> Boolean = { _, _ -> true },
) : SheetBuilder() {

    /**
     * 设置选择当前时间
     *
     * 将 [value] 设置为指定时间戳对应的时分秒（转换为一天内的秒数）。
     * 仅在弹窗展示前调用有效，弹窗展示后修改不会触发 UI 更新。
     *
     * @param timestamp 时间戳，默认当前时间，单位毫秒
     */
    fun setCurrentTime(timestamp: Long = Clock.System.now().toEpochMilliseconds()) {
        val datetime = Instant.fromEpochMilliseconds(timestamp)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        this.value = datetime.hour * 3600 + datetime.minute * 60 + datetime.second
    }

    @Composable
    override fun Build() {
        var selected by remember(value) {
            mutableIntStateOf(value)
        }
        var selectedList by remember(value) {
            mutableStateOf(listOf(value.hour(), value.minute(), value.second()))
        }
        SheetColumn {
            TitleBar(
                title = title,
                cancelText = cancelText,
                confirmText = confirmText,
                onCancelClick = onCancelClick,
                onConfirmClick = { onConfirmClick(selected, selectedList) }
            )
            TimePicker(
                value = selected,
                onValueChange = { s, l ->
                    selected = s
                    selectedList = l
                },
                start = start,
                endInclude = endInclude,
                textStyle = textStyle,
                mode = mode,
                itemText = itemText
            )
        }
    }
}

@OptIn(ExperimentalTime::class)
@Preview(showBackground = true)
@Composable
private fun PreviewTimePickerSheet() {
    TimePickerSheetBuilder(
        title = "选择时间",
        value = 36000, // 10:00:00
    ).Build()
}