package com.d10ng.compose.ui.form

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.d10ng.compose.resources.Res
import com.d10ng.compose.resources.ic_round_add_24
import com.d10ng.compose.resources.ic_round_add_circle_24
import com.d10ng.compose.resources.ic_round_remove_24
import com.d10ng.compose.resources.ic_round_remove_circle_24
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.utils.next
import com.d10ng.compose.view.Input
import org.jetbrains.compose.resources.painterResource

/**
 * 步进器
 * @Author d10ng
 * @Date 2023/9/6 15:53
 */

/**
 * 步进器样式枚举
 */
enum class StepperStyle {
    /**
     * 默认样式：带方形背景的加减按钮，按钮与输入框紧贴排列
     */
    Default,

    /**
     * 圆角样式：使用圆形图标按钮，无背景色，视觉更轻量
     */
    Round
}

/**
 * 步进器
 *
 * 通过点击加减按钮或直接输入来调整数值，支持默认和圆角两种样式。
 *
 * @param value Int 当前值
 * @param onValueChange (Int) -> Unit 值变化回调，参数为变化后的新值
 * @param min Int 最小值，默认为 0
 * @param max Int 最大值，默认为 [Int.MAX_VALUE]
 * @param step Int 每次点击加减按钮的步长，默认为 1
 * @param disabled Boolean 是否禁用整个步进器，禁用时按钮不可点击且输入框不可编辑，默认为 false
 * @param canInput Boolean 是否允许直接在输入框中输入数值，默认为 true
 * @param inputWidth Dp 中间输入框的宽度，默认为 45.dp
 * @param buttonSize Dp 加减按钮的尺寸（宽高），默认为 32.dp
 * @param style StepperStyle 步进器样式，支持 [StepperStyle.Default] 和 [StepperStyle.Round]，默认为 [StepperStyle.Default]
 */
@Composable
fun Stepper(
    value: Int,
    onValueChange: (Int) -> Unit,
    min: Int = 0,
    max: Int = Int.MAX_VALUE,
    step: Int = 1,
    disabled: Boolean = false,
    canInput: Boolean = true,
    inputWidth: Dp = 45.dp,
    buttonSize: Dp = 32.dp,
    style: StepperStyle = StepperStyle.Default
) {
    when (style) {
        StepperStyle.Default -> StepperDefault(
            value = value,
            onValueChange = onValueChange,
            min = min,
            max = max,
            step = step,
            disabled = disabled,
            canInput = canInput,
            inputWidth = inputWidth,
            buttonSize = buttonSize
        )
        StepperStyle.Round -> StepperRound(
            value = value,
            onValueChange = onValueChange,
            min = min,
            max = max,
            step = step,
            disabled = disabled,
            canInput = canInput,
            inputWidth = inputWidth,
            buttonSize = buttonSize
        )
    }
}

/**
 * 默认样式步进器
 *
 * 加减按钮具有方形圆角背景，与中间输入框拼合为一个整体条形控件。
 * 按钮不可用时背景色和图标颜色会变浅，提供视觉反馈。
 *
 * @param value Int 当前值
 * @param onValueChange (Int) -> Unit 值变化回调，参数为变化后的新值
 * @param min Int 最小值，默认为 0
 * @param max Int 最大值，默认为 [Int.MAX_VALUE]
 * @param step Int 每次点击加减按钮的步长，默认为 1
 * @param disabled Boolean 是否禁用整个步进器，默认为 false
 * @param canInput Boolean 是否允许直接在输入框中输入数值，默认为 true
 * @param inputWidth Dp 中间输入框的宽度，默认为 45.dp
 * @param buttonSize Dp 加减按钮的尺寸（宽高），默认为 32.dp
 */
