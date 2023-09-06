package com.d10ng.compose.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.d10ng.compose.R
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.base.Border

/**
 * 导航栏
 * @Author d10ng
 * @Date 2023/9/4 14:24
 */

/**
 * 导航栏
 * @param title String 标题
 * @param background Color 背景色
 * @param withStatusBar Boolean 是否包含系统状态栏
 * @param border Boolean 是否包含底部边框
 * @param onClickBack Function0<Unit>? 点击返回
 * @param right [@androidx.compose.runtime.Composable] Function0<Unit>? 右侧自定义
 */
@Composable
fun NavBar(
    title: String,
    titleAlignment: Alignment = Alignment.Center,
    background: Color = Color.White,
    withStatusBar: Boolean = true,
    border: Boolean = false,
    onClickBack: (() -> Unit)? = null,
    right: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(background)
    ) {
        // 系统状态栏
        if (withStatusBar) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsTopHeight(WindowInsets.statusBars)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 返回按钮
                if (onClickBack != null) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_round_arrow_back_ios_new_24),
                        contentDescription = "back",
                        tint = AppColor.Neutral.tips,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(46.dp)
                            .clip(AppShape.RC.Cycle)
                            .clickable { onClickBack() }
                            .padding(10.dp)
                    )
                }
                // 间隔
                Box(modifier = Modifier.weight(1f))
                // 自定义右侧
                right?.invoke()
            }
            // 标题
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 62.dp)
                    .align(Alignment.Center),
                contentAlignment = titleAlignment
            ) {
                Text(
                    text = title,
                    style = AppText.Bold.Title.large
                )
            }
        }
        // 底部边框
        if (border) Border()
    }
}