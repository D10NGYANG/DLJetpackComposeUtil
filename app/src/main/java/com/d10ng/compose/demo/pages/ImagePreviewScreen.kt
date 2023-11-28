package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.d10ng.app.managers.PhotoManager
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.PageTransitions
import com.d10ng.compose.ui.base.Button
import com.d10ng.compose.ui.base.ButtonType
import com.d10ng.compose.ui.base.CellTitle
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.compose.ui.show.ImagePreview
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

/**
 * 图片查看器
 * @Author d10ng
 * @Date 2023/11/28 16:29
 */
@RootNavGraph
@Destination(style = PageTransitions::class)
@Composable
fun ImagePreviewScreen(
    nav: DestinationsNavigator
) {
    ImagePreviewScreenView(onClickBack = nav::navigateUp)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ImagePreviewScreenView(
    onClickBack: () -> Unit = {},
) {
    // 图片路径
    var imagePath by remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
            .navigationBarsPadding()
    ) {
        NavBar(title = "ImagePreview", onClickBack = onClickBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            CellTitle(title = "基础用法")
            CustomFlowRow {
                val scope = rememberCoroutineScope()
                Button(text = "选择图片", type = ButtonType.PRIMARY) {
                    scope.launch {
                        PhotoManager.pick()?.let {
                            imagePath = it
                        }
                    }
                }
            }
        }
    }
    if (imagePath.isNotEmpty()) {
        ImagePreview(path = imagePath) {
            imagePath = ""
        }
    }
}

@Preview
@Composable
private fun ImagePreviewScreenPreview() {
    ImagePreviewScreenView()
}