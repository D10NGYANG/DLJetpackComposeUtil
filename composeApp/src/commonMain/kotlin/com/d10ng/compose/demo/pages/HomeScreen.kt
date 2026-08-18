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
import androidx.navigation3.runtime.NavKey
import com.d10ng.compose.demo.AvatarRoute
import com.d10ng.compose.demo.BadgeRoute
import com.d10ng.compose.demo.ButtonRoute
import com.d10ng.compose.demo.CellRoute
import com.d10ng.compose.demo.CheckButtonRoute
import com.d10ng.compose.demo.CheckboxRoute
import com.d10ng.compose.demo.ColorRoute
import com.d10ng.compose.demo.DialogRoute
import com.d10ng.compose.demo.FieldRoute
import com.d10ng.compose.demo.IndexBarRoute
import com.d10ng.compose.demo.NavBarRoute
import com.d10ng.compose.demo.NotifyRoute
import com.d10ng.compose.demo.PopoverRoute
import com.d10ng.compose.demo.PullRefreshRoute
import com.d10ng.compose.demo.SearchRoute
import com.d10ng.compose.demo.ShapeRoute
import com.d10ng.compose.demo.SheetRoute
import com.d10ng.compose.demo.StepperRoute
import com.d10ng.compose.demo.StepsRoute
import com.d10ng.compose.demo.SwitchRoute
import com.d10ng.compose.demo.TagRoute
import com.d10ng.compose.demo.ToastRoute
import com.d10ng.compose.demo.TypographyRoute
import com.d10ng.compose.demo.resources.Res
import com.d10ng.compose.demo.resources.ic_launcher_foreground
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.base.Cell
import com.d10ng.compose.ui.base.CellGroup
import org.jetbrains.compose.resources.painterResource

/**
 * 首页
 * @Author d10ng
 * @Date 2024/9/12 13:56
 */

@Composable
fun HomeScreen(onNavigate: (NavKey) -> Unit = {}) {
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
                onClick = { onNavigate(ColorRoute) }
            )
            Cell(
                title = "Typography 字体排版",
                link = true,
                onClick = { onNavigate(TypographyRoute) }
            )
            Cell(
                title = "Shape 容器形状",
                link = true,
                onClick = { onNavigate(ShapeRoute) }
            )
        }
        CellGroup(title = "基础组件", inset = true) {
            Cell(
                title = "Button 按钮",
                link = true,
                onClick = { onNavigate(ButtonRoute) }
            )
            Cell(
                title = "Cell 单元格",
                link = true,
                onClick = { onNavigate(CellRoute) }
            )
            Cell(
                title = "Toast 轻提示",
                link = true,
                border = false,
                onClick = { onNavigate(ToastRoute) }
            )
        }
        CellGroup(title = "表单组件", inset = true) {
            Cell(
                title = "Checkbox 复选框",
                link = true,
                onClick = { onNavigate(CheckboxRoute) }
            )
            Cell(
                title = "Field 输入框",
                link = true,
                onClick = { onNavigate(FieldRoute) }
            )
            Cell(
                title = "Switch 开关",
                link = true,
                onClick = { onNavigate(SwitchRoute) }
            )
            Cell(
                title = "CheckButton 选择按钮",
                link = true,
                onClick = { onNavigate(CheckButtonRoute) }
            )
            Cell(
                title = "Stepper 步进器",
                link = true,
                onClick = { onNavigate(StepperRoute) }
            )
            Cell(
                title = "Search 搜索",
                link = true,
                onClick = { onNavigate(SearchRoute) }
            )
        }
        CellGroup(title = "反馈组件", inset = true) {
            Cell(
                title = "Dialog 弹出框",
                link = true,
                onClick = { onNavigate(DialogRoute) }
            )
            Cell(
                title = "Sheet 操作面板",
                link = true,
                onClick = { onNavigate(SheetRoute) }
            )
            Cell(
                title = "Notify 消息提示",
                link = true,
                onClick = { onNavigate(NotifyRoute) }
            )
            Cell(
                title = "PullRefresh 下拉刷新",
                link = true,
                onClick = { onNavigate(PullRefreshRoute) }
            )
        }
        CellGroup(title = "展示组件", inset = true) {
            Cell(
                title = "Tag 标签",
                link = true,
                onClick = { onNavigate(TagRoute) }
            )
            Cell(
                title = "Badge 徽标",
                link = true,
                onClick = { onNavigate(BadgeRoute) }
            )
            Cell(
                title = "Avatar 头像",
                link = true,
                onClick = { onNavigate(AvatarRoute) }
            )
            Cell(
                title = "Steps 步骤条",
                link = true,
                onClick = { onNavigate(StepsRoute) }
            )
            Cell(
                title = "Popover 气泡弹出框",
                link = true,
                onClick = { onNavigate(PopoverRoute) }
            )
        }
        CellGroup(title = "导航组件", inset = true) {
            Cell(
                title = "NavBar 导航栏",
                link = true,
                onClick = { onNavigate(NavBarRoute) }
            )
            Cell(
                title = "IndexBar 索引栏",
                link = true,
                onClick = { onNavigate(IndexBarRoute) }
            )
        }
        Box(modifier = Modifier.height(32.dp))
    }
}