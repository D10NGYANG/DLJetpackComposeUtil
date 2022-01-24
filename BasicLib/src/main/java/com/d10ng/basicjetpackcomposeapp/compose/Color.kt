package com.d10ng.basicjetpackcomposeapp.compose

import androidx.compose.ui.graphics.Color

object AppColor {
    object System {
        var primary = Color(0xFF6200EE)
        var primaryVariant = Color(0xFF3700B3)
        var secondary = Color(0xFF03DAC6)
        var secondaryVariant = Color(0xFF018786)
        var background = Color(0xFFFFFFFF)
        var surface = Color(0xFFFFFFFF)
        var error = Color(0xFFB00020)
        var divider = Color(0xFFDDDDDD)
    }
    object On {
        var primary = Color(0xFFFFFFFF)
        var secondary = Color(0xFF000000)
        var background = Color(0xFF000000)
        var surface = Color(0xFF000000)
        var error = Color(0xFFFFFFFF)
    }
    object Text {
        var title = Color(0xFF333333)
        var body = Color(0xFF666666)
        var hint = Color(0xFF999999)
        var error = Color(0xFFB00020)
    }
}