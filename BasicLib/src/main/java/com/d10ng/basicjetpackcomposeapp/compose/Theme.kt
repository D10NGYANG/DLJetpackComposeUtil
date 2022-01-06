package com.d10ng.basicjetpackcomposeapp.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.d10ng.basicjetpackcomposeapp.model.AppViewModel
import com.d10ng.basicjetpackcomposeapp.view.LoadingDialog
import com.d10ng.basicjetpackcomposeapp.view.NormalDialog
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
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
    content: @Composable() () -> Unit
) {
    // 提示
    val isShowSnackBar by app.isShowSnackBar.observeAsState(false)
    val snackBarValue by app.snackBarValue.observeAsState("")

    MaterialTheme(
        colors = LightColorPalette,
        shapes = Shapes
    ) {
        ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = MaterialTheme.colors.isLight
            SideEffect {
                systemUiController.setStatusBarColor(
                    Color.Transparent, darkIcons = useDarkIcons)
            }
            Surface {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    content()
                    if (isShowSnackBar) {
                        Snackbar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .statusBarsPadding()
                                .padding(16.dp)
                                .navigationBarsWithImePadding()
                                .align(Alignment.TopCenter),
                            backgroundColor = AppColor.System.error
                        ) {
                            Text(text = snackBarValue, style = AppText.Medium.OnError.v14)
                        }
                    }
                }

                // 加载中弹窗
                val isShowLoading by app.isShowLoading.observeAsState(false)
                LoadingDialog(isShow = isShowLoading) {
                    app.cancelLoading()
                }

                // 普通弹窗
                val isShowNormalDialog by app.isShowNormalDialog.observeAsState(false)
                val normalDialogBuilder by app.normalDialogBuilder.observeAsState(null)
                NormalDialog(isShow = isShowNormalDialog, builder = normalDialogBuilder) {
                    app.dismissNormalDialog()
                }
            }
        }
    }
}