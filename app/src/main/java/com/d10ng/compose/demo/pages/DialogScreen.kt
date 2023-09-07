package com.d10ng.compose.demo.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.demo.PageTransitions
import com.d10ng.compose.demo.R
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.base.Cell
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.dialog.builder.TipsDialogBuilder
import com.d10ng.compose.ui.navigation.NavBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/**
 * 弹窗
 * @Author d10ng
 * @Date 2023/9/7 13:35
 */
@RootNavGraph
@Destination(style = PageTransitions::class)
@Composable
fun DialogScreen(
    nav: DestinationsNavigator
) {
    DialogScreenView(onClickBack = nav::navigateUp)
}

@Composable
private fun DialogScreenView(
    onClickBack: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "Dialog", onClickBack = onClickBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CellGroup(title = "基础用法", inset = true) {
                Cell(title = "提示弹窗", link = true, onClick = {
                    UiViewModelManager.showDialog(TipsDialogBuilder(
                        content = "这是一个提示弹窗，代码是写出来给人看的，附带能在机器上运行。",
                        onButtonClick = {
                            UiViewModelManager.showToast("点击了确定")
                            true
                        }
                    ))
                })
                Cell(title = "危险警告弹窗", link = true, onClick = {
                    UiViewModelManager.showDialog(TipsDialogBuilder(
                        type = TipsDialogBuilder.Type.Danger,
                        title = "权限激活失败",
                        content = "App未获取全部权限，无法正常使用，请到手机设置页面中进行开启。",
                        buttonText = "去开启",
                        onButtonClick = {
                            UiViewModelManager.showToast("点击了去开启")
                            true
                        }
                    ))
                })
            }
            CellGroup(title = "自定义内容", inset = true) {
                Cell(title = "提示弹窗", link = true, onClick = {
                    UiViewModelManager.showDialog(TipsDialogBuilder(
                        content = "这是一个提示弹窗，代码是写出来给人看的，附带能在机器上运行。",
                        contentSlot = {
                            Image(
                                painter = painterResource(id = R.mipmap.apple),
                                contentDescription = "",
                                modifier = Modifier
                                    .padding(top = 32.dp)
                                    .fillMaxWidth(),
                                contentScale = ContentScale.Fit
                            )
                        },
                        onButtonClick = {
                            UiViewModelManager.showToast("点击了确定")
                            true
                        }
                    ))
                })
            }
        }
    }
}

@Preview
@Composable
private fun DialogScreenPreview() {
    DialogScreenView()
}