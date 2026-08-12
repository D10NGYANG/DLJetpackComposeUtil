package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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

@Composable
fun MaterialSwitchScreen(onBack: () -> Unit = {}) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var basicUnchecked by remember { mutableStateOf(false) }
    var basicChecked by remember { mutableStateOf(true) }
    var iconChecked by remember { mutableStateOf(true) }
    var customChecked by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        NavBar(title = "Material Switch", onClickBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            SwitchSection("整行切换") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .toggleable(
                            value = notificationsEnabled,
                            role = Role.Switch,
                            onValueChange = { notificationsEnabled = it }
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("接收重要通知与较长的状态说明文字")
                        Text(
                            text = if (notificationsEnabled) "当前状态：已开启" else "当前状态：已关闭",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = null
                    )
                }
            }

            SwitchSection("基础与禁用状态") {
                SwitchExampleRow(
                    label = "未选中",
                    checked = basicUnchecked,
                    onCheckedChange = { basicUnchecked = it }
                )
                SwitchExampleRow(
                    label = "已选中",
                    checked = basicChecked,
                    onCheckedChange = { basicChecked = it }
                )
                SwitchExampleRow("禁用且关闭", checked = false, enabled = false)
                SwitchExampleRow("禁用且开启", checked = true, enabled = false)
            }

            SwitchSection("拇指图标") {
                SwitchExampleRow(
                    label = if (iconChecked) "图标状态：开启" else "图标状态：关闭",
                    checked = iconChecked,
                    onCheckedChange = { iconChecked = it },
                    thumbContent = {
                        Icon(
                            imageVector = if (iconChecked) Icons.Default.Check else Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }

            SwitchSection("自定义颜色") {
                SwitchExampleRow(
                    label = "使用主题第三色",
                    checked = customChecked,
                    onCheckedChange = { customChecked = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.onTertiary,
                        checkedTrackColor = MaterialTheme.colorScheme.tertiary,
                        uncheckedBorderColor = MaterialTheme.colorScheme.tertiary
                    )
                )
            }
        }
    }
}

@Composable
private fun SwitchSection(
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
private fun SwitchExampleRow(
    label: String,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    colors: androidx.compose.material3.SwitchColors = SwitchDefaults.colors(),
    thumbContent: (@Composable (() -> Unit))? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = if (enabled) {
                MaterialTheme.colorScheme.onBackground
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            }
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = colors,
            thumbContent = thumbContent
        )
    }
}
