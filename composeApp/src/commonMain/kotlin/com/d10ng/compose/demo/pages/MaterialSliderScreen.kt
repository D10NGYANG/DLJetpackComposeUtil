package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.navigation.NavBar
import kotlin.math.roundToInt

@Composable
fun MaterialSliderScreen(onBack: () -> Unit = {}) {
    var continuousValue by remember { mutableFloatStateOf(35f) }
    var discreteValue by remember { mutableFloatStateOf(40f) }
    var rangeValue by remember { mutableStateOf(25f..75f) }
    var customValue by remember { mutableFloatStateOf(60f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        NavBar(title = "Material Slider", onClickBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            SliderSection("连续值") {
                Slider(
                    value = continuousValue,
                    onValueChange = { continuousValue = it },
                    modifier = Modifier.fillMaxWidth(),
                    valueRange = 0f..100f
                )
                SliderValueText("当前值：${continuousValue.roundToInt()}")
            }

            SliderSection("离散刻度") {
                Slider(
                    value = discreteValue,
                    onValueChange = { discreteValue = it },
                    modifier = Modifier.fillMaxWidth(),
                    valueRange = 0f..100f,
                    steps = 4
                )
                SliderValueText("每档 20，当前值：${discreteValue.roundToInt()}")
            }

            SliderSection("范围选择") {
                RangeSlider(
                    value = rangeValue,
                    onValueChange = { rangeValue = it },
                    modifier = Modifier.fillMaxWidth(),
                    valueRange = 0f..100f,
                    steps = 3
                )
                SliderValueText(
                    "当前范围：${rangeValue.start.roundToInt()} - ${rangeValue.endInclusive.roundToInt()}"
                )
            }

            SliderSection("禁用状态") {
                Slider(
                    value = 65f,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    valueRange = 0f..100f
                )
                SliderValueText("禁用滑块保持在 65")
            }

            SliderSection("自定义颜色") {
                Slider(
                    value = customValue,
                    onValueChange = { customValue = it },
                    modifier = Modifier.fillMaxWidth(),
                    valueRange = 0f..100f,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.tertiary,
                        activeTrackColor = MaterialTheme.colorScheme.tertiary,
                        activeTickColor = MaterialTheme.colorScheme.onTertiary
                    )
                )
                SliderValueText("主题第三色：${customValue.roundToInt()}")
            }
        }
    }
}

@Composable
private fun SliderSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        content()
    }
}

@Composable
private fun SliderValueText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
