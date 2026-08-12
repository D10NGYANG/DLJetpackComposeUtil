package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.base.Button
import com.d10ng.compose.ui.base.ButtonSize
import com.d10ng.compose.ui.base.ButtonType
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.navigation.NavBar

/** Material 3 迁移后的 NavBar 特性与兼容边界预览。 */
@Composable
fun NavBarScreen(onBack: () -> Unit = {}) {
    var backCount by remember { mutableIntStateOf(0) }
    var actionCount by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "NavBar", onClickBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CellGroup(title = "基础与标题对齐", border = false) {
                NavBar(title = "标题居中", withStatusBar = false)
                SectionDivider()
                NavBar(
                    title = "标题从起始侧对齐",
                    titleAlignment = Alignment.CenterStart,
                    withStatusBar = false,
                    border = true
                )
            }
            CellGroup(title = "返回与操作槽", border = false) {
                NavBar(
                    title = "返回 $backCount 次",
                    withStatusBar = false,
                    onClickBack = { backCount++ }
                )
                SectionDivider()
                NavBar(
                    title = "不对称操作",
                    withStatusBar = false,
                    onClickBack = { backCount++ },
                    right = {
                        Button(
                            text = "保存 $actionCount",
                            modifier = Modifier.padding(end = 8.dp),
                            type = ButtonType.PRIMARY,
                            size = ButtonSize.SMALL,
                            onClick = { actionCount++ }
                        )
                    }
                )
                Text(
                    text = "返回 $backCount 次，右侧操作 $actionCount 次",
                    modifier = Modifier.padding(16.dp),
                    style = AppText.Normal.Tips.default
                )
            }
            CellGroup(title = "长标题与省略", border = false) {
                NavBar(
                    title = "这是一个用于检查窄屏、省略号和两侧槽位避让的很长页面标题",
                    withStatusBar = false,
                    onClickBack = { backCount++ },
                    right = {
                        IconButton(onClick = { actionCount++ }) {
                            Icon(Icons.Default.Add, contentDescription = "新增")
                        }
                    }
                )
                SectionDivider()
                NavBar(
                    title = "左对齐的长标题同样会在操作按钮前安全省略显示",
                    titleAlignment = Alignment.CenterStart,
                    withStatusBar = false,
                    onClickBack = { backCount++ }
                )
            }
            CellGroup(title = "颜色、状态栏与边框", border = false) {
                NavBar(
                    title = "自定义背景与底边",
                    withStatusBar = false,
                    background = Color(0xFFE8F5E9),
                    border = true,
                    onClickBack = { backCount++ }
                )
                SectionDivider()
                Text(
                    text = "下方保留系统状态栏 Insets，灰色区域可直接对照高度",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = AppText.Normal.Tips.default
                )
                NavBar(
                    title = "withStatusBar = true",
                    withStatusBar = true,
                    background = Color(0xFFF5F5F5)
                )
            }
            CellGroup(title = "RTL 逻辑方向", border = false) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    NavBar(
                        title = "RTL 居中标题",
                        withStatusBar = false,
                        onClickBack = { backCount++ },
                        right = {
                            IconButton(onClick = { actionCount++ }) {
                                Icon(Icons.Default.Add, contentDescription = "新增")
                            }
                        }
                    )
                    SectionDivider()
                    NavBar(
                        title = "RTL 起始对齐",
                        titleAlignment = Alignment.CenterStart,
                        withStatusBar = false,
                        onClickBack = { backCount++ }
                    )
                }
            }
            CellGroup(title = "兼容对齐兜底", border = false) {
                NavBar(
                    title = "Alignment.CenterEnd",
                    titleAlignment = Alignment.CenterEnd,
                    withStatusBar = false,
                    border = true
                )
                Text(
                    text = "非 Center / CenterStart 继续使用旧布局，避免公开 Alignment 参数失效。",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = AppText.Normal.Tips.default
                )
            }
        }
    }
}

@Composable
private fun SectionDivider() {
    HorizontalDivider(thickness = 8.dp, color = AppColor.Neutral.bg)
}
