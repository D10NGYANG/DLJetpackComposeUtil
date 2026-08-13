package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.dashBorder
import com.d10ng.compose.ui.debounceClick
import com.d10ng.compose.ui.navigation.NavBar

@Composable
fun ModifierScreen(onBack: () -> Unit = {}) {
    var borderWidth by remember { mutableFloatStateOf(1f) }
    var dashLength by remember { mutableFloatStateOf(5f) }
    var cornerRadius by remember { mutableFloatStateOf(8f) }
    var borderColor by remember { mutableStateOf(Color(0xFF1989FA)) }
    var debounceDelay by remember { mutableLongStateOf(800L) }
    var clickCount by remember { mutableIntStateOf(0) }
    val errorColor = MaterialTheme.colorScheme.error

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        NavBar(title = "Modifier 扩展", onClickBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ModifierSection("dashBorder 虚线边框") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(96.dp)
                        .dashBorder(
                            color = borderColor,
                            width = borderWidth.dp,
                            dashLength = dashLength.dp,
                            cornerRadiusDp = cornerRadius.dp
                        )
                        .padding(16.dp),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text("边框 ${borderWidth.toInt()}dp / 间距 ${dashLength.toInt()}dp / 圆角 ${cornerRadius.toInt()}dp")
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = borderColor == Color(0xFF1989FA),
                        onClick = { borderColor = Color(0xFF1989FA) },
                        label = { Text("蓝色") }
                    )
                    FilterChip(
                        selected = borderColor == errorColor,
                        onClick = { borderColor = errorColor },
                        label = { Text("错误色") }
                    )
                }
                ModifierSlider("边框宽度", borderWidth, 0f..4f) { borderWidth = it }
                ModifierSlider("虚线间距", dashLength, 0f..16f) { dashLength = it }
                ModifierSlider("圆角半径", cornerRadius, 0f..24f) { cornerRadius = it }
                Text(
                    text = "宽度、间距和圆角会将非安全输入归一化，适合作为纯 Modifier 复用。",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            ModifierSection("debounceClick 防抖点击") {
                Text(
                    text = "连续快速点击只接受一次，延迟结束后恢复。当前回调：$clickCount 次",
                    modifier = Modifier.semantics {
                        contentDescription = "防抖点击回调次数 $clickCount"
                    },
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .debounceClick(
                            delayMillis = debounceDelay,
                            onClick = { clickCount += 1 }
                        ),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text(
                        "快速点击测试",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                ModifierSlider(
                    label = "防抖时长",
                    value = debounceDelay.toFloat(),
                    range = 0f..2000f
                ) { debounceDelay = it.toLong() }
                Text(
                    text = "当前 ${debounceDelay}ms；设置为 0ms 可验证立即恢复。",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ModifierSection(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        content()
    }
}

@Composable
private fun ModifierSlider(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("$label：${value.toInt()}", style = MaterialTheme.typography.bodySmall)
        Slider(value = value, onValueChange = onValueChange, valueRange = range)
    }
}
