package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.PageTransitions
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.form.Search
import com.d10ng.compose.ui.navigation.NavBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/**
 * 搜索
 * @Author d10ng
 * @Date 2023/9/14 18:33
 */
@RootNavGraph
@Destination(style = PageTransitions::class)
@Composable
fun SearchScreen(
    nav: DestinationsNavigator
) {
    SearchScreenView(onClickBack = nav::navigateUp)
}

@Composable
private fun SearchScreenView(
    onClickBack: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "Search", onClickBack = onClickBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CellGroup(title = "基础用法") {
                var value1 by remember {
                    mutableStateOf("")
                }
                Search(value = value1, onValueChange = { value1 = it })
                Search(
                    value = value1,
                    onValueChange = { value1 = it },
                    placeholder = "自定义placeholder"
                )
                Search(value = value1, onValueChange = { value1 = it }, label = "地址")
            }
            CellGroup(title = "取消按钮") {
                var value1 by remember {
                    mutableStateOf("")
                }
                Search(value = value1, onValueChange = { value1 = it }, onClickCancel = {})
                Search(
                    value = value1,
                    onValueChange = { value1 = it },
                    onClickCancel = {},
                    cancelText = "cancel"
                )
            }
            CellGroup(title = "中心对齐") {
                var value1 by remember {
                    mutableStateOf("")
                }
                Search(value = value1, onValueChange = { value1 = it }, align = TextAlign.Center)
            }
            CellGroup(title = "禁用对话框") {
                var value1 by remember {
                    mutableStateOf("")
                }
                Search(value = value1, onValueChange = { value1 = it }, disabled = true)
            }
            CellGroup(title = "自定义样式") {
                var value1 by remember {
                    mutableStateOf("")
                }
                Search(value = value1, onValueChange = { value1 = it }, shape = AppShape.RC.v0)
                Search(value = value1, onValueChange = { value1 = it }, shape = AppShape.RC.Cycle)
                Search(
                    value = value1,
                    onValueChange = { value1 = it },
                    backgroundColor = Color.Yellow
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Red)
                ) {
                    Search(value = value1, onValueChange = { value1 = it }, shape = AppShape.RC.v0)
                }
            }
        }
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    SearchScreenView()
}