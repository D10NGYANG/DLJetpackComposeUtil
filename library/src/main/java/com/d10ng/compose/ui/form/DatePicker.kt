package com.d10ng.compose.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.utils.up2Length
import com.d10ng.datelib.curTime
import com.d10ng.datelib.getDateDay
import com.d10ng.datelib.getDateMonth
import com.d10ng.datelib.getDateYear
import com.d10ng.datelib.getDaysOfMonth
import com.d10ng.datelib.setDateDay
import com.d10ng.datelib.setDateMonth
import com.d10ng.datelib.setDateYear

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
        { v, l -> v.setDateYear(l[0].toInt()).setDateMonth(l[1].toInt()).setDateDay(l[2].toInt()) }
    ),

    // 年月
    YM(
        { y, m, _ -> listOf(y, m) },
        { y, m, _ -> listOf(y, m) },
        { v, l -> v.setDateYear(l[0].toInt()).setDateMonth(l[1].toInt()) }),

    // 年
    Y({ y, _, _ -> listOf(y) }, { y, _, _ -> listOf(y) }, { v, l -> v.setDateYear(l[0].toInt()) }),

    // 月日
    MD(
        { _, m, d -> listOf(m, d) },
        { _, m, d -> listOf(m, d) },
        { v, l -> v.setDateMonth(l[0].toInt()).setDateDay(l[1].toInt()) }),

    // 月
    M({ _, m, _ -> listOf(m) }, { _, m, _ -> listOf(m) }, { v, l -> v.setDateMonth(l[0].toInt()) }),
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
    endInclude: Long = curTime,
    textStyle: TextStyle = AppText.Normal.Title.default,
    mode: DatePickerMode = DatePickerMode.YMD,
    itemText: (Int, String) -> String = { _, item -> item },
) {
    // 年份列表
    val years = remember(start, endInclude) {
        (start.getDateYear()..endInclude.getDateYear()).map { it.toString() }.toSet()
    }
    // 选择的年份
    val year = remember(value) {
        value.getDateYear()
    }
    // 月份列表
    val months = remember(year, start, endInclude) {
        val startMonth = if (year == start.getDateYear()) start.getDateMonth() else 1
        val endMonth = if (year == endInclude.getDateYear()) endInclude.getDateMonth() else 12
        (startMonth..endMonth).map { it.toString().up2Length(2) }.toSet()
    }
    // 选择的月份
    val month = remember(value) {
        value.getDateMonth()
    }
    // 日期列表
    val days = remember(year, month, start, endInclude, value) {
        val startDay =
            if (year == start.getDateYear() && month == start.getDateMonth()) start.getDateDay() else 1
        val endDay =
            if (year == endInclude.getDateYear() && month == endInclude.getDateMonth()) endInclude.getDateDay()
            else getDaysOfMonth(value.getDateYear(), value.getDateMonth())
        (startDay..endDay).map { it.toString().up2Length(2) }.toSet()
    }
    // 选择的日期
    val day = remember(value) {
        value.getDateDay()
    }
    val items = remember(mode, years, months, days) {
        mode.getItems(years, months, days)
    }
    val selectedItems = remember(mode, year, month, day) {
        mode.getSelectedItems(
            year.toString(),
            month.toString().up2Length(2),
            day.toString().up2Length(2)
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