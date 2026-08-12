package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.navigation.NavBar

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MaterialBadgeScreen(onBack: () -> Unit = {}) {
    var unreadCount by remember { mutableIntStateOf(8) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        NavBar(title = "Material Badge", onClickBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            BadgeSection("内容形式") {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    BadgeExample(label = "空点") {
                        Badge(
                            modifier = Modifier.semantics {
                                contentDescription = "有新内容"
                            }
                        )
                    }
                    BadgeExample(label = "数字") {
                        CountBadge(8)
                    }
                    BadgeExample(label = "两位数") {
                        CountBadge(28)
                    }
                    BadgeExample(label = "文本") {
                        Badge { Text("NEW") }
                    }
                    BadgeExample(label = "上限") {
                        CountBadge(120)
                    }
                }
            }

            BadgeSection("图标锚点") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(48.dp)
                ) {
                    BadgedBox(
                        badge = { CountBadge(5) }
                    ) {
                        Icon(
                            Icons.Default.Mail,
                            contentDescription = "邮件",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    BadgedBox(
                        badge = {
                            Badge(
                                modifier = Modifier.semantics {
                                    contentDescription = "有新通知"
                                }
                            )
                        }
                    ) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "通知",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            BadgeSection("动态计数与上限格式化") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconButton(
                        onClick = { unreadCount = (unreadCount - 1).coerceAtLeast(0) }
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "减少未读数")
                    }
                    BadgedBox(
                        badge = {
                            if (unreadCount > 0) {
                                CountBadge(unreadCount)
                            }
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
                    Column {
                        Text("当前：$unreadCount，显示：${formatBadgeCount(unreadCount)}")
                        TextButton(onClick = { unreadCount = 120 }) {
                            Text("设为 120")
                        }
                    }
                }
            }

            BadgeSection("自定义颜色") {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    Badge(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    ) {
                        Text("8")
                    }
                    Badge(
                        containerColor = Color(0xFF006C4C),
                        contentColor = Color.White
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

@Composable
private fun CountBadge(count: Int) {
    Badge(
        modifier = Modifier.semantics {
            contentDescription = "$count 条未读"
        }
    ) {
        Text(formatBadgeCount(count))
    }
}

@Composable
private fun BadgeExample(
    label: String,
    badge: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        badge()
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun BadgeSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        content()
    }
}

private fun formatBadgeCount(count: Int, max: Int = 99): String = when {
    count <= 0 -> "0"
    count > max -> "$max+"
    else -> count.toString()
}
