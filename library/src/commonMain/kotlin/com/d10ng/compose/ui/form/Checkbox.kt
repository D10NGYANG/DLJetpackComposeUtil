package com.d10ng.compose.ui.form

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
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
 */
enum class CheckboxType(val shape: Shape) {
    // 方形（圆角矩形）
    SQUARE(AppShape.RC.v4),
    // 圆形
    CIRCLE(AppShape.RC.Cycle)
}

/**
 * 复选框
 * 支持方形和圆形两种外观，选中时显示对勾图标，可独立控制禁用状态及各阶段颜色
 * @param modifier Modifier 修饰符
 * @param checked Boolean 当前是否处于选中状态
 * @param onCheckedChange (Boolean) -> Unit 点击时切换选中状态的回调，参数为切换后的新状态，默认无操作
 * @param disabled Boolean 是否禁用，禁用时不响应点击且颜色变浅，默认 false
 * @param size Dp 复选框的整体尺寸，默认 22.dp
 * @param type CheckboxType 复选框形状，默认 [CheckboxType.SQUARE]
 * @param activeColor Color 选中时的背景填充色，默认 MaterialTheme.colorScheme.primary
 * @param inactiveColor Color 未选中时的边框颜色，默认 [AppColor.Neutral.tips]
 * @param disabledColor Color 禁用时的整体颜色（选中禁用为实心、未选中禁用为半透明），默认 [AppColor.Neutral.hint]
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
    val bgColor = remember(disabled, disabledColor, checked, activeColor, inactiveColor) {
        when {
            disabled && checked -> disabledColor
            disabled -> disabledColor.next(0.5)
            checked -> activeColor
            else -> Color.Transparent
        }
    }
    Box(modifier = modifier.size(size)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor, type.shape)
                .clip(type.shape)
                .then(
                    if (checked.not()) Modifier
                        .border(1.dp, if (disabled) disabledColor else inactiveColor, type.shape)
                    else Modifier
                )
                .clickable(disabled.not()) { onCheckedChange(checked.not()) },
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                val tintColor = remember {
                    if (disabled) disabledColor.next(0.5) else Color.White
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

