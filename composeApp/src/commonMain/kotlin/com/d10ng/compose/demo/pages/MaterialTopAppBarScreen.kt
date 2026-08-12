package com.d10ng.compose.demo.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

private enum class TitlePosition(val label: String) {
    START("起始"),
    CENTER("居中")
}

private enum class AppBarBehavior(val label: String) {
    PINNED("固定"),
    ENTER_ALWAYS("快速"),
    EXIT_UNTIL_COLLAPSED("折叠")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialTopAppBarScreen(onBack: () -> Unit = {}) {
    var titlePosition by remember { mutableStateOf(TitlePosition.START) }
    var behaviorType by remember { mutableStateOf(AppBarBehavior.PINNED) }
    var longTitle by remember { mutableStateOf(false) }
    var actionCount by remember { mutableIntStateOf(0) }
    val scrollBehavior = when (behaviorType) {
        AppBarBehavior.PINNED -> TopAppBarDefaults.pinnedScrollBehavior()
        AppBarBehavior.ENTER_ALWAYS -> TopAppBarDefaults.enterAlwaysScrollBehavior()
        AppBarBehavior.EXIT_UNTIL_COLLAPSED -> TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            OfficialTopAppBar(
                title = if (longTitle) {
                    "这是一个用于验证官方测量、省略号与操作槽避让的长标题"
                } else {
                    "Material TopAppBar"
                },
                titlePosition = titlePosition,
                scrollBehavior = scrollBehavior,
                onBack = onBack,
                onAction = { actionCount++ }
            )
        }
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("标题位置", style = MaterialTheme.typography.titleMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TitlePosition.entries.forEach { position ->
                        FilterChip(
                            selected = titlePosition == position,
                            onClick = { titlePosition = position },
                            label = { Text(position.label) }
                        )
                    }
                }
            }
            item {
                Text("滚动行为", style = MaterialTheme.typography.titleMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AppBarBehavior.entries.forEach { behavior ->
                        FilterChip(
                            selected = behaviorType == behavior,
                            onClick = { behaviorType = behavior },
                            label = { Text(behavior.label) }
                        )
                    }
                }
            }
            item {
                FilterChip(
                    selected = longTitle,
                    onClick = { longTitle = !longTitle },
                    label = { Text("长标题省略") }
                )
            }
            item {
                Text(
                    text = when (behaviorType) {
                        AppBarBehavior.PINNED -> "固定模式保持栏位可见，滚动后切换容器色。"
                        AppBarBehavior.ENTER_ALWAYS -> "快速模式上滚隐藏，下滚立即重新出现。"
                        AppBarBehavior.EXIT_UNTIL_COLLAPSED -> "折叠模式上滚收起，内容回到顶部后展开。"
                    },
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            item {
                Text(
                    text = "更多操作已点击 $actionCount 次",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            items((1..24).toList()) { index ->
                ListItem(
                    headlineContent = { Text("滚动内容 $index") },
                    supportingContent = { Text("上下滚动以观察当前 TopAppBar 行为") }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OfficialTopAppBar(
    title: String,
    titlePosition: TitlePosition,
    scrollBehavior: TopAppBarScrollBehavior,
    onBack: () -> Unit,
    onAction: () -> Unit
) {
    val titleContent = @Composable {
        Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
    val navigationIcon = @Composable {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
        }
    }
    val actions = @Composable {
        IconButton(onClick = onAction) {
            Icon(Icons.Default.MoreVert, contentDescription = "更多操作")
        }
    }

    when (titlePosition) {
        TitlePosition.START -> TopAppBar(
            title = titleContent,
            navigationIcon = navigationIcon,
            actions = { actions() },
            scrollBehavior = scrollBehavior
        )
        TitlePosition.CENTER -> CenterAlignedTopAppBar(
            title = titleContent,
            navigationIcon = navigationIcon,
            actions = { actions() },
            scrollBehavior = scrollBehavior
        )
    }
}
