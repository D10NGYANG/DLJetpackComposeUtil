package com.d10ng.compose.ui

import androidx.compose.ui.graphics.Color

object AppColor {

    /**
     * 主色
     */
    object Main {
        var primary = Color(0xFF1989FA)
    }

    /**
     * 功能色
     */
    object Func {
        /**
         * 文字链颜色
         */
        var link = Color(0xFF576B95)

        /**
         * 成功色
         */
        var success = Color(0xFF07C160)

        /**
         * 错误色
         */
        var error = Color(0xFFEE0A24)

        /**
         * 通知消息中的文本颜色
         */
        var notice = Color(0xFFED6A0C)

        /**
         * 通知消息中的背景颜色
         */
        var noticeBg = Color(0xFFFFFBE8)

        /**
         * 文字辅助颜色
         */
        var assist = Color(0xFFFAAB0C)
    }

    /**
     * 中性色
     */
    object Neutral {
        /**
         * 中性色 1 页面背景色
         */
        var bg = Color(0xFFF7F8FA)

        /**
         * 中性色 2 item card 背景色
         */
        var card = Color(0xFFF2F3F5)

        /**
         * 中性色 3 边框、线色
         */
        var line = Color(0xFFEBEDF0)

        /**
         * 中性色 4 边框、线色
         */
        var border = Color(0xFFDCDEF0)

        /**
         * 中性色 5 文字色，disable、提示文本等
         */
        var hint = Color(0xFFC8C9CC)

        /**
         * 中性色 6 文字色，辅助、说明文本
         */
        var tips = Color(0xFF969799)

        /**
         * 中性色 7 主要文本2
         */
        var body = Color(0xFF646566)

        /**
         * 中性色 8 主要文本1
         */
        var title = Color(0xFF323233)
    }
}