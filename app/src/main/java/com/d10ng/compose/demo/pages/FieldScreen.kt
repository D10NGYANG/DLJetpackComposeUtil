package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.d10ng.compose.demo.PageTransitions
import com.d10ng.compose.demo.R
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.form.Field
import com.d10ng.compose.ui.navigation.NavBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/**
 * 输入框
 * @Author d10ng
 * @Date 2023/9/5 11:15
 */
@RootNavGraph
@Destination(style = PageTransitions::class)
@Composable
fun FieldScreen(
    nav: DestinationsNavigator
) {
    FieldScreenView(onClickBack = nav::navigateUp)
}

@Composable
private fun FieldScreenView(
    onClickBack: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "Field", onClickBack = onClickBack)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                CellGroup(
                    title = "基础用法", inset = true
                ) {
                    var value by remember { mutableStateOf("") }
                    Field(value = value, onValueChange = { value = it }, label = "文本")
                }
                CellGroup(
                    title = "自定义类型", inset = true
                ) {
                    var value1 by remember { mutableStateOf("") }
                    var value2 by remember { mutableStateOf("") }
                    var value3 by remember { mutableStateOf("") }
                    var value4 by remember { mutableStateOf("") }
                    var value5 by remember { mutableStateOf("") }
                    Field(value = value1, onValueChange = { value1 = it }, label = "文本")
                    Field(
                        value = value2,
                        onValueChange = { value2 = it },
                        label = "手机号",
                        type = KeyboardType.Phone
                    )
                    Field(
                        value = value3,
                        onValueChange = { value3 = it },
                        label = "整数",
                        type = KeyboardType.Number
                    )
                    Field(
                        value = value4,
                        onValueChange = { value4 = it },
                        label = "数字",
                        type = KeyboardType.Decimal
                    )
                    Field(
                        value = value5,
                        onValueChange = { value5 = it },
                        label = "密码",
                        type = KeyboardType.Password
                    )
                }
                CellGroup(
                    title = "禁用输入框", inset = true
                ) {
                    var value1 by remember { mutableStateOf("输入框只读") }
                    var value2 by remember { mutableStateOf("输入框已禁用") }
                    Field(
                        value = value1,
                        onValueChange = { value1 = it },
                        label = "文本",
                        readonly = true
                    )
                    Field(
                        value = value2,
                        onValueChange = { value2 = it },
                        label = "文本",
                        disabled = true,
                        border = false
                    )
                }
                CellGroup(
                    title = "显示图标", inset = true
                ) {
                    var value1 by remember { mutableStateOf("显示图标") }
                    var value2 by remember { mutableStateOf("显示清除图标") }
                    Field(
                        value = value1,
                        onValueChange = { value1 = it },
                        label = "文本",
                        leftIconId = R.drawable.round_mood_24
                    )
                    Field(
                        value = value2,
                        onValueChange = { value2 = it },
                        label = "文本",
                        leftIconId = R.drawable.round_mood_24,
                        canClear = true,
                        border = false
                    )
                }
                CellGroup(
                    title = "错误提示", inset = true
                ) {
                    var value1 by remember { mutableStateOf("") }
                    var value2 by remember { mutableStateOf("") }
                    Field(
                        value = value1,
                        onValueChange = { value1 = it },
                        label = "用户名",
                        required = true,
                        error = "错误提示"
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun FieldScreenViewPreview() {
    FieldScreenView()
}