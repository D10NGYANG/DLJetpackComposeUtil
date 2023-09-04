package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.demo.PageTransitions
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.base.Button
import com.d10ng.compose.ui.base.ButtonSize
import com.d10ng.compose.ui.base.ButtonType
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
    ) {
        NavBar(title = "Button", onClickBack = onClickBack)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Text(text = "按钮类型", color = AppColor.Neutral.tips)
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(text = "主要按钮", type = ButtonType.PRIMARY) {}
                    Button(text = "成功按钮", type = ButtonType.SUCCESS) {}
                    Button(text = "默认按钮") {}
                    Button(text = "警告按钮", type = ButtonType.WARNING) {}
                    Button(text = "危险按钮", type = ButtonType.DANGER) {}
                }
            }
            item {
                Text(
                    text = "朴素按钮",
                    color = AppColor.Neutral.tips,
                    modifier = Modifier.padding(top = 16.dp)
                )
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(text = "主要按钮", type = ButtonType.PRIMARY, plain = true) {}
                    Button(text = "成功按钮", type = ButtonType.SUCCESS, plain = true) {}
                    Button(text = "默认按钮", plain = true) {}
                }
            }
            item {
                Text(
                    text = "细边框",
                    color = AppColor.Neutral.tips,
                    modifier = Modifier.padding(top = 16.dp)
                )
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
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
            }
            item {
                Text(
                    text = "禁用状态",
                    color = AppColor.Neutral.tips,
                    modifier = Modifier.padding(top = 16.dp)
                )
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(text = "主要按钮", type = ButtonType.PRIMARY, disabled = true) {}
                    Button(text = "成功按钮", type = ButtonType.SUCCESS, disabled = true) {}
                    Button(text = "默认按钮", plain = true, disabled = true) {}
                }
            }
            item {
                Text(
                    text = "加载状态",
                    color = AppColor.Neutral.tips,
                    modifier = Modifier.padding(top = 16.dp)
                )
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(text = "主要按钮", type = ButtonType.PRIMARY, loading = true) {}
                    Button(text = "成功按钮", type = ButtonType.SUCCESS, loading = true) {}
                    Button(text = "默认按钮", plain = true, loading = true) {}
                }
            }
            item {
                Text(
                    text = "按钮形状",
                    color = AppColor.Neutral.tips,
                    modifier = Modifier.padding(top = 16.dp)
                )
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        text = "圆形按钮",
                        type = ButtonType.PRIMARY,
                        shape = AppShape.RC.Cycle
                    ) {}
                    Button(text = "圆角按钮", type = ButtonType.SUCCESS, shape = AppShape.RC.v8) {}
                    Button(text = "方形按钮", plain = true, shape = AppShape.RC.v0) {}
                }
            }
            item {
                Text(
                    text = "图标按钮",
                    color = AppColor.Neutral.tips,
                    modifier = Modifier.padding(top = 16.dp)
                )
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
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
            }
            item {
                Text(
                    text = "按钮尺寸",
                    color = AppColor.Neutral.tips,
                    modifier = Modifier.padding(top = 16.dp)
                )
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(text = "迷你按钮", type = ButtonType.PRIMARY, size = ButtonSize.MINI) {}
                    Button(text = "小型按钮", type = ButtonType.SUCCESS, size = ButtonSize.SMALL) {}
                    Button(text = "默认按钮", plain = true, size = ButtonSize.NORMAL) {}
                    Button(text = "大型按钮", type = ButtonType.WARNING, size = ButtonSize.LARGE) {}
                }
            }
            item {
                Text(
                    text = "自定义颜色",
                    color = AppColor.Neutral.tips,
                    modifier = Modifier.padding(top = 16.dp)
                )
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
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
}

@Preview(device = "spec:width=1080px,height=3500px,dpi=440")
@Composable
private fun ButtonScreenViewPreview() {
    ButtonScreenView()
}