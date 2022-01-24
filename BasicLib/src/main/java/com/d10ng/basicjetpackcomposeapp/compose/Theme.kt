package com.d10ng.basicjetpackcomposeapp.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.d10ng.basicjetpackcomposeapp.model.AppViewModel
import com.d10ng.basicjetpackcomposeapp.view.*
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
    content: @Composable() () -> Unit
) {
    // 错误
    val isShowError by app.isShowError.observeAsState(false)
    val errorText by app.errorText.observeAsState("")
    // 加载中
    val isShowLoading by app.isShowLoading.observeAsState(false)
    // 警告
    val isShowWarning by app.isShowWarning.observeAsState(false)
    val warningBuilder by app.warningBuilder.observeAsState(null)
    // 提示
    val isShowDialog by app.isShowDialog.observeAsState(false)
    val dialogBuilder by app.dialogBuilder.observeAsState(null)
    // 输入
    val isShowInputDialog by app.isShowInputDialog.observeAsState(false)
    val inputDialogBuilder by app.inputDialogBuilder.observeAsState(null)
    // 单选
    val isShowRadioDialog by app.isShowRadioDialog.observeAsState(false)
    val radioDialogBuilder by app.radioDialogBuilder.observeAsState(null)
    // 日期选择
    val isShowDatePickerDialog by app.isShowDatePickerDialog.observeAsState(false)
    val datePickerDialogBuilder by app.datePickerDialogBuilder.observeAsState(null)
    // 时间选择
    val isShowTimePickerDialog by app.isShowTimePickerDialog.observeAsState(false)
    val timePickerDialogBuilder by app.timePickerDialogBuilder.observeAsState(null)
    // 进度
    val isShowProgressDialog by app.isShowProgressDialog.observeAsState(false)
    val progressDialogBuilder by app.progressDialogBuilder.observeAsState(null)

    MaterialTheme(
        colors = LightColorPalette
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
                    // 错误
                    ErrorBar(isShowError, errorText)
                }
            }
        }
    }

    // 警告
    warningBuilder?.let {
        WarningDialog(
            isShow = isShowWarning,
            builder = it,
            onDismiss = { app.hideWarningDialog() }
        )
    }

    // 提示
    dialogBuilder?.let {
        BaseDialog(
            isShow = isShowDialog,
            builder = it,
            onDismiss = { app.hideDialog() }
        )
    }

    // 输入
    inputDialogBuilder?.let {
        InputDialog(
            isShow = isShowInputDialog,
            builder = it,
            onDismiss = { app.hideInputDialog() }
        )
    }

    // 单选
    radioDialogBuilder?.let {
        RadioDialog(
            isShow = isShowRadioDialog,
            builder = it,
            onDismiss = { app.hideRadioDialog() }
        )
    }

    // 日期选择
    datePickerDialogBuilder?.let {
        DatePickerDialog(
            isShow = isShowDatePickerDialog,
            builder = it,
            onDismiss = { app.hideDatePickerDialog() }
        )
    }

    // 时间选择
    timePickerDialogBuilder?.let {
        TimePickerDialog(
            isShow = isShowTimePickerDialog,
            builder = it,
            onDismiss = { app.hideTimePickerDialog() }
        )
    }

    // 进度
    progressDialogBuilder?.let {
        ProgressDialog(
            isShow = isShowProgressDialog,
            builder = it,
            onDismiss = { app.hideProgressDialog() }
        )
    }

    // 加载中
    LoadingDialog(isShow = isShowLoading) {
        app.hideLoading()
    }
}