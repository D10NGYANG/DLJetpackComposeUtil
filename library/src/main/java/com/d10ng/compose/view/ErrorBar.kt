package com.d10ng.compose.view

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppText

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
            containerColor = AppColor.Func.error
        ) {
            Text(text = text, style = AppText.Medium.White.v14)
        }
    }
}