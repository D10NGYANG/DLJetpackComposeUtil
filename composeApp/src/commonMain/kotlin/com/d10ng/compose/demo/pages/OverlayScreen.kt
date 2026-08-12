package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.feedback.Overlay
import com.d10ng.compose.ui.navigation.NavBar

private enum class OverlayAlignment(val label: String, val alignment: Alignment) {
    CENTER("居中", Alignment.Center),
    BOTTOM("底部", Alignment.BottomCenter)
}

@Composable
fun OverlayScreen(onBack: () -> Unit = {}) {
    var visible by remember { mutableStateOf(false) }
    var alignment by remember { mutableStateOf(OverlayAlignment.CENTER) }
    var dismissCount by remember { mutableIntStateOf(0) }
    var contentClickCount by remember { mutableIntStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .navigationBarsPadding()
        ) {
            NavBar(title = "Overlay 遮罩", onClickBack = onBack)
            OverlaySection("内容对齐") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OverlayAlignment.entries.forEach { item ->
                        Button(
                            onClick = {
                                alignment = item
                                visible = true
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("${item.label}遮罩")
                        }
                    }
                }
            }
            OverlaySection("关闭与事件") {
                Text(
                    text = "遮罩关闭：$dismissCount 次",
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "内容点击：$contentClickCount 次",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "点击遮罩或系统返回会关闭；内容按钮只更新计数",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            OverlaySection("组件边界") {
                Text(
                    text = "Overlay 是 Dialog 的模态基础设施；普通弹窗优先使用 Material 3 Dialog API",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (visible) {
            Overlay(
                onDismiss = {
                    visible = false
                    dismissCount += 1
                },
                contentAlignment = alignment.alignment
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .semantics {
                            contentDescription = "${alignment.label}遮罩内容"
                        },
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${alignment.label}内容",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "遮罩覆盖背景并拦截后方交互",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Button(onClick = { contentClickCount += 1 }) {
                            Text("点击内容")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OverlaySection(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
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
