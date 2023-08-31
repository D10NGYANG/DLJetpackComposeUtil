package com.d10ng.compose.ui

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.d10ng.compose.dialog.Dialog
import com.d10ng.compose.dialog.LoadingDialog
import com.d10ng.compose.model.AppViewModel
import com.d10ng.compose.view.ErrorBar

internal val Typography = Typography()

@Composable
fun AppTheme(
    app: AppViewModel,
    colorScheme: ColorScheme = MaterialTheme.colorScheme,
    statusBarColor: Color = Color.Transparent,
    statusBarUseDarkIcons: Boolean = isSystemInDarkTheme(),
    typography: Typography = Typography,
    content: @Composable () -> Unit
) {
    // 错误
    val isShowError by app.isShowError.collectAsState()
    val errorText by app.errorText.collectAsState()
    // 加载中
    val isShowLoading by app.isShowLoading.collectAsState()
    // 弹窗
    val dialogBuilder by app.dialogBuilder.collectAsState()

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = statusBarColor.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                statusBarUseDarkIcons
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = {
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
    )

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