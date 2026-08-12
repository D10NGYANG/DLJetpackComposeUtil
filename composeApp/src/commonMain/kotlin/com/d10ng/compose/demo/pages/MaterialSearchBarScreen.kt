package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.navigation.NavBar
import kotlinx.coroutines.delay

private val searchEntries = listOf(
    "Checkbox 方形与圆形",
    "ListMenuItem 下拉菜单",
    "Material SearchBar",
    "Badge 数值适配",
    "Picker 滚轮选择"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialSearchBarScreen(onBack: () -> Unit = {}) {
    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var submitCount by remember { mutableIntStateOf(0) }
    var dockedQuery by remember { mutableStateOf("") }
    var dockedExpanded by remember { mutableStateOf(false) }

    val results = remember(query) {
        if (query.isBlank()) searchEntries
        else searchEntries.filter { it.contains(query, ignoreCase = true) }
    }
    LaunchedEffect(query) {
        if (query.isBlank()) {
            loading = false
        } else {
            loading = true
            delay(350)
            loading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        NavBar(title = "Material SearchBar", onClickBack = onBack)
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "全屏展开搜索",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Box(modifier = Modifier.height(64.dp))

                Text(
                    text = "Docked 建议列表",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                DockedSearchBar(
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = dockedQuery,
                            onQueryChange = {
                                dockedQuery = it
                                dockedExpanded = true
                            },
                            onSearch = { dockedExpanded = false },
                            expanded = dockedExpanded,
                            onExpandedChange = { dockedExpanded = it },
                            placeholder = { Text("输入查看建议") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            trailingIcon = {
                                if (dockedQuery.isNotEmpty()) {
                                    IconButton(onClick = { dockedQuery = "" }) {
                                        Icon(Icons.Default.Clear, contentDescription = "清除 Docked 查询")
                                    }
                                }
                            }
                        )
                    },
                    expanded = dockedExpanded,
                    onExpandedChange = { dockedExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    searchEntries.take(3).forEach { entry ->
                        ListItem(
                            headlineContent = { Text(entry) },
                            leadingContent = { Icon(Icons.Default.History, contentDescription = null) },
                            modifier = Modifier.clickable {
                                dockedQuery = entry
                                dockedExpanded = false
                            }
                        )
                    }
                }
            }

            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = query,
                        onQueryChange = { query = it },
                        onSearch = {
                            submitCount += 1
                            expanded = true
                        },
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        placeholder = { Text("搜索组件") },
                        leadingIcon = {
                            if (expanded) {
                                IconButton(onClick = { expanded = false }) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "收起搜索")
                                }
                            } else {
                                Icon(Icons.Default.Search, contentDescription = null)
                            }
                        },
                        trailingIcon = {
                            when {
                                loading -> CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                query.isNotEmpty() -> IconButton(onClick = { query = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "清除查询")
                                }
                            }
                        }
                    )
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                modifier = if (expanded) {
                    Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                } else {
                    Modifier
                        .align(Alignment.TopCenter)
                        .padding(horizontal = 16.dp, vertical = 48.dp)
                        .fillMaxWidth()
                }
            ) {
                SearchResults(
                    query = query,
                    results = results,
                    submitCount = submitCount,
                    onSelect = {
                        query = it
                        expanded = false
                    }
                )
            }

        }
    }
}

@Composable
private fun SearchResults(
    query: String,
    results: List<String>,
    submitCount: Int,
    onSelect: (String) -> Unit
) {
    Text(
        text = if (submitCount == 0) "输入关键词或选择历史项" else "已提交 $submitCount 次",
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.bodySmall
    )
    HorizontalDivider()
    if (results.isEmpty()) {
        Text(
            text = "没有找到与「$query」匹配的组件",
            modifier = Modifier.padding(24.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    } else {
        results.forEach { entry ->
            ListItem(
                headlineContent = { Text(entry) },
                leadingContent = { Icon(Icons.Default.History, contentDescription = null) },
                modifier = Modifier.clickable { onSelect(entry) }
            )
        }
    }
}
