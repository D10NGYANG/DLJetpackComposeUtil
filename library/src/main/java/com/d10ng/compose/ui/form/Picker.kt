package com.d10ng.compose.ui.form

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.show.HorizontalDivider
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * 选择器
 * @Author d10ng
 * @Date 2023/9/11 13:43
 */

/**
 * 单列滚轮选择器
 * @param items Set<T> 选项
 * @param itemText Function1<T, String> 选项文本
 * @param textStyle TextStyle 文本样式
 * @param selectedItem T 选中的项
 * @param onValueChange Function1<T, Unit> 选项切换事件
 */
@Composable
fun <T> Picker(
    items: Set<T>,
    itemText: (T) -> String = { it.toString() },
    textStyle: TextStyle = AppText.Normal.Title.default,
    selectedItem: T = items.first(),
    onValueChange: (T) -> Unit = {}
) {
    // 可见的选项数量
    val visibleItemCount = 5
    // 选项高度
    val itemHeight = 48.dp
    // 单个选项高度的一半
    val itemHalfHeightToPx = with(LocalDensity.current) { itemHeight.toPx() / 2 }
    // 列表滚动状态
    val state = rememberLazyListState()
    // 协程
    val scope = rememberCoroutineScope()
    // 改变事件
    val onValueChangeEvent by rememberUpdatedState(newValue = onValueChange)

    // 滚动粘性效果
    LaunchedEffect(state.isScrollInProgress, itemHalfHeightToPx) {
        if (!state.isScrollInProgress && state.firstVisibleItemScrollOffset != 0) {
            if (state.firstVisibleItemScrollOffset < itemHalfHeightToPx) {
                state.animateScrollToItem(state.firstVisibleItemIndex)
            } else if (state.firstVisibleItemScrollOffset > itemHalfHeightToPx) {
                state.animateScrollToItem(state.firstVisibleItemIndex + 1)
            }
        }
    }

    // 滚动完成事件收集
    LaunchedEffect(state, items, selectedItem) {
        snapshotFlow { state.isScrollInProgress }
            .filter { !it && state.firstVisibleItemScrollOffset == 0 }
            .drop(1)
            .map { items.elementAt(state.firstVisibleItemIndex) }
            .distinctUntilChanged()
            .filter { it != selectedItem }
            .collect { onValueChangeEvent(it) }
    }

    // 滚动到选中项
    LaunchedEffect(state, items, selectedItem) {
        val index = items.indexOf(selectedItem)
        if (index >= 0) {
            state.scrollToItem(index)
        }
    }

    // UI
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(itemHeight * visibleItemCount),
    ) {
        // 选项列表
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = state,
            contentPadding = PaddingValues(vertical = itemHeight * (visibleItemCount / 2))
        ) {
            items(count = items.size, key = { it }) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight)
                        .clickable {
                            scope.launch { state.animateScrollToItem(index) }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = itemText(items.elementAt(index)),
                        style = textStyle
                    )
                }
            }
        }
        // 前景
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // 选中项上面的渐变透明遮罩
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White,
                                Color.White.copy(alpha = 0.2f)
                            )
                        )
                    )
            )
            // 分割线
            HorizontalDivider()
            // 中间显露选择项的区域
            Box(
                modifier = Modifier
                    .height(itemHeight)
                    .fillMaxWidth()
            )
            // 分割线
            HorizontalDivider()
            // 选中项下面的渐变透明遮罩
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.2f),
                                Color.White
                            )
                        )
                    )
            )
        }
    }
}

/**
 * 多列滚轮选择器
 * @param items List<Set<T>> 选项
 * @param itemText Function2<Int, T, String> 选项文本，第一个参数为列索引，第二个参数为选项
 * @param textStyle TextStyle 文本样式
 * @param selectedItems List<T> 选中的项
 * @param onValueChange Function1<List<T>, Unit> 选项切换事件
 */
@Composable
fun <T> MultiPicker(
    items: List<Set<T>>,
    itemText: (Int, T) -> String = { _, item -> item.toString() },
    textStyle: TextStyle = AppText.Normal.Title.default,
    selectedItems: List<T> = items.map { it.first() },
    onValueChange: (List<T>) -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        items.forEachIndexed { index, set ->
            Box(modifier = Modifier.weight(1f)) {
                Picker(
                    items = set,
                    itemText = { itemText(index, it) },
                    textStyle = textStyle,
                    selectedItem = selectedItems[index] ?: set.first()
                ) {
                    val newSelectedItems = selectedItems.toMutableList()
                    newSelectedItems[index] = it
                    onValueChange(newSelectedItems)
                }
            }
        }
    }
}