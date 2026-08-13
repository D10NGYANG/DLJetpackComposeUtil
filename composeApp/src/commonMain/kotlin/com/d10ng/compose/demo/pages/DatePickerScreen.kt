package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.form.DatePicker
import com.d10ng.compose.ui.form.DatePickerMode
import com.d10ng.compose.ui.navigation.NavBar
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

@Composable
fun DatePickerScreen(onBack: () -> Unit = {}) {
    val timeZone = remember { TimeZone.currentSystemDefault() }
    val datePickerStart = remember(timeZone) { localDateMillis(2000, 1, 1, timeZone) }
    val datePickerEnd = remember(timeZone) { localDateMillis(2027, 1, 1, timeZone) - 1 }
    val datePickerInitial = remember(timeZone) { localDateMillis(2024, 4, 22, timeZone) }
    val datePickerLeapDay = remember(timeZone) { localDateMillis(2024, 2, 29, timeZone) }
    var mode by remember { mutableStateOf(DatePickerMode.YMD) }
    var value by remember(datePickerInitial) { mutableLongStateOf(datePickerInitial) }
    var enabled by remember { mutableStateOf(true) }
    val invalidValue = remember { localDateMillis(2035, 1, 1, timeZone) }
    val suffixes = remember(mode) {
        when (mode) {
            DatePickerMode.YMD -> listOf("年", "月", "日")
            DatePickerMode.YM -> listOf("年", "月")
            DatePickerMode.Y -> listOf("年")
            DatePickerMode.MD -> listOf("月", "日")
            DatePickerMode.M -> listOf("月")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        NavBar(title = "DatePicker 日期选择", onClickBack = onBack)
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "日期模式",
                modifier = Modifier.padding(start = 16.dp, top = 12.dp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                DatePickerMode.entries.forEach { item ->
                    TextButton(onClick = { mode = item }) {
                        Text(
                            text = item.name,
                            color = if (mode == item) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "${mode.name} 日期滚轮" }
            ) {
                DatePicker(
                    value = value,
                    onValueChange = { value = it },
                    start = datePickerStart,
                    endInclude = datePickerEnd,
                    mode = mode,
                    itemText = { index, item -> "$item${suffixes[index]}" },
                    enabled = enabled,
                    modifier = Modifier.semantics { contentDescription = "${mode.name} 日期滚轮" }
                )
            }

            val effectiveValue = value.coerceIn(datePickerStart, datePickerEnd)
            val datetime = Instant.fromEpochMilliseconds(effectiveValue).toLocalDateTime(timeZone)
            Text(
                text = "当前：${datetime.year}-${datetime.month.number.toString().padStart(2, '0')}-${datetime.day.toString().padStart(2, '0')}",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "时间戳：$effectiveValue（原始输入：$value）；时区：${timeZone.id}",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { value = datePickerStart },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("起始边界")
                }
                Button(
                    onClick = { value = datePickerLeapDay },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("闰年 2/29")
                }
                Button(
                    onClick = { value = datePickerEnd },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("结束边界")
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { enabled = !enabled },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (enabled) "禁用滚轮" else "启用滚轮")
                }
                Button(
                    onClick = { value = invalidValue },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("非法值回退")
                }
            }
            Text(
                text = "状态：${if (enabled) "可操作" else "禁用"}；超出范围的 value 会先回退到合法边界",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "范围：2000-01-01 至 2026-12-31；切换年月会联动有效日期",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "官方 Material3 DatePicker：月历网格、单日期/范围选择、主题和无障碍更完整；本组件：YMD/YM/Y/MD/M 滚轮、多列联动和自定义文案，暂不互相替代",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun localDateMillis(year: Int, month: Int, day: Int, timeZone: TimeZone): Long =
    LocalDate(year, month, day).atStartOfDayIn(timeZone).toEpochMilliseconds()
