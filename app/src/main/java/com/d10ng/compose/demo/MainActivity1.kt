package com.d10ng.compose.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.d10ng.app.base.PermissionManager
import com.d10ng.compose.demo.ui.DLTheme
import com.d10ng.compose.dialog.builder.BaseDialogBuilder
import com.d10ng.compose.dialog.builder.DatePickerDialogBuilder
import com.d10ng.compose.dialog.builder.DoubleNumberPickerDialogBuilder
import com.d10ng.compose.dialog.builder.InputDialogBuilder
import com.d10ng.compose.dialog.builder.IntNumberPickerDialogBuilder
import com.d10ng.compose.dialog.builder.PickerDialogBuilder
import com.d10ng.compose.dialog.builder.ProgressDialogBuilder
import com.d10ng.compose.dialog.builder.RadioDialogBuilder
import com.d10ng.compose.dialog.builder.SuccessOrFalseDialogBuilder
import com.d10ng.compose.dialog.builder.TimePickerDialogBuilder
import com.d10ng.compose.dialog.builder.WarningDialogBuilder
import com.d10ng.compose.dialog.builder.getErrorDialogBuilder
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.view.SolidButtonWithText
import com.d10ng.datelib.curHour
import com.d10ng.datelib.curMinute
import com.d10ng.datelib.curSecond
import com.d10ng.datelib.curTime
import com.d10ng.datelib.toDateStr
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity1 : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            DLTheme {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .windowInsetsTopHeight(WindowInsets.statusBars)
                            .background(AppColor.Main.primary)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(AppColor.Main.primary)
                            .padding(16.dp, 8.dp)
                    ) {
                        Text(text = "首页", style = AppText.Bold.Title.v18)
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .padding(16.dp)
                    ) {

                        item {
                            SolidButtonWithText(text = "请求定位权限", onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    val result = PermissionManager.request(
                                        arrayOf(
                                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                    UiViewModelManager.showToast("请求权限结果：$result")
                                }
                            })
                        }

                        item {
                            SolidButtonWithText(text = "显示加载中弹窗", onClick = {
                                UiViewModelManager.showLoading()
                                lifecycleScope.launch(Dispatchers.IO) {
                                    delay(1000L)
                                    UiViewModelManager.hideLoading()
                                }
                            })
                        }

                        item {
                            SolidButtonWithText(text = "显示警告", onClick = {
                                UiViewModelManager.showDialog(WarningDialogBuilder(
                                    message = "我警告你不要乱来！！！",
                                    onClickButton = {
                                        UiViewModelManager.hideDialog()
                                    }
                                ))
                            })
                        }

                        item {
                            SolidButtonWithText(text = "显示提示1", onClick = {
                                UiViewModelManager.showDialog(BaseDialogBuilder(
                                    message = "您将会收到一条提示消息，请注意查看提示内容，以免发生误操作！",
                                    onClickSure = {
                                        UiViewModelManager.hideDialog()
                                        UiViewModelManager.showToast("用户点击了【确定】")
                                    }
                                ))
                            })
                        }

                        item {
                            SolidButtonWithText(text = "显示提示2", onClick = {
                                UiViewModelManager.showDialog(BaseDialogBuilder(
                                    message = "您将会收到一条提示消息，请注意查看提示内容，以免发生误操作！",
                                    onClickSure = {
                                        UiViewModelManager.hideDialog()
                                        UiViewModelManager.showToast("用户点击了【确定】")
                                    },
                                    onClickCancel = {
                                        UiViewModelManager.hideDialog()
                                        UiViewModelManager.showToast("用户点击了【取消】")
                                    }
                                ))
                            })
                        }

                        item {
                            SolidButtonWithText(text = "显示提示3", onClick = {
                                val msg = StringBuilder().apply {
                                    for (i in 0..100) {
                                        append("您将会收到一条提示消息，请注意查看提示内容，以免发生误操作！")
                                    }
                                }
                                UiViewModelManager.showDialog(getErrorDialogBuilder(
                                    message = msg.toString(),
                                    onClickSure = {
                                        UiViewModelManager.hideDialog()
                                        UiViewModelManager.showToast("用户点击了【确定】")
                                    },
                                    onClickCancel = {
                                        UiViewModelManager.hideDialog()
                                        UiViewModelManager.showToast("用户点击了【取消】")
                                    }
                                ))
                            })
                        }

                        item {
                            SolidButtonWithText(text = "显示输入框1", onClick = {
                                UiViewModelManager.showDialog(InputDialogBuilder(
                                    message = "请输入您的身份证号码",
                                    inputs = listOf(
                                        InputDialogBuilder.Input(
                                            keyboardType = KeyboardType.Number,
                                            verify = { value ->
                                                if (value.isEmpty()) {
                                                    InputDialogBuilder.Verify(
                                                        false,
                                                        "身份证不能为空"
                                                    )
                                                } else if (!value.matches("[0-9X]+".toRegex())) {
                                                    InputDialogBuilder.Verify(
                                                        false,
                                                        "身份证格式不正确"
                                                    )
                                                } else {
                                                    InputDialogBuilder.Verify(true)
                                                }
                                            }
                                        ),
                                    ),
                                    onClickSure = {
                                        UiViewModelManager.hideDialog()
                                        UiViewModelManager.showToast("身份证：${it[0]}")
                                    },
                                    onClickCancel = {
                                        UiViewModelManager.hideDialog()
                                    }
                                ))
                            })
                        }

                        item {
                            var selectStr by remember {
                                mutableStateOf("1080*1920px")
                            }
                            SolidButtonWithText(text = "显示单选弹窗", onClick = {
                                UiViewModelManager.showDialog(
                                    RadioDialogBuilder(
                                        title = "屏幕分辨率",
                                        map = mutableMapOf(
                                            Pair("360*640px", "360P"),
                                            Pair("720*1280px", "720P"),
                                            Pair("1080*1920px", "1080P"),
                                            Pair("1440*2560px", "2K"),
                                        ),
                                        select = selectStr,
                                        onSelect = {
                                            UiViewModelManager.hideDialog()
                                            UiViewModelManager.showToast("【${it.second}】= ${it.first}")
                                            selectStr = it.first
                                        }
                                ))
                            })
                        }

                        item {
                            var selectStr by remember {
                                mutableStateOf("1080*1920px")
                            }
                            SolidButtonWithText(text = "显示单选弹窗-自定义", onClick = {
                                UiViewModelManager.showDialog(
                                    RadioDialogBuilder(
                                        title = "屏幕分辨率",
                                        map = mutableMapOf(
                                            Pair("360*640px", "360P"),
                                            Pair("720*1280px", "720P"),
                                            Pair("1080*1920px", "1080P"),
                                            Pair("1440*2560px", "2K"),
                                        ),
                                        select = selectStr,
                                        customItemView = { isSelect, info, onClick ->
                                            CustomRadioDialogItem(
                                            isSelect,
                                            info,
                                            onClick
                                        )
                                    },
                                    isRow = true,
                                    onSelect = {
                                        UiViewModelManager.hideDialog()
                                        UiViewModelManager.showToast("【${it.second}】= ${it.first}")
                                        selectStr = it.first
                                    }
                                ))
                            })
                        }

                        item {
                            var selectTime by remember {
                                mutableStateOf(curTime)
                            }
                            SolidButtonWithText(text = "显示日期选择", onClick = {
                                UiViewModelManager.showDialog(
                                    DatePickerDialogBuilder(
                                        title = "选择日期",
                                        message = "请选择您的出生日期",
                                        initValue = selectTime,
                                        onClickSure = {
                                            selectTime = it
                                            UiViewModelManager.hideDialog()
                                            UiViewModelManager.showToast(
                                                "您的出生日期为${
                                                    it.toDateStr(
                                                        "yyyy-MM-dd"
                                                    )
                                                }"
                                            )
                                        },
                                        onClickCancel = {
                                            UiViewModelManager.hideDialog()
                                    }
                                ))
                            })
                        }

                        item {
                            var selectHour by remember {
                                mutableStateOf(curHour)
                            }
                            var selectMinute by remember {
                                mutableStateOf(curMinute)
                            }
                            var selectSecond by remember {
                                mutableStateOf(curSecond)
                            }
                            SolidButtonWithText(text = "显示时间选择", onClick = {
                                UiViewModelManager.showDialog(
                                    TimePickerDialogBuilder(
                                        title = "选择时间",
                                        message = "请选择您的到店时间",
                                        hour = selectHour,
                                        minute = selectMinute,
                                        second = selectSecond,
                                        onClickSure = { h, m, s ->
                                            selectHour = h
                                            selectMinute = m
                                            selectSecond = s
                                            UiViewModelManager.hideDialog()
                                            UiViewModelManager.showToast("您的到店时间为 $selectHour:$selectMinute:$selectSecond")
                                    },
                                    onClickCancel = {
                                        UiViewModelManager.hideDialog()
                                    }
                                ))
                            })
                        }

                        item {
                            var select by remember {
                                mutableStateOf(1)
                            }
                            SolidButtonWithText(text = "显示整型数字选择", onClick = {
                                UiViewModelManager.showDialog(
                                    IntNumberPickerDialogBuilder(
                                        title = "提示",
                                        message = "请选择地图缩放级别",
                                        value = select,
                                        start = 0,
                                        endInclude = 22,
                                        onClickSure = { v ->
                                            select = v
                                            UiViewModelManager.hideDialog()
                                            UiViewModelManager.showToast("您选择的地图缩放级别 $select")
                                        },
                                    onClickCancel = {
                                        UiViewModelManager.hideDialog()
                                    }
                                ))
                            })
                        }

                        item {
                            var select by remember {
                                mutableStateOf(1.0)
                            }
                            SolidButtonWithText(text = "显示浮点型数字选择", onClick = {
                                UiViewModelManager.showDialog(
                                    DoubleNumberPickerDialogBuilder(
                                        title = "提示",
                                        message = "请选择地图缩放级别",
                                        value = select,
                                        start = 0.0,
                                        endInclude = 10.0,
                                        step = 0.5,
                                        onClickSure = { v ->
                                            select = v
                                            UiViewModelManager.hideDialog()
                                            UiViewModelManager.showToast("您选择的地图缩放级别 $select")
                                    },
                                    onClickCancel = {
                                        UiViewModelManager.hideDialog()
                                    }
                                ))
                            })
                        }

                        item {
                            val list = remember {
                                listOf(
                                    CustomPickInfo("360*640px", "360P"),
                                    CustomPickInfo("720*1280px", "720P"),
                                    CustomPickInfo("1080*1920px", "1080P"),
                                    CustomPickInfo("1440*2560px", "2K"),
                                )
                            }
                            var select by remember {
                                mutableStateOf(list[0])
                            }
                            SolidButtonWithText(text = "显示实体类选择", onClick = {
                                UiViewModelManager.showDialog(
                                    PickerDialogBuilder(
                                        title = "提示",
                                        message = "请选择屏幕分辨率",
                                        label = { it.text },
                                        value = select,
                                        list = list,
                                        onClickSure = { v ->
                                            select = v
                                            UiViewModelManager.hideDialog()
                                            UiViewModelManager.showToast("您选择的屏幕分辨率 ${select.text} : ${select.name}")
                                        },
                                    onClickCancel = {
                                        UiViewModelManager.hideDialog()
                                    }
                                ))
                            })
                        }

                        item {
                            SolidButtonWithText(text = "显示进度", onClick = {
                                val builder = ProgressDialogBuilder(
                                    title = "发送中",
                                    message = "请稍等片刻",
                                    progress = 0,
                                    max = 100
                                )
                                UiViewModelManager.showDialog(builder)
                                lifecycleScope.launch(Dispatchers.IO) {
                                    var index = 0
                                    while (index < 100) {
                                        index++
                                        val _builder = builder.copy(
                                            progress = index.toLong()
                                        )
                                        UiViewModelManager.showDialog(_builder)
                                        delay(100)
                                    }
                                    UiViewModelManager.hideDialog()
                                }
                            })
                        }

                        item {
                            SolidButtonWithText(text = "显示成功", onClick = {
                                UiViewModelManager.showDialog(SuccessOrFalseDialogBuilder(
                                    title = "发送成功",
                                    isSuccess = true,
                                    onClickButton = {
                                        UiViewModelManager.hideDialog()
                                    }
                                ))
                            })
                        }

                        item {
                            SolidButtonWithText(text = "显示失败", onClick = {
                                UiViewModelManager.showDialog(SuccessOrFalseDialogBuilder(
                                    title = "发送失败",
                                    isSuccess = false,
                                    onClickButton = {
                                        UiViewModelManager.hideDialog()
                                    }
                                ))
                            })
                        }
                    }
                }
            }
        }
    }
}

data class CustomPickInfo(
    var text: String = "",
    var name: String = ""
)

@Composable
fun CustomRadioDialogItem(
    isSelect: Boolean,
    info: Pair<String, Any>,
    onClick: () -> Unit
) {
    val background = remember(isSelect) {
        if (isSelect) AppColor.Main.primary else Color.Transparent
    }
    val borderWidth = remember(isSelect) {
        if (isSelect) 0.dp else 1.dp
    }
    val textColor = remember(isSelect) {
        if (isSelect) Color.White else AppColor.Neutral.body
    }
    Box(
        modifier = Modifier
            .padding(top = 8.dp)
            .size(width = 104.dp, height = 40.dp)
            .background(background, AppShape.RC.Cycle)
            .border(borderWidth, AppColor.Neutral.hint, AppShape.RC.Cycle)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = info.first, style = AppText.Medium.Body.v14, color = textColor)
    }
}