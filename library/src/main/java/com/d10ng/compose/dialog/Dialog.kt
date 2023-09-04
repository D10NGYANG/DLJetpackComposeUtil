package com.d10ng.compose.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText

@Composable
fun LoadingDialog (
    background: Color = Color(0x97454545)
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .pointerInput(Unit) {
                // 拦截外部的点击
                detectTapGestures { }
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            contentAlignment= Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .background(background, shape = AppShape.RC.v8)
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    }
}

@Composable
fun Dialog(
    onDismiss: () -> Unit = {},
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .pointerInput(Unit) {
                // 拦截外部的点击
                detectTapGestures { onDismiss.invoke() }
            }
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
        content = content,
        contentAlignment = contentAlignment
    )
}

@Composable
fun DialogColumn(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp)
            .wrapContentHeight()
            .background(Color.White, AppShape.RC.v16)
            .padding(25.dp),
        content = content
    )
}

@Composable
fun DialogTitle(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = AppColor.Neutral.title
) {
    Text(
        text = text,
        style = AppText.Bold.Title.v24,
        modifier = modifier,
        color = color
    )
}

@Composable
fun DialogMessage(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = AppColor.Neutral.body
) {
    Text(
        text = text,
        style = AppText.Normal.Body.v14,
        modifier = modifier,
        color = color
    )
}