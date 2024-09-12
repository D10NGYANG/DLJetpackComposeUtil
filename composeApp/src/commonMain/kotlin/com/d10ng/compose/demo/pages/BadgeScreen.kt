package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.compose.ui.show.Badge
import com.d10ng.compose.ui.show.BadgeBox

/**
 * 徽标
 * @Author d10ng
 * @Date 2023/9/12 13:36
 */
class BadgeScreen : Screen {
    @Composable
    override fun Content() {
        BadgeScreenView()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BadgeScreenView() {
    val navigator = LocalNavigator.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "Badge", onClickBack = { navigator?.pop() })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CellGroup(title = "基础用法") {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 19.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    listOf("", "1", "5", "10", "HOT").forEach { item ->
                        BadgeBox(badge = { Badge(item) }) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(AppColor.Neutral.line, AppShape.RC.v8)
                            )
                        }
                    }
                }
            }
            CellGroup(title = "最大值") {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 19.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    listOf(10 to 9, 21 to 20, 100 to 99).forEach { item ->
                        BadgeBox(badge = { Badge(item.first, item.second) }) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(AppColor.Neutral.line, AppShape.RC.v8)
                            )
                        }
                    }
                }
            }
            CellGroup(title = "自定义颜色") {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 19.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    listOf("", "1", "5", "10", "HOT").forEach { item ->
                        BadgeBox(badge = { Badge(item, AppColor.Main.primary) }) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(AppColor.Neutral.line, AppShape.RC.v8)
                            )
                        }
                    }
                }
            }
        }
    }
}