package com.d10ng.compose.ui.form

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox as MaterialCheckbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.d10ng.compose.resources.Res
import com.d10ng.compose.resources.ic_round_check_24
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.utils.next
import org.jetbrains.compose.resources.painterResource

/**
 * 复选框形状类型
 *
 * [shape] 为兼容既有调用保留；[SQUARE] 的实际视觉由 Material 3 Checkbox token 绘制，
 * [CIRCLE] 继续使用该形状进行自定义绘制。
 */
enum class CheckboxType(val shape: Shape) {
    // 方形（圆角矩形）
    SQUARE(AppShape.RC.v4),
    // 圆形
    CIRCLE(AppShape.RC.Cycle)
}

/**
 * 复选框
 * 支持方形和圆形两种外观。方形视觉委托 Material 3 Checkbox，圆形保留兼容绘制；
 * 外层继续按 [size] 提供精确布局与切换语义，避免官方最小交互尺寸改变既有占位。
 * @param modifier Modifier 修饰符
 * @param checked Boolean 当前是否处于选中状态
 * @param onCheckedChange (Boolean) -> Unit 点击时切换选中状态的回调，参数为切换后的新状态，默认无操作
 * @param disabled Boolean 是否禁用，禁用时不响应点击且颜色变浅，默认 false
 * @param size Dp 复选框的整体尺寸，默认 22.dp
 * @param type CheckboxType 复选框形状，默认 [CheckboxType.SQUARE]
 * @param activeColor Color 选中时的背景填充色，默认 MaterialTheme.colorScheme.primary
 * @param inactiveColor Color 未选中时的边框颜色，默认 [AppColor.Neutral.tips]
 * @param disabledColor Color 禁用时的整体颜色（选中禁用为实心、未选中禁用为与白色混合后的浅色），默认 [AppColor.Neutral.hint]
 */
@Composable
fun Checkbox(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit = {},
    disabled: Boolean = false,
    size: Dp = 22.dp,
    type: CheckboxType = CheckboxType.SQUARE,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = AppColor.Neutral.tips,
    disabledColor: Color = AppColor.Neutral.hint,
) {
    val disabledUncheckedColor = disabledColor.next(0.5)
    Box(
        modifier = modifier
            .size(size)
            .toggleable(
                value = checked,
                enabled = !disabled,
                role = Role.Checkbox,
                onValueChange = onCheckedChange
            ),
        contentAlignment = Alignment.Center
    ) {
        when (type) {
            CheckboxType.SQUARE -> MaterialCheckbox(
                checked = checked,
                onCheckedChange = null,
                enabled = !disabled,
                colors = CheckboxDefaults.colors(
                    checkedCheckmarkColor = if (disabled) disabledUncheckedColor else Color.White,
                    uncheckedCheckmarkColor = Color.Transparent,
                    disabledCheckmarkColor = disabledUncheckedColor,
                    checkedBoxColor = activeColor,
                    uncheckedBoxColor = Color.Transparent,
                    disabledCheckedBoxColor = disabledColor,
                    disabledUncheckedBoxColor = disabledUncheckedColor,
                    disabledIndeterminateBoxColor = disabledColor,
                    checkedBorderColor = activeColor,
                    uncheckedBorderColor = inactiveColor,
                    disabledBorderColor = disabledColor,
                    disabledUncheckedBorderColor = disabledColor,
                    disabledIndeterminateBorderColor = disabledColor
                ),
                modifier = Modifier
                    .requiredSize(MaterialCheckboxVisualSize)
                    .graphicsLayer {
                        val scale = size.toPx() / MaterialCheckboxVisualSize.toPx()
                        scaleX = scale
                        scaleY = scale
                    }
            )

            CheckboxType.CIRCLE -> CircularCheckboxVisual(
                checked = checked,
                disabled = disabled,
                activeColor = activeColor,
                inactiveColor = inactiveColor,
                disabledColor = disabledColor,
                disabledUncheckedColor = disabledUncheckedColor
            )
        }
    }
}

// Material 3 1.11.0-alpha07 defaults to the legacy 20.dp visual path.
private val MaterialCheckboxVisualSize = 20.dp

@Composable
private fun CircularCheckboxVisual(
    checked: Boolean,
    disabled: Boolean,
    activeColor: Color,
    inactiveColor: Color,
    disabledColor: Color,
    disabledUncheckedColor: Color
) {
    val bgColor = remember(disabled, disabledColor, checked, activeColor, inactiveColor) {
        when {
            disabled && checked -> disabledColor
            disabled -> disabledUncheckedColor
            checked -> activeColor
            else -> Color.Transparent
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor, CheckboxType.CIRCLE.shape)
            .clip(CheckboxType.CIRCLE.shape)
            .then(
                if (checked.not()) Modifier.border(
                    width = 1.dp,
                    color = if (disabled) disabledColor else inactiveColor,
                    shape = CheckboxType.CIRCLE.shape
                ) else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            val tintColor = remember(disabled, disabledUncheckedColor) {
                if (disabled) disabledUncheckedColor else Color.White
            }
            Icon(
                painter = painterResource(resource = Res.drawable.ic_round_check_24),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                tint = tintColor,
            )
        }
    }
}

@Preview
@Composable
fun PreviewCheckbox() {
    Column(
        modifier = Modifier.background(Color.White).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 方形：未选中 / 选中
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Checkbox(checked = false, onCheckedChange = {})
            Checkbox(checked = true, onCheckedChange = {})
        }
        // 圆形：未选中 / 选中
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Checkbox(checked = false, type = CheckboxType.CIRCLE, onCheckedChange = {})
            Checkbox(checked = true, type = CheckboxType.CIRCLE, onCheckedChange = {})
        }
        // 禁用：未选中禁用 / 选中禁用
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Checkbox(checked = false, disabled = true, onCheckedChange = {})
            Checkbox(checked = true, disabled = true, onCheckedChange = {})
        }
        // 不同尺寸
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = true, size = 16.dp, onCheckedChange = {})
            Checkbox(checked = true, size = 22.dp, onCheckedChange = {})
            Checkbox(checked = true, size = 30.dp, onCheckedChange = {})
        }
    }
}
