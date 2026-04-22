@file:OptIn(ExperimentalTime::class)

package com.d10ng.compose.ui.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.d10ng.compose.ui.AppText
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * 日期选择器模式
 * 控制 [DatePicker] 显示的列组合，每种模式决定了滚轮列数及各列含义
 */
enum class DatePickerMode(
    val getItems: (Set<String>, Set<String>, Set<String>) -> List<Set<String>>,
    val getSelectedItems: (String, String, String) -> List<String>,
    val getDate: (Long, List<String>) -> Long,
) {
    // 年月日（三列）
    YMD(
        { y, m, d -> listOf(y, m, d) },
        { y, m, d -> listOf(y, m, d) },
        { v, l ->
            createSystemLocalDateTime(v)
                .copy(l[0].toInt(), l[1].toInt(), l[2].toInt())
                .toInstant(TimeZone.currentSystemDefault())
                .toEpochMilliseconds()
        }
    ),

    // 年月（两列）
    YM(
        { y, m, _ -> listOf(y, m) },
        { y, m, _ -> listOf(y, m) },
        { v, l ->
            createSystemLocalDateTime(v)
                .copy(l[0].toInt(), l[1].toInt())
                .toInstant(TimeZone.currentSystemDefault())
                .toEpochMilliseconds()
        }
    ),

    // 仅年（单列）
    Y(
        { y, _, _ -> listOf(y) },
        { y, _, _ -> listOf(y) },
        { v, l ->
            createSystemLocalDateTime(v)
                .copy(l[0].toInt())
                .toInstant(TimeZone.currentSystemDefault())
                .toEpochMilliseconds()
        }
    ),

    // 月日（两列）
    MD(
        { _, m, d -> listOf(m, d) },
        { _, m, d -> listOf(m, d) },
        { v, l ->
            createSystemLocalDateTime(v)
                .copy(month = l[0].toInt(), day = l[1].toInt())
                .toInstant(TimeZone.currentSystemDefault())
                .toEpochMilliseconds()
        }
    ),

    // 仅月（单列）
    M(
        { _, m, _ -> listOf(m) },
        { _, m, _ -> listOf(m) },
        { v, l ->
            createSystemLocalDateTime(v)
                .copy(month = l[0].toInt())
                .toInstant(TimeZone.currentSystemDefault())
                .toEpochMilliseconds()
        }
    ),
}

/**
 * 日期选择器
 * 基于 [MultiPicker] 实现的日期滚轮选择组件，根据 [mode] 自动计算并显示对应的年/月/日列
 * 自动处理各列之间的联动（如月份变化时重新计算当月天数），并将选中结果限制在 [[start], [endInclude]] 范围内
 * @param value Long 当前选中日期的时间戳（毫秒）
 * @param onValueChange (Long) -> Unit 选中日期变更回调，参数为新选中日期的时间戳（毫秒），已裁剪至合法范围
 * @param start Long 可选范围的开始时间戳（毫秒），默认 0（即 1970-01-01）
 * @param endInclude Long 可选范围的结束时间戳（毫秒，包含该天），默认当前系统时间
 * @param textStyle TextStyle 选项文字样式，默认 `AppText.Normal.Title.default`
 * @param mode DatePickerMode 日期列组合模式，默认 [DatePickerMode.YMD]（年月日三列）
 * @param itemText (Int, String) -> String 自定义选项文字格式化函数，第一个参数为列索引（0 起），第二个参数为原始文字（如 "04"），默认原样返回
 */
