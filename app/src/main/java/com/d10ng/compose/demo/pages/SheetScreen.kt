package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.d10ng.compose.demo.PageTransitions
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.base.Cell
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.form.DatePickerMode
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.compose.ui.sheet.builder.ActionSheetBuilder
import com.d10ng.compose.ui.sheet.builder.DatePickerSheetBuilder
import com.d10ng.compose.ui.sheet.builder.MultiPickerSheetBuilder
import com.d10ng.compose.ui.sheet.builder.RadioSheetBuilder
import com.d10ng.compose.ui.sheet.builder.SinglePickerSheetBuilder
import com.d10ng.datelib.curTime
import com.d10ng.datelib.toDateStr
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/**
 * 底部弹窗
 * @Author d10ng
 * @Date 2023/9/11 10:41
 */
@RootNavGraph
@Destination(style = PageTransitions::class)
@Composable
fun SheetScreen(
    nav: DestinationsNavigator
) {
    SheetScreenView(onClickBack = nav::navigateUp)
}

@Composable
private fun SheetScreenView(
    onClickBack: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "Sheet", onClickBack = onClickBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                Cell(title = "动作面板弹窗", link = true, onClick = {
                    UiViewModelManager.showSheet(ActionSheetBuilder(
                        items = Options.entries.toSet(),
                        itemText = { it.text },
                        onItemClick = {
                            UiViewModelManager.showToast("选择了${it.text}")
                        }
                    ))
                })
                var state by remember { mutableIntStateOf(0) }
                val titles = listOf("Tab 1", "Tab 2", "Tab 3 with lots of text")
                Column {
                    TabRow(selectedTabIndex = state, divider = {}) {
                        titles.forEachIndexed { index, title ->
                            Tab(
                                selected = state == index,
                                onClick = { state = index },
                                text = { Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis) }
                            )
                        }
                    }
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = "Text tab ${state + 1} selected",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
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
            CellGroup(title = "日期时间选择", inset = true) {
                var value1 by remember {
                    mutableLongStateOf(curTime)
                }
                Cell(title = "日期选择面板弹窗(年月日)", link = true, onClick = {
                    UiViewModelManager.showSheet(DatePickerSheetBuilder(
                        title = "选择日期",
                        value = value1,
                        onConfirmClick = {
                            value1 = it
                            UiViewModelManager.showToast("选择了 ${it.toDateStr("yyyy-MM-dd")}")
                            true
                        }
                    ))
                })
                var value2 by remember {
                    mutableLongStateOf(curTime)
                }
                Cell(title = "日期选择面板弹窗(年月)", link = true, onClick = {
                    UiViewModelManager.showSheet(DatePickerSheetBuilder(
                        title = "选择日期",
                        value = value2,
                        mode = DatePickerMode.YM,
                        onConfirmClick = {
                            value2 = it
                            UiViewModelManager.showToast("选择了 ${it.toDateStr("yyyy-MM")}")
                            true
                        }
                    ))
                })
                var value3 by remember {
                    mutableLongStateOf(curTime)
                }
                Cell(title = "日期选择面板弹窗(年)", link = true, onClick = {
                    UiViewModelManager.showSheet(DatePickerSheetBuilder(
                        title = "选择日期",
                        value = value3,
                        mode = DatePickerMode.Y,
                        onConfirmClick = {
                            value3 = it
                            UiViewModelManager.showToast("选择了 ${it.toDateStr("yyyy")}")
                            true
                        }
                    ))
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

@Preview
@Composable
private fun SheetScreenPreview() {
    SheetScreenView()
}