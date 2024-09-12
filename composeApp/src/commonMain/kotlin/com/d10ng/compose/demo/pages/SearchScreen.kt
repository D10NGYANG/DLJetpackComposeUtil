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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.form.Search
import com.d10ng.compose.ui.navigation.NavBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 搜索
 * @Author d10ng
 * @Date 2023/9/14 18:33
 */
class SearchScreen : Screen {
    @Composable
    override fun Content() {
        SearchScreenView()
    }
}

@Composable
private fun SearchScreenView() {
    val navigator = LocalNavigator.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "Search", onClickBack = { navigator?.pop() })
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
            CellGroup(title = "动作按钮") {
                var value1 by remember {
                    mutableStateOf("")
                }
                Search(value = value1, onValueChange = { value1 = it }, onClickAction = {})
                Search(
                    value = value1,
                    onValueChange = { value1 = it },
                    onClickAction = {},
                    actionText = "cancel"
                )
                Search(
                    value = value1,
                    onValueChange = { value1 = it },
                    onClickAction = {},
                    actionText = "搜索"
                )
                Search(
                    value = value1,
                    onValueChange = { value1 = it },
                    onClickAction = {},
                    actionText = "搜索",
                    actionTextStyle = AppText.Normal.Primary.default
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
            CellGroup(title = "输入加载") {
                var value1 by remember {
                    mutableStateOf("")
                }
                var loading by remember {
                    mutableStateOf(false)
                }
                var loadingJob by remember {
                    mutableStateOf<Job?>(null)
                }
                Search(
                    value = value1,
                    onValueChange = {
                        value1 = it
                    },
                    loading = loading,
                    actionText = "搜索",
                    onClickAction = {
                        loading = true
                        loadingJob?.cancel()
                        loadingJob = CoroutineScope(Dispatchers.IO).launch {
                            delay(1000)
                            loading = false
                        }
                    }
                )
                Search(
                    value = value1,
                    onValueChange = {
                        value1 = it
                        loading = true
                        loadingJob?.cancel()
                        loadingJob = CoroutineScope(Dispatchers.IO).launch {
                            delay(1000)
                            loading = false
                        }
                    },
                    loading = loading
                )
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