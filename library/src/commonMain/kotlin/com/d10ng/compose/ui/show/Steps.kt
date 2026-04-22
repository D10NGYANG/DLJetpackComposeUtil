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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.resources.Res
import com.d10ng.compose.resources.ic_round_steps_active_20
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import org.jetbrains.compose.resources.painterResource

/**
 * 步骤条
 * @Author d10ng
 * @Date 2023/9/8 18:10
 */

/**
 * 步骤条
 *
 * 横向展示多个步骤的进度状态，每个步骤包含标签文本和状态图标，相邻步骤之间以连接线衔接。
 * 步骤状态分为三种：已完成（[StepMode.Active]）、进行中（[StepMode.Running]）、未开始（[StepMode.Inactive]）。
 *
 * @param items Set<String> 步骤标签集合，按集合迭代顺序从左到右依次渲染
 * @param runningIndex Int 当前进行中步骤的索引（从 0 开始）；小于该索引的步骤为已完成，大于的为未开始，默认为 0
 * @param runningColor Color 进行中及已完成步骤的颜色（图标、连接线、文字），默认为 [AppColor.Main.primary]
 * @param activeColor Color 已完成步骤的文字颜色，默认为 [AppColor.Neutral.body]
 * @param inactiveColor Color 未开始步骤的颜色（图标、连接线、文字），默认为 [AppColor.Neutral.hint]
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

/**
 * 步骤状态枚举
 */
enum class StepMode {
    /** 已完成：小圆点图标，使用进行中颜色 */
    Active,
    /** 未开始：小圆点图标，使用未激活颜色 */
    Inactive,
    /** 进行中：圆形图标，使用进行中颜色 */
    Running
}

/**
 * 单个步骤项
 *
 * 包含顶部标签文本、中间状态图标及两侧连接线，需在 [RowScope] 中使用（通常由 [Steps] 自动调用）。
 * 第一个步骤左侧连接线透明，最后一个步骤右侧连接线透明。
 *
 * @param label String 步骤标签文本，超出宽度时截断显示
 * @param mode StepMode 步骤状态，决定图标样式和颜色
 * @param first Boolean 是否为第一个步骤，为 true 时左侧连接线不显示，默认为 false
 * @param last Boolean 是否为最后一个步骤，为 true 时右侧连接线不显示，默认为 false
 * @param runningColor Color 进行中及已完成状态的颜色，默认为 [AppColor.Main.primary]
 * @param activeColor Color 已完成状态的文字颜色，默认为 [AppColor.Neutral.body]
 * @param inactiveColor Color 未开始状态的颜色，默认为 [AppColor.Neutral.hint]
 */
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
            style = AppText.Normal.Scrim.mini,
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

@Preview(showBackground = true)
@Composable
fun PreviewSteps() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 第一步进行中
        Steps(
            items = linkedSetOf("步骤一", "步骤二", "步骤三", "步骤四"),
            runningIndex = 0
        )
        // 第二步进行中（第一步已完成）
        Steps(
            items = linkedSetOf("步骤一", "步骤二", "步骤三", "步骤四"),
            runningIndex = 1
        )
        // 第三步进行中
        Steps(
            items = linkedSetOf("步骤一", "步骤二", "步骤三", "步骤四"),
            runningIndex = 2
        )
        // 最后一步进行中
        Steps(
            items = linkedSetOf("步骤一", "步骤二", "步骤三", "步骤四"),
            runningIndex = 3
        )
    }
}

