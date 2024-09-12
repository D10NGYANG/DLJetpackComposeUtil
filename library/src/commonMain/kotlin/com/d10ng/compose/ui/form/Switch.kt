package com.d10ng.compose.ui.form

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.utils.next
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * Switch 开关
 * @Author d10ng
 * @Date 2023/9/6 14:22
 */

/**
 * Switch 开关
 * @param modifier Modifier
 * @param checked Boolean 是否选中
 * @param onCheckedChange Function1<Boolean, Unit> 选中状态变化
 * @param disabled Boolean 是否禁用
 * @param loading Boolean 是否加载中
 * @param activeColor Color 选中颜色
 * @param inactiveColor Color 未选中颜色
 * @param iconResource DrawableResource? 图标ID
 * @param size Dp 大小
 */
@Composable
fun Switch(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    disabled: Boolean = false,
    loading: Boolean = false,
    activeColor: Color = AppColor.Main.primary,
    inactiveColor: Color = AppColor.Neutral.line,
    iconResource: DrawableResource? = null,
    size: Dp = 32.dp
) {
    val color = SwitchDefaults.colors(
        checkedThumbColor = Color.White,
        uncheckedThumbColor = Color.White,
        disabledCheckedThumbColor = Color.White.next(0.2),
        disabledUncheckedThumbColor = Color.White.next(0.2),
        checkedTrackColor = activeColor,
        uncheckedTrackColor = inactiveColor,
        disabledCheckedTrackColor = activeColor.next(0.2),
        disabledUncheckedTrackColor = inactiveColor.next(0.2),
        checkedBorderColor = activeColor,
        uncheckedBorderColor = inactiveColor,
        disabledCheckedBorderColor = activeColor.next(0.2),
        disabledUncheckedBorderColor = inactiveColor.next(0.2),
        checkedIconColor = activeColor,
        uncheckedIconColor = inactiveColor,
        disabledCheckedIconColor = activeColor.next(0.2),
        disabledUncheckedIconColor = inactiveColor.next(0.2),
    )
    val loadingColor = remember(checked, disabled, activeColor, inactiveColor) {
        val cc = if (checked) activeColor else inactiveColor
        if (disabled) cc.next(0.2) else cc
    }
    val scale = remember(size) {
        size.value / 32f
    }
    androidx.compose.material3.Switch(
        modifier = modifier
            .scale(scale)
            .height(size),
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = color,
        enabled = !disabled && !loading,
        thumbContent = {
            if (loading && iconResource == null) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(5.dp),
                    color = loadingColor,
                    strokeCap = StrokeCap.Round,
                    strokeWidth = 1.5.dp
                )
            }
            if (iconResource != null) {
                Icon(
                    painter = painterResource(resource = iconResource),
                    contentDescription = null,
                    tint = loadingColor,
                    modifier = Modifier.padding(5.dp),
                )
            }
        }
    )
}