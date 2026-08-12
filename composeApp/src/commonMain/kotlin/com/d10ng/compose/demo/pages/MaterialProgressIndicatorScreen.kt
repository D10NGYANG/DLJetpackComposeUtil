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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.navigation.NavBar
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun MaterialProgressIndicatorScreen(onBack: () -> Unit = {}) {
    var dynamicProgress by remember { mutableFloatStateOf(0.35f) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
        while (isRunning && dynamicProgress < 1f) {
            delay(100)
            dynamicProgress = (dynamicProgress + 0.01f).coerceAtMost(1f)
        }
        if (dynamicProgress >= 1f) {
            isRunning = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        NavBar(title = "Material ProgressIndicator", onClickBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ProgressSection("确定进度") {
                LinearProgressIndicator(
                    progress = { 0.68f },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("线性进度 68%", style = MaterialTheme.typography.bodySmall)
                    CircularProgressIndicator(
                        progress = { 0.42f },
                        modifier = Modifier.size(48.dp)
                    )
                    Text("环形进度 42%", style = MaterialTheme.typography.bodySmall)
                }
            }

            ProgressSection("不确定进度") {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(40.dp))
                    Text("用于无法预估完成时间的任务")
                }
            }

            ProgressSection("动态进度") {
                LinearProgressIndicator(
                    progress = { dynamicProgress },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("当前：${(dynamicProgress * 100).roundToInt()}%")
                    Row {
                        IconButton(
                            onClick = { isRunning = !isRunning },
                            enabled = dynamicProgress < 1f
                        ) {
                            Icon(
                                imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isRunning) "暂停进度" else "开始进度"
                            )
                        }
                        IconButton(
                            onClick = {
                                isRunning = false
                                dynamicProgress = 0f
                            }
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "重置进度")
                        }
                    }
                }
            }

            ProgressSection("自定义样式") {
                LinearProgressIndicator(
                    progress = { 0.75f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp),
                    color = MaterialTheme.colorScheme.tertiary,
                    trackColor = MaterialTheme.colorScheme.tertiaryContainer,
                    strokeCap = StrokeCap.Round
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(
                        progress = { 0.75f },
                        modifier = Modifier.size(56.dp),
                        color = MaterialTheme.colorScheme.tertiary,
                        trackColor = MaterialTheme.colorScheme.tertiaryContainer,
                        strokeWidth = 8.dp,
                        strokeCap = StrokeCap.Round
                    )
                    Text("第三主题色、圆角线帽与加粗轨道")
                }
            }
        }
    }
}

@Composable
private fun ProgressSection(
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
