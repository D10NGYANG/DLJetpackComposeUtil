package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.navigation.NavBar

@Composable
fun MaterialListItemScreen(onBack: () -> Unit = {}) {
    var clickCount by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        NavBar(title = "Material ListItem", onClickBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ListItemSection("行数与文本层级") {
                ListItem(
                    headlineContent = { Text("单行列表项") }
                )
                HorizontalDivider()
                ListItem(
                    headlineContent = { Text("两行列表项") },
                    supportingContent = { Text("使用 supportingContent 提供补充信息") }
                )
                HorizontalDivider()
                ListItem(
                    overlineContent = { Text("分组标签") },
                    headlineContent = { Text("三行列表项") },
                    supportingContent = { Text("同时展示 overline、headline 与 supporting 三层文本") }
                )
            }

            ListItemSection("前导与尾随槽") {
                ListItem(
                    headlineContent = { Text("个人资料") },
                    supportingContent = { Text("前导头像图标与尾随操作提示") },
                    leadingContent = {
                        Icon(Icons.Default.AccountCircle, contentDescription = null)
                    },
                    trailingContent = {
                        Icon(Icons.Default.ChevronRight, contentDescription = null)
                    }
                )
                HorizontalDivider()
                ListItem(
                    headlineContent = { Text("通知") },
                    supportingContent = { Text("尾随区域也可以展示文本状态") },
                    leadingContent = {
                        Icon(Icons.Default.Notifications, contentDescription = null)
                    },
                    trailingContent = { Text("已开启") }
                )
            }

            ListItemSection("点击与禁用") {
                ListItem(
                    headlineContent = { Text("可点击列表项") },
                    supportingContent = { Text("已点击 $clickCount 次") },
                    trailingContent = {
                        Icon(Icons.Default.ChevronRight, contentDescription = null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            role = Role.Button,
                            onClick = { clickCount++ }
                        )
                )
                HorizontalDivider()
                ListItem(
                    headlineContent = { Text("禁用列表项") },
                    supportingContent = { Text("保留布局，但不响应点击") },
                    trailingContent = {
                        Icon(Icons.Default.ChevronRight, contentDescription = null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.38f)
                        .clickable(
                            enabled = false,
                            role = Role.Button,
                            onClick = { clickCount++ }
                        )
                )
            }

            ListItemSection("长文本与窄屏") {
                ListItem(
                    overlineContent = { Text("较长的辅助分类标签") },
                    headlineContent = {
                        Text("这是一条用于检查窄屏换行表现的长标题列表项")
                    },
                    supportingContent = {
                        Text("支持文字也会自然换行，并与前导、尾随内容保持稳定的排版间距。")
                    },
                    leadingContent = {
                        Icon(Icons.Default.Notifications, contentDescription = null)
                    },
                    trailingContent = { Text("详情") }
                )
            }
        }
    }
}

@Composable
private fun ListItemSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        content()
    }
}
