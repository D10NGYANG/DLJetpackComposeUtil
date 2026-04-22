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
import androidx.compose.ui.tooling.preview.Preview
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
 * 以 LazyColumn 实现可滚动选项列表，固定显示 5 行，中间高亮行为当前选中项
 * 滚动结束后自动吸附到最近选项（Snap 效果），并回调选中变更
 * @param items Set<T> 所有可选项，要求非空（默认选中项取 first()）
 * @param itemText (T) -> String 将选项转换为显示文字的函数，默认调用 toString()
 * @param textStyle TextStyle 选项文字样式，默认 `AppText.Normal.Title.default`
 * @param selectedItem T 当前选中的项，默认为第一项（items 为空时会抛出异常）
 * @param onValueChange (T) -> Unit 选中项变更回调，滚动停止后触发，默认无操作
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
 * 将多个 [Picker] 横向等宽排列，每列独立滚动，共同构成多维度的选择结果
 * @param items List<Set<T>> 每列的可选项列表，列表长度决定列数
 * @param itemText (Int, T) -> String 将选项转换为显示文字的函数，第一个参数为列索引（从 0 起），第二个参数为该列的选项，默认调用 toString()
 * @param textStyle TextStyle 所有列共用的选项文字样式，默认 `AppText.Normal.Title.default`
 * @param selectedItems List<T> 各列当前选中项的列表，长度须与 [items] 一致，默认每列取第一项
 * @param onValueChange (List<T>) -> Unit 任意列选中项变更后的回调，参数为所有列最新选中项组成的完整列表，默认无操作
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

@Preview
@Composable
fun PreviewPicker() {
    val items = linkedSetOf("选项一", "选项二", "选项三", "选项四", "选项五", "选项六", "选项七")
    Box(modifier = Modifier.background(Color.White)) {
        Picker(
            items = items,
            selectedItem = "选项三",
            onValueChange = {}
        )
    }
}

@Preview
@Composable
fun PreviewMultiPicker() {
    val years = (2020..2030).map { "${it}年" }.toCollection(LinkedHashSet())
    val months = (1..12).map { "${it}月" }.toCollection(LinkedHashSet())
    val days = (1..31).map { "${it}日" }.toCollection(LinkedHashSet())
    Box(modifier = Modifier.background(Color.White)) {
        MultiPicker(
            items = listOf(years, months, days),
            selectedItems = listOf("2024年", "4月", "22日"),
            onValueChange = {}
        )
    }
}
