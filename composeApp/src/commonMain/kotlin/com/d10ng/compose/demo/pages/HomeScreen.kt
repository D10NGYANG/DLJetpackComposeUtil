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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.d10ng.compose.demo.Nav
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.base.Cell
import com.d10ng.compose.ui.base.CellGroup
import dljetpackcomposeutil_project.composeapp.generated.resources.Res
import dljetpackcomposeutil_project.composeapp.generated.resources.ic_launcher_foreground
import org.jetbrains.compose.resources.painterResource

/**
 * 首页
 * @Author d10ng
 * @Date 2024/9/12 13:56
 */

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        // 标题
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(19.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(resource = Res.drawable.ic_launcher_foreground),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .height(64.dp)
                    .background(Color.White, AppShape.RC.v24)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 19.dp)
            ) {
                Text(text = "DLJetpackComposeUtil", style = AppText.Bold.Title.large)
                Text(
                    text = "Jetpack Compose UI 组件库（仿Vant）",
                    style = AppText.Normal.Body.small,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        CellGroup(title = "样式配置", inset = true) {
            Cell(
                title = "Color 色彩系统",
                link = true,
                onClick = { Nav.instant().navigate(Nav.ColorRoute) }
            )
            Cell(
                title = "Typography 字体排版",
                link = true,
                onClick = { Nav.instant().navigate(Nav.TypographyRoute) }
            )
            Cell(
                title = "Shape 容器形状",
                link = true,
                onClick = { Nav.instant().navigate(Nav.ShapeRoute) }
            )
        }
        CellGroup(title = "基础组件", inset = true) {
            Cell(
                title = "Button 按钮",
                link = true,
                onClick = { Nav.instant().navigate(Nav.ButtonRoute) }
            )
            Cell(
                title = "Cell 单元格",
                link = true,
                onClick = { Nav.instant().navigate(Nav.CellRoute) }
            )
            Cell(
                title = "Toast 轻提示",
                link = true,
                border = false,
                onClick = { Nav.instant().navigate(Nav.ToastRoute) }
            )
        }
        CellGroup(title = "表单组件", inset = true) {
            Cell(
                title = "Checkbox 复选框",
                link = true,
                onClick = { Nav.instant().navigate(Nav.CheckboxRoute) }
            )
            Cell(
                title = "Field 输入框",
                link = true,
                onClick = { Nav.instant().navigate(Nav.FieldRoute) }
            )
            Cell(
                title = "Switch 开关",
                link = true,
                onClick = { Nav.instant().navigate(Nav.SwitchRoute) }
            )
            Cell(
                title = "CheckButton 选择按钮",
                link = true,
                onClick = { Nav.instant().navigate(Nav.CheckButtonRoute) }
            )
            Cell(
                title = "Stepper 步进器",
                link = true,
                onClick = { Nav.instant().navigate(Nav.StepperRoute) }
            )
            Cell(
                title = "Search 搜索",
                link = true,
                onClick = { Nav.instant().navigate(Nav.SearchRoute) }
            )
        }
        CellGroup(title = "反馈组件", inset = true) {
            Cell(
                title = "Dialog 弹出框",
                link = true,
                onClick = { Nav.instant().navigate(Nav.DialogRoute) }
            )
            Cell(
                title = "Sheet 操作面板",
                link = true,
                onClick = { Nav.instant().navigate(Nav.SheetRoute) }
            )
            Cell(
                title = "Notify 消息提示",
                link = true,
                onClick = { Nav.instant().navigate(Nav.NotifyRoute) }
            )
            Cell(
                title = "PullRefresh 下拉刷新",
                link = true,
                onClick = { Nav.instant().navigate(Nav.PullRefreshRoute) }
            )
        }
        CellGroup(title = "展示组件", inset = true) {
            Cell(
                title = "Tag 标签",
                link = true,
                onClick = { Nav.instant().navigate(Nav.TagRoute) }
            )
            Cell(
                title = "Badge 徽标",
                link = true,
                onClick = { Nav.instant().navigate(Nav.BadgeRoute) }
            )
            Cell(
                title = "Avatar 头像",
                link = true,
                onClick = { Nav.instant().navigate(Nav.AvatarRoute) }
            )
            Cell(
                title = "Steps 步骤条",
                link = true,
                onClick = { Nav.instant().navigate(Nav.StepsRoute) }
            )
            Cell(
                title = "Popover 气泡弹出框",
                link = true,
                onClick = { Nav.instant().navigate(Nav.PopoverRoute) }
            )
        }
        CellGroup(title = "导航组件", inset = true) {
            Cell(
                title = "NavBar 导航栏",
                link = true,
                onClick = { Nav.instant().navigate(Nav.NavBarRoute) }
            )
        }
        Box(modifier = Modifier.height(32.dp))
    }
}