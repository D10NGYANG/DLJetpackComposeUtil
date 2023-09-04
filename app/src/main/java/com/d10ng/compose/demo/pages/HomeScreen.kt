package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.demo.PageTransitions
import com.d10ng.compose.demo.pages.destinations.ButtonScreenDestination
import com.d10ng.compose.demo.pages.destinations.CellScreenDestination
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.base.Cell
import com.d10ng.compose.ui.base.CellGroup
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/**
 * 首页
 * @Author d10ng
 * @Date 2023/9/4 13:54
 */
@RootNavGraph(start = true)
@Destination(style = PageTransitions::class)
@Composable
fun HomeScreen(
    nav: DestinationsNavigator
) {
    HomeScreenView(
        onClickButton = {
            nav.navigate(ButtonScreenDestination)
        },
        onClickCell = {
            nav.navigate(CellScreenDestination)
        }
    )
}

@Composable
private fun HomeScreenView(
    onClickButton: () -> Unit = {},
    onClickCell: () -> Unit = {},
    onClickToast: () -> Unit = {},
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
            .statusBarsPadding()
    ) {
        // 标题
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "DLJetpackComposeUtil", style = AppText.Bold.Title.v24)
                Text(
                    text = "Jetpack Compose UI 组件库",
                    style = AppText.Normal.Body.v14,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
        item {
            CellGroup(title = "基础组件", inset = true) {
                Cell(title = "Button 按钮", isLink = true, onClick = onClickButton)
                Cell(title = "Cell 单元格", isLink = true, onClick = onClickCell)
                Cell(title = "Toast 轻提示", isLink = true, onClick = onClickToast)
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenViewPreview() {
    HomeScreenView()
}