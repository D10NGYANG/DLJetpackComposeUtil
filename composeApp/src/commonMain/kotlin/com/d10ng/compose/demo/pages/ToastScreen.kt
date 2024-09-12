package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.base.Cell
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.base.ToastPosition
import com.d10ng.compose.ui.navigation.NavBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 轻提示
 * @Author d10ng
 * @Date 2023/9/4 15:14
 */
class ToastScreen : Screen {
    @Composable
    override fun Content() {
        ToastScreenView()
    }
}

@Composable
private fun ToastScreenView() {
    val navigator = LocalNavigator.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "Toast", onClickBack = { navigator?.pop() })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CellGroup(title = "基础用法", inset = true) {
                Cell(
                    title = "文字提示",
                    link = true,
                    onClick = { UiViewModelManager.showToast("提示内容") })
                Cell(title = "加载提示", link = true, onClick = {
                    UiViewModelManager.showLoading("加载中...")
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(1500)
                        UiViewModelManager.hideLoading()
                    }
                })
                Cell(title = "无文本加载提示", link = true, onClick = {
                    UiViewModelManager.showLoading()
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(1500)
                        UiViewModelManager.hideLoading()
                    }
                })
                Cell(
                    title = "成功提示",
                    link = true,
                    onClick = { UiViewModelManager.showSuccessToast("成功文案") })
                Cell(
                    title = "失败提示",
                    label = "提示文案不建议8个字符",
                    link = true,
                    onClick = { UiViewModelManager.showFailToast("失败文案失败文案") })
            }
            CellGroup(title = "自定义位置", inset = true) {
                Cell(
                    title = "顶部展示",
                    link = true,
                    onClick = {
                        UiViewModelManager.showToast(
                            "提示内容",
                            position = ToastPosition.Top
                        )
                    })
                Cell(
                    title = "底部展示",
                    link = true,
                    onClick = {
                        UiViewModelManager.showToast(
                            "提示内容",
                            position = ToastPosition.Bottom
                        )
                    })
            }
            CellGroup(title = "动态更新提示", inset = true) {
                Cell(title = "动态更新提示", link = true, onClick = {
                    var wait = 3
                    UiViewModelManager.showLoading("等待${wait}秒...")
                    CoroutineScope(Dispatchers.IO).launch {
                        while (wait > 0) {
                            delay(1000)
                            wait--
                            UiViewModelManager.showLoading("等待${wait}秒...")
                        }
                        UiViewModelManager.hideLoading()
                    }
                })
            }
        }
    }
}