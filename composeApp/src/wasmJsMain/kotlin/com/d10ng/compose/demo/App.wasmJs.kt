package com.d10ng.compose.demo

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import dljetpackcomposeutil_project.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi


@OptIn(ExperimentalResourceApi::class)
actual suspend fun loadCustomFontFamily(): FontFamily? {
    val normal = Res.readBytes("font/MiSans_Normal.ttf")
    return FontFamily(
        Font("MiSans_Normal", normal, FontWeight.Normal, FontStyle.Normal)
    )
}