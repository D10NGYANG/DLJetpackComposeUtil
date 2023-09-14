package com.d10ng.compose.ui.form

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.d10ng.compose.ui.AppShape

/**
 * 搜索
 * @Author d10ng
 * @Date 2023/9/14 17:47
 */

@Composable
fun Search(
    value: String,
    onValueChange: (String) -> Unit = {},
    label: String = "",
    placeholder: String = "请输入搜索关键词",
    //align: Alignment = Alignment.Start,
    disabled: Boolean = false,
    shape: RoundedCornerShape = AppShape.RC.v6,
    backgroundColor: Color = Color.White,
    onClickCancel: (() -> Unit)? = null,
) {

}