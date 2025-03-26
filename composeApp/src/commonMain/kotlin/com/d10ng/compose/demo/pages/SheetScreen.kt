package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.base.Cell
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.form.DatePickerMode
import com.d10ng.compose.ui.form.TimePickerMode
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.compose.ui.sheet.builder.ActionSheetBuilder
import com.d10ng.compose.ui.sheet.builder.DatePickerSheetBuilder
import com.d10ng.compose.ui.sheet.builder.MultiPickerSheetBuilder
import com.d10ng.compose.ui.sheet.builder.RadioSheetBuilder
import com.d10ng.compose.ui.sheet.builder.SinglePickerSheetBuilder
import com.d10ng.compose.ui.sheet.builder.TimePickerSheetBuilder
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * 底部弹窗
 * @Author d10ng
 * @Date 2023/9/11 10:41
 */
class SheetScreen : Screen {
    @Composable
    override fun Content() {
        SheetScreenView()
    }
}

@Composable
private fun SheetScreenView() {
    val navigator = LocalNavigator.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "Sheet", onClickBack = { navigator?.pop() })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            CellGroup(title = "基础用法", inset = true) {
                var value by remember {
                    mutableStateOf(Options.ORIGINAL)
                }
                Cell(title = "单选面板弹窗", link = true, onClick = {
                    UiViewModelManager.showSheet(RadioSheetBuilder(
                        title = "选择分辨率",
                        items = Options.entries.toSet(),
                        itemText = { it.text },
                        selectedItem = value,
                        onConfirmClick = {
                            value = it
                            UiViewModelManager.showToast("选择了${it.text}")
                            true
                        }
                    ))
                })
                Cell(title = "单选面板弹窗-无确定按钮", link = true, onClick = {
                    UiViewModelManager.showSheet(RadioSheetBuilder(
                        title = "选择分辨率",
                        items = Options.entries.toSet(),
                        itemText = { it.text },
                        selectedItem = value,
                        showButton = false,
                        onConfirmClick = {
                            value = it
                            UiViewModelManager.showToast("选择了${it.text}")
                            true
                        }
                    ))
                })
                Cell(title = "动作面板弹窗", link = true, onClick = {
                    UiViewModelManager.showSheet(ActionSheetBuilder(
                        items = Options.entries.toSet(),
                        itemText = { it.text },
                        onItemClick = {
                            UiViewModelManager.showToast("选择了${it.text}")
                        }
                    ))
                })
                Cell(title = "动作面板弹窗2", link = true, onClick = {
                    UiViewModelManager.showSheet(ActionSheetBuilder(
                        items = setOf("高德地图", "百度地图"),
                        onItemClick = {
                            UiViewModelManager.showToast("选择了${it}")
                        }
                    ))
                })
                Cell(title = "动作面板弹窗-自定义字体样式", link = true, onClick = {
                    UiViewModelManager.showSheet(ActionSheetBuilder(
                        items = setOf("编辑", "删除"),
                        itemStyle = { str ->
                            if (str.contentEquals("删除")) AppText.Normal.Error.default else AppText.Normal.Title.default
                        },
                        onItemClick = {
                            UiViewModelManager.showToast("选择了${it}")
                        }
                    ))
                })
            }
            CellGroup(title = "滚轮选择", inset = true) {
                var value by remember {
                    mutableStateOf(WeekType.entries.first())
                }
                Cell(title = "单列滚轮选择面板弹窗", link = true, onClick = {
                    UiViewModelManager.showSheet(SinglePickerSheetBuilder(
                        title = "选择时间",
                        items = WeekType.entries.toSet(),
                        itemText = { it.text },
                        selectedItem = value,
                        onConfirmClick = {
                            value = it
                            UiViewModelManager.showToast("选择了${value.text}")
                            true
                        }
                    ))
                })
                var value1 by remember {
                    mutableStateOf(listOf(WeekType.MONDAY, DayTimeType.AM))
                }
                Cell(title = "多列滚轮选择面板弹窗", link = true, onClick = {
                    UiViewModelManager.showSheet(MultiPickerSheetBuilder(
                        title = "选择",
                        items = listOf(WeekType.entries.toSet(), DayTimeType.entries.toSet()),
                        itemText = { _, item -> item.text },
                        selectedItems = value1,
                        onConfirmClick = {
                            value1 = it
                            UiViewModelManager.showToast("选择了 ${value1[0].text}:${value1[1].text}")
                            true
                        }
                    ))
                })
            }
            CellGroup(title = "日期选择", inset = true) {
                var value1 by remember {
                    mutableLongStateOf(Clock.System.now().toEpochMilliseconds())
                }
                Cell(title = "日期选择面板弹窗(年月日)", link = true, onClick = {
                    UiViewModelManager.showSheet(
                        DatePickerSheetBuilder(
                            title = "选择日期",
                            value = value1,
                            onConfirmClick = {
                                value1 = it
                                val datetime = Instant.fromEpochMilliseconds(it)
                                    .toLocalDateTime(TimeZone.currentSystemDefault())
                                UiViewModelManager.showToast("选择了 ${datetime.year}-${datetime.monthNumber}-${datetime.dayOfMonth}")
                            true
                        }
                    ))
                })
                var value2 by remember {
                    mutableLongStateOf(Clock.System.now().toEpochMilliseconds())
                }
                Cell(title = "日期选择面板弹窗(年月)", link = true, onClick = {
                    UiViewModelManager.showSheet(DatePickerSheetBuilder(
                        title = "选择年月",
                        value = value2,
                        mode = DatePickerMode.YM,
                        onConfirmClick = {
                            value2 = it
                            val datetime = Instant.fromEpochMilliseconds(it)
                                .toLocalDateTime(TimeZone.currentSystemDefault())
                            UiViewModelManager.showToast("选择了 ${datetime.year}-${datetime.monthNumber}")
                            true
                        }
                    ))
                })
                var value3 by remember {
                    mutableLongStateOf(Clock.System.now().toEpochMilliseconds())
                }
                Cell(title = "日期选择面板弹窗(年)", link = true, onClick = {
                    UiViewModelManager.showSheet(DatePickerSheetBuilder(
                        title = "选择年份",
                        value = value3,
                        mode = DatePickerMode.Y,
                        onConfirmClick = {
                            value3 = it
                            val datetime = Instant.fromEpochMilliseconds(it)
                                .toLocalDateTime(TimeZone.currentSystemDefault())
                            UiViewModelManager.showToast("选择了 ${datetime.year}")
                            true
                        }
                    ))
                })
                var value4 by remember {
                    mutableLongStateOf(Clock.System.now().toEpochMilliseconds())
                }
                Cell(title = "日期选择面板弹窗(月日)", link = true, onClick = {
                    UiViewModelManager.showSheet(DatePickerSheetBuilder(
                        title = "选择月日",
                        value = value4,
                        mode = DatePickerMode.MD,
                        onConfirmClick = {
                            value4 = it
                            val datetime = Instant.fromEpochMilliseconds(it)
                                .toLocalDateTime(TimeZone.currentSystemDefault())
                            UiViewModelManager.showToast("选择了 ${datetime.monthNumber}-${datetime.dayOfMonth}")
                            true
                        },
                        itemText = { i, item ->
                            when (i) {
                                0 -> "${item}月"
                                1 -> "${item}日"
                                else -> item
                            }
                        }
                    ))
                })
                var value5 by remember {
                    mutableLongStateOf(Clock.System.now().toEpochMilliseconds())
                }
                Cell(title = "日期选择面板弹窗(月)", link = true, onClick = {
                    UiViewModelManager.showSheet(DatePickerSheetBuilder(
                        title = "选择月份",
                        value = value5,
                        mode = DatePickerMode.M,
                        onConfirmClick = {
                            value5 = it
                            val datetime = Instant.fromEpochMilliseconds(it)
                                .toLocalDateTime(TimeZone.currentSystemDefault())
                            UiViewModelManager.showToast("选择了 ${datetime.monthNumber}")
                            true
                        },
                        itemText = { _, item -> "${item}月" }
                    ))
                })
            }
            CellGroup(title = "时间选择", inset = true) {
                var value1 by remember {
                    mutableIntStateOf(0)
                }
                Cell(title = "时间选择面板弹窗(时分秒)", link = true, onClick = {
                    UiViewModelManager.showSheet(TimePickerSheetBuilder(
                        title = "选择时间",
                        value = value1,
                        onConfirmClick = { v, l ->
                            value1 = v
                            UiViewModelManager.showToast("选择了 $l}")
                            true
                        }
                    ))
                })
                var value2 by remember {
                    mutableIntStateOf(0)
                }
                Cell(title = "时间选择面板弹窗(时分)", link = true, onClick = {
                    UiViewModelManager.showSheet(TimePickerSheetBuilder(
                        title = "选择时间",
                        value = value2,
                        mode = TimePickerMode.HM,
                        itemText = { i, item ->
                            when (i) {
                                0 -> "${item}时"
                                1 -> "${item}分"
                                else -> item
                            }
                        },
                        onConfirmClick = { v, l ->
                            value2 = v
                            UiViewModelManager.showToast("选择了 $l}")
                            true
                        }
                    ))
                })
                Cell(title = "时间选择面板弹窗(默认当前时间)", link = true, onClick = {
                    UiViewModelManager.showSheet(TimePickerSheetBuilder(
                        title = "选择时间",
                        onConfirmClick = { _, l ->
                            UiViewModelManager.showToast("选择了 $l}")
                            true
                        }
                    ).apply { setCurrentTime() })
                })
            }
        }
    }
}

private interface BaseOption {
    val text: String
}

// 分辨率类型
private enum class Options(override val text: String): BaseOption {
    // 保持原图
    ORIGINAL("保持原图"),
    // 240P
    P240("240P"),
    // 360P
    P360("360P"),
    // 480P
    P480("480P"),
    // 720P
    P720("720P"),
    // 1080P
    P1080("1080P"),
    // 2K
    P2K("2K"),
    // 4K
    P4K("4K"),
}

// 星期类型
private enum class WeekType(override val text: String): BaseOption {
    // 周一
    MONDAY("周一"),
    // 周二
    TUESDAY("周二"),
    // 周三
    WEDNESDAY("周三"),
    // 周四
    THURSDAY("周四"),
    // 周五
    FRIDAY("周五"),
    // 周六
    SATURDAY("周六"),
    // 周日
    SUNDAY("周日"),
}

// 一天的时间段类型
private enum class DayTimeType(override val text: String): BaseOption {
    // 上午
    AM("上午"),
    // 中午
    NOON("中午"),
    // 下午
    PM("下午"),
    // 晚上
    NIGHT("晚上"),
}