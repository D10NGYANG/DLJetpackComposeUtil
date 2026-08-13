package com.d10ng.compose.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText

/**
 * 索引栏
 *
 * 右侧字母索引导航组件，常用于联系人列表、城市选择等需要快速定位的长列表场景。
 * 用户通过按下并滑动右侧字母栏可快速跳转到对应分组，触摸时屏幕中央会显示当前选中的字母提示。
 *
 * @param modifier Modifier 应用于最外层 [Box] 容器的修饰符
 * @param chars List<Char> 索引字符列表，默认为 A-Z 加 # 共 27 个字符
 * @param onSelect (Char) -> Unit 用户滑动或点击选中某个索引字符时的回调
 * @param touchColor Color 触摸索引栏时的背景高亮色，默认为 [AppColor.Neutral.card]
 * @param charText @Composable (String) -> Unit 每个索引字符的自定义渲染插槽，默认使用 [DefaultIndexBarCharText]
 * @param selectText @Composable BoxScope.(String) -> Unit 选中字符时屏幕中央的提示文本渲染插槽，默认使用 [DefaultIndexBarSelectText]
 * @param content @Composable BoxScope.() -> Unit 主体内容区域，通常放置可滚动的列表
 */
@Composable
fun IndexBar(
    modifier: Modifier = Modifier,
    chars: List<Char> = "ABCDEFGHIJKLMNOPQRSTUVWXYZ#".toCharArray().asList(),
    onSelect: (Char) -> Unit = {},
    touchColor: Color = AppColor.Neutral.card,
    charText: @Composable (String) -> Unit = {
        DefaultIndexBarCharText(text = it)
    },
    selectText: @Composable BoxScope.(String) -> Unit = {
        DefaultIndexBarSelectText(text = it)
    },
    content: @Composable BoxScope.() -> Unit
) {
    var isTouch by remember {
        mutableStateOf(false)
    }
    var selectChar by remember {
        mutableStateOf<Char?>(null)
    }
    var keyboardIndex by remember(chars) { mutableStateOf(-1) }
    val onSelectState = rememberUpdatedState(onSelect)
    val onTouchState = rememberUpdatedState { touched: Boolean, char: Char? ->
        isTouch = touched
        selectChar = char
    }
    Box(
        modifier = modifier
    ) {
        content()

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(if (isTouch) touchColor else Color.Transparent)
                .padding(vertical = 16.dp)
                .align(Alignment.CenterEnd)
            .semantics {
                contentDescription = "索引栏"
                stateDescription = selectChar?.let { "当前索引 $it" }
                    ?: if (chars.isEmpty()) "无可用索引" else "${chars.size} 个索引"
                if (chars.isEmpty()) disabled()
            }
            .focusable(enabled = chars.isNotEmpty())
            .onKeyEvent { event ->
                if (chars.isEmpty() || event.type != KeyEventType.KeyDown) return@onKeyEvent false
                val nextIndex = when (event.key) {
                    Key.DirectionDown, Key.DirectionRight -> (keyboardIndex + 1).coerceAtMost(chars.lastIndex)
                    Key.DirectionUp, Key.DirectionLeft -> (if (keyboardIndex < 0) chars.lastIndex else keyboardIndex - 1).coerceAtLeast(0)
                    Key.Enter, Key.NumPadEnter, Key.Spacebar -> if (keyboardIndex < 0) 0 else keyboardIndex
                    else -> return@onKeyEvent false
                }
                keyboardIndex = nextIndex
                selectChar = chars[nextIndex]
                onSelectState.value(chars[nextIndex])
                true
            }
            .indexSelectionGesture(
                    chars = chars,
                    onSelect = { onSelectState.value(it) },
                    onTouchStateChange = { touched, char ->
                        onTouchState.value(touched, char)
                    }
                ),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            chars.forEach { char ->
                charText(char.toString())
            }
        }

        if (selectChar != null) {
            selectText(selectChar.toString())
        }
    }
}

private fun Modifier.indexSelectionGesture(
    chars: List<Char>,
    onSelect: (Char) -> Unit,
    onTouchStateChange: (Boolean, Char?) -> Unit
): Modifier = if (chars.isEmpty()) this else pointerInput(chars) {
    awaitEachGesture {
        var selectedChar: Char? = null
        try {
            val down = awaitFirstDown(requireUnconsumed = false)

            fun selectAt(y: Float) {
                val barHeight = size.height
                if (barHeight <= 0) return
                val index = (y.coerceIn(0f, barHeight.toFloat()) / barHeight * chars.size)
                    .toInt()
                    .coerceAtMost(chars.lastIndex)
                val char = chars[index]
                if (char != selectedChar) {
                    selectedChar = char
                    onTouchStateChange(true, char)
                    onSelect(char)
                }
            }

            selectAt(down.position.y)
            down.consume()
            do {
                val event = awaitPointerEvent()
                val change = event.changes.firstOrNull { it.id == down.id } ?: break
                if (change.pressed) {
                    selectAt(change.position.y)
                    change.consume()
                }
            } while (change.pressed)
        } finally {
            onTouchStateChange(false, null)
        }
    }
}

/**
 * 索引栏默认字符文本
 *
 * 用于 [IndexBar] 右侧每个索引字符的默认渲染样式，文本居中显示在固定宽度区域内。
 *
 * @param text String 要显示的单个字符文本
 */
@Composable
fun DefaultIndexBarCharText(
    text: String
) {
    Text(
        text = text,
        style = AppText.Normal.Title.small,
        modifier = Modifier
            .width(36.dp),
        textAlign = TextAlign.Center
    )
}

/**
 * 索引栏默认选中提示文本
 *
 * 用于 [IndexBar] 触摸选中字符时，在屏幕中央显示的半透明黑底白字提示框。
 *
 * @param text String 当前选中的字符文本
 */
@Composable
fun BoxScope.DefaultIndexBarSelectText(
    text: String
) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .align(Alignment.Center)
            .background(Color.Black.copy(alpha = 0.4f), AppShape.RC.v8),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = AppText.Bold.Surface.huge)
    }
}

@Preview
@Composable
fun PreviewIndexBar() {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ#".toCharArray().asList()
    var selected by remember { mutableStateOf<Char?>(null) }

    IndexBar(
        modifier = Modifier.fillMaxSize(),
        chars = chars,
        onSelect = { selected = it }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(chars) { char ->
                Text(
                    text = "项目 $char",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    style = AppText.Normal.Title.default
                )
            }
        }
    }
}
