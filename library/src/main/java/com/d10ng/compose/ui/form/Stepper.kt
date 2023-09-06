package com.d10ng.compose.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 步进器
 * @Author d10ng
 * @Date 2023/9/6 15:53
 */

@Composable
fun Stepper(
    value: Int,
    onValueChange: (Int) -> Unit,
    min: Int = 0,
    max: Int = Int.MAX_VALUE,
    step: Int = 1,
    disabled: Boolean = false,
    canInput: Boolean = true,
    inputWidth: Dp = 80.dp,
    buttonSize: Dp = 32.dp,
) {

}