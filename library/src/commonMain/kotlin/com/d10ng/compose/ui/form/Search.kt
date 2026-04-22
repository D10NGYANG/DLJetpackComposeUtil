package com.d10ng.compose.ui.form

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.resources.Res
import com.d10ng.compose.resources.ic_round_cancel_24
import com.d10ng.compose.resources.ic_round_search_22
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.defaultPaddingSize
import com.d10ng.compose.view.Input
import org.jetbrains.compose.resources.painterResource

/**
 * 搜索输入框
 * 包含左侧搜索图标（或自定义标签）、输入框、可选清除按钮及可选右侧动作按钮
 * 获得焦点且有内容时自动显示清除按钮；[loading] 为 true 时显示进度指示器并隐藏清除按钮
 * @param value String 当前输入的搜索关键词
 * @param onValueChange (String) -> Unit 输入内容变更回调，默认无操作
 * @param label String 输入框左侧的文字标签，非空时替代默认搜索图标显示，默认为空（显示搜索图标）
 * @param placeholder String 输入框占位文字，默认「请输入搜索关键词」
 * @param align TextAlign 输入文字的对齐方式，默认 [TextAlign.Start]
 * @param disabled Boolean 是否禁用输入，禁用时输入框不可编辑且不显示清除按钮，默认 false
 * @param loading Boolean 是否显示加载状态，为 true 时在输入框右侧显示进度圈并隐藏清除按钮，默认 false
 * @param shape RoundedCornerShape 输入框背景圆角，默认 [AppShape.RC.v6]
 * @param backgroundColor Color 输入框背景色，默认 [AppColor.Neutral.card]
 * @param actionText String 右侧动作按钮的文字，仅在 [onClickAction] 不为 null 时显示，默认「取消」
 * @param actionTextStyle TextStyle 右侧动作按钮文字样式，仅在 [onClickAction] 不为 null 时生效，默认 `AppText.Normal.Title.default`
 * @param onClickAction (() -> Unit)? 右侧动作按钮点击回调，为 null 时不显示动作按钮，默认 null
 */
@Composable
fun Search(
    value: String,
    onValueChange: (String) -> Unit = {},
    label: String = "",
    placeholder: String = "请输入搜索关键词",
    align: TextAlign = TextAlign.Start,
    disabled: Boolean = false,
    loading: Boolean = false,
    shape: RoundedCornerShape = AppShape.RC.v6,
    backgroundColor: Color = AppColor.Neutral.card,
    actionText: String = "取消",
    actionTextStyle: TextStyle = AppText.Normal.Title.default,
    onClickAction: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .height(65.dp)
            .padding(start = defaultPaddingSize)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 输入框
        Row(
            modifier = Modifier
                .weight(1f)
                .height(38.dp)
                .background(backgroundColor, shape)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (label.isNotEmpty()) {
                // label
                Text(
                    text = label,
                    style = AppText.Normal.Title.default,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            } else {
                // 图标
                Icon(
                    painter = painterResource(resource = Res.drawable.ic_round_search_22),
                    contentDescription = "search",
                    tint = AppColor.Neutral.body,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(16.dp)
                )
            }
            // 输入框是否获取焦点
            var isFocus by remember {
                mutableStateOf(false)
            }
            // 是否显示清除按钮
            val isShowClear = remember(value, isFocus) {
                isFocus && value.isNotEmpty()
            }
            // 输入框
            Input(
                value = value,
                onValueChange = onValueChange,
                placeholder = placeholder,
                singleLine = true,
                enabled = !disabled,
                textAlign = align,
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { isFocus = it.isFocused }
            )
            if (isShowClear && !disabled && !loading) {
                Icon(
                    painter = painterResource(resource = Res.drawable.ic_round_cancel_24),
                    contentDescription = "清除",
                    tint = AppColor.Neutral.hint,
                    modifier = Modifier
                        .size(20.dp)
                        .clip(AppShape.RC.Cycle)
                        .clickable { onValueChange("") }
                )
            }
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(20.dp)
                        .padding(2.dp),
                    color = AppColor.Main.primary,
                    strokeWidth = 1.dp,
                    strokeCap = StrokeCap.Round
                )
            }
        }
        // 动作按钮
        if (onClickAction != null) {
            TextButton(
                modifier = Modifier.padding(horizontal = 8.dp),
                onClick = onClickAction
            ) {
                Text(text = actionText, style = actionTextStyle)
            }
        } else {
            Box(modifier = Modifier.width(16.dp))
        }
    }
}

@Preview
@Composable
fun PreviewSearch() {
    Column {
        // 基础搜索框（无内容）
        Search(value = "", onValueChange = {})
        // 带内容
        Search(value = "搜索关键词", onValueChange = {})
        // 带动作按钮
        Search(value = "", onValueChange = {}, onClickAction = {})
        // 带内容 + 动作按钮
        Search(value = "搜索关键词", onValueChange = {}, onClickAction = {})
        // 加载中
        Search(value = "搜索关键词", onValueChange = {}, loading = true)
        // 禁用
        Search(value = "禁用状态", onValueChange = {}, disabled = true)
        // 带自定义标签
        Search(value = "", onValueChange = {}, label = "分类", onClickAction = {})
    }
}
