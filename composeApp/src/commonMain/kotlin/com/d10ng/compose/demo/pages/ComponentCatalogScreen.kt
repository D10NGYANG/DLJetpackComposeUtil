package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.d10ng.compose.demo.AvatarRoute
import com.d10ng.compose.demo.BadgeRoute
import com.d10ng.compose.demo.ButtonRoute
import com.d10ng.compose.demo.CellRoute
import com.d10ng.compose.demo.CheckButtonRoute
import com.d10ng.compose.demo.CheckboxRoute
import com.d10ng.compose.demo.ColorRoute
import com.d10ng.compose.demo.ComponentCategory
import com.d10ng.compose.demo.ComponentCategoryRoute
import com.d10ng.compose.demo.ComponentSource
import com.d10ng.compose.demo.DialogRoute
import com.d10ng.compose.demo.FieldRoute
import com.d10ng.compose.demo.IndexBarRoute
import com.d10ng.compose.demo.MaterialButtonRoute
import com.d10ng.compose.demo.MaterialBadgeRoute
import com.d10ng.compose.demo.MaterialCheckboxRoute
import com.d10ng.compose.demo.MaterialChipRoute
import com.d10ng.compose.demo.MaterialListItemRoute
import com.d10ng.compose.demo.MaterialProgressIndicatorRoute
import com.d10ng.compose.demo.MaterialRadioButtonRoute
import com.d10ng.compose.demo.MaterialSliderRoute
import com.d10ng.compose.demo.MaterialSnackbarRoute
import com.d10ng.compose.demo.MaterialSwitchRoute
import com.d10ng.compose.demo.MaterialTextFieldRoute
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
import com.d10ng.compose.ui.base.Cell
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.navigation.NavBar

private data class ComponentPage(
    val title: String,
    val route: NavKey? = null
)

private val categoryTitles = mapOf(
    ComponentCategory.THEME to "样式配置",
    ComponentCategory.BASIC to "基础组件",
    ComponentCategory.FORM to "表单组件",
    ComponentCategory.FEEDBACK to "反馈组件",
    ComponentCategory.DISPLAY to "展示组件",
    ComponentCategory.NAVIGATION to "导航组件",
    ComponentCategory.DOMAIN to "领域组件",
    ComponentCategory.MODIFIER to "Modifier 扩展",
    ComponentCategory.PLATFORM to "平台能力"
)

private val libraryPages = mapOf(
    ComponentCategory.THEME to listOf(
        ComponentPage("Color 色彩系统", ColorRoute),
        ComponentPage("Typography 字体排版", TypographyRoute),
        ComponentPage("Shape 容器形状", ShapeRoute)
    ),
    ComponentCategory.BASIC to listOf(
        ComponentPage("Button 按钮", ButtonRoute),
        ComponentPage("Cell 单元格", CellRoute)
    ),
    ComponentCategory.FORM to listOf(
        ComponentPage("Checkbox 复选框", CheckboxRoute),
        ComponentPage("Field 输入框", FieldRoute),
        ComponentPage("Switch 开关", SwitchRoute),
        ComponentPage("CheckButton 选择按钮", CheckButtonRoute),
        ComponentPage("Stepper 步进器", StepperRoute),
        ComponentPage("Search 搜索", SearchRoute),
        ComponentPage("Input 输入原语"),
        ComponentPage("MenuInput 菜单输入"),
        ComponentPage("RadioCell 单选行"),
        ComponentPage("Picker 滚轮选择"),
        ComponentPage("DatePicker 日期选择"),
        ComponentPage("TimePicker 时间选择")
    ),
    ComponentCategory.FEEDBACK to listOf(
        ComponentPage("Toast 轻提示", ToastRoute),
        ComponentPage("Dialog 弹出框", DialogRoute),
        ComponentPage("Sheet 操作面板", SheetRoute),
        ComponentPage("Notify 消息提示", NotifyRoute),
        ComponentPage("PullRefresh 下拉刷新", PullRefreshRoute),
        ComponentPage("Overlay 遮罩")
    ),
    ComponentCategory.DISPLAY to listOf(
        ComponentPage("Tag 标签", TagRoute),
        ComponentPage("Badge 徽标", BadgeRoute),
        ComponentPage("Avatar 头像", AvatarRoute),
        ComponentPage("Steps 步骤条", StepsRoute),
        ComponentPage("Popover 气泡弹出框", PopoverRoute),
        ComponentPage("Divider 分割线")
    ),
    ComponentCategory.NAVIGATION to listOf(
        ComponentPage("NavBar 导航栏", NavBarRoute),
        ComponentPage("IndexBar 索引栏", IndexBarRoute)
    ),
    ComponentCategory.DOMAIN to listOf(
        ComponentPage("BDSignalBeamBox 信号波束")
    ),
    ComponentCategory.MODIFIER to listOf(
        ComponentPage("dashBorder 虚线边框"),
        ComponentPage("debounceClick 防抖点击")
    ),
    ComponentCategory.PLATFORM to listOf(
        ComponentPage("ImagePreview 图片预览")
    )
)

