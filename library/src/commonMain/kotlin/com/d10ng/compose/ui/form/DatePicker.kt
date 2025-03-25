package com.d10ng.compose.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import com.d10ng.compose.ui.AppText
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

/**
 * 日期选择器
 * @Author d10ng
 * @Date 2023/9/11 17:16
 */

enum class DatePickerMode(
    val getItems: (Set<String>, Set<String>, Set<String>) -> List<Set<String>>,
    val getSelectedItems: (String, String, String) -> List<String>,
    val getDate: (Long, List<String>) -> Long,
) {
    // 年月日
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

    // 年月
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

    // 年
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

    // 月日
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

    // 月
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
 * @param value Long 选中的日期
 * @param onValueChange Function1<Long, Unit> 选中日期改变事件
 * @param start Long 开始日期
 * @param endInclude Long 结束日期，包含
 * @param textStyle TextStyle 文本样式
 * @param mode DatePickerMode 选择器模式
 * @param itemText Function2<Int, Int, String> 选项文本
 */
@Composable
fun DatePicker(
    value: Long,
    onValueChange: (Long) -> Unit,
    start: Long = 0,
    endInclude: Long = Clock.System.now().toEpochMilliseconds(),
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
        val startMonth = if (year == startDateTime.year) startDateTime.monthNumber else 1
        val endMonth = if (year == endDateTime.year) endDateTime.monthNumber else 12
        (startMonth..endMonth).map { it.toString().padStart(2, '0') }.toSet()
    }
    // 选择的月份
    val month = remember(datetime) { datetime.monthNumber }
    // 日期列表
    val days = remember(year, month, startDateTime, endDateTime, datetime) {
        val startDay = if (year == startDateTime.year && month == startDateTime.monthNumber) startDateTime.dayOfMonth else 1
        val endDay =
            if (year == endDateTime.year && month == endDateTime.monthNumber) endDateTime.dayOfMonth
            else daysOfMonth(datetime.year, datetime.monthNumber)
        (startDay..endDay).map { it.toString().padStart(2, '0') }.toSet()
    }
    // 选择的日期
    val day = remember(datetime) { datetime.dayOfMonth }
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
    month: Int = this.monthNumber,
    day: Int = this.dayOfMonth,
    hour: Int = this.hour,
    minute: Int = this.minute,
    second: Int = this.second,
    nanosecond: Int = this.nanosecond
): LocalDateTime {
    val y = year.coerceIn(1970, 9999)
    val m = month.coerceIn(1, 12)
    return LocalDateTime(
        year = y,
        monthNumber = m,
        dayOfMonth = day.coerceIn(1, daysOfMonth(y, m)),
        hour = hour.coerceIn(0, 23),
        minute = minute.coerceIn(0, 59),
        second = second.coerceIn(0, 59),
        nanosecond = nanosecond.coerceIn(0, 999999999)
    )
}