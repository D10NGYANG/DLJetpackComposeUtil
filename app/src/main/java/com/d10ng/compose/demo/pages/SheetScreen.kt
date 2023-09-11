package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.d10ng.compose.demo.PageTransitions
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.base.Cell
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.compose.ui.sheet.builder.RadioSheetBuilder
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
            CellGroup(title = "使用方式", inset = true) {
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
                    ).apply { clickOutsideDismiss = true })
                })
            }
        }
    }
}

enum class Options(val text: String) {
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

@Preview
@Composable
private fun SheetScreenPreview() {
    SheetScreenView()
}