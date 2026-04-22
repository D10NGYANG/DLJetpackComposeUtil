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
import androidx.compose.ui.tooling.preview.Preview
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
 *
 * 基于 Material3 Switch 封装，支持禁用、加载中及自定义图标等状态。
 * 加载中时滑块内显示圆形进度指示器；传入图标资源时滑块内显示对应图标。
 * 禁用状态下颜色会自动变浅，提供视觉反馈。
 *
 * @param modifier Modifier 修饰符，默认为 [Modifier]
 * @param checked Boolean 当前是否处于选中（开启）状态
 * @param onCheckedChange (Boolean) -> Unit 选中状态变化回调，参数为变化后的新状态
 * @param disabled Boolean 是否禁用开关，禁用时不可交互且颜色变浅，默认为 false
 * @param loading Boolean 是否处于加载中状态，加载中时开关不可交互并在滑块内显示进度指示器，默认为 false
 * @param activeColor Color 开启状态下轨道及边框的颜色，默认为 [AppColor.Main.primary]
 * @param inactiveColor Color 关闭状态下轨道及边框的颜色，默认为 [AppColor.Neutral.line]
 * @param iconResource DrawableResource? 滑块内显示的图标资源，为 null 时不显示图标；加载中时优先显示进度指示器，默认为 null
 * @param size Dp 开关整体高度（同时影响缩放比例），默认为 32.dp
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

@Preview
@Composable
fun PreviewSwitch() {
    androidx.compose.foundation.layout.Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
    ) {
        // 开启状态
        Switch(checked = true, onCheckedChange = {})
        // 关闭状态
        Switch(checked = false, onCheckedChange = {})
        // 开启且禁用
        Switch(checked = true, onCheckedChange = {}, disabled = true)
        // 关闭且禁用
        Switch(checked = false, onCheckedChange = {}, disabled = true)
        // 加载中（开启）
        Switch(checked = true, onCheckedChange = {}, loading = true)
        // 加载中（关闭）
        Switch(checked = false, onCheckedChange = {}, loading = true)
    }
}
