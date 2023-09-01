package com.d10ng.compose.base

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.utils.isDark
import com.d10ng.compose.utils.next

@OptIn(ExperimentalLayoutApi::class)
@Preview(device = "spec:width=1080px,height=3000px,dpi=440")
@Composable
internal fun ButtonPreview() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
            .padding(16.dp)
    ) {
        item {
            Text(text = "按钮类型", color = AppColor.Neutral.tips)
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VanButton(text = "主要按钮", type = ButtonType.PRIMARY) {}
                VanButton(text = "成功按钮", type = ButtonType.SUCCESS) {}
                VanButton(text = "默认按钮") {}
                VanButton(text = "警告按钮", type = ButtonType.WARNING) {}
                VanButton(text = "危险按钮", type = ButtonType.DANGER) {}
            }
            Text(
                text = "朴素按钮",
                color = AppColor.Neutral.tips,
                modifier = Modifier.padding(top = 16.dp)
            )
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VanButton(text = "主要按钮", type = ButtonType.PRIMARY, plain = true) {}
                VanButton(text = "成功按钮", type = ButtonType.SUCCESS, plain = true) {}
                VanButton(text = "默认按钮", plain = true) {}
            }
            Text(
                text = "细边框",
                color = AppColor.Neutral.tips,
                modifier = Modifier.padding(top = 16.dp)
            )
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VanButton(
                    text = "主要按钮",
                    type = ButtonType.PRIMARY,
                    plain = true,
                    hairline = true
                ) {}
                VanButton(
                    text = "成功按钮",
                    type = ButtonType.SUCCESS,
                    plain = true,
                    hairline = true
                ) {}
                VanButton(text = "默认按钮", plain = true, hairline = true) {}
            }
            Text(
                text = "禁用状态",
                color = AppColor.Neutral.tips,
                modifier = Modifier.padding(top = 16.dp)
            )
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VanButton(text = "主要按钮", type = ButtonType.PRIMARY, disabled = true) {}
                VanButton(text = "成功按钮", type = ButtonType.SUCCESS, disabled = true) {}
                VanButton(text = "默认按钮", plain = true, disabled = true) {}
            }
            Text(
                text = "加载状态",
                color = AppColor.Neutral.tips,
                modifier = Modifier.padding(top = 16.dp)
            )
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VanButton(text = "主要按钮", type = ButtonType.PRIMARY, loading = true) {}
                VanButton(text = "成功按钮", type = ButtonType.SUCCESS, loading = true) {}
                VanButton(text = "默认按钮", plain = true, loading = true) {}
            }
            Text(
                text = "按钮形状",
                color = AppColor.Neutral.tips,
                modifier = Modifier.padding(top = 16.dp)
            )
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VanButton(
                    text = "圆形按钮",
                    type = ButtonType.PRIMARY,
                    shape = AppShape.RC.Cycle
                ) {}
                VanButton(text = "圆角按钮", type = ButtonType.SUCCESS, shape = AppShape.RC.v8) {}
                VanButton(text = "方形按钮", plain = true, shape = AppShape.RC.v0) {}
            }
            Text(
                text = "图标按钮",
                color = AppColor.Neutral.tips,
                modifier = Modifier.padding(top = 16.dp)
            )
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VanButton(text = "", type = ButtonType.PRIMARY, icon = {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "",
                        modifier = Modifier.size(18.dp)
                    )
                }) {}
                VanButton(text = "按钮", type = ButtonType.SUCCESS, icon = {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "",
                        modifier = Modifier.size(18.dp)
                    )
                }) {}
                VanButton(text = "图标按钮", plain = true, icon = {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = "",
                        modifier = Modifier.size(18.dp)
                    )
                }) {}
            }
            Text(
                text = "按钮尺寸",
                color = AppColor.Neutral.tips,
                modifier = Modifier.padding(top = 16.dp)
            )
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VanButton(text = "迷你按钮", type = ButtonType.PRIMARY, size = ButtonSize.MINI) {}
                VanButton(text = "小型按钮", type = ButtonType.SUCCESS, size = ButtonSize.SMALL) {}
                VanButton(text = "默认按钮", plain = true, size = ButtonSize.NORMAL) {}
                VanButton(text = "大型按钮", type = ButtonType.WARNING, size = ButtonSize.LARGE) {}
            }
            Text(
                text = "自定义颜色",
                color = AppColor.Neutral.tips,
                modifier = Modifier.padding(top = 16.dp)
            )
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val color = Color(0xFF9C27B0)
                VanButton(text = "主要按钮", type = ButtonType.PRIMARY, color = color) {}
                VanButton(text = "默认按钮", plain = true, color = color) {}
                VanButton(
                    text = "主要按钮",
                    type = ButtonType.PRIMARY,
                    color = color,
                    disabled = true
                ) {}
            }
        }
    }
}

