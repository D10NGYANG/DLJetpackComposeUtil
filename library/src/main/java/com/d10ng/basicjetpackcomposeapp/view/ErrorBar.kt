package com.d10ng.basicjetpackcomposeapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.compose.AppText

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
                .navigationBarsPadding()
                .imePadding()
                .align(Alignment.TopCenter),
            backgroundColor = AppColor.System.error
        ) {
            Text(text = text, style = AppText.Medium.OnError.v14)
        }
    }
}