@Composable
fun DatePicker(
    value: Long,
    onValueChange: (Long) -> Unit,
    start: Long = 0,
    endInclude: Long = kotlin.time.Clock.System.now().toEpochMilliseconds(),
    textStyle: TextStyle = AppText.Normal.Title.default,
    mode: DatePickerMode = DatePickerMode.YMD,
    itemText: (Int, String) -> String = { _, item -> item },
) {
    val datetime = remember(value) {
        createSystemLocalDateTime(value)
    }
    val startDateTime = remember(start) {
        createSystemLocalDateTime(start)
    }
    val endDateTime = remember(endInclude) {
        createSystemLocalDateTime(endInclude)
    }
    // 年份列表
    val years = remember(startDateTime, endDateTime) {
        (startDateTime.year .. endDateTime.year).map { it.toString() }.toSet()
    }
    // 选择的年份
    val year = remember(datetime) { datetime.year }
    // 月份列表
    val months = remember(year, startDateTime, endDateTime) {
        val startMonth = if (year == startDateTime.year) startDateTime.month.number else 1
        val endMonth = if (year == endDateTime.year) endDateTime.month.number else 12
        (startMonth..endMonth).map { it.toString().padStart(2, '0') }.toSet()
    }
    // 选择的月份
    val month = remember(datetime) { datetime.month.number }
    // 日期列表
    val days = remember(year, month, startDateTime, endDateTime, datetime) {
        val startDay = if (year == startDateTime.year && month == startDateTime.month.number) startDateTime.day else 1
        val endDay =
            if (year == endDateTime.year && month == endDateTime.month.number) endDateTime.day
            else daysOfMonth(datetime.year, datetime.month.number)
        (startDay..endDay).map { it.toString().padStart(2, '0') }.toSet()
    }
    // 选择的日期
    val day = remember(datetime) { datetime.day }
    val items = remember(mode, years, months, days) {
        mode.getItems(years, months, days)
    }
    val selectedItems = remember(mode, year, month, day) {
        mode.getSelectedItems(
            year.toString(),
            month.toString().padStart(2, '0'),
            day.toString().padStart(2, '0')
        )
    }
    MultiPicker(
        items = items,
        itemText = itemText,
        textStyle = textStyle,
        selectedItems = selectedItems,
        onValueChange = {
            onValueChange(mode.getDate(value, it).coerceAtLeast(start).coerceAtMost(endInclude))
        }
    )
}

private fun createSystemLocalDateTime(milliseconds: Long): LocalDateTime {
    val instant = Instant.fromEpochMilliseconds(milliseconds)
    return instant.toLocalDateTime(TimeZone.currentSystemDefault())
}

private fun daysOfMonth(year: Int, month: Int): Int {
    val start = LocalDate(year, month, 1)
    val end = start + DatePeriod(months = 1)
    return start.daysUntil(end)
}

private fun LocalDateTime.copy(
    year: Int = this.year,
    month: Int = this.month.number,
    day: Int = this.day,
    hour: Int = this.hour,
    minute: Int = this.minute,
    second: Int = this.second,
    nanosecond: Int = this.nanosecond
): LocalDateTime {
    val y = year.coerceIn(1970, 9999)
    val m = month.coerceIn(1, 12)
    return LocalDateTime(
        year = y,
        month = m,
        day = day.coerceIn(1, daysOfMonth(y, m)),
        hour = hour.coerceIn(0, 23),
        minute = minute.coerceIn(0, 59),
        second = second.coerceIn(0, 59),
        nanosecond = nanosecond.coerceIn(0, 999999999)
    )
}

@Preview
@Composable
fun PreviewDatePickerYMD() {
    // 2024-04-22 对应的毫秒时间戳
    val value = 1713744000000L
    val start = 946684800000L  // 2000-01-01
    Box(modifier = Modifier.background(Color.White)) {
        DatePicker(
            value = value,
            onValueChange = {},
            start = start,
            endInclude = 1777651200000L, // 2026-05-01
            mode = DatePickerMode.YMD
        )
    }
}

@Preview
@Composable
fun PreviewDatePickerYM() {
    val value = 1713744000000L
    Box(modifier = Modifier.background(Color.White)) {
        DatePicker(
            value = value,
            onValueChange = {},
            start = 946684800000L,
            endInclude = 1777651200000L,
            mode = DatePickerMode.YM
        )
    }
}

@Preview
@Composable
fun PreviewDatePickerModes() {
    val value = 1713744000000L
    Column(modifier = Modifier.background(Color.White)) {
        // 仅年
        DatePicker(value = value, onValueChange = {}, mode = DatePickerMode.Y)
        // 月日
        DatePicker(value = value, onValueChange = {}, mode = DatePickerMode.MD)
        // 仅月
        DatePicker(value = value, onValueChange = {}, mode = DatePickerMode.M)
    }
}