enum class ButtonType(val color: Color, val border: Color) {
    // 默认
    DEFAULT(Color.White, AppColor.Neutral.border),

    // 主要
    PRIMARY(AppColor.Main.primary, AppColor.Main.primary),

    // 成功
    SUCCESS(AppColor.Func.success, AppColor.Func.success),

    // 警告
    WARNING(AppColor.Func.assist, AppColor.Func.assist),

    // 危险
    DANGER(AppColor.Func.error, AppColor.Func.error),
}

enum class ButtonSize(val textSize: TextUnit, val paddingValues: PaddingValues, val height: Dp) {
    // 默认
    NORMAL(14.sp, ButtonDefaults.ContentPadding, 40.dp),

    // 迷你
    MINI(10.sp, PaddingValues(6.dp, 2.dp), 28.dp),

    // 小
    SMALL(12.sp, PaddingValues(12.dp, 4.dp), 36.dp),

    // 大
    LARGE(18.sp, PaddingValues(36.dp, 12.dp), 64.dp),
}

/**
 * 按钮
 * @param text String 按钮文字
 * @param modifier Modifier 按钮样式
 * @param icon [@androidx.compose.runtime.Composable] Function0<Unit>? 按钮图标
 * @param type ButtonType 按钮类型
 * @param size ButtonSize 按钮尺寸
 * @param plain Boolean 是否朴素按钮
 * @param hairline Boolean 是否细边框
 * @param disabled Boolean 是否禁用
 * @param loading Boolean 是否加载中
 * @param shape RoundedCornerShape 按钮形状
 * @param color Color? 按钮颜色
 * @param onClick Function0<Unit> 点击事件
 */
@Composable
fun VanButton(
    text: String,
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    type: ButtonType = ButtonType.DEFAULT,
    size: ButtonSize = ButtonSize.NORMAL,
    plain: Boolean = false,
    hairline: Boolean = false,
    disabled: Boolean = false,
    loading: Boolean = false,
    shape: RoundedCornerShape = AppShape.RC.v4,
    color: Color? = null,
    onClick: () -> Unit
) {
    // 主要颜色
    val mainColor = color ?: type.color
    // 背景颜色
    val bgColor = if (plain) Color.White else mainColor
    // 内容颜色
    val contentColor = if (plain) {
        if (mainColor.isDark()) mainColor else AppColor.Neutral.title
    } else if (mainColor.isDark()) Color.White else AppColor.Neutral.title
    // 边框颜色
    val borderColor = color ?: type.border
    val thisModifier = remember {
        if (size == ButtonSize.MINI || size == ButtonSize.SMALL) {
            modifier.height(size.height)
        } else {
            modifier
        }
    }
    Button(
        onClick = {
            if (!disabled && !loading) onClick()
        },
        modifier = thisModifier,
        enabled = !disabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = bgColor,
            disabledContainerColor = bgColor.next(0.5),
            contentColor = contentColor,
            disabledContentColor = contentColor.next(0.5),
        ),
        border = BorderStroke(
            width = if (hairline) 0.3.dp else 1.dp,
            color = if (disabled) borderColor.next(0.5) else borderColor
        ),
        contentPadding = size.paddingValues
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(14.dp),
                color = contentColor,
                strokeWidth = 2.dp,
                strokeCap = StrokeCap.Round
            )
        } else {
            if (icon != null) {
                icon()
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(text = text, fontSize = size.textSize)
        }
    }
}