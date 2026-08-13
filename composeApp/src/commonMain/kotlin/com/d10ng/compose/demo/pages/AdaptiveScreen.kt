package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.navigation.NavBar

private enum class AdaptiveMode(val label: String, val description: String) {
    COMPACT("Compact", "窄屏单列，优先保证内容可读和触控空间"),
    MEDIUM("Medium", "中等宽度双列，平衡信息密度与留白"),
    EXPANDED("Expanded", "宽屏三列，适合列表、详情和辅助信息并排")
}

@Composable
fun AdaptiveScreen(onBack: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        NavBar(title = "Adaptive 自适应布局", onClickBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "公共 Compose 断点验证",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "使用 BoxWithConstraints 读取可用宽度，不依赖 Android 专属窗口库。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val availableWidth = maxWidth
                val mode = when {
                    availableWidth < 600.dp -> AdaptiveMode.COMPACT
                    availableWidth < 840.dp -> AdaptiveMode.MEDIUM
                    else -> AdaptiveMode.EXPANDED
                }
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AdaptiveStatus(mode = mode, width = availableWidth)
                    AdaptiveContent(mode = mode)
                }
            }
        }
    }
}

@Composable
private fun AdaptiveStatus(mode: AdaptiveMode, width: Dp) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "当前断点：${mode.label}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "可用宽度：${width.value.toInt()}dp",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = mode.description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun AdaptiveContent(mode: AdaptiveMode) {
    when (mode) {
        AdaptiveMode.COMPACT -> Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            AdaptivePanel("列表", "单列内容区域")
            AdaptivePanel("详情", "跟随列表向下排列")
            AdaptivePanel("辅助信息", "窄屏下收纳在主流程之后")
        }
        AdaptiveMode.MEDIUM -> Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AdaptivePanel("列表", "左侧主区域", Modifier.weight(1f))
            AdaptivePanel("详情", "右侧上下文", Modifier.weight(1f))
        }
        AdaptiveMode.EXPANDED -> Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AdaptivePanel("列表", "导航区域", Modifier.weight(1f))
            AdaptivePanel("详情", "主要内容", Modifier.weight(2f))
            AdaptivePanel("辅助信息", "上下文区域", Modifier.weight(1f))
        }
    }
}

@Composable
private fun AdaptivePanel(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title.take(1),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
