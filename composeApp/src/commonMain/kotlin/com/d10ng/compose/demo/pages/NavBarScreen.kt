package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.base.Button
import com.d10ng.compose.ui.base.ButtonSize
import com.d10ng.compose.ui.base.ButtonType
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.navigation.NavBar
import dljetpackcomposeutil.composeapp.generated.resources.Res
import dljetpackcomposeutil.composeapp.generated.resources.round_add_circle_outline_24
import org.jetbrains.compose.resources.painterResource

/**
 * 导航栏
 * @Author d10ng
 * @Date 2023/9/6 13:53
 */
class NavBarScreen: Screen {

    @Composable
    override fun Content() {
        NavBarScreenView()
    }
}

@Composable
private fun NavBarScreenView() {
    val navigator = LocalNavigator.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "NavBar", onClickBack = { navigator?.pop() })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CellGroup(title = "基础用法", border = false) {
                NavBar(title = "标题", withStatusBar = false)
                HorizontalDivider(thickness = 8.dp, color = AppColor.Neutral.bg)
                NavBar(
                    title = "标题",
                    withStatusBar = false,
                    titleAlignment = Alignment.CenterStart,
                    border = true
                )
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
                            painter = painterResource(resource = Res.drawable.round_add_circle_outline_24),
                            contentDescription = "",
                            tint = AppColor.Neutral.tips,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(46.dp)
                                .clip(AppShape.RC.Cycle)
                                .clickable { }
                                .padding(10.dp)
                        )
                    }
                )
                HorizontalDivider(thickness = 8.dp, color = AppColor.Neutral.bg)
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
                                .padding(end = 19.dp),
                            type = ButtonType.PRIMARY,
                            size = ButtonSize.SMALL
                        ) {}
                    }
                )
                HorizontalDivider(thickness = 8.dp, color = AppColor.Neutral.bg)
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
                                .padding(end = 19.dp),
                            type = ButtonType.DANGER,
                            size = ButtonSize.SMALL,
                            plain = true
                        ) {}
                    }
                )
                HorizontalDivider(thickness = 8.dp, color = AppColor.Neutral.bg)
                NavBar(
                    title = "标题",
                    withStatusBar = false,
                    titleAlignment = Alignment.CenterStart,
                    border = true,
                    right = {
                        Icon(
                            painter = painterResource(resource = Res.drawable.round_add_circle_outline_24),
                            contentDescription = "",
                            tint = AppColor.Neutral.tips,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(46.dp)
                                .clip(AppShape.RC.Cycle)
                                .clickable { }
                                .padding(10.dp)
                        )
                    }
                )
                HorizontalDivider(thickness = 8.dp, color = AppColor.Neutral.bg)
            }
        }
    }
}