@Composable
fun StepperDefault(
    value: Int,
    onValueChange: (Int) -> Unit,
    min: Int = 0,
    max: Int = Int.MAX_VALUE,
    step: Int = 1,
    disabled: Boolean = false,
    canInput: Boolean = true,
    inputWidth: Dp = 45.dp,
    buttonSize: Dp = 32.dp
) {
    val disabledNext = 0.5
    Row {
        // 减少按钮
        val lessShape = remember {
            RoundedCornerShape(
                topStart = 4.dp,
                topEnd = 0.dp,
                bottomEnd = 0.dp,
                bottomStart = 4.dp
            )
        }
        val lessEnable = remember(value, min, disabled) {
            disabled.not() && value > min
        }
        Box(
            modifier = Modifier
                .padding(end = 1.dp)
                .size(buttonSize)
                .background(
                    AppColor.Neutral.line.next(if (lessEnable) 0.0 else disabledNext),
                    lessShape
                )
                .clip(lessShape)
                .clickable(enabled = lessEnable) {
                    if (value > min) {
                        onValueChange(maxOf(value - step, min))
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(resource = Res.drawable.ic_round_remove_24),
                contentDescription = "减少",
                modifier = Modifier.size(16.dp),
                tint = AppColor.Neutral.title.next(if (lessEnable) 0.0 else disabledNext)
            )
        }
        // 输入框
        Box(
            modifier = Modifier
                .width(inputWidth)
                .height(buttonSize)
                .background(AppColor.Neutral.line.next(if (disabled.not()) 0.0 else disabledNext)),
            contentAlignment = Alignment.Center
        ) {
            Input(
                value = value.toString(),
                onValueChange = {
                    val newValue = it.toIntOrNull() ?: value
                    if (newValue in min..max) {
                        onValueChange(newValue)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = AppText.Normal.Title.default.copy(
                    color = AppColor.Neutral.title.next(if (disabled.not()) 0.0 else disabledNext)
                ),
                textAlign = TextAlign.Center,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Decimal
                ),
                readOnly = !canInput,
                singleLine = true,
                enabled = !disabled
            )
        }
        // 增加按钮
        val addShape = remember {
            RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 4.dp,
                bottomEnd = 4.dp,
                bottomStart = 0.dp
            )
        }
        val addEnable = remember(value, max, disabled) {
            disabled.not() && value < max
        }
        Box(
            modifier = Modifier
                .padding(start = 1.dp)
                .size(buttonSize)
                .background(
                    AppColor.Neutral.line.next(if (addEnable) 0.0 else disabledNext),
                    addShape
                )
                .clip(addShape)
                .clickable(enabled = addEnable) {
                    if (value < max) {
                        onValueChange(minOf(value + step, max))
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(resource = Res.drawable.ic_round_add_24),
                contentDescription = "增加",
                modifier = Modifier.size(16.dp),
                tint = AppColor.Neutral.title.next(if (addEnable) 0.0 else disabledNext)
            )
        }
    }
}

/**
 * 圆角样式步进器
 *
 * 加减按钮使用圆形图标（无方块背景），视觉上更加轻量简洁，适合界面风格较为圆润的场景。
 * 按钮不可用时图标颜色会变浅，提供视觉反馈。
 *
 * @param value Int 当前值
 * @param onValueChange (Int) -> Unit 值变化回调，参数为变化后的新值
 * @param min Int 最小值，默认为 0
 * @param max Int 最大值，默认为 [Int.MAX_VALUE]
 * @param step Int 每次点击加减按钮的步长，默认为 1
 * @param disabled Boolean 是否禁用整个步进器，默认为 false
 * @param canInput Boolean 是否允许直接在输入框中输入数值，默认为 true
 * @param inputWidth Dp 中间输入框的宽度，默认为 45.dp
 * @param buttonSize Dp 加减按钮的尺寸（宽高），默认为 32.dp
 */
@Composable
fun StepperRound(
    value: Int,
    onValueChange: (Int) -> Unit,
    min: Int = 0,
    max: Int = Int.MAX_VALUE,
    step: Int = 1,
    disabled: Boolean = false,
    canInput: Boolean = true,
    inputWidth: Dp = 45.dp,
    buttonSize: Dp = 32.dp
) {
    val disabledNext = 0.5
    Row {
        // 减少按钮
        val lessEnable = remember(value, min, disabled) {
            disabled.not() && value > min
        }
        Box(
            modifier = Modifier
                .padding(end = 1.dp)
                .size(buttonSize)
                .clip(AppShape.RC.Cycle)
                .clickable(enabled = lessEnable) {
                    if (value > min) {
                        onValueChange(maxOf(value - step, min))
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(resource = Res.drawable.ic_round_remove_circle_24),
                contentDescription = "减少",
                modifier = Modifier.size(22.dp),
                tint = AppColor.Neutral.hint.next(if (lessEnable) 0.0 else disabledNext)
            )
        }
        // 输入框
        Box(
            modifier = Modifier
                .width(inputWidth)
                .height(buttonSize),
            contentAlignment = Alignment.Center
        ) {
            Input(
                value = value.toString(),
                onValueChange = {
                    val newValue = it.toIntOrNull() ?: value
                    if (newValue in min..max) {
                        onValueChange(newValue)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = AppText.Normal.Title.default.copy(
                    color = AppColor.Neutral.title.next(if (disabled.not()) 0.0 else disabledNext)
                ),
                textAlign = TextAlign.Center,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Decimal
                ),
                readOnly = !canInput,
                singleLine = true,
                enabled = !disabled
            )
        }
        // 增加按钮
        val addEnable = remember(value, max, disabled) {
            disabled.not() && value < max
        }
        Box(
            modifier = Modifier
                .padding(start = 1.dp)
                .size(buttonSize)
                .clip(AppShape.RC.Cycle)
                .clickable(enabled = addEnable) {
                    if (value < max) {
                        onValueChange(minOf(value + step, max))
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(resource = Res.drawable.ic_round_add_circle_24),
                contentDescription = "增加",
                modifier = Modifier.size(22.dp),
                tint = AppColor.Neutral.hint.next(if (addEnable) 0.0 else disabledNext)
            )
        }
    }
}

@Preview
@Composable
fun PreviewStepperDefault() {
    androidx.compose.foundation.layout.Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
    ) {
        // 正常状态
        StepperDefault(value = 5, onValueChange = {})
        // 最小值禁用减按钮
        StepperDefault(value = 0, onValueChange = {}, min = 0, max = 10)
        // 最大值禁用加按钮
        StepperDefault(value = 10, onValueChange = {}, min = 0, max = 10)
        // 全部禁用
        StepperDefault(value = 3, onValueChange = {}, disabled = true)
    }
}

@Preview
@Composable
fun PreviewStepperRound() {
    androidx.compose.foundation.layout.Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
    ) {
        // 正常状态
        StepperRound(value = 5, onValueChange = {})
        // 最小值禁用减按钮
        StepperRound(value = 0, onValueChange = {}, min = 0, max = 10)
        // 最大值禁用加按钮
        StepperRound(value = 10, onValueChange = {}, min = 0, max = 10)
        // 全部禁用
        StepperRound(value = 3, onValueChange = {}, disabled = true)
    }
}

@Preview
@Composable
fun PreviewStepper() {
    androidx.compose.foundation.layout.Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
    ) {
        // 默认样式
        Stepper(value = 5, onValueChange = {}, style = StepperStyle.Default)
        // 圆角样式
        Stepper(value = 5, onValueChange = {}, style = StepperStyle.Round)
    }
}

