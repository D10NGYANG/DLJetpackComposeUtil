package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.d10ng.compose.demo.Nav
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.base.Cell
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.feedback.NotifyType
import com.d10ng.compose.ui.navigation.NavBar

/**
 * 消息通知
 * @Author d10ng
 * @Date 2023/9/12 18:26
 */
@Composable
fun NotifyScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "Notify", onClickBack = { Nav.instant().popBackStack() })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CellGroup(title = "基础用法", inset = true) {
                Cell(title = "主要通知", link = true, onClick = {
                    UiViewModelManager.showNotify(NotifyType.Primary, "这是一条通知消息")
                })
                Cell(title = "成功通知", link = true, onClick = {
                    UiViewModelManager.showNotify(NotifyType.Success, "这是一条通知消息")
                })
                Cell(title = "危险通知", link = true, onClick = {
                    UiViewModelManager.showNotify(NotifyType.Error, "这是一条通知消息")
                })
                Cell(title = "警告通知", link = true, onClick = {
                    UiViewModelManager.showNotify(NotifyType.Warning, "这是一条通知消息")
                })
            }
            CellGroup(title = "其他样式", inset = true) {
                Cell(title = "长文本通知", link = true, onClick = {
                    UiViewModelManager.showNotify(
                        NotifyType.Primary,
                        "生命远不止连轴转和忙到极限，人类的体验远比这辽阔、丰富得多。代码是写出来给人看的，附带能在机器上运行。如果解决方法是丑陋的，那就肯定还有更好的解决方法，只是还没有发现而已。"
                    )
                })
            }
            CellGroup(title = "自定义配置", inset = true) {
                Cell(title = "自定义时长", link = true, onClick = {
                    UiViewModelManager.showNotify(NotifyType.Primary, "通知3秒", 3000)
                })
            }
        }
    }
}