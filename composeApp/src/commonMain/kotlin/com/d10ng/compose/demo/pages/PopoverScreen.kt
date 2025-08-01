package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.d10ng.compose.demo.Nav
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.base.Button
import com.d10ng.compose.ui.base.ButtonType
import com.d10ng.compose.ui.base.CellTitle
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.compose.ui.show.Popover
import com.d10ng.compose.ui.show.PopoverColumnItems

/**
 * Popover 气泡弹出框
 * @Author d10ng
 * @Date 2023/9/18 14:28
 */

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PopoverScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "Popover", onClickBack = { Nav.instant().popBackStack() })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CellTitle(title = "基础用法")
            FlowRow(
                modifier = Modifier.fillMaxWidth().padding(start = 19.dp),
                horizontalArrangement = Arrangement.spacedBy(19.dp)
            ) {
                Box {
                    var expanded by remember { mutableStateOf(false) }
                    Button(
                        text = "浅色风格",
                        onClick = { expanded = true },
                        type = ButtonType.PRIMARY
                    )
                    Popover(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        PopoverColumnItems(
                            value = setOf("选项一", "选项二", "选项三"),
                            onClick = {
                                UiViewModelManager.showToast(it)
                                expanded = false
                            }
                        )
                    }
                }
                Box {
                    var expanded by remember { mutableStateOf(false) }
                    Button(
                        text = "深色风格",
                        onClick = { expanded = true },
                        type = ButtonType.PRIMARY
                    )
                    Popover(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        dark = true
                    ) {
                        PopoverColumnItems(
                            value = setOf("选项一", "选项二", "选项三"),
                            dark = true,
                            onClick = {
                                UiViewModelManager.showToast(it)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}