package com.d10ng.compose.ui.form

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.R
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.view.Input

/**
 * 搜索
 * @Author d10ng
 * @Date 2023/9/14 17:47
 */

@Preview
@Composable
private fun SearchTest() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        CellGroup(title = "基础用法") {
            Search(
                value = "ashdgahjsd",
                label = "地址",
                shape = AppShape.RC.v6,
                //onClickCancel = {}
            )
        }
        CellGroup(title = "基础用法") {
            Search(
                value = "ashdgahjsd",
                label = "地址",
                shape = AppShape.RC.v6,
                onClickAction = {}
            )
        }
    }
}

/**
 * 搜索
 * @param value String 输入内容
 * @param onValueChange Function1<String, Unit> 输入改变回调
 * @param label String 标签
 * @param placeholder String 占位文本
 * @param align TextAlign 对齐方式
 * @param disabled Boolean 是否禁用
 * @param loading Boolean 是否显示加载中
 * @param shape RoundedCornerShape 圆角
 * @param backgroundColor Color 背景色
 * @param actionText String 动作按钮文本
 * @param actionTextStyle TextStyle 动作按钮文本样式
 * @param onClickAction Function0<Unit>? 动作按钮点击回调
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
            .padding(start = 16.dp)
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
                    painter = painterResource(id = R.drawable.ic_round_search_22),
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
                    painter = painterResource(id = R.drawable.ic_round_cancel_24),
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