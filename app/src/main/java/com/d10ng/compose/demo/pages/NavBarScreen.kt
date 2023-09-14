package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.demo.R
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.PageTransitions
import com.d10ng.compose.ui.base.Button
import com.d10ng.compose.ui.base.ButtonSize
import com.d10ng.compose.ui.base.ButtonType
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.navigation.NavBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/**
 * 导航栏
 * @Author d10ng
 * @Date 2023/9/6 13:53
 */
@RootNavGraph
@Destination(style = PageTransitions::class)
@Composable
fun NavBarScreen(
    nav: DestinationsNavigator
) {
    NavBarScreenView(onClickBack = nav::navigateUp)
}

@Composable
private fun NavBarScreenView(
    onClickBack: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "NavBar", onClickBack = onClickBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CellGroup(title = "基础用法", border = false) {
                NavBar(title = "标题", withStatusBar = false)
            }
            CellGroup(title = "返回上级", border = false) {
                NavBar(title = "标题", withStatusBar = false, onClickBack = {})
            }
            CellGroup(title = "标题靠左", border = false) {
                NavBar(
                    title = "标题",
                    withStatusBar = false,
                    onClickBack = {},
                    titleAlignment = Alignment.CenterStart
                )
            }
            CellGroup(title = "右侧按钮", border = false) {
                NavBar(
                    title = "标题",
                    withStatusBar = false,
                    onClickBack = {},
                    titleAlignment = Alignment.CenterStart,
                    border = true,
                    right = {
                        Icon(
                            painter = painterResource(id = R.drawable.round_add_circle_outline_24),
                            contentDescription = "",
                            tint = AppColor.Neutral.tips,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(46.dp)
                                .clip(AppShape.RC.Cycle)
                                .clickable { }
                                .padding(10.dp)
                        )
                    })
                NavBar(
                    title = "标题",
                    withStatusBar = false,
                    onClickBack = {},
                    titleAlignment = Alignment.CenterStart,
                    border = true,
                    right = {
                        Button(
                            text = "保存",
                            modifier = Modifier
                                .padding(end = 16.dp),
                            type = ButtonType.PRIMARY,
                            size = ButtonSize.MINI
                        ) {}
                    })
                NavBar(
                    title = "标题",
                    withStatusBar = false,
                    onClickBack = {},
                    titleAlignment = Alignment.CenterStart,
                    border = true,
                    right = {
                        Button(
                            text = "删除",
                            modifier = Modifier
                                .padding(end = 16.dp),
                            type = ButtonType.DANGER,
                            size = ButtonSize.MINI,
                            plain = true
                        ) {}
                    })
            }
        }
    }
}

@Preview
@Composable
private fun NavBarScreenViewPreview() {
    NavBarScreenView()
}