package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.PageTransitions
import com.d10ng.compose.ui.base.Button
import com.d10ng.compose.ui.base.ButtonSize
import com.d10ng.compose.ui.base.ButtonType
import com.d10ng.compose.ui.base.CellTitle
import com.d10ng.compose.ui.navigation.NavBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/**
 * 按钮页面
 * @Author d10ng
 * @Date 2023/9/4 14:16
 */
@RootNavGraph
@Destination(style = PageTransitions::class)
@Composable
fun ButtonScreen(
    nav: DestinationsNavigator
) {
    ButtonScreenView(onClickBack = nav::navigateUp)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ButtonScreenView(
    onClickBack: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
            .navigationBarsPadding()
    ) {
        NavBar(title = "Button", onClickBack = onClickBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
        ) {
            CellTitle(title = "按钮类型")
            CustomFlowRow {
                Button(text = "主要按钮", type = ButtonType.PRIMARY) {}
                Button(text = "成功按钮", type = ButtonType.SUCCESS) {}
                Button(text = "默认按钮") {}
                Button(text = "警告按钮", type = ButtonType.WARNING) {}
                Button(text = "危险按钮", type = ButtonType.DANGER) {}
            }
            CellTitle(title = "朴素按钮")
            CustomFlowRow {
                Button(text = "主要按钮", type = ButtonType.PRIMARY, plain = true) {}
                Button(text = "成功按钮", type = ButtonType.SUCCESS, plain = true) {}
                Button(text = "默认按钮", plain = true) {}
            }
            CellTitle(title = "细边框")
            CustomFlowRow {
                Button(
                    text = "主要按钮",
                    type = ButtonType.PRIMARY,
                    plain = true,
                    hairline = true
                ) {}
                Button(
                    text = "成功按钮",
                    type = ButtonType.SUCCESS,
                    plain = true,
                    hairline = true
                ) {}
                Button(text = "默认按钮", plain = true, hairline = true) {}
            }
            CellTitle(title = "禁用状态")
            CustomFlowRow {
                Button(text = "主要按钮", type = ButtonType.PRIMARY, disabled = true) {}
                Button(text = "成功按钮", type = ButtonType.SUCCESS, disabled = true) {}
                Button(text = "默认按钮", plain = true, disabled = true) {}
            }
            CellTitle(title = "加载状态")
            CustomFlowRow {
                Button(text = "主要按钮", type = ButtonType.PRIMARY, loading = true) {}
                Button(text = "成功按钮", type = ButtonType.SUCCESS, loading = true) {}
                Button(text = "默认按钮", plain = true, loading = true) {}
            }
            CellTitle(title = "按钮形状")
            CustomFlowRow {
                Button(
                    text = "圆形按钮",
                    type = ButtonType.PRIMARY,
                    shape = AppShape.RC.Cycle
                ) {}
                Button(text = "圆角按钮", type = ButtonType.SUCCESS, shape = AppShape.RC.v8) {}
                Button(text = "方形按钮", plain = true, shape = AppShape.RC.v0) {}
            }
            CellTitle(title = "图标按钮")
            CustomFlowRow {
                Button(text = "", type = ButtonType.PRIMARY, icon = {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "",
                        modifier = Modifier.size(18.dp)
                    )
                }) {}
                Button(text = "按钮", type = ButtonType.SUCCESS, icon = {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "",
                        modifier = Modifier.size(18.dp)
                    )
                }) {}
                Button(text = "图标按钮", plain = true, icon = {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = "",
                        modifier = Modifier.size(18.dp)
                    )
                }) {}
            }
            CellTitle(title = "按钮尺寸")
            CustomFlowRow {
                Button(text = "迷你按钮", type = ButtonType.PRIMARY, size = ButtonSize.MINI) {}
                Button(text = "小型按钮", type = ButtonType.SUCCESS, size = ButtonSize.SMALL) {}
                Button(text = "默认按钮", plain = true, size = ButtonSize.NORMAL) {}
                Button(text = "大型按钮", type = ButtonType.WARNING, size = ButtonSize.BIG) {}
                Button(text = "巨大按钮", type = ButtonType.PRIMARY, size = ButtonSize.LARGE) {}
            }
            CellTitle(title = "自定义颜色")
            CustomFlowRow {
                val color = Color(0xFF9C27B0)
                Button(text = "主要按钮", type = ButtonType.PRIMARY, color = color) {}
                Button(text = "默认按钮", plain = true, color = color) {}
                Button(
                    text = "主要按钮",
                    type = ButtonType.PRIMARY,
                    color = color,
                    disabled = true
                ) {}
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomFlowRow(
    content: @Composable FlowRowScope.() -> Unit
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 19.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = content
    )
}

@Preview(device = "spec:width=1080px,height=3500px,dpi=440")
@Composable
private fun ButtonScreenViewPreview() {
    ButtonScreenView()
}