package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.base.Button
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.compose.ui.show.Steps

/**
 * 步骤条
 * @Author d10ng
 * @Date 2023/9/13 13:58
 */
class StepsScreen : Screen {
    @Composable
    override fun Content() {
        StepsScreenView()
    }
}

@Composable
private fun StepsScreenView() {
    val navigator = LocalNavigator.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "Steps", onClickBack = { navigator?.pop() })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            var index by remember {
                mutableIntStateOf(0)
            }
            CellGroup(title = "基础用法", border = false) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 19.dp)
                ) {
                    Steps(
                        items = setOf("步骤一", "步骤二", "步骤三", "步骤四"),
                        runningIndex = index
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 19.dp)
                ) {
                    Steps(
                        items = setOf("步骤一", "步骤二", "步骤三", "步骤四"),
                        runningIndex = index,
                        runningColor = Color.Red
                    )
                }
            }
            Button(text = "下一步") {
                index++
                if (index > 3) index = 0
            }
        }
    }
}