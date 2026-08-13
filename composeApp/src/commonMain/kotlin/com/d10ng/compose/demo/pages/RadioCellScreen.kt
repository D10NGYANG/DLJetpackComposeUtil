package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.form.RadioCell
import com.d10ng.compose.ui.base.Cell
import com.d10ng.compose.ui.navigation.NavBar

@Composable
fun RadioCellScreen(onBack: () -> Unit = {}) {
    var selectedPlan by remember { mutableStateOf("standard") }
    var notificationMode by remember { mutableStateOf("important") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        NavBar(title = "RadioCell 单选行", onClickBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            RadioCellSection("互斥选择与整行点击") {
                Column(Modifier.selectableGroup()) {
                    RadioCell(
                        label = "基础版",
                        selected = selectedPlan == "basic",
                        onClick = { selectedPlan = "basic" }
                    )
                    RadioCell(
                        label = "标准版",
                        selected = selectedPlan == "standard",
                        onClick = { selectedPlan = "standard" }
                    )
                    RadioCell(
                        label = "专业版",
                        selected = selectedPlan == "pro",
                        border = false,
                        onClick = { selectedPlan = "pro" }
                    )
                }
                RadioCellStatus("当前方案：$selectedPlan")
            }

            RadioCellSection("禁用状态") {
                Column(Modifier.selectableGroup()) {
                    RadioCell(
                        label = "禁用且未选中",
                        selected = false,
                        disabled = true
                    )
                    RadioCell(
                        label = "禁用且已选中",
                        selected = true,
                        disabled = true,
                        border = false
                    )
                }
                RadioCellStatus("点击禁用行不会改变状态")
            }

            RadioCellSection("边框与长文本") {
                Column(Modifier.selectableGroup()) {
                    RadioCell(
                        label = "仅接收重要通知",
                        selected = notificationMode == "important",
                        onClick = { notificationMode = "important" }
                    )
                    RadioCell(
                        label = "接收所有通知，包括用于检查 390px 窄屏换行表现的较长选项说明",
                        selected = notificationMode == "all",
                        border = false,
                        onClick = { notificationMode = "all" }
                    )
                }
                RadioCellStatus("最后一行 border = false；当前模式：$notificationMode")
            }

            RadioCellSection("Cell 尾随槽对照") {
                Cell(
                    title = "Cell + 官方 RadioButton",
                    label = "适合已有 Cell 表单，但需由父行维护选择语义",
                    value = "尾随槽",
                    onClick = { notificationMode = "cell" },
                    afterValue = {
                        RadioButton(
                            selected = notificationMode == "cell",
                            onClick = null,
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                )
                RadioCellStatus("RadioCell 保留独立 Role.RadioButton；Cell 尾随槽依赖父行点击")
            }
        }
    }
}

@Composable
private fun RadioCellSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        content()
    }
}

@Composable
private fun RadioCellStatus(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
