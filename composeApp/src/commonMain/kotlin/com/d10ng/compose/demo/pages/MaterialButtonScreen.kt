package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.navigation.NavBar

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MaterialButtonScreen(onBack: () -> Unit = {}) {
    var clickCount by remember { mutableIntStateOf(0) }
    var lastAction by remember { mutableStateOf("尚未点击") }
    var loading by remember { mutableStateOf(true) }
    val onAction: (String) -> Unit = { action ->
        clickCount += 1
        lastAction = "$action · $clickCount 次"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        NavBar(title = "Material Button", onClickBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ButtonSection("按钮变体") {
                ButtonFlow {
                    Button(onClick = { onAction("Filled") }) { Text("Filled") }
                    FilledTonalButton(onClick = { onAction("Tonal") }) { Text("Tonal") }
                    ElevatedButton(onClick = { onAction("Elevated") }) { Text("Elevated") }
                    OutlinedButton(onClick = { onAction("Outlined") }) { Text("Outlined") }
                    TextButton(onClick = { onAction("Text") }) { Text("Text") }
                }
                Text(
                    text = lastAction,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            ButtonSection("可用状态") {
                ButtonFlow {
                    Button(onClick = { onAction("Enabled") }) { Text("Enabled") }
                    Button(onClick = {}, enabled = false) { Text("Disabled") }
                    OutlinedButton(onClick = {}, enabled = false) { Text("Disabled") }
                }
            }

            ButtonSection("加载状态") {
                Button(
                    onClick = {},
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(Modifier.size(10.dp))
                        Text("处理中")
                    } else {
                        Text("处理完成")
                    }
                }
                TextButton(onClick = { loading = !loading }) {
                    Text(if (loading) "切换为完成" else "重新加载")
                }
            }

            ButtonSection("图标与内容") {
                ButtonFlow {
                    Button(onClick = { onAction("新建") }) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(Modifier.size(8.dp))
                        Text("新建")
                    }
                    OutlinedButton(onClick = { onAction("收藏") }) {
                        Icon(Icons.Default.Favorite, contentDescription = null)
                        Spacer(Modifier.size(8.dp))
                        Text("收藏")
                    }
                }
            }

            ButtonSection("宽度与长文本") {
                FilledTonalButton(
                    onClick = { onAction("全宽") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("填满可用宽度")
                }
                OutlinedButton(
                    onClick = { onAction("长文本") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("这是一段用于检查窄屏换行和字体放大效果的按钮文字")
                }
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ButtonSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        content()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ButtonFlow(content: @Composable () -> Unit) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        content()
    }
}
