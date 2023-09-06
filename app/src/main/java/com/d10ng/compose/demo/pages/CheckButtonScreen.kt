package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.d10ng.compose.demo.PageTransitions
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.form.CheckButtonGroup
import com.d10ng.compose.ui.navigation.NavBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/**
 * 选择按钮
 * @Author d10ng
 * @Date 2023/9/6 16:55
 */
@RootNavGraph
@Destination(style = PageTransitions::class)
@Composable
fun CheckButtonScreen(
    nav: DestinationsNavigator
) {
    CheckButtonScreenView(onClickBack = nav::navigateUp)
}

@Composable
private fun CheckButtonScreenView(
    onClickBack: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "CheckButton", onClickBack = onClickBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CellGroup(title = "基础用法", border = false) {
                val items = remember {
                    setOf("滑坠", "山洪", "溺水", "车祸", "落石", "失温", "中暑", "心脏病", "高山病", "其他")
                }
                var checked by remember {
                    mutableStateOf(items.first())
                }
                CheckButtonGroup(
                    items = items,
                    checked = checked,
                    onCheckedChange = { checked = it }
                )
            }
            CellGroup(title = "修改颜色", border = false) {
                val items = remember {
                    setOf("伤势一般", "伤势中等", "伤势严重", "无伤势")
                }
                var checked by remember {
                    mutableStateOf(items.first())
                }
                CheckButtonGroup(
                    items = items,
                    checked = checked,
                    onCheckedChange = { checked = it },
                    activeColor = Color.Red
                )
            }
            CellGroup(title = "多选模式", border = false) {
                val items = remember {
                    setOf("状态良好", "一切顺利", "到达终点", "到达营地", "正在攀登",
                        "到达山腰", "到达山顶", "航行正常", "已靠岸", "飞行正常", "已落地", "安全", "到达")
                }
                var checked by remember {
                    mutableStateOf(setOf("安全", "到达"))
                }
                CheckButtonGroup(
                    items = items,
                    checked = checked,
                    onCheckedChange = { checked = it },
                    activeColor = Color(0xFF2ABF55)
                )
            }
        }
    }
}

@Preview
@Composable
private fun CheckButtonScreenViewPreview() {
    CheckButtonScreenView()
}