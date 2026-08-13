package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.compose.ui.show.BDSignalBeamBox
import com.d10ng.compose.ui.show.SignalLevelLabels

private enum class BeamScenario(
    val label: String,
    val beams: List<Pair<Int, Int>>
) {
    STANDARD(
        "常规",
        listOf(1 to 45, 2 to 30, 3 to 58, 4 to 12, 5 to 0, 6 to 50, 7 to 22, 8 to 60, 9 to 38, 10 to 5)
    ),
    EMPTY("空数据", emptyList()),
    BOUNDS("上下限", (1..10).map { it to if (it % 2 == 0) 60 else 0 }),
    LONG("超长", (1..20).map { it to (it * 7 % 61) }),
    OUT_OF_RANGE("越界", listOf(1 to -20, 2 to 0, 3 to 30, 4 to 60, 5 to 95))
}

@Composable
fun BDSignalBeamBoxScreen(onBack: () -> Unit = {}) {
    var scenario by remember { mutableStateOf(BeamScenario.STANDARD) }
    var customColor by remember { mutableStateOf(false) }
    var longCopy by remember { mutableStateOf(false) }
    var rtl by remember { mutableStateOf(false) }
    val strengths = scenario.beams.map { it.second.coerceIn(0, 60) }
    val summary = if (strengths.isEmpty()) {
        "输入 0 路；渲染 10 个空柱"
    } else {
        "输入 ${strengths.size} 路；最小 ${strengths.min()}；最大 ${strengths.max()}"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        NavBar(title = "北斗信号波束", onClickBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            BeamSection("数据场景") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BeamScenario.entries.forEach { item ->
                        FilterChip(
                            selected = scenario == item,
                            onClick = { scenario = item },
                            label = { Text(item.label) }
                        )
                    }
                }
                Text(
                    text = summary,
                    modifier = Modifier.semantics { contentDescription = "信号摘要：$summary" },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            BeamSection("显示选项") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = customColor,
                        onClick = { customColor = !customColor },
                        label = { Text(if (customColor) "自定义配色" else "默认配色") }
                    )
                    FilterChip(
                        selected = longCopy,
                        onClick = { longCopy = !longCopy },
                        label = { Text(if (longCopy) "长文案" else "默认文案") }
                    )
                    FilterChip(
                        selected = rtl,
                        onClick = { rtl = !rtl },
                        label = { Text(if (rtl) "RTL" else "LTR") }
                    )
                }
            }
            androidx.compose.runtime.CompositionLocalProvider(
                LocalLayoutDirection provides if (rtl) LayoutDirection.Rtl else LayoutDirection.Ltr
            ) {
                BDSignalBeamBox(
                    beams = if (scenario == BeamScenario.LONG) {
                        scenario.beams.mapIndexed { index, pair ->
                            (1000 + index * 37) to pair.second
                        }
                    } else scenario.beams,
                    color = if (customColor) Color(0xFF00897B) else MaterialTheme.colorScheme.primary,
                    title = if (longCopy) {
                        "北斗卫星通信终端当前可用信号波束强度"
                    } else {
                        "北斗通讯卫星信号"
                    },
                    tips = if (longCopy) {
                        "卫星信号会受到建筑、山体和树木遮挡影响。请将终端放置在室外开阔区域，并让天线保持正确朝向，以获得更稳定的通信质量。"
                    } else {
                        "遮挡物会影响卫星信号的接收，请在室外空旷开阔处使用。"
                    },
                    levelLabels = SignalLevelLabels(
                        strong = "强",
                        medium = "中",
                        weak = "弱"
                    ),
                    modifier = Modifier.semantics {
                        contentDescription = "${if (rtl) "RTL" else "LTR"} 北斗信号图"
                    }
                )
            }
            BeamSection("组件边界") {
                Text(
                    text = "Material 3 无同类领域组件；保留 Composable，支持 Modifier、暗色主题、长编号、本地化等级文案和 RTL",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun BeamSection(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
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
