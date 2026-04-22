package com.d10ng.compose.ui.form

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.d10ng.compose.ui.defaultPaddingSize
import com.d10ng.compose.utils.next

/**
 * 选择按钮
 * @Author d10ng
 * @Date 2023/9/6 16:01
 */

/**
 * 单选选择按钮组
 * 对多选版本的封装，每次只能选中一个选项，点击已选中项或未选中项均会更新选中值
 * @param modifier Modifier 修饰符
 * @param items Set<String> 所有可选项列表
 * @param checked String 当前选中的选项值
 * @param onCheckedChange (String) -> Unit 选中项变更回调，参数为新选中的选项值
 * @param mode CheckButtonMode 按钮外观模式，默认 [CheckButtonMode.Clip]
 * @param disabled Set<String> 禁用的选项集合，集合内的选项不可点击，默认为空
 * @param activeColor Color 选中状态的颜色，默认取 [mode] 的默认选中色
 * @param inactiveColor Color 未选中状态的颜色，默认取 [mode] 的默认未选中色
 */
@Composable
fun CheckButtonGroup(
    modifier: Modifier = Modifier,
    items: Set<String>,
    checked: String,
    onCheckedChange: (String) -> Unit,
    mode: CheckButtonMode = CheckButtonMode.Clip,
    disabled: Set<String> = setOf(),
    activeColor: Color = mode.activeColor,
    inactiveColor: Color = mode.inactiveColor,
) {
    CheckButtonGroup(
        modifier = modifier,
        items = items,
        checked = setOf(checked),
        onCheckedChange = {
            val newCheck = if (it.size > 1) it.find { c -> c.contentEquals(checked).not() }!!
            else it.firstOrNull() ?: checked
            onCheckedChange(newCheck)
        },
        mode = mode,
        disabled = disabled,
        activeColor = activeColor,
        inactiveColor = inactiveColor
    )
}

/**
 * 多选选择按钮组
 * 使用 FlowRow 自动换行排列所有选项，支持同时选中多个
 * @param modifier Modifier 修饰符
 * @param items Set<String> 所有可选项列表
 * @param checked Set<String> 当前已选中的选项集合
 * @param onCheckedChange (Set<String>) -> Unit 选中项集合变更回调，参数为变更后的完整选中集合
 * @param mode CheckButtonMode 按钮外观模式，默认 [CheckButtonMode.Clip]
 * @param disabled Set<String> 禁用的选项集合，集合内的选项不可点击，默认为空
 * @param activeColor Color 选中状态的颜色，默认取 [mode] 的默认选中色
 * @param inactiveColor Color 未选中状态的颜色，默认取 [mode] 的默认未选中色
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CheckButtonGroup(
    modifier: Modifier = Modifier,
    items: Set<String>,
    checked: Set<String>,
    onCheckedChange: (Set<String>) -> Unit,
    mode: CheckButtonMode = CheckButtonMode.Clip,
    disabled: Set<String> = setOf(),
    activeColor: Color = mode.activeColor,
    inactiveColor: Color = mode.inactiveColor,
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = defaultPaddingSize, vertical = 12.dp),
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
                mode = mode,
                disabled = dis,
                activeColor = activeColor,
                inactiveColor = inactiveColor
            )
        }
    }
}

/**
 * 选择按钮外观模式
 */
enum class CheckButtonMode(val activeColor: Color, val inactiveColor: Color) {
    // 标签样式：选中时填充背景，未选中时显示虚线边框、背景透明
    Clip(AppColor.Main.primary, AppColor.Neutral.body),
    // 按钮样式：选中与未选中均有背景色，通过背景深浅区分状态
    Button(AppColor.Main.primary, AppColor.Neutral.card)
}

/**
 * 单个选择按钮
 * 根据 [mode] 呈现两种外观：
 * - [CheckButtonMode.Clip]：标签样式，选中填充色，未选中虚线边框
 * - [CheckButtonMode.Button]：按钮样式（62×42dp），选中深色背景，未选中浅色背景
 * @param text String 按钮显示文字
 * @param checked Boolean 当前是否处于选中状态
 * @param onCheckedChange (Boolean) -> Unit 点击时的状态切换回调，参数为切换后的新状态
 * @param mode CheckButtonMode 按钮外观模式，默认 [CheckButtonMode.Clip]
 * @param disabled Boolean 是否禁用，禁用时不响应点击且颜色变浅，默认 false
 * @param activeColor Color 选中状态的颜色（Clip 模式为背景色，Button 模式为背景色），默认取 [mode] 的默认选中色
 * @param inactiveColor Color 未选中状态的颜色（Clip 模式为边框和文字色，Button 模式为背景色），默认取 [mode] 的默认未选中色
 */
@Composable
fun CheckButton(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    mode: CheckButtonMode = CheckButtonMode.Clip,
    disabled: Boolean = false,
    activeColor: Color = mode.activeColor,
    inactiveColor: Color = mode.inactiveColor,
) {
    val acColor = remember(disabled) {
        if (disabled) activeColor.next(0.2) else activeColor
    }
    val inColor = remember(disabled) {
        if (disabled) inactiveColor.next(0.2) else inactiveColor
    }
    when (mode) {
        CheckButtonMode.Button -> {
            Box(
                modifier = Modifier
                    .size(62.dp, 42.dp)
                    .clip(AppShape.RC.v6)
                    .background(if (checked) acColor else inColor)
                    .clickable(enabled = !disabled) { onCheckedChange(!checked) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    style = AppText.Normal.Body.default,
                    color = if (checked) Color.White else inColor.next(-0.5)
                )
            }
        }

        CheckButtonMode.Clip -> {
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
    }
}

@Preview
@Composable
fun PreviewCheckButton() {
    Column(
        modifier = Modifier.background(Color.White).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Clip 模式：未选中 / 选中 / 禁用未选 / 禁用已选
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CheckButton(text = "标签", checked = false, onCheckedChange = {})
            CheckButton(text = "标签", checked = true, onCheckedChange = {})
            CheckButton(text = "禁用", checked = false, disabled = true, onCheckedChange = {})
            CheckButton(text = "禁用", checked = true, disabled = true, onCheckedChange = {})
        }
        // Button 模式：未选中 / 选中 / 禁用
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CheckButton(text = "选项", checked = false, mode = CheckButtonMode.Button, onCheckedChange = {})
            CheckButton(text = "选项", checked = true, mode = CheckButtonMode.Button, onCheckedChange = {})
            CheckButton(text = "禁用", checked = false, mode = CheckButtonMode.Button, disabled = true, onCheckedChange = {})
        }
    }
}

@Preview
@Composable
fun PreviewCheckButtonGroup() {
    val items = linkedSetOf("选项一", "选项二", "选项三", "选项四", "选项五")
    Column(modifier = Modifier.background(Color.White)) {
        // 单选 - Clip 模式
        CheckButtonGroup(
            items = items,
            checked = "选项一",
            onCheckedChange = {}
        )
        // 单选 - Button 模式
        CheckButtonGroup(
            items = items,
            checked = "选项二",
            mode = CheckButtonMode.Button,
            onCheckedChange = {}
        )
        // 多选 - Clip 模式（含禁用项）
        CheckButtonGroup(
            items = items,
            checked = setOf("选项一", "选项三"),
            disabled = setOf("选项五"),
            onCheckedChange = {}
        )
    }
}

