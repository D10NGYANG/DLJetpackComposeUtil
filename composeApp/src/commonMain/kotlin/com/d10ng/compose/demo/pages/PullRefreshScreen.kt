package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.d10ng.compose.demo.Nav
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.base.Cell
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.feedback.PullRefreshIndicator
import com.d10ng.compose.ui.feedback.pullRefresh
import com.d10ng.compose.ui.feedback.rememberPullRefreshState
import com.d10ng.compose.ui.navigation.NavBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 下拉刷新
 * @Author d10ng
 * @Date 2023/11/8 18:29
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullRefreshScreen() {
    var isRefreshing by remember {
        mutableStateOf(false)
    }
    val state = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        isRefreshing = true
        CoroutineScope(Dispatchers.Default).launch {
            delay(1000)
            isRefreshing = false
        }
    })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "PullRefreshScreen", onClickBack = { Nav.instant().popBackStack() })
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.pullRefresh(state),
                verticalArrangement = Arrangement.spacedBy(19.dp)
            ) {
                items(listOf("test 1", "test 2")) {
                    CellGroup(
                        title = "基础用法"
                    ) {
                        Cell(title = it)
                    }
                }
            }
            PullRefreshIndicator(refreshing = isRefreshing, state = state,
                modifier = Modifier
                    .align(Alignment.TopCenter)
            )
        }
    }
}