package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.d10ng.compose.demo.PageTransitions
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.base.Cell
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.form.Stepper
import com.d10ng.compose.ui.form.StepperStyle
import com.d10ng.compose.ui.navigation.NavBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/**
 * 步进器
 * @Author d10ng
 * @Date 2023/9/6 17:47
 */
@RootNavGraph
@Destination(style = PageTransitions::class)
@Composable
fun StepperScreen(
    nav: DestinationsNavigator
) {
    StepperScreenView(onClickBack = nav::navigateUp)
}

@Composable
private fun StepperScreenView(
    onClickBack: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "Stepper", onClickBack = onClickBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CellGroup(title = "使用方式", inset = true) {
                Cell(title = "基础用法") {
                    var value1 by remember { mutableIntStateOf(1) }
                    Stepper(value = value1, onValueChange = { value1 = it })
                }
                Cell(title = "步长设置") {
                    var value1 by remember { mutableIntStateOf(0) }
                    Stepper(value = value1, onValueChange = { value1 = it }, step = 2)
                }
                Cell(title = "限制输入范围") {
                    var value1 by remember { mutableIntStateOf(10) }
                    Stepper(value = value1, onValueChange = { value1 = it }, max = 10)
                }
                Cell(title = "禁用状态") {
                    var value1 by remember { mutableIntStateOf(0) }
                    Stepper(value = value1, onValueChange = { value1 = it }, disabled = true)
                }
                Cell(title = "禁用输入框") {
                    var value1 by remember { mutableIntStateOf(0) }
                    Stepper(value = value1, onValueChange = { value1 = it }, canInput = false)
                }
                Cell(title = "圆角风格") {
                    var value1 by remember { mutableIntStateOf(0) }
                    Stepper(value = value1, onValueChange = { value1 = it }, style = StepperStyle.Round)
                }
            }
        }
    }
}

@Preview
@Composable
private fun StepperScreenViewPreview() {
    StepperScreenView()
}