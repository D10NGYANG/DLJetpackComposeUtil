package com.d10ng.compose.demo.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.PageTransitions
import com.d10ng.compose.ui.base.Cell
import com.d10ng.compose.ui.base.CellArrowDirection
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.navigation.NavBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/**
 * 单元格
 * @Author d10ng
 * @Date 2023/9/4 14:48
 */
@RootNavGraph
@Destination(style = PageTransitions::class)
@Composable
fun CellScreen(
    nav: DestinationsNavigator
) {
    CellScreenView(onClickBack = nav::navigateUp)
}

@Composable
private fun CellScreenView(
    onClickBack: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "Cell", onClickBack = onClickBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            CellGroup(
                title = "基础用法"
            ) {
                Cell(title = "单元格")
                Cell(title = "单元格", value = "内容")
                Cell(title = "单元格", value = "内容", label = "描述信息", border = false)
            }
            CellGroup(inset = true, title = "卡片风格") {
                Cell(title = "单元格", link = true)
                Cell(title = "单元格", value = "内容")
                Cell(title = "单元格", value = "内容", label = "描述信息", border = false)
            }
            CellGroup(title = "展示图标") {
                Cell(title = "单元格", value = "内容", icon = {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = "",
                        modifier = Modifier.size(14.dp)
                    )
                })
                Cell(
                    title = "单元格",
                    value = "内容",
                    label = "描述信息",
                    border = false,
                    icon = {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = "",
                            modifier = Modifier.size(14.dp)
                        )
                    })
            }
            CellGroup(title = "展示箭头") {
                Cell(title = "单元格", link = true)
                Cell(title = "单元格", value = "内容", link = true)
                Cell(
                    title = "单元格",
                    value = "内容",
                    link = true,
                    arrowDirection = CellArrowDirection.DOWN
                )
                Cell(
                    title = "单元格",
                    value = "内容",
                    label = "描述信息",
                    border = false,
                    link = true
                )
            }
            CellGroup(title = "点击效果") {
                val ctx = LocalContext.current
                Cell(title = "单元格", value = "内容", link = true, onClick = {
                    Toast.makeText(ctx, "点击了单元格", Toast.LENGTH_SHORT).show()
                })
                Cell(
                    title = "单元格",
                    value = "内容",
                    link = true,
                    label = "描述信息",
                    border = false,
                    onClick = {
                        UiViewModelManager.showToast("点击了单元格")
                    })
            }
        }
    }
}

@Preview
@Composable
private fun CellScreenViewPreview() {
    CellScreenView()
}