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
    statusBarColor: Color = Color.Transparent,
    statusBarUseDarkIcons: Boolean = MaterialTheme.colors.isLight,
    content: @Composable() () -> Unit
) {
    // 错误
    val isShowError by app.isShowError.collectAsState()
    val errorText by app.errorText.collectAsState()
    // 加载中
    val isShowLoading by app.isShowLoading.collectAsState()
    // 警告
    val isShowWarning by app.isShowWarning.collectAsState()
    val warningBuilder by app.warningBuilder.collectAsState()
    // 提示
    val isShowDialog by app.isShowDialog.collectAsState()
    val dialogBuilder by app.dialogBuilder.collectAsState()
    // 输入
    val isShowInputDialog by app.isShowInputDialog.collectAsState()
    val inputDialogBuilder by app.inputDialogBuilder.collectAsState()
    // 单选
    val isShowRadioDialog by app.isShowRadioDialog.collectAsState()
    val radioDialogBuilder by app.radioDialogBuilder.collectAsState()
    // 日期选择
    val isShowDatePickerDialog by app.isShowDatePickerDialog.collectAsState()
    val datePickerDialogBuilder by app.datePickerDialogBuilder.collectAsState()
    // 时间选择
    val isShowTimePickerDialog by app.isShowTimePickerDialog.collectAsState()
    val timePickerDialogBuilder by app.timePickerDialogBuilder.collectAsState()
    // 进度
    val isShowProgressDialog by app.isShowProgressDialog.collectAsState()
    val progressDialogBuilder by app.progressDialogBuilder.collectAsState()
    // 成功或失败
    val isShowSuccessOrFalseDialog by app.isShowSuccessOrFalseDialog.collectAsState()
    val successOrFalseDialogBuilder by app.successOrFalseDialogBuilder.collectAsState()

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

    // 成功或失败
    successOrFalseDialogBuilder?.let {
        SuccessOrFalseDialog(
            isShow = isShowSuccessOrFalseDialog,
            builder = it,
            onDismiss = { app.hideSuccessOrFalseDialog() }
        )
    }

    // 加载中
    LoadingDialog(isShow = isShowLoading) {
        app.hideLoading()
    }
}