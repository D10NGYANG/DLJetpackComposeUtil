package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.form.MultiPicker
import com.d10ng.compose.ui.form.Picker
import com.d10ng.compose.ui.navigation.NavBar

private val singlePickerItems = (1..12).toCollection(LinkedHashSet())
private val multiPickerItems = listOf(
    linkedSetOf("广东省", "江苏省", "浙江省"),
    linkedSetOf("深圳市", "广州市", "南京市", "杭州市"),
    linkedSetOf("南山区", "福田区", "天河区", "鼓楼区")
)

@Composable
fun PickerScreen(onBack: () -> Unit = {}) {
    var singleSelection by remember { mutableStateOf(6) }
    var multiSelection by remember {
        mutableStateOf(listOf("广东省", "深圳市", "南山区"))
    }
    var pickerEnabled by remember { mutableStateOf(true) }
    var invalidSelection by remember { mutableStateOf("不存在的选项") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        NavBar(title = "Picker 滚轮选择", onClickBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            PickerSection("单列滚轮、点击与吸附") {
                Picker(
                    items = singlePickerItems,
                    selectedItem = singleSelection,
                    itemText = { "第 $it 项" },
                    textStyle = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    ),
                    onValueChange = { singleSelection = it }
                )
                PickerStatus("当前选中：第 $singleSelection 项")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            singleSelection = (singleSelection - 1).coerceAtLeast(1)
                        },
                        enabled = singleSelection > 1,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("上一项")
                    }
                    Button(
                        onClick = {
                            singleSelection = (singleSelection + 1).coerceAtMost(12)
                        },
                        enabled = singleSelection < 12,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("下一项")
                    }
                }
            }

            PickerSection("多列滚轮与自定义文本") {
                Column(
                    modifier = Modifier.semantics {
                        contentDescription = "省市区多列滚轮"
                    }
                ) {
                    MultiPicker(
                        items = multiPickerItems,
                        selectedItems = multiSelection,
                    itemText = { index, item ->
                            when (index) {
                                0 -> "省：$item"
                                1 -> "市：$item"
                                else -> "区：$item"
                        }
                    },
                    columnContentDescription = { index ->
                        when (index) {
                            0 -> "省份滚轮"
                            1 -> "城市滚轮"
                            else -> "区域滚轮"
                        }
                    },
                    onValueChange = { multiSelection = it }
                    )
                }
                PickerStatus("当前组合：${multiSelection.joinToString(" / ")}")
                Button(
                    onClick = {
                        multiSelection = listOf("浙江省", "杭州市", "鼓楼区")
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text("外部设置组合")
                }
            }

            PickerSection("输入契约与无障碍") {
                Button(
                    onClick = { pickerEnabled = !pickerEnabled },
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(if (pickerEnabled) "禁用单列 Picker" else "启用单列 Picker")
                }
                Picker(
                    items = singlePickerItems,
                    selectedItem = singleSelection,
                    enabled = pickerEnabled,
                    accessibilityDescription = "可操作的月份滚轮",
                    itemText = { "第 $it 项" },
                    onValueChange = { singleSelection = it }
                )
                PickerStatus("禁用状态：${if (pickerEnabled) "可操作" else "不可操作"}；读屏可执行上一项/下一项")
                Picker(
                    items = linkedSetOf("有效选项", "第二选项"),
                    selectedItem = invalidSelection,
                    accessibilityDescription = "非法选中值回退示例",
                    onValueChange = { invalidSelection = it }
                )
                PickerStatus("非法 selectedItem 会回退到第一项：$invalidSelection")
                Picker(
                    items = emptySet<String>(),
                    enabled = false,
                    accessibilityDescription = "空集合 Picker",
                    onValueChange = {}
                )
                PickerStatus("空集合显示稳定空态，不调用 first()；enabled=false 时保持不可操作")
                MultiPicker(
                    items = listOf(linkedSetOf("A", "B"), emptySet(), linkedSetOf("1", "2")),
                    selectedItems = listOf("非法值"),
                    enabled = pickerEnabled,
                    columnContentDescription = { "契约测试第 ${it + 1} 列" },
                    onValueChange = {}
                )
                PickerStatus("MultiPicker 会按列回退非法/缺失选中值，并安全渲染空列")
            }

            PickerSection("输入约束") {
                PickerStatus("空列显示空态；缺失或非法 selectedItems 会按列回退")
                PickerStatus("API 支持 enabled、方向键和读屏上一项/下一项动作")
            }
        }
    }
}

@Composable
private fun PickerSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        content()
    }
}

@Composable
private fun PickerStatus(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 16.dp),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
