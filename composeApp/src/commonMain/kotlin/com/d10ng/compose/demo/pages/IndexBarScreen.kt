package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.d10ng.compose.demo.stores.AreaStore
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.base.CellTitle
import com.d10ng.compose.ui.navigation.IndexBar
import com.d10ng.compose.ui.navigation.NavBar
import kotlinx.coroutines.launch

/**
 * IndexBar 索引栏
 * @Author d10ng
 * @Date 2023/9/18 10:50
 */
@Composable
fun IndexBarScreen(
    onBack: () -> Unit = {},
) {
    var loading by remember { mutableStateOf(!AreaStore.loaded) }
    LaunchedEffect(Unit) {
        AreaStore.load()
        loading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "IndexBar", onClickBack = onBack)
        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val data = remember { AreaStore.list }
            val listState = rememberLazyListState()
            val scope = rememberCoroutineScope()

            IndexBar(
                modifier = Modifier.fillMaxSize(),
                onSelect = { value ->
                    val index = data.indexOfFirst { it.indexLetter.equals(value.toString(), true) }
                    if (index >= 0) {
                        scope.launch { listState.scrollToItem(index) }
                    }
                }
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState
                ) {
                    itemsIndexed(data, { i, _ -> i }) { index, item ->
                        val isShowTag = if (index == 0) true else {
                            val last = data[index - 1]
                            last.indexLetter != item.indexLetter
                        }
                        ItemView(item, isShowTag)
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemView(
    value: AreaStore.Info,
    showTag: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (showTag) CellTitle(title = value.indexLetter)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color.White)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value.zh,
                style = AppText.Normal.Title.default,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = value.code.toString(),
                style = AppText.Normal.Body.small,
                modifier = Modifier
                    .padding(end = 32.dp)
                    .background(AppColor.Neutral.card)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
        Box(modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(1.dp)
            .background(AppColor.Neutral.line))
    }
}
