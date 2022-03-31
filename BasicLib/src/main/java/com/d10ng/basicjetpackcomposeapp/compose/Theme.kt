package com.d10ng.basicjetpackcomposeapp.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.d10ng.basicjetpackcomposeapp.dialog.Dialog
import com.d10ng.basicjetpackcomposeapp.dialog.LoadingDialog
import com.d10ng.basicjetpackcomposeapp.model.AppViewModel
import com.d10ng.basicjetpackcomposeapp.view.ErrorBar
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LightColorPalette = lightColors(
    primary = AppColor.System.primary,
    primaryVariant = AppColor.System.primaryVariant,
    secondary = AppColor.System.secondary,
    secondaryVariant = AppColor.System.secondaryVariant,
    background = AppColor.System.background,
    surface = AppColor.System.surface,
    error = AppColor.System.error,
    onPrimary = AppColor.On.primary,
    onSecondary = AppColor.On.secondary,
    onBackground = AppColor.On.background,
    onSurface = AppColor.On.surface,
    onError = AppColor.On.error
)

@Composable
fun AppTheme(
    app: AppViewModel,
    statusBarColor: Color = Color.Transparent,
    statusBarUseDarkIcons: Boolean = MaterialTheme.colors.isLight,
    content: @Composable() () -> Unit
) {
    // 错误
    val isShowError by app.isShowError.collectAsState()
    val errorText by app.errorText.collectAsState()
    // 加载中
    val isShowLoading by app.isShowLoading.collectAsState()
    // 弹窗
    val dialogBuilder by app.dialogBuilder.collectAsState()

    MaterialTheme(
        colors = LightColorPalette
    ) {
        ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
            val systemUiController = rememberSystemUiController()
            SideEffect {
                systemUiController.setStatusBarColor(
                    statusBarColor, darkIcons = statusBarUseDarkIcons)
            }
            Surface {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    content()
                    // 错误
                    ErrorBar(isShowError, errorText)
                }
            }
        }
    }

    // 提示
    dialogBuilder?.let {
        Dialog(
            onDismiss = {
                if (it.isClickOutsideDismiss) {
                    app.hideDialog()
                }
            },
            contentAlignment = it.contentAlignment
        ) {
            it.Build()
        }
    }

    // 加载中
    if (isShowLoading) {
        LoadingDialog()
    }
}