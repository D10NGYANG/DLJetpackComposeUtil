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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.d10ng.compose.resources.Res
import com.d10ng.compose.resources.ic_round_back_22
import com.d10ng.compose.resources.nav_bar_back
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.defaultPaddingSize
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * 导航栏
 *
 * 顶部导航栏组件，支持标题居中/自定义对齐、可选返回按钮、右侧自定义内容、
 * 系统状态栏占位及底部分割线等常用配置。
 *
 * 常用的 [Alignment.Center] 和 [Alignment.CenterStart] 分别委托 Material 3
 * [CenterAlignedTopAppBar] 和 [TopAppBar]；其他对齐方式继续使用兼容布局。
 *
 * @param title String 导航栏标题文本
 * @param titleAlignment Alignment 标题在导航栏内容区的对齐方式，默认为 [Alignment.Center]
 * @param background Color 导航栏背景色，默认为 `MaterialTheme.colorScheme.surfaceContainerLowest`
 * @param withStatusBar Boolean 是否在导航栏顶部预留系统状态栏高度，默认为 true
 * @param border Boolean 是否在导航栏底部显示分割线，默认为 false
 * @param onClickBack (() -> Unit)? 点击返回按钮的回调；为 null 时不显示返回按钮，默认为 null
 * @param right (@Composable () -> Unit)? 导航栏右侧自定义内容；为 null 时不显示，默认为 null
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar(
    title: String,
    titleAlignment: Alignment = Alignment.Center,
    background: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
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
        if (titleAlignment == Alignment.Center || titleAlignment == Alignment.CenterStart) {
            MaterialNavBarContent(
                title = title,
                centered = titleAlignment == Alignment.Center,
                background = background,
                withStatusBar = withStatusBar,
                onClickBack = onClickBack,
                right = right
            )
        } else {
            LegacyNavBarContent(
                title = title,
                titleAlignment = titleAlignment,
                withStatusBar = withStatusBar,
                onClickBack = onClickBack,
                right = right
            )
        }
        if (border) HorizontalDivider()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MaterialNavBarContent(
    title: String,
    centered: Boolean,
    background: Color,
    withStatusBar: Boolean,
    onClickBack: (() -> Unit)?,
    right: @Composable (() -> Unit)?
) {
    val titleContent = @Composable {
        Text(
            text = title,
            style = AppText.Bold.Title.large,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
    val navigationIcon = @Composable { NavBarBackButton(onClickBack) }
    val colors = TopAppBarDefaults.topAppBarColors(
        containerColor = background,
        scrolledContainerColor = background
    )
    val windowInsets = if (withStatusBar) TopAppBarDefaults.windowInsets else WindowInsets(0)

    if (centered) {
        CenterAlignedTopAppBar(
            title = titleContent,
            navigationIcon = navigationIcon,
            actions = { right?.invoke() },
            expandedHeight = 60.dp,
            windowInsets = windowInsets,
            colors = colors
        )
    } else {
        TopAppBar(
            title = titleContent,
            navigationIcon = navigationIcon,
            actions = { right?.invoke() },
            expandedHeight = 60.dp,
            windowInsets = windowInsets,
            colors = colors
        )
    }
}

@Composable
private fun LegacyNavBarContent(
    title: String,
    titleAlignment: Alignment,
    withStatusBar: Boolean,
    onClickBack: (() -> Unit)?,
    right: @Composable (() -> Unit)?
) {
    val layoutDirection = LocalLayoutDirection.current
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
            if (onClickBack != null) {
                Image(
                    painter = painterResource(resource = Res.drawable.ic_round_back_22),
                    contentDescription = stringResource(Res.string.nav_bar_back),
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .size(40.dp)
                        .clip(RoundedCornerShape(100))
                        .clickable(onClick = onClickBack)
                        .graphicsLayer {
                            scaleX = if (layoutDirection == LayoutDirection.Rtl) -1f else 1f
                        }
                )
            }
            Box(modifier = Modifier.weight(1f))
            right?.invoke()
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (onClickBack != null) 59.dp else defaultPaddingSize)
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
}

@Composable
private fun NavBarBackButton(onClickBack: (() -> Unit)?) {
    if (onClickBack == null) return
    val layoutDirection = LocalLayoutDirection.current
    IconButton(onClick = onClickBack) {
        Image(
            painter = painterResource(resource = Res.drawable.ic_round_back_22),
            contentDescription = stringResource(Res.string.nav_bar_back),
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .size(22.dp)
                .graphicsLayer {
                    scaleX = if (layoutDirection == LayoutDirection.Rtl) -1f else 1f
                }
        )
    }
}

@Preview
@Composable
fun PreviewNavBar() {
    Column {
        // 仅标题
        NavBar(title = "页面标题", withStatusBar = false)
        // 带返回按钮
        NavBar(title = "页面标题", withStatusBar = false, onClickBack = {})
        // 带返回按钮 + 底部边框
        NavBar(title = "页面标题", withStatusBar = false, border = true, onClickBack = {})
        // 带返回按钮 + 右侧自定义内容
        NavBar(
            title = "页面标题",
            withStatusBar = false,
            onClickBack = {},
            right = {
                Text(
                    text = "完成",
                    modifier = Modifier.padding(end = 16.dp),
                    style = AppText.Normal.Primary.default
                )
            }
        )
        // 标题左对齐（有返回按钮时）
        NavBar(
            title = "页面标题",
            titleAlignment = Alignment.CenterStart,
            withStatusBar = false,
            onClickBack = {}
        )
    }
}
