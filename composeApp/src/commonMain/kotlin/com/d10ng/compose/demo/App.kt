package com.d10ng.compose.demo

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.d10ng.compose.demo.pages.AvatarScreen
import com.d10ng.compose.demo.pages.BadgeScreen
import com.d10ng.compose.demo.pages.BDSignalBeamBoxScreen
import com.d10ng.compose.demo.pages.ButtonScreen
import com.d10ng.compose.demo.pages.CellScreen
import com.d10ng.compose.demo.pages.CheckButtonScreen
import com.d10ng.compose.demo.pages.CheckboxScreen
import com.d10ng.compose.demo.pages.ColorScreen
import com.d10ng.compose.demo.pages.ComponentCatalogScreen
import com.d10ng.compose.demo.pages.ComponentCategoryScreen
import com.d10ng.compose.demo.pages.DialogScreen
import com.d10ng.compose.demo.pages.DatePickerScreen
import com.d10ng.compose.demo.pages.DividerScreen
import com.d10ng.compose.demo.pages.FieldScreen
import com.d10ng.compose.demo.pages.HomeScreen
import com.d10ng.compose.demo.pages.IndexBarScreen
import com.d10ng.compose.demo.pages.InputScreen
import com.d10ng.compose.demo.pages.LibraryListInputItemScreen
import com.d10ng.compose.demo.pages.LibraryListItemScreen
import com.d10ng.compose.demo.pages.LibraryListMenuItemScreen
import com.d10ng.compose.demo.pages.MaterialButtonScreen
import com.d10ng.compose.demo.pages.MaterialAlertDialogScreen
import com.d10ng.compose.demo.pages.MaterialBadgeScreen
import com.d10ng.compose.demo.pages.MaterialCheckboxScreen
import com.d10ng.compose.demo.pages.MaterialChipScreen
import com.d10ng.compose.demo.pages.MaterialListItemScreen
import com.d10ng.compose.demo.pages.MaterialModalBottomSheetScreen
import com.d10ng.compose.demo.pages.MaterialProgressIndicatorScreen
import com.d10ng.compose.demo.pages.MaterialRadioButtonScreen
import com.d10ng.compose.demo.pages.MaterialSearchBarScreen
import com.d10ng.compose.demo.pages.MaterialSliderScreen
import com.d10ng.compose.demo.pages.MaterialSnackbarScreen
import com.d10ng.compose.demo.pages.MaterialSwitchScreen
import com.d10ng.compose.demo.pages.MaterialTextFieldScreen
import com.d10ng.compose.demo.pages.MenuInputScreen
import com.d10ng.compose.demo.pages.NavBarScreen
import com.d10ng.compose.demo.pages.NotifyScreen
import com.d10ng.compose.demo.pages.OverlayScreen
import com.d10ng.compose.demo.pages.PickerScreen
import com.d10ng.compose.demo.pages.PopoverScreen
import com.d10ng.compose.demo.pages.PullRefreshScreen
import com.d10ng.compose.demo.pages.RadioCellScreen
import com.d10ng.compose.demo.pages.SearchScreen
import com.d10ng.compose.demo.pages.ShapeScreen
import com.d10ng.compose.demo.pages.SheetScreen
import com.d10ng.compose.demo.pages.StepperScreen
import com.d10ng.compose.demo.pages.StepsScreen
import com.d10ng.compose.demo.pages.SwitchScreen
import com.d10ng.compose.demo.pages.TagScreen
import com.d10ng.compose.demo.pages.TimePickerScreen
import com.d10ng.compose.demo.pages.ToastScreen
import com.d10ng.compose.demo.pages.TypographyScreen
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.AppColor
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun App() {
    val fontFamilyResolver = LocalFontFamilyResolver.current
    var loading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        loadCustomFontFamily()?.apply {
            fontFamilyResolver.preload(this)
        }
        loading = false
    }

    if (loading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        MaterialTheme(
            colorScheme = AppColor.toColorScheme()
        ) {
            val backStack = remember { mutableStateListOf<Any>(HomeRoute) }

            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                // 设置【前进 / Push】时的动画：新页面从右进，旧页面往左退
                transitionSpec = {
                    slideInHorizontally(
                        animationSpec = tween(300), // 动画时长 300ms
                        initialOffsetX = { fullWidth -> fullWidth } // 新页面从屏幕正右侧(100%)开始进入
                    ) togetherWith slideOutHorizontally(
                        animationSpec = tween(300),
                        targetOffsetX = { fullWidth -> -fullWidth / 2 } // 旧页面向左侧退出一半距离 (制造视差效果，不写 /2 就是完全移出)
                    )
                },
                // 设置【后退 / Pop】时的动画：新页面从左进，旧页面往右退
                popTransitionSpec = {
                    slideInHorizontally(
                        animationSpec = tween(300),
                        initialOffsetX = { fullWidth -> -fullWidth / 2 } // 历史页面从左侧一半的位置开始恢复
                    ) togetherWith slideOutHorizontally(
                        animationSpec = tween(300),
                        targetOffsetX = { fullWidth -> fullWidth } // 当前页面向屏幕正右侧(100%)滑出
                    )
                },
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator()
                ),
                entryProvider = { key ->
                    when (key) {
                        is HomeRoute -> NavEntry(key) {
                            HomeScreen(onNavigate = { backStack.add(it) })
                        }
                        is ComponentCatalogRoute -> NavEntry(key) {
                            ComponentCatalogScreen(
                                source = key.source,
                                onNavigate = { backStack.add(it) },
                                onBack = { backStack.removeLastOrNull() }
                            )
                        }
                        is ComponentCategoryRoute -> NavEntry(key) {
                            ComponentCategoryScreen(
                                source = key.source,
                                category = key.category,
                                onNavigate = { backStack.add(it) },
                                onBack = { backStack.removeLastOrNull() }
                            )
                        }
                        is MaterialButtonRoute -> NavEntry(key) {
                            MaterialButtonScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is MaterialCheckboxRoute -> NavEntry(key) {
                            MaterialCheckboxScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is MaterialRadioButtonRoute -> NavEntry(key) {
                            MaterialRadioButtonScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is MaterialSwitchRoute -> NavEntry(key) {
                            MaterialSwitchScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is MaterialTextFieldRoute -> NavEntry(key) {
                            MaterialTextFieldScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is MaterialSearchBarRoute -> NavEntry(key) {
                            MaterialSearchBarScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is MaterialListItemRoute -> NavEntry(key) {
                            MaterialListItemScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is MaterialBadgeRoute -> NavEntry(key) {
                            MaterialBadgeScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is MaterialChipRoute -> NavEntry(key) {
                            MaterialChipScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is MaterialSliderRoute -> NavEntry(key) {
                            MaterialSliderScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is MaterialProgressIndicatorRoute -> NavEntry(key) {
                            MaterialProgressIndicatorScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is MaterialSnackbarRoute -> NavEntry(key) {
                            MaterialSnackbarScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is MaterialAlertDialogRoute -> NavEntry(key) {
                            MaterialAlertDialogScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is MaterialModalBottomSheetRoute -> NavEntry(key) {
                            MaterialModalBottomSheetScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is ColorRoute -> NavEntry(key) {
                            ColorScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is TypographyRoute -> NavEntry(key) {
                            TypographyScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is ShapeRoute -> NavEntry(key) {
                            ShapeScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is ButtonRoute -> NavEntry(key) {
                            ButtonScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is CellRoute -> NavEntry(key) {
                            CellScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is ToastRoute -> NavEntry(key) {
                            ToastScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is CheckboxRoute -> NavEntry(key) {
                            CheckboxScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is FieldRoute -> NavEntry(key) {
                            FieldScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is SwitchRoute -> NavEntry(key) {
                            SwitchScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is CheckButtonRoute -> NavEntry(key) {
                            CheckButtonScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is StepperRoute -> NavEntry(key) {
                            StepperScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is SearchRoute -> NavEntry(key) {
                            SearchScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is InputRoute -> NavEntry(key) {
                            InputScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is MenuInputRoute -> NavEntry(key) {
                            MenuInputScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is LibraryListItemRoute -> NavEntry(key) {
                            LibraryListItemScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is LibraryListInputItemRoute -> NavEntry(key) {
                            LibraryListInputItemScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is LibraryListMenuItemRoute -> NavEntry(key) {
                            LibraryListMenuItemScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is RadioCellRoute -> NavEntry(key) {
                            RadioCellScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is PickerRoute -> NavEntry(key) {
                            PickerScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is DatePickerRoute -> NavEntry(key) {
                            DatePickerScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is TimePickerRoute -> NavEntry(key) {
                            TimePickerScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is DividerRoute -> NavEntry(key) {
                            DividerScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is OverlayRoute -> NavEntry(key) {
                            OverlayScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is BDSignalBeamBoxRoute -> NavEntry(key) {
                            BDSignalBeamBoxScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is DialogRoute -> NavEntry(key) {
                            DialogScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is SheetRoute -> NavEntry(key) {
                            SheetScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is NotifyRoute -> NavEntry(key) {
                            NotifyScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is PullRefreshRoute -> NavEntry(key) {
                            PullRefreshScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is TagRoute -> NavEntry(key) {
                            TagScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is BadgeRoute -> NavEntry(key) {
                            BadgeScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is AvatarRoute -> NavEntry(key) {
                            AvatarScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is StepsRoute -> NavEntry(key) {
                            StepsScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is PopoverRoute -> NavEntry(key) {
                            PopoverScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is NavBarRoute -> NavEntry(key) {
                            NavBarScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        is IndexBarRoute -> NavEntry(key) {
                            IndexBarScreen(onBack = { backStack.removeLastOrNull() })
                        }
                        else -> NavEntry(key as NavKey) {
                            HomeScreen(onNavigate = { backStack.add(it) })
                        }
                    }
                }
            )
            UiViewModelManager.Init()
        }
    }
}

expect suspend fun loadCustomFontFamily(): FontFamily?
