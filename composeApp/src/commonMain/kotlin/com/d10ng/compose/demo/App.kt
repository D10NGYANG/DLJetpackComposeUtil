package com.d10ng.compose.demo

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
@Preview
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
            val navController = rememberNavController()
            LaunchedEffect(navController) {
                Nav.init(navController)
            }
            NavHost(
                navController = navController,
                startDestination = Nav.HomeRoute,
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
            ) {
                composable<Nav.HomeRoute> { HomeScreen() }
                composable<Nav.ColorRoute> { ColorScreen() }
                composable<Nav.TypographyRoute> { TypographyScreen() }
                composable<Nav.ShapeRoute> { ShapeScreen() }
                composable<Nav.ButtonRoute> { ButtonScreen() }
                composable<Nav.CellRoute> { CellScreen() }
                composable<Nav.ToastRoute> { ToastScreen() }
                composable<Nav.CheckboxRoute> { CheckboxScreen() }
                composable<Nav.FieldRoute> { FieldScreen() }
                composable<Nav.SwitchRoute> { SwitchScreen() }
                composable<Nav.CheckButtonRoute> { CheckButtonScreen() }
                composable<Nav.StepperRoute> { StepperScreen() }
                composable<Nav.SearchRoute> { SearchScreen() }
                composable<Nav.DialogRoute> { DialogScreen() }
                composable<Nav.SheetRoute> { SheetScreen() }
                composable<Nav.NotifyRoute> { NotifyScreen() }
                composable<Nav.PullRefreshRoute> { PullRefreshScreen() }
                composable<Nav.TagRoute> { TagScreen() }
                composable<Nav.BadgeRoute> { BadgeScreen() }
                composable<Nav.AvatarRoute> { AvatarScreen() }
                composable<Nav.StepsRoute> { StepsScreen() }
                composable<Nav.PopoverRoute> { PopoverScreen() }
                composable<Nav.NavBarRoute> { NavBarScreen() }
            }
            UiViewModelManager.Init()
        }
    }
}

expect suspend fun loadCustomFontFamily(): FontFamily?