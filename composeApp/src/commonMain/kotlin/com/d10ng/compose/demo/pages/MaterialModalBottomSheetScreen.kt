package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.navigation.NavBar
import kotlinx.coroutines.launch

@Composable
fun MaterialModalBottomSheetScreen(onBack: () -> Unit = {}) {
    var example by remember { mutableStateOf<SheetExample?>(null) }
    var resultText by remember { mutableStateOf("尚未打开底部面板") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        NavBar(title = "Material BottomSheet", onClickBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SheetSection("展开模式") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { example = SheetExample.PARTIAL },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.ExpandMore, contentDescription = null)
                        Text("部分展开", modifier = Modifier.padding(start = 8.dp))
                    }
                    FilledTonalButton(
                        onClick = { example = SheetExample.FULL },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.ExpandLess, contentDescription = null)
                        Text("完全展开", modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
            SheetSection("自定义外观") {
                OutlinedButton(
                    onClick = { example = SheetExample.CUSTOM },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Palette, contentDescription = null)
                    Text("打开主题色面板", modifier = Modifier.padding(start = 8.dp))
                }
            }
            SheetSection("最近结果") {
                Text(resultText, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }

    example?.let { selected ->
        ExampleModalBottomSheet(
            example = selected,
            onDismissRequest = {
                example = null
                resultText = "通过返回键、遮罩或下滑关闭"
            },
            onClose = {
                example = null
                resultText = "已点击面板内关闭按钮"
            }
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ExampleModalBottomSheet(
    example: SheetExample,
    onDismissRequest: () -> Unit,
    onClose: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = example != SheetExample.PARTIAL
    )
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = if (example == SheetExample.CUSTOM) {
            MaterialTheme.colorScheme.tertiaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerLow
        },
        contentColor = if (example == SheetExample.CUSTOM) {
            MaterialTheme.colorScheme.onTertiaryContainer
        } else {
            MaterialTheme.colorScheme.onSurface
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(560.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = when (example) {
                    SheetExample.PARTIAL -> "可部分展开面板"
                    SheetExample.FULL -> "直接完全展开面板"
                    SheetExample.CUSTOM -> "第三主题色面板"
                },
                style = MaterialTheme.typography.headlineSmall
            )
            Text("拖动顶部把手，或使用下方按钮切换面板状态。")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (example == SheetExample.PARTIAL) {
                    FilledTonalButton(
                        onClick = { scope.launch { sheetState.partialExpand() } },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("部分展开")
                    }
                }
                Button(
                    onClick = { scope.launch { sheetState.expand() } },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("完全展开")
                }
            }
            HorizontalDivider()
            repeat(14) { index ->
                Text("内容项 ${index + 1}：用于验证面板内部滚动与长内容布局")
            }
            TextButton(
                onClick = {
                    scope.launch {
                        sheetState.hide()
                        onClose()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("关闭面板")
            }
        }
    }
}

@Composable
private fun SheetSection(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        content()
    }
}

private enum class SheetExample {
    PARTIAL,
    FULL,
    CUSTOM
}
