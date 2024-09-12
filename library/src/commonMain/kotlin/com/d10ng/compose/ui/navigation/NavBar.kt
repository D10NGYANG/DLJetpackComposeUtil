package com.d10ng.compose.ui.navigation

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.show.HorizontalDivider
import dljetpackcomposeutil.library.generated.resources.Res
import dljetpackcomposeutil.library.generated.resources.ic_round_back_22
import org.jetbrains.compose.resources.painterResource

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
                    .windowInsetsTopHeight(WindowInsets.systemBars)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 返回按钮
                if (onClickBack != null) {
                    Image(
                        painter = painterResource(resource = Res.drawable.ic_round_back_22),
                        contentDescription = "back",
                        colorFilter = ColorFilter.tint(AppColor.Neutral.tips),
                        contentScale = ContentScale.Inside,
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .size(40.dp)
                            .clip(AppShape.RC.Cycle)
                            .clickable { onClickBack() }
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
                    .padding(horizontal = if (onClickBack != null) 62.dp else 19.dp)
                    .align(Alignment.Center),
                contentAlignment = titleAlignment
            ) {
                Text(
                    text = title,
                    style = AppText.Bold.Title.large,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        // 底部边框
        if (border) HorizontalDivider()
    }
}