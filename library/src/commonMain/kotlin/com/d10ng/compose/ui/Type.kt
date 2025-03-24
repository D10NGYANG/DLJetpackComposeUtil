package com.d10ng.compose.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

class BasicTextFontWeight(style: TextStyle) {
    val Primary = BasicTextFontSize(style.copy(color = AppColor.Main.primary))
    val Title = BasicTextFontSize(style.copy(color = AppColor.Neutral.title))
    val Body = BasicTextFontSize(style.copy(color = AppColor.Neutral.body))
    val Hint = BasicTextFontSize(style.copy(color = AppColor.Neutral.hint))
    val Tips = BasicTextFontSize(style.copy(color = AppColor.Neutral.tips))
    val Link = BasicTextFontSize(style.copy(color = AppColor.Func.link))
    val Success = BasicTextFontSize(style.copy(color = AppColor.Func.success))
    val Error = BasicTextFontSize(style.copy(color = AppColor.Func.error))
    val Notice = BasicTextFontSize(style.copy(color = AppColor.Func.notice))
    val Assist = BasicTextFontSize(style.copy(color = AppColor.Func.assist))

    // 白色
    val White = BasicTextFontSize(style.copy(color = Color.White))

    // 黑色
    val Black = BasicTextFontSize(style.copy(color = Color.Black))
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

    // 超大
    val huge = v24

    // 巨大
    val large = v22

    // 大
    val big = v20

    // 中
    val medium = v18

    // 默认
    val default = v16

    // 小
    val small = v14

    // 迷你
    val mini = v12
}

@Deprecated("不推荐使用")
object AppText {
    private val textBasic = TextStyle()
    val Bold = BasicTextFontWeight(textBasic.copy(fontWeight = FontWeight.Bold))
    val Medium = BasicTextFontWeight(textBasic.copy(fontWeight = FontWeight.Medium))
    val Normal = BasicTextFontWeight(textBasic.copy(fontWeight = FontWeight.Normal))
}

val defaultTypography
    @Composable @ReadOnlyComposable get() = MaterialTheme.typography.copy(
        titleLarge = TextStyle(fontWeight = FontWeight.W600, fontSize = 22.sp, lineHeight = 32.sp),
        titleMedium = TextStyle(fontWeight = FontWeight.W600, fontSize = 20.sp, lineHeight = 28.sp),
        titleSmall = TextStyle(fontWeight = FontWeight.W600, fontSize = 18.sp, lineHeight = 26.sp),
        bodyLarge = TextStyle(fontWeight = FontWeight.W400, fontSize = 18.sp, lineHeight = 24.sp),
        bodyMedium = TextStyle(fontWeight = FontWeight.W400, fontSize = 16.sp, lineHeight = 22.sp),
        bodySmall = TextStyle(fontWeight = FontWeight.W400, fontSize = 14.sp, lineHeight = 20.sp),
        labelLarge = TextStyle(fontWeight = FontWeight.W400, fontSize = 14.sp, lineHeight = 18.sp),
        labelMedium = TextStyle(fontWeight = FontWeight.W400, fontSize = 12.sp, lineHeight = 16.sp),
        labelSmall = TextStyle(fontWeight = FontWeight.W400, fontSize = 10.sp, lineHeight = 14.sp),
    )