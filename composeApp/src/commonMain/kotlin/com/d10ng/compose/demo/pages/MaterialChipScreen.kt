package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.navigation.NavBar

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MaterialChipScreen(onBack: () -> Unit = {}) {
    var assistMessage by remember { mutableStateOf("尚未操作") }
    var selectedFilter by remember { mutableStateOf("全部") }
    var inputVisible by remember { mutableStateOf(true) }
    var suggestionSelected by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        NavBar(title = "Material Chip", onClickBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ChipSection("Assist 与 ElevatedAssist") {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AssistChip(
                        onClick = { assistMessage = "点击了设置" },
                        label = { Text("设置") },
                        leadingIcon = {
                            Icon(Icons.Default.Settings, contentDescription = null)
                        }
                    )
                    ElevatedAssistChip(
                        onClick = { assistMessage = "点击了新建" },
                        label = { Text("新建") },
                        leadingIcon = {
                            Icon(Icons.Default.Add, contentDescription = null)
                        }
                    )
                    AssistChip(
                        onClick = {},
                        label = { Text("禁用") },
                        enabled = false
                    )
                }
                Text(
                    text = assistMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            ChipSection("Filter 互斥选择") {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("全部", "进行中", "已完成").forEach { option ->
                        val selected = selectedFilter == option
                        FilterChip(
                            selected = selected,
                            onClick = { selectedFilter = option },
                            label = { Text(option) },
                            leadingIcon = if (selected) {
                                {
                                    Icon(Icons.Default.Check, contentDescription = null)
                                }
                            } else {
                                null
                            }
                        )
                    }
                    FilterChip(
                        selected = false,
                        onClick = {},
                        label = { Text("禁用筛选") },
                        enabled = false
                    )
                }
                Text(
                    text = "当前筛选：$selectedFilter",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            ChipSection("Input 删除交互") {
                if (inputVisible) {
                    InputChip(
                        selected = true,
                        onClick = { inputVisible = false },
                        label = { Text("Compose") },
                        trailingIcon = {
                            Icon(Icons.Default.Close, contentDescription = "删除 Compose 标签")
                        }
                    )
                } else {
                    Text("标签已删除")
                }
                TextButton(onClick = { inputVisible = true }) {
                    Text("恢复标签")
                }
            }

            ChipSection("Suggestion 与长文本") {
                SuggestionChip(
                    onClick = { suggestionSelected = !suggestionSelected },
                    label = {
                        Text(if (suggestionSelected) "已采用建议" else "采用建议")
                    }
                )
                SuggestionChip(
                    onClick = { suggestionSelected = true },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = "这是用于检查窄屏布局和较长标签内容的建议操作",
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun ChipSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        content()
    }
}
