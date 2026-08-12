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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.navigation.NavBar

@Composable
fun MaterialAlertDialogScreen(onBack: () -> Unit = {}) {
    var visibleDialog by remember { mutableStateOf<DialogExample?>(null) }
    var resultText by remember { mutableStateOf("尚未打开弹窗") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        NavBar(title = "Material AlertDialog", onClickBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            DialogSection("基础操作") {
                Button(
                    onClick = { visibleDialog = DialogExample.BASIC },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Info, contentDescription = null)
                    Text("打开确认弹窗", modifier = Modifier.padding(start = 8.dp))
                }
            }

            DialogSection("危险操作与长文本") {
                FilledTonalButton(
                    onClick = { visibleDialog = DialogExample.DESTRUCTIVE },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Text("打开删除确认", modifier = Modifier.padding(start = 8.dp))
                }
            }

            DialogSection("自定义外观") {
                OutlinedButton(
                    onClick = { visibleDialog = DialogExample.CUSTOM },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Palette, contentDescription = null)
                    Text("打开主题色弹窗", modifier = Modifier.padding(start = 8.dp))
                }
            }

            DialogSection("最近结果") {
                Text(
                    text = resultText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }

    visibleDialog?.let { example ->
        ExampleAlertDialog(
            example = example,
            onDismissRequest = {
                visibleDialog = null
                resultText = "通过返回键或外部点击关闭"
            },
            onCancel = {
                visibleDialog = null
                resultText = "已点击取消"
            },
            onConfirm = {
                visibleDialog = null
                resultText = when (example) {
                    DialogExample.BASIC -> "已确认继续"
                    DialogExample.DESTRUCTIVE -> "已确认删除"
                    DialogExample.CUSTOM -> "已确认主题设置"
                }
            }
        )
    }
}

@Composable
private fun ExampleAlertDialog(
    example: DialogExample,
    onDismissRequest: () -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    val title = when (example) {
        DialogExample.BASIC -> "继续操作？"
        DialogExample.DESTRUCTIVE -> "删除本地数据？"
        DialogExample.CUSTOM -> "应用主题设置？"
    }
    val message = when (example) {
        DialogExample.BASIC -> "确认后将进入下一步流程。"
        DialogExample.DESTRUCTIVE ->
            "删除后，本设备上的缓存、离线文件和未同步草稿都将被永久移除，此操作无法撤销。"
        DialogExample.CUSTOM -> "该示例使用第三主题色容器、图标插槽和自定义内容色。"
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        icon = {
            Icon(
                imageVector = when (example) {
                    DialogExample.BASIC -> Icons.Default.Info
                    DialogExample.DESTRUCTIVE -> Icons.Default.Delete
                    DialogExample.CUSTOM -> Icons.Default.Palette
                },
                contentDescription = null
            )
        },
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(if (example == DialogExample.DESTRUCTIVE) "删除" else "确认")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("取消")
            }
        },
        containerColor = if (example == DialogExample.CUSTOM) {
            MaterialTheme.colorScheme.tertiaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerHigh
        },
        iconContentColor = if (example == DialogExample.DESTRUCTIVE) {
            MaterialTheme.colorScheme.error
        } else if (example == DialogExample.CUSTOM) {
            MaterialTheme.colorScheme.tertiary
        } else {
            MaterialTheme.colorScheme.primary
        },
        titleContentColor = when (example) {
            DialogExample.BASIC -> MaterialTheme.colorScheme.onSurface
            DialogExample.DESTRUCTIVE -> MaterialTheme.colorScheme.error
            DialogExample.CUSTOM -> MaterialTheme.colorScheme.onTertiaryContainer
        },
        textContentColor = if (example == DialogExample.CUSTOM) {
            MaterialTheme.colorScheme.onTertiaryContainer
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
    )
}

@Composable
private fun DialogSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        content()
    }
}

private enum class DialogExample {
    BASIC,
    DESTRUCTIVE,
    CUSTOM
}
