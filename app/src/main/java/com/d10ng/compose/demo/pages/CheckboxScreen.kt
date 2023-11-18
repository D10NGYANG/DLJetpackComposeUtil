package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.PageTransitions
import com.d10ng.compose.ui.base.Cell
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.base.CellTitle
import com.d10ng.compose.ui.form.Checkbox
import com.d10ng.compose.ui.form.CheckboxType
import com.d10ng.compose.ui.navigation.NavBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/**
 * 复选框
 * @Author d10ng
 * @Date 2023/11/16 03:14
 */
@RootNavGraph
@Destination(style = PageTransitions::class)
@Composable
fun CheckboxScreen(
    nav: DestinationsNavigator
) {
    CheckboxScreenView(onClickBack = nav::navigateUp)
}

@Composable
private fun CheckboxScreenView(
    onClickBack: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "Checkbox", onClickBack = onClickBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            val checkboxModifier = Modifier.padding(start = 19.dp)
            var value1 by remember { mutableStateOf(true) }
            CellTitle(title = "基础用法")
            Checkbox(checked = value1, onCheckedChange = { value1 = it }, modifier = checkboxModifier)
            CellTitle(title = "禁用状态")
            Checkbox(checked = value1, onCheckedChange = { value1 = it }, modifier = checkboxModifier, disabled = true)
            CellTitle(title = "圆形样式")
            Checkbox(checked = value1, onCheckedChange = { value1 = it }, modifier = checkboxModifier, type = CheckboxType.CIRCLE)
            CellTitle(title = "自定义大小")
            Checkbox(checked = value1, onCheckedChange = { value1 = it }, modifier = checkboxModifier, size = 50.dp)
            CellTitle(title = "自定义颜色")
            Checkbox(checked = value1, onCheckedChange = { value1 = it }, modifier = checkboxModifier, activeColor = Color.Red)
            CellGroup(title = "搭配单元格使用", border = false) {
                Cell(title = "标题", border = false) {
                    Checkbox(checked = value1, onCheckedChange = { value1 = it }, modifier = checkboxModifier)
                }
            }
        }
    }
}

@Preview
@Composable
private fun CheckboxScreenPreview() {
    CheckboxScreenView()
}