package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavOptions
import com.d10ng.compose.demo.PageTransitions
import com.d10ng.compose.demo.R
import com.d10ng.compose.demo.pages.destinations.AvatarScreenDestination
import com.d10ng.compose.demo.pages.destinations.BadgeScreenDestination
import com.d10ng.compose.demo.pages.destinations.ButtonScreenDestination
import com.d10ng.compose.demo.pages.destinations.CellScreenDestination
import com.d10ng.compose.demo.pages.destinations.CheckButtonScreenDestination
import com.d10ng.compose.demo.pages.destinations.DialogScreenDestination
import com.d10ng.compose.demo.pages.destinations.FieldScreenDestination
import com.d10ng.compose.demo.pages.destinations.NavBarScreenDestination
import com.d10ng.compose.demo.pages.destinations.NotifyScreenDestination
import com.d10ng.compose.demo.pages.destinations.SheetScreenDestination
import com.d10ng.compose.demo.pages.destinations.StepperScreenDestination
import com.d10ng.compose.demo.pages.destinations.StepsScreenDestination
import com.d10ng.compose.demo.pages.destinations.SwitchScreenDestination
import com.d10ng.compose.demo.pages.destinations.TagScreenDestination
import com.d10ng.compose.demo.pages.destinations.ToastScreenDestination
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.base.Cell
import com.d10ng.compose.ui.base.CellGroup
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.Direction

/**
 * 首页
 * @Author d10ng
 * @Date 2023/9/4 13:54
 */
@RootNavGraph(start = true)
@Destination(style = PageTransitions::class)
@Composable
fun HomeScreen(
    nav: DestinationsNavigator
) {
    HomeScreenView(
        onClick = {
            nav.navigate(it, navOptions = NavOptions.Builder().setLaunchSingleTop(true).build())
        }
    )
}

@Composable
private fun HomeScreenView(
    onClick: (Direction) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        // 标题
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                tint = AppColor.Main.primary,
                modifier = Modifier
                    .height(64.dp)
                    .background(Color.White, AppShape.RC.v24)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            ) {
                Text(text = "DLJetpackComposeUtil", style = AppText.Bold.Title.large)
                Text(
                    text = "Jetpack Compose UI 组件库（仿Vant）",
                    style = AppText.Normal.Body.small,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        CellGroup(title = "基础组件", inset = true) {
            Cell(
                title = "Button 按钮",
                link = true,
                onClick = { onClick(ButtonScreenDestination) })
            Cell(
                title = "Cell 单元格",
                link = true,
                onClick = { onClick(CellScreenDestination) })
            Cell(
                title = "Toast 轻提示",
                link = true,
                border = false,
                onClick = { onClick(ToastScreenDestination) })
        }
        CellGroup(title = "表单组件", inset = true) {
            Cell(
                title = "Field 输入框",
                link = true,
                onClick = { onClick(FieldScreenDestination) })
            Cell(
                title = "Switch 开关",
                link = true,
                onClick = { onClick(SwitchScreenDestination) })
            Cell(
                title = "CheckButton 选择按钮",
                link = true,
                onClick = { onClick(CheckButtonScreenDestination) })
            Cell(
                title = "Stepper 步进器",
                link = true,
                onClick = { onClick(StepperScreenDestination) })
        }
        CellGroup(title = "反馈组件", inset = true) {
            Cell(
                title = "Dialog 弹出框",
                link = true,
                onClick = { onClick(DialogScreenDestination) })
            Cell(
                title = "Sheet 操作面板",
                link = true,
                onClick = { onClick(SheetScreenDestination) })
            Cell(
                title = "Notify 消息提示",
                link = true,
                onClick = { onClick(NotifyScreenDestination) })
        }
        CellGroup(title = "展示组件", inset = true) {
            Cell(
                title = "Tag 标签",
                link = true,
                onClick = { onClick(TagScreenDestination) })
            Cell(
                title = "Badge 徽标",
                link = true,
                onClick = { onClick(BadgeScreenDestination) })
            Cell(
                title = "Avatar 头像",
                link = true,
                onClick = { onClick(AvatarScreenDestination) })
            Cell(
                title = "Steps 步骤条",
                link = true,
                onClick = { onClick(StepsScreenDestination) })
        }
        CellGroup(title = "导航组件", inset = true) {
            Cell(
                title = "NavBar 导航栏",
                link = true,
                onClick = { onClick(NavBarScreenDestination) })
        }
        Box(modifier = Modifier.height(32.dp))
    }
}

@Preview
@Composable
private fun HomeScreenViewPreview() {
    HomeScreenView()
}