private val materialPages = mapOf(
    ComponentCategory.BASIC to listOf(
        ComponentPage("Button 按钮族", MaterialButtonRoute),
        ComponentPage("ListItem 列表项", MaterialListItemRoute),
        ComponentPage("Divider 分割线")
    ),
    ComponentCategory.FORM to listOf(
        ComponentPage("Checkbox 复选框", MaterialCheckboxRoute),
        ComponentPage("RadioButton 单选按钮", MaterialRadioButtonRoute),
        ComponentPage("Switch 开关", MaterialSwitchRoute),
        ComponentPage("TextField 文本输入", MaterialTextFieldRoute),
        ComponentPage("Slider 滑块", MaterialSliderRoute),
        ComponentPage("Chip 标签控件", MaterialChipRoute)
    ),
    ComponentCategory.FEEDBACK to listOf(
        ComponentPage("Snackbar 消息条", MaterialSnackbarRoute),
        ComponentPage("AlertDialog 弹出框"),
        ComponentPage("ModalBottomSheet 底部面板"),
        ComponentPage("ProgressIndicator 进度指示", MaterialProgressIndicatorRoute)
    ),
    ComponentCategory.DISPLAY to listOf(
        ComponentPage("Badge 徽标", MaterialBadgeRoute)
    ),
    ComponentCategory.NAVIGATION to listOf(
        ComponentPage("TopAppBar 顶部栏")
    ),
    ComponentCategory.PLATFORM to listOf(
        ComponentPage("Adaptive 自适应布局")
    )
)

@Composable
fun ComponentCatalogScreen(
    source: ComponentSource,
    onNavigate: (NavKey) -> Unit,
    onBack: () -> Unit
) {
    val pages = pagesFor(source)
    CatalogScaffold(
        title = if (source == ComponentSource.LIBRARY) "自有 UI 组件" else "Material 3 组件",
        onBack = onBack
    ) {
        CellGroup(title = "组件分类", inset = true) {
            pages.keys.forEachIndexed { index, category ->
                Cell(
                    title = categoryTitles.getValue(category),
                    value = "${pages.getValue(category).size} 项",
                    link = true,
                    border = index != pages.size - 1,
                    onClick = {
                        onNavigate(ComponentCategoryRoute(source, category))
                    }
                )
            }
        }
    }
}

@Composable
fun ComponentCategoryScreen(
    source: ComponentSource,
    category: ComponentCategory,
    onNavigate: (NavKey) -> Unit,
    onBack: () -> Unit
) {
    val pages = pagesFor(source)[category].orEmpty()
    CatalogScaffold(
        title = categoryTitles.getValue(category),
        onBack = onBack
    ) {
        CellGroup(inset = true) {
            pages.forEachIndexed { index, page ->
                Cell(
                    title = page.title,
                    value = if (page.route == null) "待建设" else "查看",
                    link = page.route != null,
                    border = index != pages.lastIndex,
                    onClick = page.route?.let { route -> { onNavigate(route) } }
                )
            }
        }
    }
}

@Composable
private fun CatalogScaffold(
    title: String,
    onBack: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        NavBar(title = title, onClickBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            content()
            Box(Modifier.height(32.dp))
        }
    }
}

private fun pagesFor(source: ComponentSource) = when (source) {
    ComponentSource.LIBRARY -> libraryPages
    ComponentSource.MATERIAL3 -> materialPages
}
