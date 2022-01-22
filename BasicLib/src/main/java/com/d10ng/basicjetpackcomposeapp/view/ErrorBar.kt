package com.d10ng.basicjetpackcomposeapp.view

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.compose.AppText
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun BoxScope.ErrorBar(
    isShowError: Boolean,
    text: String
) {
    if (isShowError) {
        Snackbar(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .statusBarsPadding()
                .padding(16.dp)
                .navigationBarsWithImePadding()
                .align(Alignment.TopCenter),
            backgroundColor = AppColor.System.error
        ) {
            Text(text = text, style = AppText.Medium.OnError.v14)
        }
    }
}