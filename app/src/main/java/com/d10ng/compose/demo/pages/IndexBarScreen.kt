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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.demo.stores.AreaStore
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.PageTransitions
import com.d10ng.compose.ui.base.CellTitle
import com.d10ng.compose.ui.navigation.IndexBar
import com.d10ng.compose.ui.navigation.NavBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

/**
 * IndexBar 索引栏
 * @Author d10ng
 * @Date 2023/9/18 10:50
 */
@RootNavGraph
@Destination(style = PageTransitions::class)
@Composable
fun IndexBarScreen(
    nav: DestinationsNavigator
) {
    IndexBarScreenView(onClickBack = nav::navigateUp)
}

@Composable
private fun IndexBarScreenView(
    onClickBack: () -> Unit = {},
) {
    val data = remember {
        AreaStore.list
    }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "IndexBar", onClickBack = onClickBack)
        IndexBar(
            modifier = Modifier.fillMaxSize(),
            onSelect = { value ->
                val index = data.indexOfFirst { it.py[0].uppercase().contains(value, true) }
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
                        last.py[0].uppercase() != item.py[0].uppercase()
                    }
                    ItemView(item, isShowTag)
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
        if (showTag) CellTitle(title = value.py[0].uppercase())
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

@Preview
@Composable
private fun IndexBarScreenPreview() {
    IndexBarScreenView()
}