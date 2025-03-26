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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.base.Cell
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.dialog.builder.ConfirmDialogBuilder
import com.d10ng.compose.ui.dialog.builder.InputDialogBuilder
import com.d10ng.compose.ui.dialog.builder.ProgressDialogBuilder
import com.d10ng.compose.ui.dialog.builder.ResultDialogBuilder
import com.d10ng.compose.ui.dialog.builder.TipsDialogBuilder
import com.d10ng.compose.ui.navigation.NavBar
import dljetpackcomposeutil_project.composeapp.generated.resources.Res
import dljetpackcomposeutil_project.composeapp.generated.resources.apple
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

/**
 * 弹窗
 * @Author d10ng
 * @Date 2023/9/7 13:35
 */
class DialogScreen : Screen {
    @Composable
    override fun Content() {
        DialogScreenView()
    }
}

@Composable
private fun DialogScreenView() {
    val navigator = LocalNavigator.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "Dialog", onClickBack = { navigator?.pop() })
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
                Cell(title = "提示弹窗-内容靠左", link = true, onClick = {
                    UiViewModelManager.showDialog(TipsDialogBuilder(
                        titleAlign = TextAlign.Start,
                        content = "这是一个提示弹窗，代码是写出来给人看的，附带能在机器上运行。",
                        contentAlign = TextAlign.Start,
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
                Cell(title = "确认弹窗", link = true, onClick = {
                    UiViewModelManager.showDialog(ConfirmDialogBuilder(
                        content = "如果解决方法是丑陋的，那就肯定还有更好的解决方法，只是还没有发现而已。",
                        onConfirmClick = {
                            UiViewModelManager.showToast("点击了确定")
                            true
                        },
                        onCancelClick = {
                            UiViewModelManager.showToast("点击了取消")
                            true
                        }
                    ))
                })
                Cell(title = "确认弹窗-内容靠左", link = true, onClick = {
                    UiViewModelManager.showDialog(ConfirmDialogBuilder(
                        titleAlign = TextAlign.Start,
                        content = "如果解决方法是丑陋的，那就肯定还有更好的解决方法，只是还没有发现而已。",
                        contentAlign = TextAlign.Start,
                        onConfirmClick = {
                            UiViewModelManager.showToast("点击了确定")
                            true
                        },
                        onCancelClick = {
                            UiViewModelManager.showToast("点击了取消")
                            true
                        }
                    ))
                })
                Cell(title = "确认警告弹窗", link = true, onClick = {
                    UiViewModelManager.showDialog(ConfirmDialogBuilder(
                        type = ConfirmDialogBuilder.Type.Danger,
                        content = "您确定退出当前登录账号吗？",
                        onConfirmClick = {
                            UiViewModelManager.showToast("点击了确定")
                            true
                        },
                        onCancelClick = {
                            UiViewModelManager.showToast("点击了取消")
                            true
                        }
                    ))
                })
                Cell(title = "结果弹窗-成功", link = true, onClick = {
                    UiViewModelManager.showDialog(ResultDialogBuilder(
                        title = "发送成功",
                        status = ResultDialogBuilder.Status.SUCCESS,
                        content = "信息已发送成功！",
                        showCancel = false,
                        onConfirmClick = {
                            UiViewModelManager.showToast("点击了确定")
                            true
                        }
                    ))
                })
                Cell(title = "结果弹窗-失败", link = true, onClick = {
                    UiViewModelManager.showDialog(ResultDialogBuilder(
                        title = "发送失败",
                        status = ResultDialogBuilder.Status.ERROR,
                        content = "信息发送失败，是否重新发送？",
                        onConfirmClick = {
                            UiViewModelManager.showToast("点击了确定")
                            true
                        },
                        onCancelClick = {
                            UiViewModelManager.showToast("点击了取消")
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
                                painter = painterResource(resource = Res.drawable.apple),
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
            CellGroup(title = "输入弹窗", inset = true) {
                Cell(title = "单个输入框", link = true, onClick = {
                    UiViewModelManager.showDialog(InputDialogBuilder(
                        title = "手机号",
                        inputs = listOf(
                            InputDialogBuilder.Input(
                                initValue = "",
                                label = "请输入国内手机号",
                                keyboardType = KeyboardType.Phone,
                                verify = { value ->
                                    val pass = value.isMobileNumber()
                                    InputDialogBuilder.Verify(
                                        pass = pass,
                                        errorText = if (pass) "" else "手机号格式不正确"
                                    )
                                }
                            )
                        ),
                        onConfirmClick = {
                            UiViewModelManager.showToast("输入内容：\"${it[0]}\"")
                            true
                        }
                    ))
                })
                Cell(title = "两个输入框", link = true, onClick = {
                    UiViewModelManager.showDialog(InputDialogBuilder(
                        title = "经纬度",
                        inputs = listOf(
                            InputDialogBuilder.Input(
                                initValue = "",
                                label = "请输入目标纬度，-90至90，eg:22.3",
                                keyboardType = KeyboardType.Number,
                                verify = { value ->
                                    val temp = value.toDoubleOrNull()
                                    val pass = temp != null && temp in -90.0..90.0
                                    InputDialogBuilder.Verify(
                                        pass = pass,
                                        errorText = if (pass) "" else "纬度格式不正确"
                                    )
                                }
                            ),
                            InputDialogBuilder.Input(
                                initValue = "",
                                label = "请输入目标经度，-180至180，eg:113.5",
                                keyboardType = KeyboardType.Number,
                                verify = { value ->
                                    val temp = value.toDoubleOrNull()
                                    val pass = temp != null && temp in -180.0..180.0
                                    InputDialogBuilder.Verify(
                                        pass = pass,
                                        errorText = if (pass) "" else "经度格式不正确"
                                    )
                                }
                            )
                        ),
                        onConfirmClick = {
                            UiViewModelManager.showToast("输入内容：\"${it[0]}, ${it[1]}\"")
                            true
                        }
                    ))
                })
                Cell(title = "多行输入框", link = true, onClick = {
                    UiViewModelManager.showDialog(InputDialogBuilder(
                        title = "留言",
                        inputs = listOf(
                            InputDialogBuilder.Input(
                                initValue = "",
                                label = "请输入您的留言",
                                singleLine = false
                            )
                        ),
                        onConfirmClick = {
                            UiViewModelManager.showToast("输入内容：\"${it[0]}\"")
                            true
                        }
                    ))
                })
            }
            CellGroup(title = "进度弹窗", inset = true) {
                Cell(title = "展示百分比进度", link = true, onClick = {
                    showProgressDialog(ProgressDialogBuilder.Type.PERCENTAGE)
                })
                Cell(title = "展示具体进度", link = true, onClick = {
                    showProgressDialog(ProgressDialogBuilder.Type.PROGRESS_AND_MAX)
                })
                Cell(title = "不展示进度文本", link = true, onClick = {
                    showProgressDialog(ProgressDialogBuilder.Type.NONE)
                })
            }
        }
    }
}

private fun showProgressDialog(type: ProgressDialogBuilder.Type) {
    val max = 30L
    val builder = ProgressDialogBuilder(
        title = "发送中",
        progress = 0,
        max = max,
        type = type
    )
    val id = UiViewModelManager.showDialog(builder)
    CoroutineScope(Dispatchers.Default).launch {
        for (i in 0..max) {
            val temp = builder.copy(progress = i)
            UiViewModelManager.updateDialog(id, temp)
            delay(100)
        }
        UiViewModelManager.hideDialog(id)
    }
}