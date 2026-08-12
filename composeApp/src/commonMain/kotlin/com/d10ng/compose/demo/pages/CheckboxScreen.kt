package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.base.Cell
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.base.CellTitle
import com.d10ng.compose.ui.form.Checkbox
import com.d10ng.compose.ui.form.CheckboxType
import com.d10ng.compose.ui.navigation.NavBar

/**
 * 复选框
 * @Author d10ng
 * @Date 2023/11/16 03:14
 */
@Composable
@OptIn(ExperimentalLayoutApi::class)
fun CheckboxScreen(onBack: () -> Unit = {}) {
    var squareChecked by remember { mutableStateOf(false) }
    var circleChecked by remember { mutableStateOf(true) }
    var cellChecked by remember { mutableStateOf(false) }
    var disabledCallbackCount by remember { mutableIntStateOf(0) }
    val sizeStates = remember {
        mutableStateMapOf(16 to true, 22 to true, 30 to true, 50 to true)
    }
    var redChecked by remember { mutableStateOf(true) }
    var borderChecked by remember { mutableStateOf(false) }
    var greenCircleChecked by remember { mutableStateOf(true) }
    var largeCircleChecked by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "Checkbox", onClickBack = { onBack() })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CellTitle(title = "方形 · Material 3")
            CheckboxExample(
                label = if (squareChecked) "方形已选中" else "方形未选中",
                checked = squareChecked,
                onCheckedChange = { squareChecked = it }
            )
            CellTitle(title = "圆形 · 兼容变体")
            CheckboxExample(
                label = if (circleChecked) "圆形已选中" else "圆形未选中",
                checked = circleChecked,
                onCheckedChange = { circleChecked = it },
                type = CheckboxType.CIRCLE
            )
            CellTitle(title = "禁用状态")
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 19.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CheckboxExample(
                    label = "禁用未选中",
                    checked = false,
                    onCheckedChange = { disabledCallbackCount += 1 },
                    disabled = true,
                    compact = true
                )
                CheckboxExample(
                    label = "禁用已选中",
                    checked = true,
                    onCheckedChange = { disabledCallbackCount += 1 },
                    disabled = true,
                    compact = true
                )
                CheckboxExample(
                    label = "禁用圆形未选中",
                    checked = false,
                    onCheckedChange = { disabledCallbackCount += 1 },
                    disabled = true,
                    type = CheckboxType.CIRCLE,
                    compact = true
                )
                CheckboxExample(
                    label = "禁用圆形已选中",
                    checked = true,
                    onCheckedChange = { disabledCallbackCount += 1 },
                    disabled = true,
                    type = CheckboxType.CIRCLE,
                    compact = true
                )
            }
            Text(
                text = "禁用回调：$disabledCallbackCount 次",
                modifier = Modifier.padding(horizontal = 19.dp)
            )
            CellTitle(title = "自定义大小")
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 19.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                sizeStates.keys.sorted().forEach { sizeValue ->
                    Checkbox(
                        checked = sizeStates.getValue(sizeValue),
                        onCheckedChange = { sizeStates[sizeValue] = it },
                        size = sizeValue.dp,
                        modifier = Modifier.semantics {
                            contentDescription = "$sizeValue dp 方形复选框"
                        }
                    )
                }
                Checkbox(
                    checked = largeCircleChecked,
                    onCheckedChange = { largeCircleChecked = it },
                    size = 30.dp,
                    type = CheckboxType.CIRCLE,
                    modifier = Modifier.semantics { contentDescription = "30 dp 圆形复选框" }
                )
            }
            CellTitle(title = "自定义颜色")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 19.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                Checkbox(
                    checked = redChecked,
                    onCheckedChange = { redChecked = it },
                    activeColor = Color.Red,
                    modifier = Modifier.semantics { contentDescription = "红色选中" }
                )
                Checkbox(
                    checked = borderChecked,
                    onCheckedChange = { borderChecked = it },
                    inactiveColor = AppColor.Main.primary,
                    modifier = Modifier.semantics { contentDescription = "主题色边框" }
                )
                Checkbox(
                    checked = greenCircleChecked,
                    onCheckedChange = { greenCircleChecked = it },
                    type = CheckboxType.CIRCLE,
                    activeColor = AppColor.Func.success,
                    modifier = Modifier.semantics { contentDescription = "绿色圆形" }
                )
            }
            CellTitle(title = "自定义禁用颜色")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 19.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                Checkbox(
                    checked = false,
                    disabled = true,
                    disabledColor = Color(0xFF7E57C2),
                    modifier = Modifier.semantics { contentDescription = "紫色禁用未选中" }
                )
                Checkbox(
                    checked = true,
                    disabled = true,
                    disabledColor = Color(0xFF7E57C2),
                    modifier = Modifier.semantics { contentDescription = "紫色禁用已选中" }
                )
            }
            CellGroup(title = "搭配单元格使用", border = false) {
                Cell(title = if (cellChecked) "单元格已选中" else "单元格未选中", border = false) {
                    Checkbox(
                        checked = cellChecked,
                        onCheckedChange = { cellChecked = it },
                        modifier = Modifier.semantics { contentDescription = "单元格复选框" }
                    )
                }
            }
        }
    }
}

@Composable
private fun CheckboxExample(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit = {},
    disabled: Boolean = false,
    type: CheckboxType = CheckboxType.SQUARE,
    compact: Boolean = false
) {
    Column(
        modifier = if (compact) Modifier else Modifier.padding(horizontal = 19.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            disabled = disabled,
            type = type,
            modifier = Modifier.semantics { contentDescription = label }
        )
        Text(text = label)
    }
}
