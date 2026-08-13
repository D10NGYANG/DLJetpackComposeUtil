package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.base.Cell
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.base.ToastPosition
import com.d10ng.compose.ui.navigation.NavBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 轻提示
 * @Author d10ng
 * @Date 2023/9/4 15:14
 */
@Composable
fun ToastScreen(onBack: () -> Unit = {}) {
    val scope = rememberCoroutineScope()
    var loadingState by remember { mutableStateOf("尚未启动阻塞 Loading") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "Toast", onClickBack = { onBack() })
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
                Cell(
                    title = "长文本提示",
                    link = true,
                    onClick = { UiViewModelManager.showToast("显示一段很长很长很长很长很长很长很长很长很长很长的文本提示") })
                Cell(title = "加载提示", link = true, onClick = {
                    loadingState = "Loading 阻塞中，1.5 秒后主动关闭"
                    UiViewModelManager.showLoading("加载中...")
                    scope.launch {
                        delay(1500)
                        UiViewModelManager.hideLoading()
                        loadingState = "Loading 已主动关闭"
                    }
                })
                Cell(title = "无文本加载提示", link = true, onClick = {
                    loadingState = "无文本 Loading 阻塞中，1.5 秒后主动关闭"
                    UiViewModelManager.showLoading()
                    scope.launch {
                        delay(1500)
                        UiViewModelManager.hideLoading()
                        loadingState = "无文本 Loading 已主动关闭"
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
                Text(
                    text = "普通文字提示现由 Material 3 Snackbar 承载，固定贴近宿主底部；ToastPosition 仅为兼容旧调用保留。",
                    modifier = Modifier.padding(horizontal = 19.dp, vertical = 12.dp),
                    style = AppText.Normal.Tips.default
                )
            }
            CellGroup(title = "官方队列行为", inset = true) {
                Cell(title = "连续发送三条消息", link = true, onClick = {
                    UiViewModelManager.showToast("队列 1/3：开始同步")
                    UiViewModelManager.showToast("队列 2/3：正在处理")
                    UiViewModelManager.showToast("队列 3/3：同步完成")
                })
                Cell(title = "长时长文字消息", link = true, onClick = {
                    UiViewModelManager.showToast("长时长 Snackbar 消息", duration = 8_000L)
                })
            }
            CellGroup(title = "动态更新提示", inset = true) {
                Cell(title = "动态更新提示", link = true, onClick = {
                    var wait = 3
                    UiViewModelManager.showLoading("等待${wait}秒...")
                    loadingState = "Loading 文案正在动态更新"
                    scope.launch {
                        while (wait > 0) {
                            delay(1000)
                            wait--
                            UiViewModelManager.showLoading("等待${wait}秒...")
                            loadingState = "Loading 文案更新为等待${wait}秒"
                        }
                        UiViewModelManager.hideLoading()
                        loadingState = "动态 Loading 已主动关闭"
                    }
                })
                Text(
                    text = loadingState,
                    modifier = Modifier.padding(horizontal = 19.dp, vertical = 12.dp),
                    style = AppText.Normal.Tips.default
                )
            }
        }
    }
}
