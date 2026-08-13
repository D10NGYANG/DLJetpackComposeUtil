package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.form.TimePicker
import com.d10ng.compose.ui.form.TimePickerMode
import com.d10ng.compose.ui.navigation.NavBar

private const val timePickerStart = 8 * 3600 + 15 * 60
private const val timePickerEnd = 20 * 3600 + 45 * 60 + 59
private const val timePickerInitial = 14 * 3600 + 30 * 60 + 45

@Composable
fun TimePickerScreen(onBack: () -> Unit = {}) {
    var mode by remember { mutableStateOf(TimePickerMode.HMS) }
    var value by remember { mutableIntStateOf(timePickerInitial) }
    var enabled by remember { mutableStateOf(true) }
    val effectiveValue = value.coerceIn(timePickerStart, timePickerEnd)
    var callbackParts by remember { mutableStateOf(mode.getDateList(value)) }
    val suffixes = remember(mode) {
        when (mode) {
            TimePickerMode.HMS -> listOf("时", "分", "秒")
            TimePickerMode.HM -> listOf("时", "分")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        NavBar(title = "TimePicker 时间选择", onClickBack = onBack)
        Text(
            text = "时间模式",
            modifier = Modifier.padding(start = 16.dp, top = 12.dp),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            TimePickerMode.entries.forEach { item ->
                FilterChip(
                    selected = mode == item,
                    modifier = Modifier.semantics {
                        contentDescription = if (mode == item) {
                            "${item.name} 模式，已选中"
                        } else {
                            "${item.name} 模式，未选中"
                        }
                    },
                    onClick = {
                        val normalizedValue = if (item == TimePickerMode.HM) {
                            value / 60 * 60
                        } else {
                            value
                        }
                        mode = item
                        value = normalizedValue
                        callbackParts = item.getDateList(normalizedValue)
                    },
                    label = {
                        Text(if (item == TimePickerMode.HMS) "HMS 时分秒" else "HM 时分")
                    }
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription =
                        "${mode.name} 时间滚轮，当前 ${formatTime(effectiveValue, mode)}"
                }
        ) {
            TimePicker(
                value = value,
                onValueChange = { time, parts ->
                    value = time
                    callbackParts = parts
                },
                start = timePickerStart,
                endInclude = timePickerEnd,
                mode = mode,
                itemText = { index, item -> "$item${suffixes[index]}" },
                enabled = enabled,
                modifier = Modifier.semantics { contentDescription = "${mode.name} 时间滚轮" }
            )
        }

        Text(
            text = "当前：${formatTime(effectiveValue, mode)}（有效 $effectiveValue 秒，原始 $value 秒）",
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .semantics { liveRegion = LiveRegionMode.Polite },
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "回调列值：${callbackParts.joinToString(prefix = "[", postfix = "]")}",
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
            TimePresetButton("起始", timePickerStart, mode) { time, parts ->
                value = time
                callbackParts = parts
            }
            TimePresetButton("中间值", timePickerInitial, mode) { time, parts ->
                value = time
                callbackParts = parts
            }
            TimePresetButton("结束", timePickerEnd, mode) { time, parts ->
                value = time
                callbackParts = parts
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
                onClick = { value = 99 * 3600 },
                modifier = Modifier.weight(1f)
            ) {
                Text("非法值回退")
            }
        }
        Text(
            text = "范围：08:15:00 至 20:45:59；边界小时会联动分钟和秒，HM 回调不包含秒列",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "官方 Material3 TimePicker：普通 HM 时分选择和主题/无障碍更完整；本组件：HMS 秒列、精确起止秒范围和滚轮联动，暂不互相替代",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun RowScope.TimePresetButton(
    text: String,
    time: Int,
    mode: TimePickerMode,
    onClick: (Int, List<Int>) -> Unit
) {
    val normalizedTime = if (mode == TimePickerMode.HM) time / 60 * 60 else time
    Button(
        onClick = { onClick(normalizedTime, mode.getDateList(normalizedTime)) },
        modifier = Modifier.weight(1f)
    ) {
        Text(text)
    }
}

private fun formatTime(value: Int, mode: TimePickerMode): String {
    val parts = listOf(value / 3600, value / 60 % 60, value % 60)
    return parts
        .take(if (mode == TimePickerMode.HMS) 3 else 2)
        .joinToString(":") { it.toString().padStart(2, '0') }
}
