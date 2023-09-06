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
import com.d10ng.compose.demo.PageTransitions
import com.d10ng.compose.demo.R
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.base.Cell
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.base.CellTitle
import com.d10ng.compose.ui.form.Switch
import com.d10ng.compose.ui.navigation.NavBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/**
 * Switch 开关
 * @Author d10ng
 * @Date 2023/9/6 14:29
 */
@RootNavGraph
@Destination(style = PageTransitions::class)
@Composable
fun SwitchScreen(
    nav: DestinationsNavigator
) {
    SwitchScreenView(onClickBack = nav::navigateUp)
}

@Composable
private fun SwitchScreenView(
    onClickBack: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "Switch", onClickBack = onClickBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            val switchModifier = Modifier.padding(start = 16.dp)
            var value1 by remember { mutableStateOf(true) }
            CellTitle(title = "基础用法")
            Switch(checked = value1, onCheckedChange = { value1 = it }, modifier = switchModifier)
            CellTitle(title = "禁用状态")
            Switch(
                checked = value1,
                onCheckedChange = { value1 = it },
                modifier = switchModifier,
                disabled = true
            )
            CellTitle(title = "加载状态")
            Switch(
                checked = value1,
                onCheckedChange = { value1 = it },
                modifier = switchModifier,
                loading = true
            )
            CellTitle(title = "自定义大小")
            Switch(
                checked = value1,
                onCheckedChange = { value1 = it },
                modifier = switchModifier,
                size = 25.dp
            )
            CellTitle(title = "自定义颜色")
            Switch(
                checked = value1,
                onCheckedChange = { value1 = it },
                modifier = switchModifier,
                activeColor = Color.Red
            )
            CellTitle(title = "自定义按钮")
            Switch(
                checked = value1,
                onCheckedChange = { value1 = it },
                modifier = switchModifier,
                iconId = R.drawable.round_add_circle_outline_24
            )
            CellGroup(title = "搭配单元格使用", border = false) {
                Cell(title = "标题", border = false) {
                    Switch(
                        checked = value1,
                        onCheckedChange = { value1 = it },
                        modifier = switchModifier,
                        size = 25.dp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SwitchScreenViewPreview() {
    SwitchScreenView()
}