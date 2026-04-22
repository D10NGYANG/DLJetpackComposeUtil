package com.d10ng.compose.demo

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
import androidx.navigation3.ui.NavDisplay
import com.d10ng.compose.demo.pages.AvatarScreen
import com.d10ng.compose.demo.pages.BadgeScreen
import com.d10ng.compose.demo.pages.ButtonScreen
import com.d10ng.compose.demo.pages.CellScreen
import com.d10ng.compose.demo.pages.CheckButtonScreen
import com.d10ng.compose.demo.pages.CheckboxScreen
import com.d10ng.compose.demo.pages.ColorScreen
import com.d10ng.compose.demo.pages.DialogScreen
import com.d10ng.compose.demo.pages.FieldScreen
import com.d10ng.compose.demo.pages.HomeScreen
import com.d10ng.compose.demo.pages.IndexBarScreen
import com.d10ng.compose.demo.pages.NavBarScreen
import com.d10ng.compose.demo.pages.NotifyScreen
import com.d10ng.compose.demo.pages.PopoverScreen
import com.d10ng.compose.demo.pages.PullRefreshScreen
import com.d10ng.compose.demo.pages.SearchScreen
import com.d10ng.compose.demo.pages.ShapeScreen
import com.d10ng.compose.demo.pages.SheetScreen
import com.d10ng.compose.demo.pages.StepperScreen
import com.d10ng.compose.demo.pages.StepsScreen
import com.d10ng.compose.demo.pages.SwitchScreen
import com.d10ng.compose.demo.pages.TagScreen
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
                entryProvider = { key ->
                    when (key) {
                        is HomeRoute -> NavEntry(key) {
                            HomeScreen(onNavigate = { backStack.add(it) })
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