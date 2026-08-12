package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.compose.ui.show.Badge
import com.d10ng.compose.ui.show.BadgeBox

/**
 * 徽标
 * @Author d10ng
 * @Date 2023/9/12 13:36
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BadgeScreen(onBack: () -> Unit = {}) {
    var unreadCount by remember { mutableIntStateOf(8) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "Badge", onClickBack = { onBack() })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CellGroup(title = "基础用法") {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 19.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    listOf("空点" to "", "数字" to "8", "两位数" to "28", "文本" to "HOT")
                        .forEach { (label, value) ->
                            BadgeExample(label) {
                                BadgeBox(badge = { Badge(value) }) {
                                    BadgeAnchor()
                                }
                            }
                        }
                }
            }
            CellGroup(title = "数值边界") {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 19.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    listOf(
                        "0 隐藏" to (0 to 99),
                        "负数隐藏" to (-1 to 99),
                        "等于上限" to (99 to 99),
                        "超过上限" to (100 to 99),
                        "自定义上限" to (21 to 20)
                    ).forEach { (label, values) ->
                        BadgeExample(label) {
                            BadgeBox(badge = { Badge(values.first, values.second) }) {
                                BadgeAnchor()
                            }
                        }
                    }
                }
            }
            CellGroup(title = "动态计数") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 19.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = { unreadCount = (unreadCount - 1).coerceAtLeast(0) }) {
                        Icon(Icons.Default.Remove, contentDescription = "减少未读数")
                    }
                    BadgeBox(
                        badge = {
                            Badge(
                                num = unreadCount,
                                color = AppColor.Func.error
                            )
                        }
                    ) {
                        Icon(
                            Icons.Default.Mail,
                            contentDescription = "动态未读邮件",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    IconButton(onClick = { unreadCount++ }) {
                        Icon(Icons.Default.Add, contentDescription = "增加未读数")
                    }
                    Text("当前：$unreadCount；显示：${badgeDisplayText(unreadCount)}")
                }
                TextButton(
                    modifier = Modifier.padding(horizontal = 19.dp),
                    onClick = { unreadCount = 120 }
                ) {
                    Text("设为 120")
                }
            }
            CellGroup(title = "长文本裁剪") {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 19.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    listOf("NEW", "PROMOTION", "1234567890").forEach { value ->
                        BadgeExample(value) {
                            BadgeBox(badge = { Badge(value) }) {
                                BadgeAnchor()
                            }
                        }
                    }
                }
            }
            CellGroup(title = "自定义颜色") {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 19.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    listOf("" to "主题色空点", "8" to "主题色数字", "OK" to "主题色文本")
                        .forEach { (value, label) ->
                            BadgeExample(label) {
                                BadgeBox(badge = { Badge(value, AppColor.Main.primary) }) {
                                    BadgeAnchor()
                                }
                            }
                        }
                }
            }
        }
    }
}

@Composable
private fun BadgeAnchor() {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(AppColor.Neutral.line, AppShape.RC.v8)
    )
}

@Composable
private fun BadgeExample(
    label: String,
    content: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        content()
        Text(label)
    }
}

private fun badgeDisplayText(count: Int, max: Int = 99): String = when {
    count <= 0 -> "隐藏"
    count > max -> "$max+"
    else -> count.toString()
}
