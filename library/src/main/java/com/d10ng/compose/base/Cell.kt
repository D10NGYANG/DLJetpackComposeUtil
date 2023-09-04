package com.d10ng.compose.base

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.R
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape

@Preview(device = "spec:width=1080px,height=3500px,dpi=440")
@Composable
fun CellPreview() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        item {
            CellGroup(
                title = "基础用法"
            ) {
                Cell(title = "单元格")
                Cell(title = "单元格", value = "内容")
                Cell(title = "单元格", value = "内容", label = "描述信息", border = false)
            }
            CellGroup(inset = true, title = "卡片风格") {
                Cell(title = "单元格", isLink = true)
                Cell(title = "单元格", value = "内容")
                Cell(title = "单元格", value = "内容", label = "描述信息", border = false)
            }
            CellGroup(title = "展示图标") {
                Cell(title = "单元格", value = "内容", icon = {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = "",
                        modifier = Modifier.size(14.dp)
                    )
                })
                Cell(
                    title = "单元格",
                    value = "内容",
                    label = "描述信息",
                    border = false,
                    icon = {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = "",
                            modifier = Modifier.size(14.dp)
                        )
                    })
            }
            CellGroup(title = "展示箭头") {
                Cell(title = "单元格", isLink = true)
                Cell(title = "单元格", value = "内容", isLink = true)
                Cell(
                    title = "单元格",
                    value = "内容",
                    isLink = true,
                    arrowDirection = CellArrowDirection.DOWN
                )
                Cell(
                    title = "单元格",
                    value = "内容",
                    label = "描述信息",
                    border = false,
                    isLink = true
                )
            }
            CellGroup(title = "点击效果") {
                val ctx = LocalContext.current
                Cell(title = "单元格", value = "内容", isLink = true, onClick = {
                    Toast.makeText(ctx, "点击了单元格", Toast.LENGTH_SHORT).show()
                })
                Cell(
                    title = "单元格",
                    value = "内容",
                    isLink = true,
                    label = "描述信息",
                    border = false,
                    onClick = {})
            }
        }
    }
}

@Composable
private fun Border() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(0.5.dp)
            .background(AppColor.Neutral.border)
    )
}

/**
 * 单元格组
 * @param modifier Modifier 修饰符
 * @param title String 标题
 * @param inset Boolean 是否为圆角卡片风格
 * @param border Boolean 是否显示边框
 * @param bgColor Color 背景色
 * @param content [@androidx.compose.runtime.Composable] [@kotlin.ExtensionFunctionType] Function1<ColumnScope, Unit>
 */
@Composable
fun CellGroup(
    modifier: Modifier = Modifier,
    title: String = "",
    inset: Boolean = false,
    border: Boolean = true,
    bgColor: Color = Color.White,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        if (title.isNotEmpty()) {
            Text(text = title, color = AppColor.Neutral.tips, modifier = Modifier.padding(16.dp))
        }
        val shape = if (inset) AppShape.RC.v8 else AppShape.RC.v0
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (inset) 16.dp else 0.dp)
                .background(bgColor, shape)
        ) {
            if (border && !inset) Border()
            content()
            if (border && !inset) Border()
        }
    }
}

enum class CellArrowDirection(val degrees: Float) {
    LEFT(180f),
    RIGHT(0f),
    UP(-90f),
    DOWN(90f)
}

/**
 * 单元格
 * @param modifier Modifier 修饰符
 * @param title String 标题
 * @param value String 内容
 * @param label String 描述信息
 * @param icon [@androidx.compose.runtime.Composable] Function0<Unit>? 图标
 * @param border Boolean 是否显示边框
 * @param isLink Boolean 是否展示右侧箭头并开启点击反馈
 * @param required Boolean 是否显示必填标记
 * @param arrowDirection CellArrowDirection 箭头方向
 * @param onClick Function0<Unit>? 点击事件
 * @param afterTitle [@androidx.compose.runtime.Composable] Function0<Unit>? 标题后面的内容
 * @param afterValue [@androidx.compose.runtime.Composable] Function0<Unit>? 内容后面的内容
 */
@Composable
fun Cell(
    modifier: Modifier = Modifier,
    title: String,
    value: String = "",
    label: String = "",
    icon: @Composable (() -> Unit)? = null,
    border: Boolean = true,
    isLink: Boolean = false,
    required: Boolean = false,
    arrowDirection: CellArrowDirection = CellArrowDirection.RIGHT,
    onClick: (() -> Unit)? = null,
    afterTitle: @Composable (() -> Unit)? = null,
    afterValue: @Composable (() -> Unit)? = null
) {
    BaseCell(
        modifier = modifier,
        border = border,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.invoke()
            if (required) {
                Text(
                    text = "*",
                    color = AppColor.Func.error,
                    modifier = Modifier.padding(start = if (icon != null) 8.dp else 0.dp)
                )
            }
            Text(
                text = title,
                color = AppColor.Neutral.title,
                modifier = Modifier
                    .padding(start = if (icon != null && !required) 8.dp else 0.dp)
            )
            afterTitle?.invoke()
            Spacer(modifier = Modifier.weight(1f))
            if (value.isNotEmpty()) {
                Text(
                    text = value,
                    color = AppColor.Neutral.body
                )
            }
            afterValue?.invoke()
            if (isLink) Icon(
                painter = painterResource(id = R.drawable.ic_round_arrow_forward_ios_24),
                contentDescription = "箭头",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(18.dp)
                    .rotate(arrowDirection.degrees),
                tint = AppColor.Neutral.body
            )
        }
        if (label.isNotEmpty()) {
            Text(
                text = label,
                color = AppColor.Neutral.tips,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

/**
 * 基础单元格
 * @param modifier Modifier
 * @param border Boolean
 * @param onClick Function0<Unit>?
 * @param content [@androidx.compose.runtime.Composable] Function0<Unit>
 */
@Composable
fun BaseCell(
    modifier: Modifier = Modifier,
    border: Boolean = true,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = onClick != null,
                role = Role.Button
            ) {
                onClick?.invoke()
            }
            .padding(horizontal = 16.dp),
    ) {
        content()
        if (border) Border()
    }
}