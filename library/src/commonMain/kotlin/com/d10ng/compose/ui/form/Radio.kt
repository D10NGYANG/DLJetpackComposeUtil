package com.d10ng.compose.ui.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.defaultPaddingSize
import com.d10ng.compose.ui.show.HorizontalDivider
import com.d10ng.compose.utils.next

/**
 * 单选按钮
 * @Author d10ng
 * @Date 2023/9/8 17:03
 */

/**
 * 单选单元格
 * 以列表行形式展示单选项，右侧显示 RadioButton，标题居左
 * 注意：[disabled] 仅影响 RadioButton 的视觉状态和颜色，不会拦截外层行的点击事件
 * @param modifier Modifier 修饰符
 * @param label String 选项标题文字
 * @param selected Boolean 是否处于选中状态
 * @param disabled Boolean 是否禁用，禁用时 RadioButton 颜色变浅，但行点击事件仍会触发，默认 false
 * @param border Boolean 是否在底部显示分割线，默认 true
 * @param onClick () -> Unit 行点击回调，默认无操作
 */
@Composable
fun RadioCell(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean,
    disabled: Boolean = false,
    border: Boolean = true,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .clickable { onClick() }
            .padding(horizontal = defaultPaddingSize)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = defaultPaddingSize),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 标题
            Text(
                text = label,
                style = AppText.Normal.Title.default,
                color = AppColor.Neutral.title.next(if (disabled) 0.5 else 0.0)
            )
            // 间隔
            Box(modifier = Modifier.weight(1f))
            // 单选按钮
            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = AppColor.Main.primary,
                    unselectedColor = AppColor.Neutral.tips,
                    disabledSelectedColor = AppColor.Main.primary.next(0.5),
                    disabledUnselectedColor = AppColor.Neutral.tips.next(0.5),
                ),
                enabled = !disabled,
                modifier = Modifier.size(18.dp)
            )
        }
        if (border) HorizontalDivider()
    }
}

@Preview
@Composable
fun PreviewRadioCell() {
    Column {
        // 未选中
        RadioCell(label = "选项一", selected = false, onClick = {})
        // 选中
        RadioCell(label = "选项二", selected = true, onClick = {})
        // 禁用未选中
        RadioCell(label = "禁用未选中", selected = false, disabled = true, onClick = {})
        // 禁用选中
        RadioCell(label = "禁用选中", selected = true, disabled = true, onClick = {})
        // 无边框
        RadioCell(label = "无边框", selected = false, border = false, onClick = {})
    }
}
