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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.form.CheckButtonGroup
import com.d10ng.compose.ui.form.CheckButtonMode
import com.d10ng.compose.ui.navigation.NavBar

/**
 * 选择按钮
 * @Author d10ng
 * @Date 2023/9/6 16:55
 */
class CheckButtonScreen : Screen {
    @Composable
    override fun Content() {
        CheckButtonScreenView()
    }
}

@Composable
private fun CheckButtonScreenView() {
    val navigator = LocalNavigator.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "CheckButton", onClickBack = { navigator?.pop() })
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
            CellGroup(title = "按钮样式", border = false) {
                val items = remember {
                    setOf("1min", "5min", "10min", "30min", "60min")
                }
                var checked by remember {
                    mutableStateOf(items.first())
                }
                CheckButtonGroup(
                    items = items,
                    checked = checked,
                    onCheckedChange = { checked = it },
                    activeColor = Color.Red,
                    mode = CheckButtonMode.Button
                )
            }
        }
    }
}