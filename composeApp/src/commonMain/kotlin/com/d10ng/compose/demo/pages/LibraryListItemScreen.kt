package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.d10ng.compose.demo.resources.Res
import com.d10ng.compose.demo.resources.round_mood_24
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.compose.view.ListItem

@Composable
fun LibraryListItemScreen(onBack: () -> Unit = {}) {
    var clickCount by remember { mutableIntStateOf(0) }
    var disabledClickCount by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        NavBar(title = "ListItem 列表项", onClickBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            LibraryListItemSection("字符串重载") {
                ListItem(
                    title = "静态列表项",
                    isShowArrow = false,
                    center = { Text("无点击语义") }
                )
                HorizontalDivider()
                ListItem(
                    title = "账户信息",
                    note = "补充说明展示在标题下方",
                    right = "已认证"
                )
                HorizontalDivider()
                ListItem(
                    title = "联系人",
                    note = "必填标记、资源图标与自定义图标颜色",
                    iconResource = Res.drawable.round_mood_24,
                    iconColorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    isShowTitleStar = true
                )
            }

            LibraryListItemSection("完整内容槽") {
                ListItem(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .semantics {
                            role = Role.Button
                            contentDescription = "完整插槽列表项"
                        },
                    onClick = { clickCount += 1 },
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    },
                    title = {
                        Text("可点击插槽列表项", style = MaterialTheme.typography.titleMedium)
                    },
                    note = {
                        Text("已点击 $clickCount 次", style = MaterialTheme.typography.bodySmall)
                    },
                    center = {
                        Text(
                            "中心槽",
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    },
                    right = { Text("详情") },
                    end = {
                        Icon(Icons.Default.ChevronRight, contentDescription = null)
                    }
                )
            }

            LibraryListItemSection("排列与对齐") {
                ListItem(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                    title = {
                        Text("顶部对齐", style = MaterialTheme.typography.titleMedium)
                    },
                    note = {
                        Text("第二行用于检查垂直对齐", style = MaterialTheme.typography.bodySmall)
                    },
                    right = { Text("右侧内容") }
                )
            }

            LibraryListItemSection("禁用与可访问性") {
                ListItem(
                    title = "禁用列表项",
                    note = "enabled=false，不应触发点击回调",
                    enabled = false,
                    onClick = { disabledClickCount += 1 }
                )
                Text(
                    text = "禁用点击次数：$disabledClickCount",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                ListItem(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    enabled = false,
                    onClick = { disabledClickCount += 1 },
                    icon = {},
                    title = { Text("禁用插槽列表项") },
                    note = { Text("插槽重载同样保留 disabled 语义") },
                    end = { Text("不可用") }
                )
            }

            LibraryListItemSection("长文本与窄屏") {
                ListItem(
                    title = "这是一条用于检查窄屏换行表现的长标题列表项",
                    note = "较长的补充文字会自然换行，用于检查多行标题和说明的布局稳定性。",
                    isShowArrow = false
                )
            }
        }
    }
}

@Composable
private fun LibraryListItemSection(
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
