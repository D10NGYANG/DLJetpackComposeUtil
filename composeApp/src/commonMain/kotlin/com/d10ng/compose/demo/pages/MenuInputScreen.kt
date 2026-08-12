package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.compose.view.ListItemMenu
import com.d10ng.compose.view.MenuInput

private val cityMenus = listOf(
    "北京", "上海", "广州", "深圳", "杭州", "南京", "苏州", "成都", "重庆", "武汉",
    "西安", "长沙", "青岛", "厦门", "福州", "昆明", "合肥", "郑州", "天津", "宁波",
    "无锡", "济南", "南昌", "大连", "珠海"
).mapIndexed { index, city -> ListItemMenu(show = city, data = index + 1) }

@Composable
fun MenuInputScreen(onBack: () -> Unit = {}) {
    val focusManager = LocalFocusManager.current
    var cityValue by remember { mutableStateOf("") }
    var cityFocused by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf<ListItemMenu?>(null) }
    var amountValue by remember { mutableStateOf("12A") }
    var passwordValue by remember { mutableStateOf("compose123") }
    var nextCount by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        NavBar(title = "MenuInput 菜单输入", onClickBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            MenuInputSection("过滤与选择") {
                MenuInput(
                    value = cityValue,
                    onValueChange = {
                        cityValue = it
                        selectedCity = null
                    },
                    modifier = Modifier.semantics { contentDescription = "城市菜单输入" },
                    placeholder = "输入城市名称",
                    menus = cityMenus,
                    noMatchMenuTips = "没有匹配的城市",
                    isFocus = cityFocused,
                    onFocus = { cityFocused = true },
                    onNoFocus = { cityFocused = false },
                    onMenuSelect = { item ->
                        cityValue = item.show
                        selectedCity = item
                        cityFocused = false
                        focusManager.clearFocus()
                    }
                )
                MenuInputStatus(
                    selectedCity?.let { "已选择：${it.show}（编号 ${it.data}）" }
                        ?: "输入关键字过滤；空输入最多展示前 20 项"
                )
            }

            MenuInputSection("无匹配结果") {
                var noMatchFocused by remember { mutableStateOf(false) }
                var noMatchValue by remember { mutableStateOf("火星") }
                MenuInput(
                    value = noMatchValue,
                    onValueChange = { noMatchValue = it },
                    modifier = Modifier.semantics { contentDescription = "空结果菜单输入" },
                    menus = cityMenus,
                    noMatchMenuTips = "没有匹配的城市",
                    isFocus = noMatchFocused,
                    onFocus = { noMatchFocused = true },
                    onNoFocus = { noMatchFocused = false }
                )
                MenuInputStatus("聚焦后可检查自定义空结果文案")
            }

            MenuInputSection("前后缀、错误与固定宽度") {
                val amountError = when {
                    amountValue.isBlank() -> "请输入金额"
                    amountValue.toIntOrNull() == null -> "金额只能包含数字"
                    else -> ""
                }
                MenuInput(
                    value = amountValue,
                    onValueChange = { amountValue = it },
                    modifier = Modifier.semantics { contentDescription = "金额菜单输入" },
                    placeholder = "输入金额",
                    prefix = "CNY ",
                    suffix = " .00",
                    error = amountError,
                    keyboardType = KeyboardType.Number,
                    inputWidth = 240.dp,
                    inputBackgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                    inputBackgroundShape = RoundedCornerShape(8.dp)
                )
                MenuInputStatus("清空或输入非数字内容可检查错误状态")
            }

            MenuInputSection("密码显隐与 Next") {
                MenuInput(
                    value = passwordValue,
                    onValueChange = { passwordValue = it },
                    modifier = Modifier.semantics { contentDescription = "密码菜单输入" },
                    placeholder = "输入密码",
                    keyboardType = KeyboardType.Password,
                    isPassword = true,
                    onClickNext = {
                        nextCount += 1
                        focusManager.clearFocus()
                    }
                )
                MenuInputStatus("Next 已触发 $nextCount 次")
            }
        }
    }
}

@Composable
private fun MenuInputSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        content()
    }
}

@Composable
private fun MenuInputStatus(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
