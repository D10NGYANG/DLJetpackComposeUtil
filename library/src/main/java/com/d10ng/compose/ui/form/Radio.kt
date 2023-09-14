package com.d10ng.compose.ui.form

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.show.Border
import com.d10ng.compose.utils.next

/**
 * 单选按钮
 * @Author d10ng
 * @Date 2023/9/8 17:03
 */

@Preview
@Composable
private fun RadioTest() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        CellGroup(title = "基础用法", inset = true) {
            RadioCell(label = "选择1", selected = true)
            RadioCell(label = "选择2", selected = false)
            RadioCell(label = "选择3", selected = true, disabled = true)
            RadioCell(label = "选择1", selected = false, disabled = true)
        }
    }
}

/**
 * 单选单元格
 * @param modifier Modifier 修饰符
 * @param label String 标题
 * @param selected Boolean 是否选中
 * @param disabled Boolean 是否禁用
 * @param border Boolean 是否显示边框
 * @param onClick Function0<Unit>
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
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
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
        if (border) Border()
    }
}