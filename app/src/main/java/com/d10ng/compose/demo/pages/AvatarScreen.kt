package com.d10ng.compose.demo.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.d10ng.compose.demo.PageTransitions
import com.d10ng.compose.demo.R
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.compose.ui.show.Avatar
import com.d10ng.compose.ui.show.Badge
import com.d10ng.compose.ui.show.BadgeBox
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/**
 * 头像
 * @Author d10ng
 * @Date 2023/9/12 16:13
 */
@RootNavGraph
@Destination(style = PageTransitions::class)
@Composable
fun AvatarScreen(
    nav: DestinationsNavigator
) {
    AvatarScreenView(onClickBack = nav::navigateUp)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AvatarScreenView(
    onClickBack: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "Avatar", onClickBack = onClickBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CellGroup(title = "基础用法") {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Avatar(char = 'A')
                    Avatar(char = '杨')
                    Avatar(char = '吴', backgroundColor = AppColor.Main.primary)
                    Avatar(
                        char = '张',
                        backgroundColor = AppColor.Neutral.line,
                        contentColor = Color.Black
                    )
                    Avatar(char = 'B', shape = AppShape.RC.v8)
                }
            }
            CellGroup(title = "自定义背景") {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Avatar(char = 'A') {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .rotate(-45f)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFFFEB76E),
                                            Color(0xFFFD5216)
                                        )
                                    ), AppShape.RC.Cycle
                                )
                        )
                    }
                    Avatar(char = 'B') {
                        Image(
                            painter = painterResource(id = R.drawable.ic_avatar_40),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Avatar(char = ' ') {
                        Image(
                            painter = painterResource(id = R.drawable.ic_group_avatar_40),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Avatar(char = ' ') {
                        Image(
                            painter = painterResource(id = R.drawable.ic_command_center_avatar_40),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
            CellGroup(title = "与Badge一起使用") {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BadgeBox(badge = { Badge() }) {
                        Avatar(char = 'A', shape = AppShape.RC.v8)
                    }
                    BadgeBox(badge = { Badge(12) }) {
                        Avatar(char = 'B')
                    }
                    BadgeBox(badge = { Badge(100) }) {
                        Avatar(char = 'C')
                    }
                }
            }
            CellGroup(title = "自定义尺寸") {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Avatar(char = '杨', size = 48.dp)
                    Avatar(char = '杨', size = 32.dp, fontSize = 16.sp)
                }
            }
        }
    }
}

@Preview
@Composable
private fun AvatarScreenPreview() {
    AvatarScreenView()
}