package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.d10ng.compose.demo.resources.Res
import com.d10ng.compose.demo.resources.round_mood_24
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.compose.view.ListItemMenu
import com.d10ng.compose.view.ListMenuItem

private val themeMenus = listOf(
    ListItemMenu(show = "跟随系统", data = "system"),
    ListItemMenu(show = "浅色模式", data = "light"),
    ListItemMenu(show = "深色模式", data = "dark")
)

private val longMenus = (1..18).map { index ->
    ListItemMenu(show = "第 $index 个滚动菜单选项", data = index)
}

private val roleMenus = listOf(
    ListItemMenu(show = "访客", data = 1),
    ListItemMenu(show = "成员", data = 2),
    ListItemMenu(show = "管理员", data = 3)
)

private val longTextMenus = listOf(
    ListItemMenu(
        show = "这是一条用于检查窄屏截断与测量的超长菜单显示文本",
        data = "long"
    ),
    ListItemMenu(show = "较短选项", data = "short")
)

@Composable
fun LibraryListMenuItemScreen(onBack: () -> Unit = {}) {
    var theme by remember { mutableStateOf<Any?>("unknown") }
    var role by remember { mutableStateOf<Any?>(2) }
    var longSelection by remember { mutableStateOf<Any?>(null) }
    var longTextSelection by remember { mutableStateOf<Any?>("long") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        NavBar(title = "ListMenuItem 列表菜单", onClickBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ListMenuItemSection("未匹配值与受控选择") {
                ListMenuItem(
                    modifier = Modifier.semantics { contentDescription = "主题列表菜单" },
                    title = "主题",
                    value = theme,
                    placeholder = "请选择主题",
                    inputWidth = 200.dp,
                    menus = themeMenus,
                    onMenuSelect = { theme = it.data }
                )
                ListMenuItemStatus(
                    if (theme == "unknown") "当前 value 未匹配，显示 placeholder"
                    else "当前 data：$theme"
                )
                Button(
                    onClick = { theme = "unknown" },
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text("外部重置为未匹配值")
                }
            }

            ListMenuItemSection("图标、必填与初始选中") {
                ListMenuItem(
                    modifier = Modifier.semantics { contentDescription = "角色列表菜单" },
                    title = "角色",
                    note = "初始 value 映射为成员",
                    iconResource = Res.drawable.round_mood_24,
                    iconColorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    isShowTitleStar = true,
                    value = role,
                    inputWidth = 176.dp,
                    menus = roleMenus,
                    onMenuSelect = { role = it.data }
                )
                ListMenuItemStatus("当前 data：$role")
            }

            ListMenuItemSection("空菜单") {
                ListMenuItem(
                    modifier = Modifier.semantics { contentDescription = "空列表菜单" },
                    title = "空列表",
                    note = "箭头会切换，但不会出现选项或空态提示",
                    value = null,
                    placeholder = "没有可选项",
                    inputWidth = 200.dp,
                    menus = emptyList()
                )
            }

            ListMenuItemSection("长菜单滚动") {
                ListMenuItem(
                    modifier = Modifier.semantics { contentDescription = "长列表菜单" },
                    title = "选项",
                    value = longSelection,
                    placeholder = "展开 18 个选项",
                    inputWidth = 220.dp,
                    menus = longMenus,
                    onMenuSelect = { longSelection = it.data }
                )
                ListMenuItemStatus(
                    longSelection?.let { "已选择第 $it 项" } ?: "菜单最大高度 200dp，可滚动到底部"
                )
            }

            ListMenuItemSection("自定义样式与长文本") {
                ListMenuItem(
                    modifier = Modifier.semantics { contentDescription = "自定义列表菜单" },
                    title = "用于窄屏检查的菜单标题",
                    note = "较长说明会占用标签区宽度",
                    value = longTextSelection,
                    inputWidth = null,
                    inputBackgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                    inputBackgroundShape = RoundedCornerShape(10.dp),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        fontWeight = FontWeight.SemiBold
                    ),
                    menus = longTextMenus,
                    onMenuSelect = { longTextSelection = it.data }
                )
                ListMenuItemStatus("用于检查 390px 下长标题、长说明、长选项和自适应宽度")
            }
        }
    }
}

@Composable
private fun ListMenuItemSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        content()
    }
}

@Composable
private fun ListMenuItemStatus(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 16.dp),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
