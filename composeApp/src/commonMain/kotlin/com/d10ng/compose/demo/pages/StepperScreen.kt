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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.base.Cell
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.form.Stepper
import com.d10ng.compose.ui.form.StepperStyle
import com.d10ng.compose.ui.navigation.NavBar

/**
 * 步进器
 * @Author d10ng
 * @Date 2023/9/6 17:47
 */
class StepperScreen : Screen {
    @Composable
    override fun Content() {
        StepperScreenView()
    }
}

@Composable
private fun StepperScreenView() {
    val navigator = LocalNavigator.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "Stepper", onClickBack = { navigator?.pop() })
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