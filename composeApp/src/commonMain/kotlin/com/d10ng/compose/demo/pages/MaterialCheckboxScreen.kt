package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.selection.triStateToggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.navigation.NavBar

@Composable
fun MaterialCheckboxScreen(onBack: () -> Unit = {}) {
    var checked by remember { mutableStateOf(false) }
    var rowChecked by remember { mutableStateOf(true) }
    var triState by remember { mutableStateOf(ToggleableState.Indeterminate) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        NavBar(title = "Material Checkbox", onClickBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CheckboxSection("交互状态") {
                CheckboxRow(
                    label = if (checked) "已选中" else "未选中",
                    checked = checked,
                    onCheckedChange = { checked = it }
                )
            }

            CheckboxSection("禁用状态") {
                CheckboxRow(label = "禁用未选中", checked = false, enabled = false)
                CheckboxRow(label = "禁用已选中", checked = true, enabled = false)
            }

            CheckboxSection("三态选择") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .triStateToggleable(
                            state = triState,
                            role = Role.Checkbox,
                            onClick = { triState = triState.next() }
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TriStateCheckbox(
                        state = triState,
                        onClick = null
                    )
                    Text(
                        text = when (triState) {
                            ToggleableState.Off -> "未选中"
                            ToggleableState.On -> "已选中"
                            ToggleableState.Indeterminate -> "部分选中"
                        },
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }

            CheckboxSection("整行点击") {
                CheckboxRow(
                    label = "点击文字或控件均可切换",
                    checked = rowChecked,
                    onCheckedChange = { rowChecked = it }
                )
            }

            CheckboxSection("自定义颜色") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = true,
                        onCheckedChange = {},
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.tertiary
                        )
                    )
                    Checkbox(
                        checked = false,
                        onCheckedChange = {},
                        colors = CheckboxDefaults.colors(
                            uncheckedColor = MaterialTheme.colorScheme.error
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun CheckboxSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
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
private fun CheckboxRow(
    label: String,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Checkbox,
                onValueChange = onCheckedChange
            )
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null,
            enabled = enabled
        )
        Text(
            text = label,
            modifier = Modifier.padding(start = 12.dp),
            color = if (enabled) {
                MaterialTheme.colorScheme.onBackground
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            }
        )
    }
}

private fun ToggleableState.next() = when (this) {
    ToggleableState.Off -> ToggleableState.Indeterminate
    ToggleableState.Indeterminate -> ToggleableState.On
    ToggleableState.On -> ToggleableState.Off
}
