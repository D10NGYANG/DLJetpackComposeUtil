package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.PageTransitions
import com.d10ng.compose.ui.base.Cell
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.compose.ui.show.Tag
import com.d10ng.compose.ui.show.TagStyle
import com.d10ng.compose.ui.show.TagType
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/**
 * 标签
 * @Author d10ng
 * @Date 2023/9/7 10:52
 */
@RootNavGraph
@Destination(style = PageTransitions::class)
@Composable
fun TagScreen(
    nav: DestinationsNavigator
) {
    TagScreenView(onClickBack = nav::navigateUp)
}

@Composable
private fun TagScreenView(
    onClickBack: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "Tag", onClickBack = onClickBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CellGroup(title = "基础用法", inset = true) {
                Cell(title = "primary 类型") {
                    Tag(text = "标签")
                }
                Cell(title = "success 类型") {
                    Tag(text = "标签", type = TagType.Success)
                }
                Cell(title = "warning 类型") {
                    Tag(text = "标签", type = TagType.Warning)
                }
                Cell(title = "danger 类型", border = false) {
                    Tag(text = "标签", type = TagType.Danger)
                }
            }
            CellGroup(title = "样式风格", inset = true) {
                Cell(title = "填充样式") {
                    Tag(text = "标签", style = TagStyle.Fill)
                }
                Cell(title = "圆角样式") {
                    Tag(text = "标签", style = TagStyle.Round)
                }
                Cell(title = "标记样式") {
                    Tag(text = "标签", style = TagStyle.Mark)
                }
                Cell(title = "边框样式", border = false) {
                    Tag(text = "标签", style = TagStyle.Outline)
                }
            }
            CellGroup(title = "自定义颜色", inset = true) {
                Cell(title = "背景颜色") {
                    Tag(text = "标签", color = Color.Gray)
                }
                Cell(title = "文字颜色") {
                    Tag(text = "标签", color = Color(0xFFffe1e1), contentColor = Color(0xFFc14036))
                }
                Cell(title = "空心颜色", border = false) {
                    Tag(text = "标签", color = Color.Magenta, style = TagStyle.Outline)
                }
            }
        }
    }
}

@Preview
@Composable
private fun TagScreenPreview() {
    TagScreenView()
}