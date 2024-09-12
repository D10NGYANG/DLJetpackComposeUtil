package com.d10ng.compose.ui.show

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import dljetpackcomposeutil.library.generated.resources.Res
import dljetpackcomposeutil.library.generated.resources.ic_round_steps_active_20
import org.jetbrains.compose.resources.painterResource

/**
 * 步骤条
 * @Author d10ng
 * @Date 2023/9/8 18:10
 */

/**
 * 步骤条
 * @param items Set<String> 步骤列表
 * @param runningIndex Int 当前步骤
 * @param runningColor Color 运行中颜色
 * @param activeColor Color 激活颜色
 * @param inactiveColor Color 未激活颜色
 */
@Composable
fun Steps(
    items: Set<String>,
    runningIndex: Int = 0,
    runningColor: Color = AppColor.Main.primary,
    activeColor: Color = AppColor.Neutral.body,
    inactiveColor: Color = AppColor.Neutral.hint,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        items.forEachIndexed { index, item ->
            Step(
                label = item,
                mode = when {
                    index < runningIndex -> StepMode.Active
                    index == runningIndex -> StepMode.Running
                    else -> StepMode.Inactive
                },
                first = index == 0,
                last = index == items.size - 1,
                activeColor = activeColor,
                inactiveColor = inactiveColor,
                runningColor = runningColor
            )
        }
    }
}

enum class StepMode {
    Active,
    Inactive,
    Running
}

@Composable
fun RowScope.Step(
    label: String,
    mode: StepMode,
    first: Boolean = false,
    last: Boolean = false,
    runningColor: Color = AppColor.Main.primary,
    activeColor: Color = AppColor.Neutral.body,
    inactiveColor: Color = AppColor.Neutral.hint,
) {
    Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 文本
        Text(
            text = label,
            style = AppText.Normal.Black.mini,
            color = when (mode) {
                StepMode.Active -> activeColor
                StepMode.Inactive -> inactiveColor
                StepMode.Running -> runningColor
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row(
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 左侧线
            Line(
                if (first) Color.Transparent
                else if (mode == StepMode.Inactive) inactiveColor
                else runningColor
            )
            // 中间标记
            Box(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .size(20.dp),
                contentAlignment = Alignment.Center
            ) {
                when (mode) {
                    StepMode.Active -> {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(runningColor, AppShape.RC.Cycle)
                        )
                    }

                    StepMode.Inactive -> {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(inactiveColor, AppShape.RC.Cycle)
                        )
                    }

                    StepMode.Running -> {
                        Icon(
                            painter = painterResource(resource = Res.drawable.ic_round_steps_active_20),
                            contentDescription = null,
                            tint = runningColor
                        )
                    }
                }
            }
            // 右侧线
            Line(
                if (last) Color.Transparent
                else if (mode != StepMode.Active) inactiveColor
                else runningColor
            )
        }
    }
}

@Composable
private fun RowScope.Line(
    color: Color
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(0.5.dp)
            .background(color)
    )
}