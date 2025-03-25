package com.d10ng.compose.ui.navigation

import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import kotlin.math.roundToInt

/**
 * IndexBar 索引栏
 * @Author d10ng
 * @Date 2023/9/18 10:38
 */

@OptIn(ExperimentalComposeUiApi::class)
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
    var barSize by remember {
        mutableIntStateOf(0)
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
                .onSizeChanged {
                    barSize = it.height
                }
                .pointerInteropFilter {
                    when (it.action) {
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                            isTouch = false
                            selectChar = null
                        }

                        else -> {
                            isTouch = true
                            if (barSize != 0) {
                                val pos = it.y / barSize * chars.size
                                val c = chars[pos
                                    .roundToInt()
                                    .coerceAtLeast(0)
                                    .coerceAtMost(chars.size - 1)]
                                if (c != selectChar) {
                                    selectChar = c
                                    onSelect.invoke(c)
                                }
                            }
                        }
                    }
                    true
                },
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