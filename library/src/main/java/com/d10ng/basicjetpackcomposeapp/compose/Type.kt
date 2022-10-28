package com.d10ng.basicjetpackcomposeapp.compose

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

class BasicTextFontWeight(style: TextStyle) {
    val Title = BasicTextFontSize(style.copy(color = AppColor.Text.title))
    val Body = BasicTextFontSize(style.copy(color = AppColor.Text.body))
    val Hint = BasicTextFontSize(style.copy(color = AppColor.Text.hint))
    val Error = BasicTextFontSize(style.copy(color = AppColor.Text.error))
    val Primary = BasicTextFontSize(style.copy(color = AppColor.System.primary))
    val PrimaryVariant = BasicTextFontSize(style.copy(color = AppColor.System.primaryVariant))
    val Secondary = BasicTextFontSize(style.copy(color = AppColor.System.secondary))
    val SecondaryVariant = BasicTextFontSize(style.copy(color = AppColor.System.secondaryVariant))
    val OnPrimary = BasicTextFontSize(style.copy(color = AppColor.On.primary))
    val OnSecondary = BasicTextFontSize(style.copy(color = AppColor.On.secondary))
    val OnError = BasicTextFontSize(style.copy(color = AppColor.On.error))
}

class BasicTextFontSize(style: TextStyle) {
    val v60 = style.copy(fontSize = 60.sp)
    val v58 = style.copy(fontSize = 58.sp)
    val v56 = style.copy(fontSize = 56.sp)
    val v54 = style.copy(fontSize = 54.sp)
    val v52 = style.copy(fontSize = 52.sp)
    val v50 = style.copy(fontSize = 50.sp)
    val v48 = style.copy(fontSize = 48.sp)
    val v46 = style.copy(fontSize = 46.sp)
    val v44 = style.copy(fontSize = 44.sp)
    val v42 = style.copy(fontSize = 42.sp)
    val v40 = style.copy(fontSize = 40.sp)
    val v38 = style.copy(fontSize = 38.sp)
    val v36 = style.copy(fontSize = 36.sp)
    val v34 = style.copy(fontSize = 34.sp)
    val v32 = style.copy(fontSize = 32.sp)
    val v30 = style.copy(fontSize = 30.sp)
    val v28 = style.copy(fontSize = 28.sp)
    val v26 = style.copy(fontSize = 26.sp)
    val v24 = style.copy(fontSize = 24.sp)
    val v22 = style.copy(fontSize = 22.sp)
    val v20 = style.copy(fontSize = 20.sp)
    val v18 = style.copy(fontSize = 18.sp)
    val v16 = style.copy(fontSize = 16.sp)
    val v14 = style.copy(fontSize = 14.sp)
    val v12 = style.copy(fontSize = 12.sp)
    val v10 = style.copy(fontSize = 10.sp)
    val v8 = style.copy(fontSize = 8.sp)
}

object AppText {
    private val textBasic = TextStyle()
    val Bold = BasicTextFontWeight(textBasic.copy(fontWeight = FontWeight.Bold))
    val Medium = BasicTextFontWeight(textBasic.copy(fontWeight = FontWeight.Medium))
    val Normal = BasicTextFontWeight(textBasic.copy(fontWeight = FontWeight.Normal))
}