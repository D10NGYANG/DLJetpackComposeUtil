package com.d10ng.compose.ui.form

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.dashBorder
import com.d10ng.compose.utils.next

/**
 * 选择按钮
 * @Author d10ng
 * @Date 2023/9/6 16:01
 */

@Preview
@Composable
fun CheckButtonTest() {
    Column {
        CheckButton(text = "测试", checked = true, onCheckedChange = {})
        CheckButton(text = "测试", checked = false, onCheckedChange = {})
        CheckButton(text = "测试", checked = true, onCheckedChange = {}, disabled = true)
        CheckButton(text = "测试", checked = false, onCheckedChange = {}, disabled = true)
    }
}

/**
 * 选择按钮组
 * @param modifier Modifier 外部传入的修饰符
 * @param items Set<String> 选项列表
 * @param checked String 选中的选项
 * @param onCheckedChange Function1<String, Unit> 选中状态切换
 * @param disabled Set<String> 禁用的选项
 * @param activeColor Color 选中颜色
 * @param inactiveColor Color 未选中颜色
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CheckButtonGroup(
    modifier: Modifier = Modifier,
    items: Set<String>,
    checked: String,
    onCheckedChange: (String) -> Unit,
    disabled: Set<String> = setOf(),
    activeColor: Color = AppColor.Main.primary,
    inactiveColor: Color = AppColor.Neutral.body,
) {
    CheckButtonGroup(
        modifier = modifier,
        items = items,
        checked = setOf(checked),
        onCheckedChange = {
            val newCheck = if (it.size > 1) it.find { c -> c.contentEquals(checked).not() }!!
            else it.firstOrNull()?: checked
            onCheckedChange(newCheck)
        },
        disabled = disabled,
        activeColor = activeColor,
        inactiveColor = inactiveColor
    )
}

/**
 * 多选选择按钮组
 * @param modifier Modifier 外部传入的修饰符
 * @param items Set<String> 选项列表
 * @param checked Set<String> 选中的选项
 * @param onCheckedChange Function1<Set<String>, Unit> 选中状态切换
 * @param disabled Set<String> 禁用的选项
 * @param activeColor Color 选中颜色
 * @param inactiveColor Color 未选中颜色
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CheckButtonGroup(
    modifier: Modifier = Modifier,
    items: Set<String>,
    checked: Set<String>,
    onCheckedChange: (Set<String>) -> Unit,
    disabled: Set<String> = setOf(),
    activeColor: Color = AppColor.Main.primary,
    inactiveColor: Color = AppColor.Neutral.body,
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items.forEach { item ->
            val dis = remember(disabled) { disabled.contains(item) }
            CheckButton(
                text = item,
                checked = checked.contains(item),
                onCheckedChange = {
                    val newChecked = if (checked.contains(item)) {
                        checked - item
                    } else {
                        checked + item
                    }
                    onCheckedChange(newChecked)
                },
                disabled = dis,
                activeColor = activeColor,
                inactiveColor = inactiveColor
            )
        }
    }
}

/**
 * 选择按钮
 * @param text String 文本
 * @param checked Boolean 是否选中
 * @param onCheckedChange Function1<Boolean, Unit> 选中状态切换
 * @param disabled Boolean 是否禁用
 * @param activeColor Color 选中颜色
 * @param inactiveColor Color 未选中颜色
 */
@Composable
fun CheckButton(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    disabled: Boolean = false,
    activeColor: Color = AppColor.Main.primary,
    inactiveColor: Color = AppColor.Neutral.body,
) {
    val acColor = remember(disabled) {
        if (disabled) activeColor.next(0.2) else activeColor
    }
    val inColor = remember(disabled) {
        if (disabled) inactiveColor.next(0.2) else inactiveColor
    }
    Box(
        modifier = Modifier
            .then(
                if (!checked) Modifier.dashBorder(
                    color = inColor,
                    cornerRadiusDp = 16.dp
                ) else Modifier
            )
            .clip(AppShape.RC.v16)
            .background(if (checked) acColor else Color.Transparent)
            .clickable(enabled = !disabled) { onCheckedChange(!checked) }
            .padding(horizontal = 12.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = AppText.Normal.Body.small,
            color = if (checked) Color.White else inColor
        )
    }
}