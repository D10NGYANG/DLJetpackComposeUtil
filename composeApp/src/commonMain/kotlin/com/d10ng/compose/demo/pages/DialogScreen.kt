package com.d10ng.compose.demo.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.d10ng.compose.demo.resources.Res
import com.d10ng.compose.demo.resources.apple
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.base.Button
import com.d10ng.compose.ui.base.ButtonType
import com.d10ng.compose.ui.base.Cell
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.dialog.DialogColumn
import com.d10ng.compose.ui.dialog.builder.ConfirmDialogBuilder
import com.d10ng.compose.ui.dialog.builder.DialogBuilder
import com.d10ng.compose.ui.dialog.builder.InputDialogBuilder
import com.d10ng.compose.ui.dialog.builder.ProgressDialogBuilder
import com.d10ng.compose.ui.dialog.builder.ResultDialogBuilder
import com.d10ng.compose.ui.dialog.builder.TipsDialogBuilder
import com.d10ng.compose.ui.navigation.NavBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

/**
 * 弹窗
 * @Author d10ng
 * @Date 2023/9/7 13:35
 */
@Composable
fun DialogScreen(onBack: () -> Unit = {}) {
    val scope = rememberCoroutineScope()
    var backgroundClickCount by remember { mutableIntStateOf(0) }
    var actionState by remember { mutableStateOf("尚未执行弹窗操作") }
    var progressState by remember { mutableStateOf("尚未启动进度任务") }

    LaunchedEffect(Unit) {
        actionState = "页面焦点可用；关闭弹窗后应恢复到当前页面"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "Dialog", onClickBack = { onBack() })
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
                                contentDescription = "弹窗中的苹果示例图片",
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
                Cell(title = "底部对齐与长文本", link = true, onClick = {
                    UiViewModelManager.showDialog(
                        TipsDialogBuilder(
                            title = "底部操作确认",
                            titleAlign = TextAlign.Start,
                            content = "此弹窗保留旧 Builder 的 Alignment.BottomCenter 定位能力，同时使用官方模态窗口处理遮罩、焦点、Insets 和进入退出动画。长文本用于检查窄屏换行、底部安全区和内容边距。",
                            contentAlign = TextAlign.Start,
                            onButtonClick = {
                                actionState = "已确认底部对齐弹窗"
                                true
                            }
                        ).apply {
                            clickOutsideDismiss = true
                            contentAlignment = Alignment.BottomCenter
                        }
                    )
                })
            }
            CellGroup(title = "关闭策略与模态隔离", inset = true) {
                Cell(
                    title = "底层点击计数：$backgroundClickCount",
                    label = "弹窗显示期间点击此行不应增加",
                    onClick = { backgroundClickCount += 1 }
                )
                Cell(title = "允许外点与返回关闭", link = true, onClick = {
                    UiViewModelManager.showDialog(
                        TipsDialogBuilder(
                            title = "可关闭弹窗",
                            content = "点击内容外区域，或使用系统返回键 / Escape，弹窗应关闭。",
                            onButtonClick = {
                                actionState = "已通过按钮关闭可关闭弹窗"
                                true
                            }
                        ).apply { clickOutsideDismiss = true }
                    )
                })
                Cell(title = "禁止外点与返回关闭", link = true, onClick = {
                    UiViewModelManager.showDialog(
                        TipsDialogBuilder(
                            title = "强制操作弹窗",
                            content = "点击遮罩或按返回键不应关闭，只能使用弹窗内的确定按钮。",
                            onButtonClick = {
                                actionState = "强制操作弹窗仅由按钮关闭"
                                true
                            }
                        )
                    )
                })
                Text(
                    text = actionState,
                    modifier = Modifier.padding(horizontal = 19.dp, vertical = 12.dp),
                    style = AppText.Normal.Tips.default
                )
            }
            CellGroup(title = "输入弹窗", inset = true) {
                Cell(title = "单个输入框", link = true, onClick = {
                    UiViewModelManager.showDialog(InputDialogBuilder(
                        title = "手机号",
                        inputs = listOf(
                            InputDialogBuilder.InputField(
                                initValue = "",
                                label = "请输入国内手机号",
                                keyboardType = KeyboardType.Phone,
                                verify = { value ->
                                    val pass = value.isMobileNumber()
                                    InputDialogBuilder.VerifyResult(
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
                            InputDialogBuilder.InputField(
                                initValue = "",
                                label = "请输入目标纬度，-90至90，eg:22.3",
                                keyboardType = KeyboardType.Number,
                                verify = { value ->
                                    val temp = value.toDoubleOrNull()
                                    val pass = temp != null && temp in -90.0..90.0
                                    InputDialogBuilder.VerifyResult(
                                        pass = pass,
                                        errorText = if (pass) "" else "纬度格式不正确"
                                    )
                                }
                            ),
                            InputDialogBuilder.InputField(
                                initValue = "",
                                label = "请输入目标经度，-180至180，eg:113.5",
                                keyboardType = KeyboardType.Number,
                                verify = { value ->
                                    val temp = value.toDoubleOrNull()
                                    val pass = temp != null && temp in -180.0..180.0
                                    InputDialogBuilder.VerifyResult(
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
                            InputDialogBuilder.InputField(
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
            CellGroup(title = "多层窗口", inset = true) {
                Cell(title = "依次打开两层 Dialog", link = true, onClick = {
                    UiViewModelManager.showDialog(
                        FeatureDialogBuilder(
                            title = "第一层 Dialog",
                            content = "打开第二层后，第一层应保留但不能接收操作；关闭第二层后焦点与操作权回到第一层。",
                        ) { firstId ->
                            Button(
                                text = "打开第二层",
                                modifier = Modifier.fillMaxWidth(),
                                type = ButtonType.PRIMARY,
                            ) {
                                actionState = "第二层 Dialog 已打开"
                                UiViewModelManager.showDialog(
                                    TipsDialogBuilder(
                                        title = "第二层 Dialog",
                                        content = "当前只有顶层 Dialog 可操作。点击确定后应返回仍然存在的第一层。",
                                        onButtonClick = {
                                            actionState = "第二层已关闭，返回第一层"
                                            true
                                        }
                                    )
                                )
                            }
                            Button(
                                text = "关闭第一层",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                            ) {
                                actionState = "第一层 Dialog 已关闭"
                                DialogBuilder.dismiss(firstId)
                            }
                        }
                    )
                })
            }
            CellGroup(title = "进度弹窗", inset = true) {
                Cell(title = "展示百分比进度", link = true, onClick = {
                    showProgressDialog(
                        type = ProgressDialogBuilder.Type.PERCENTAGE,
                        launch = { block -> scope.launch { block() } },
                        onStateChange = { progressState = it },
                    )
                })
                Cell(title = "展示具体进度", link = true, onClick = {
                    showProgressDialog(
                        type = ProgressDialogBuilder.Type.PROGRESS_AND_MAX,
                        launch = { block -> scope.launch { block() } },
                        onStateChange = { progressState = it },
                    )
                })
                Cell(title = "不展示进度文本", link = true, onClick = {
                    showProgressDialog(
                        type = ProgressDialogBuilder.Type.NONE,
                        launch = { block -> scope.launch { block() } },
                        onStateChange = { progressState = it },
                    )
                })
                Text(
                    text = progressState,
                    modifier = Modifier.padding(horizontal = 19.dp, vertical = 12.dp),
                    style = AppText.Normal.Tips.default
                )
            }
        }
    }
}

private fun showProgressDialog(
    type: ProgressDialogBuilder.Type,
    launch: (suspend () -> Unit) -> Unit,
    onStateChange: (String) -> Unit,
) {
    val max = 30L
    val builder = ProgressDialogBuilder(
        title = "发送中",
        progress = 0,
        max = max,
        type = type
    )
    val id = UiViewModelManager.showDialog(builder)
    onStateChange("进度任务已启动：0/$max")
    launch {
        try {
            for (i in 0..max) {
                val temp = builder.copy(progress = i)
                UiViewModelManager.updateDialog(id, temp)
                onStateChange("进度任务更新：$i/$max")
                delay(100)
            }
            onStateChange("进度任务已完成并主动关闭")
        } finally {
            // 页面离开时 rememberCoroutineScope 会取消任务，不能留下阻塞型弹窗。
            UiViewModelManager.hideDialog(id)
        }
    }
}

private class FeatureDialogBuilder(
    private val title: String,
    private val content: String,
    private val actions: @Composable (id: String) -> Unit,
) : DialogBuilder(clickOutsideDismiss = false) {

    @Composable
    override fun Build(id: String) {
        DialogColumn {
            TipsDialogBuilder.TitleText(text = title)
            TipsDialogBuilder.ContentText(text = content, textAlign = TextAlign.Start)
            actions(id)
        }
    }
}
