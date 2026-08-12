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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
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
fun MaterialSnackbarScreen(onBack: () -> Unit = {}) {
    val hostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var resultText by remember { mutableStateOf("尚未触发消息") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        NavBar(title = "Material Snackbar", onClickBack = onBack)
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            snackbarHost = {
                SnackbarHost(hostState = hostState) { data ->
                    if (data.visuals is CustomSnackbarVisuals) {
                        Snackbar(
                            snackbarData = data,
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                            actionColor = MaterialTheme.colorScheme.tertiary
                        )
                    } else {
                        Snackbar(snackbarData = data)
                    }
                }
            }
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                SnackbarSection("基础与时长") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                scope.launch {
                                    resultText = hostState.showSnackbar(
                                        message = "操作已保存",
                                        duration = SnackbarDuration.Short
                                    ).toResultText()
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Schedule, contentDescription = null)
                            Text("短消息", modifier = Modifier.padding(start = 8.dp))
                        }
                        FilledTonalButton(
                            onClick = {
                                scope.launch {
                                    resultText = hostState.showSnackbar(
                                        message = "文件已删除",
                                        actionLabel = "撤销",
                                        withDismissAction = true,
                                        duration = SnackbarDuration.Indefinite
                                    ).toResultText()
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Restore, contentDescription = null)
                            Text("带操作", modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }

                SnackbarSection("队列与关闭") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilledTonalButton(
                            onClick = {
                                scope.launch {
                                    hostState.showSnackbar("队列 1/2：正在同步")
                                    resultText = hostState.showSnackbar("队列 2/2：同步完成").toResultText()
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.FormatListNumbered, contentDescription = null)
                            Text("顺序展示", modifier = Modifier.padding(start = 8.dp))
                        }
                        OutlinedButton(
                            onClick = {
                                hostState.currentSnackbarData?.dismiss()
                                resultText = "已请求关闭当前消息"
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null)
                            Text("关闭当前", modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }

                SnackbarSection("自定义样式") {
                    Button(
                        onClick = {
                            scope.launch {
                                resultText = hostState.showSnackbar(CustomSnackbarVisuals()).toResultText()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Palette, contentDescription = null)
                        Text("显示自定义 Snackbar", modifier = Modifier.padding(start = 8.dp))
                    }
                }

                SnackbarSection("最近结果") {
                    Text(
                        text = resultText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun SnackbarSection(
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

private fun SnackbarResult.toResultText(): String = when (this) {
    SnackbarResult.ActionPerformed -> "操作按钮已执行"
    SnackbarResult.Dismissed -> "消息已关闭或超时"
}

private data class CustomSnackbarVisuals(
    override val message: String = "使用第三主题色的 Snackbar",
    override val actionLabel: String = "知道了",
    override val withDismissAction: Boolean = true,
    override val duration: SnackbarDuration = SnackbarDuration.Indefinite
) : SnackbarVisuals
