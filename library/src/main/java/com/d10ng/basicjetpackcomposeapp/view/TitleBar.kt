package com.d10ng.basicjetpackcomposeapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.basicjetpackcomposeapp.R
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.compose.AppShape
import com.d10ng.basicjetpackcomposeapp.compose.AppText

@Preview
@Composable
private fun TitleBar_Test() {
    Column {
        TitleBar("测试", onClickBack = {})
        TitleBar("测试1")
    }
}

@Composable
fun TitleBar(
    value: String,
    background: Color = AppColor.System.primary,
    titleStyle: TextStyle = AppText.Bold.Title.v18,
    backButtonTint: Color = AppColor.Text.hint,
    onClickBack: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit = {}
) {
    val isHasBack = onClickBack != null
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(background)
    ) {
        // 状态栏
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsTopHeight(WindowInsets.statusBars)
        )
        // 标题栏
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
        ) {
            if (isHasBack) {
                Image(
                    painter = painterResource(id = R.drawable.ic_toolbar_back_20),
                    contentDescription = "返回",
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 4.dp)
                        .size(46.dp)
                        .clip(AppShape.RC.Cycle)
                        .clickable { onClickBack?.invoke() },
                    contentScale = ContentScale.Inside,
                    colorFilter = ColorFilter.tint(backButtonTint)
                )
            }
            Text(
                text = value,
                modifier = Modifier
                    .align(if (isHasBack) Alignment.CenterStart else Alignment.Center)
                    .padding(start = if (isHasBack) 65.dp else 0.dp),
                style = titleStyle
            )
            content()
        }
    }
}