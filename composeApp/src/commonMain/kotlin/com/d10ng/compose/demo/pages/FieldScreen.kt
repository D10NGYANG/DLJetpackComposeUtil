package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.d10ng.common.calculate.isMobileNumber
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.form.Field
import com.d10ng.compose.ui.form.FieldLabelAlign
import com.d10ng.compose.ui.navigation.NavBar
import dljetpackcomposeutil.composeapp.generated.resources.Res
import dljetpackcomposeutil.composeapp.generated.resources.round_mood_24
import kotlinx.coroutines.launch

/**
 * 输入框
 * @Author d10ng
 * @Date 2023/9/5 11:15
 */
class FieldScreen : Screen {
    @Composable
    override fun Content() {
        FieldScreenView()
    }
}

@Composable
private fun FieldScreenView() {
    val navigator = LocalNavigator.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "Field", onClickBack = { navigator?.pop() })

        val scrollState = rememberScrollState()
        val scope = rememberCoroutineScope()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            CellGroup(
                title = "基础用法", inset = true
            ) {
                var value by remember { mutableStateOf("") }
                Field(value = value, onValueChange = { value = it }, label = "文本", border = false)
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
                    type = KeyboardType.Password,
                    canClear = true,
                    border = false
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
                    leftIconResource = Res.drawable.round_mood_24
                )
                Field(
                    value = value2,
                    onValueChange = { value2 = it },
                    label = "文本",
                    leftIconResource = Res.drawable.round_mood_24,
                    canClear = true,
                    border = false
                )
            }
            CellGroup(
                title = "错误提示", inset = true
            ) {
                var value1 by remember { mutableStateOf("") }
                val error1 = remember(value1) {
                    if (value1.isEmpty()) "请输入用户名" else ""
                }
                var value2 by remember { mutableStateOf("123") }
                val error2 = remember(value2) {
                    if (value2.isMobileNumber().not()) "手机号格式错误" else ""
                }
                Field(
                    value = value1,
                    onValueChange = { value1 = it },
                    label = "用户名",
                    required = true,
                    error = error1,
                    canClear = true
                )
                Field(
                    value = value2,
                    onValueChange = { value2 = it },
                    label = "手机号",
                    required = true,
                    error = error2,
                    canClear = true,
                    border = false
                )
            }
            CellGroup(
                title = "高度自适应", inset = true
            ) {
                var value1 by remember { mutableStateOf("") }
                var value2 by remember { mutableStateOf("") }
                var value3 by remember { mutableStateOf("") }
                Field(
                    value = value1,
                    onValueChange = { value1 = it },
                    label = "留言",
                    autoSize = true,
                    onSizeChange = { _, offset ->
                        scope.launch {
                            scrollState.scrollBy(offset.height.toFloat())
                        }
                    }
                )
                Field(
                    value = value2,
                    onValueChange = { value2 = it },
                    label = "留言",
                    placeholder = "多行模式",
                    minLines = 3,
                    autoSize = true,
                    onSizeChange = { _, offset ->
                        scope.launch {
                            scrollState.scrollBy(offset.height.toFloat())
                        }
                    }
                )
                Field(
                    value = value3,
                    onValueChange = { value3 = it },
                    label = "留言",
                    placeholder = "行数限制3行，超出滚动显示",
                    minLines = 3
                )
            }
            CellGroup(
                title = "输入框文本位置", inset = true
            ) {
                var value1 by remember { mutableStateOf("") }
                var value2 by remember { mutableStateOf("") }
                var value3 by remember { mutableStateOf("") }
                var value4 by remember { mutableStateOf("") }
                Field(
                    value = value1,
                    onValueChange = { value1 = it },
                    label = "文本",
                    labelAlign = FieldLabelAlign.TOP,
                    placeholder = "顶部对齐"
                )
                Field(
                    value = value2,
                    onValueChange = { value2 = it },
                    label = "文本",
                    labelAlign = FieldLabelAlign.LEFT,
                    placeholder = "左对齐"
                )
                Field(
                    value = value3,
                    onValueChange = { value3 = it },
                    label = "文本",
                    labelAlign = FieldLabelAlign.CENTER,
                    placeholder = "居中对齐"
                )
                Field(
                    value = value4,
                    onValueChange = { value4 = it },
                    label = "文本",
                    labelAlign = FieldLabelAlign.RIGHT,
                    placeholder = "右对齐",
                    border = false
                )
            }
        }
    }
}