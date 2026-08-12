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
import androidx.compose.foundation.selection.selectable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.navigation.NavBar

private data class RadioOption(
    val id: String,
    val label: String,
    val enabled: Boolean = true
)

@Composable
fun MaterialRadioButtonScreen(onBack: () -> Unit = {}) {
    val options = remember {
        listOf(
            RadioOption("standard", "标准选项"),
            RadioOption("long", "带有较长说明文字，用于检查窄屏换行和整行点击区域"),
            RadioOption("disabled", "禁用选项", enabled = false)
        )
    }
    var selectedId by remember { mutableStateOf(options.first().id) }
    var compactSelection by remember { mutableStateOf("A") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        NavBar(title = "Material RadioButton", onClickBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            RadioSection("互斥选择组") {
                Column(Modifier.selectableGroup()) {
                    options.forEach { option ->
                        RadioOptionRow(
                            label = option.label,
                            selected = selectedId == option.id,
                            enabled = option.enabled,
                            onClick = { selectedId = option.id }
                        )
                    }
                }
                Text(
                    text = "当前选择：${options.first { it.id == selectedId }.label}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            RadioSection("紧凑排列") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectableGroup(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    listOf("A", "B", "C").forEach { option ->
                        Row(
                            modifier = Modifier.selectable(
                                selected = compactSelection == option,
                                role = Role.RadioButton,
                                onClick = { compactSelection = option }
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = compactSelection == option,
                                onClick = null
                            )
                            Text(option)
                        }
                    }
                }
            }

            RadioSection("自定义颜色") {
                RadioButton(
                    selected = true,
                    onClick = {},
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.tertiary
                    )
                )
            }
        }
    }
}

@Composable
private fun RadioSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        content()
    }
}

@Composable
private fun RadioOptionRow(
    label: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                enabled = enabled,
                role = Role.RadioButton,
                onClick = onClick
            )
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            enabled = enabled
        )
        Text(
            text = label,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            color = if (enabled) {
                MaterialTheme.colorScheme.onBackground
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            }
        )
    }
}
