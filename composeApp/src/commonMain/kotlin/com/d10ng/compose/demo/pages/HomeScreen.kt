package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.d10ng.compose.demo.ComponentCatalogRoute
import com.d10ng.compose.demo.ComponentSource
import com.d10ng.compose.demo.resources.Res
import com.d10ng.compose.demo.resources.ic_launcher_foreground
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.base.Cell
import com.d10ng.compose.ui.base.CellGroup
import org.jetbrains.compose.resources.painterResource

/**
 * 首页
 * @Author d10ng
 * @Date 2024/9/12 13:56
 */

@Composable
fun HomeScreen(onNavigate: (NavKey) -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        // 标题
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(19.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(resource = Res.drawable.ic_launcher_foreground),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .height(64.dp)
                    .background(Color.White, AppShape.RC.v24)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 19.dp)
            ) {
                Text(text = "DLJetpackComposeUtil", style = AppText.Bold.Title.large)
                Text(
                    text = "Jetpack Compose UI 组件库（仿Vant）",
                    style = AppText.Normal.Body.small,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        CellGroup(title = "组件目录", inset = true) {
            Cell(
                title = "自有 UI 组件",
                value = "按类别查看",
                link = true,
                onClick = {
                    onNavigate(ComponentCatalogRoute(ComponentSource.LIBRARY))
                }
            )
            Cell(
                title = "Material 3 官方组件",
                value = "按类别查看",
                link = true,
                border = false,
                onClick = {
                    onNavigate(ComponentCatalogRoute(ComponentSource.MATERIAL3))
                }
            )
        }
        Box(modifier = Modifier.height(32.dp))
    }
}
