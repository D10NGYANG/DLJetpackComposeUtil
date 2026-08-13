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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.d10ng.compose.demo.resources.Res
import com.d10ng.compose.demo.resources.round_mood_24
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.compose.view.ListInputItem
import com.d10ng.compose.view.ListItemMenu

private val departmentMenus = listOf(
    "产品设计", "客户端开发", "服务端开发", "质量保障", "项目管理", "用户运营"
).mapIndexed { index, name -> ListItemMenu(show = name, data = index + 1) }

@Composable
fun LibraryListInputItemScreen(onBack: () -> Unit = {}) {
    val focusManager = LocalFocusManager.current
    var name by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("Compose 用户") }
    var amount by remember { mutableStateOf("12A") }
    var department by remember { mutableStateOf("") }
    var departmentFocused by remember { mutableStateOf(false) }
    var selectedDepartment by remember { mutableStateOf<ListItemMenu?>(null) }
    var password by remember { mutableStateOf("compose123") }
    var nextCount by remember { mutableIntStateOf(0) }
    var address by remember {
        mutableStateOf("深圳市南山区科技园科苑路用于检查窄屏长输入内容的详细地址")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        NavBar(title = "ListInputItem 列表输入", onClickBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ListInputItemSection("基础输入与空值") {
                ListInputItem(
                    modifier = Modifier.semantics { contentDescription = "姓名列表输入" },
                    title = "姓名",
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "请输入姓名"
                )
                ListInputItemStatus(if (name.isEmpty()) "当前为空" else "当前值：$name")
            }

            ListInputItemSection("图标、必填与说明") {
                ListInputItem(
                    modifier = Modifier.semantics { contentDescription = "昵称列表输入" },
                    title = "昵称",
                    note = "公开展示名称",
                    iconResource = Res.drawable.round_mood_24,
                    iconColorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    isShowTitleStar = true,
                    value = nickname,
                    onValueChange = { nickname = it },
                    inputWidth = 176.dp
                )
            }

            ListInputItemSection("前后缀、错误与样式") {
                val amountError = when {
                    amount.isBlank() -> "请输入预算"
                    amount.toIntOrNull() == null -> "预算只能包含数字"
                    else -> ""
                }
                ListInputItem(
                    modifier = Modifier.semantics { contentDescription = "预算列表输入" },
                    title = "预算",
                    value = amount,
                    onValueChange = { amount = it },
                    placeholder = "金额",
                    prefix = "CNY ",
                    suffix = " .00",
                    error = amountError,
                    keyboardType = KeyboardType.Number,
                    inputWidth = 200.dp,
                    inputBackgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                    inputBackgroundShape = RoundedCornerShape(8.dp)
                )
                ListInputItemStatus(amountError.ifEmpty { "金额格式有效" })
            }

            ListInputItemSection("菜单过滤与选择") {
                ListInputItem(
                    modifier = Modifier.semantics { contentDescription = "部门列表输入" },
                    title = "部门",
                    value = department,
                    onValueChange = {
                        department = it
                        selectedDepartment = null
                    },
                    placeholder = "输入部门",
                    menus = departmentMenus,
                    noMatchMenuTips = "没有匹配的部门",
                    isFocus = departmentFocused,
                    inputWidth = 200.dp,
                    onFocus = { departmentFocused = true },
                    onNoFocus = { departmentFocused = false },
                    onMenuSelect = { item ->
                        department = item.show
                        selectedDepartment = item
                        departmentFocused = false
                        focusManager.clearFocus()
                    }
                )
                ListInputItemStatus(
                    selectedDepartment?.let { "已选择：${it.show}（编号 ${it.data}）" }
                        ?: "输入关键字过滤部门"
                )
            }

            ListInputItemSection("密码与 Next") {
                ListInputItem(
                    modifier = Modifier.semantics { contentDescription = "密码列表输入" },
                    title = "密码",
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "输入密码",
                    keyboardType = KeyboardType.Password,
                    isPassword = true,
                    inputWidth = 200.dp,
                    onClickNext = {
                        nextCount += 1
                        focusManager.clearFocus()
                    }
                )
                ListInputItemStatus("Next 已触发 $nextCount 次")
            }

            ListInputItemSection("只读与禁用") {
                ListInputItem(
                    modifier = Modifier.semantics { contentDescription = "只读列表输入" },
                    title = "只读值",
                    note = "可选择但不允许编辑或展开菜单",
                    value = "只读内容",
                    onValueChange = {},
                    readOnly = true,
                    menus = departmentMenus
                )
                ListInputItem(
                    modifier = Modifier.semantics { contentDescription = "禁用列表输入" },
                    title = "禁用值",
                    note = "不响应输入、焦点和菜单操作",
                    value = "禁用内容",
                    onValueChange = {},
                    enabled = false,
                    menus = departmentMenus
                )
            }

            ListInputItemSection("自适应宽度与长文本") {
                ListInputItem(
                    modifier = Modifier.semantics { contentDescription = "地址列表输入" },
                    title = "用于窄屏检查的详细地址",
                    note = "较长说明用于观察标签区换行与输入区的剩余宽度",
                    value = address,
                    onValueChange = { address = it },
                    placeholder = "输入详细地址",
                    maxLines = 2,
                    inputWidth = null
                )
                ListInputItemStatus("用于检查 390px 下长标题、长说明和多行输入的组合布局")
            }
        }
    }
}

@Composable
private fun ListInputItemSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        content()
    }
}

@Composable
private fun ListInputItemStatus(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 16.dp),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
