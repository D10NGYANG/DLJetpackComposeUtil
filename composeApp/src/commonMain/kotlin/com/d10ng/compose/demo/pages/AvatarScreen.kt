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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.compose.ui.show.Avatar
import com.d10ng.compose.ui.show.Badge
import com.d10ng.compose.ui.show.BadgeBox
import dljetpackcomposeutil.composeapp.generated.resources.Res
import dljetpackcomposeutil.composeapp.generated.resources.ic_avatar_40
import dljetpackcomposeutil.composeapp.generated.resources.ic_command_center_avatar_40
import dljetpackcomposeutil.composeapp.generated.resources.ic_group_avatar_40
import org.jetbrains.compose.resources.painterResource

/**
 * 头像
 * @Author d10ng
 * @Date 2023/9/12 16:13
 */
class AvatarScreen : Screen {
    @Composable
    override fun Content() {
        AvatarScreenView()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AvatarScreenView() {
    val navigator = LocalNavigator.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "Avatar", onClickBack = { navigator?.pop() })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CellGroup(title = "基础用法") {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(19.dp),
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
                        .padding(19.dp),
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
                            painter = painterResource(resource = Res.drawable.ic_avatar_40),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Avatar(char = ' ') {
                        Image(
                            painter = painterResource(resource = Res.drawable.ic_group_avatar_40),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Avatar(char = ' ') {
                        Image(
                            painter = painterResource(resource = Res.drawable.ic_command_center_avatar_40),
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
                        .padding(19.dp),
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
                        .padding(19.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Avatar(char = '杨', size = 48.dp)
                    Avatar(char = '杨', size = 32.dp, fontSize = 16.sp)
                }
            }
        }
    }
}