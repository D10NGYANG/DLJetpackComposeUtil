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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.utils.next
import com.d10ng.compose.view.Input
import dljetpackcomposeutil.library.generated.resources.Res
import dljetpackcomposeutil.library.generated.resources.ic_round_add_24
import dljetpackcomposeutil.library.generated.resources.ic_round_add_circle_24
import dljetpackcomposeutil.library.generated.resources.ic_round_remove_24
import dljetpackcomposeutil.library.generated.resources.ic_round_remove_circle_24
import org.jetbrains.compose.resources.painterResource

/**
 * 步进器
 * @Author d10ng
 * @Date 2023/9/6 15:53
 */

enum class StepperStyle {
    /**
     * 默认样式
     */
    Default,

    /**
     * 圆角样式
     */
    Round
}

/**
 * 步进器
 * @param value Int 当前值
 * @param onValueChange Function1<Int, Unit> 值变化回调
 * @param min Int 最小值
 * @param max Int 最大值
 * @param step Int 步长
 * @param disabled Boolean 是否禁用
 * @param canInput Boolean 是否可以输入
 * @param inputWidth Dp 输入框宽度
 * @param buttonSize Dp 按钮大小
 * @param style StepperStyle 样式
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