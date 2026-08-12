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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider as MaterialHorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider as MaterialVerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp.Companion.Hairline
import androidx.compose.ui.platform.LocalLayoutDirection
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.compose.ui.show.HorizontalDivider
import com.d10ng.compose.ui.show.VerticalDivider

@Composable
fun DividerScreen(onBack: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        NavBar(title = "Divider 分割线", onClickBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            DividerSection("自有横向适配") {
                LabeledHorizontalDivider("默认 0.5dp") { HorizontalDivider() }
                LabeledHorizontalDivider("设备单像素 Hairline") {
                    HorizontalDivider(thickness = Hairline)
                }
                LabeledHorizontalDivider("起始/末端 24dp") {
                    HorizontalDivider(paddingStart = 24.dp, paddingEnd = 24.dp)
                }
                LabeledHorizontalDivider("主题色 3dp") {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.primary,
                        thickness = 3.dp
                    )
                }
            }
            DividerSection("方向与宽度约束") {
                LabeledHorizontalDivider("LTR start 48dp / end 8dp") {
                    HorizontalDivider(paddingStart = 48.dp, paddingEnd = 8.dp)
                }
                androidx.compose.runtime.CompositionLocalProvider(
                    LocalLayoutDirection provides LayoutDirection.Rtl
                ) {
                    LabeledHorizontalDivider("RTL start 48dp / end 8dp") {
                        HorizontalDivider(paddingStart = 48.dp, paddingEnd = 8.dp)
                    }
                }
                LabeledHorizontalDivider("限定宽度 220dp，内部 16dp") {
                    HorizontalDivider(
                        modifier = Modifier.width(220.dp),
                        paddingStart = 16.dp,
                        paddingEnd = 16.dp,
                        thickness = 2.dp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            DividerSection("自有纵向适配") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(88.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    VerticalDivider()
                    VerticalDivider(paddingTop = 16.dp, paddingBottom = 16.dp)
                    VerticalDivider(
                        color = MaterialTheme.colorScheme.tertiary,
                        thickness = 4.dp,
                        paddingTop = 8.dp,
                        paddingBottom = 8.dp
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(androidx.compose.foundation.layout.IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.Center
                ) {
                    IntrinsicSampleText("内容驱动\n第一列")
                    VerticalDivider(
                        thickness = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        paddingTop = 4.dp,
                        paddingBottom = 4.dp
                    )
                    IntrinsicSampleText("IntrinsicSize.Min\n第二列")
                }
            }
            DividerSection("Material 3 官方等价实现") {
                LabeledHorizontalDivider("HorizontalDivider 1dp") {
                    MaterialHorizontalDivider()
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    MaterialVerticalDivider(color = Color.Gray)
                }
                Text(
                    text = "自有 API 已委托官方实现，仅保留 0.5dp 默认值与内边距兼容参数",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun DividerSection(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        content()
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.IntrinsicSampleText(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .weight(1f)
            .padding(12.dp),
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
private fun LabeledHorizontalDivider(
    label: String,
    divider: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        divider()
    }
}
