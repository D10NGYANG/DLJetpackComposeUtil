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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            Text(
                text = "基础用法",
                color = AppColor.Neutral.tips,
                modifier = Modifier.padding(16.dp)
            )
            VenCellGroup {
                VenCell(title = "单元格")
                VenCell(title = "单元格", value = "内容")
                VenCell(title = "单元格", value = "内容", label = "描述信息", border = false)
            }
            Text(
                text = "卡片风格",
                color = AppColor.Neutral.tips,
                modifier = Modifier.padding(16.dp)
            )
            VenCellGroup(inset = true) {
                VenCell(title = "单元格", isLink = true)
                VenCell(title = "单元格", value = "内容")
                VenCell(title = "单元格", value = "内容", label = "描述信息", border = false)
            }
            Text(
                text = "展示图标",
                color = AppColor.Neutral.tips,
                modifier = Modifier.padding(16.dp)
            )
            VenCellGroup {
                VenCell(title = "单元格", value = "内容", icon = {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = "",
                        modifier = Modifier.size(14.dp)
                    )
                })
                VenCell(
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
            Text(
                text = "展示箭头",
                color = AppColor.Neutral.tips,
                modifier = Modifier.padding(16.dp)
            )
            VenCellGroup {
                VenCell(title = "单元格", isLink = true)
                VenCell(title = "单元格", value = "内容", isLink = true)
                VenCell(
                    title = "单元格",
                    value = "内容",
                    isLink = true,
                    arrowDirection = CellArrowDirection.DOWN
                )
                VenCell(
                    title = "单元格",
                    value = "内容",
                    label = "描述信息",
                    border = false,
                    isLink = true
                )
            }
            Text(
                text = "点击效果",
                color = AppColor.Neutral.tips,
                modifier = Modifier.padding(16.dp)
            )
            VenCellGroup {
                val ctx = LocalContext.current
                VenCell(title = "单元格", value = "内容", isLink = true, onClick = {
                    Toast.makeText(ctx, "点击了单元格", Toast.LENGTH_SHORT).show()
                })
                VenCell(
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

@Composable
fun VenCellGroup(
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

enum class CellArrowDirection(val icon: ImageVector) {
    LEFT(Icons.Default.KeyboardArrowLeft),
    RIGHT(Icons.Default.KeyboardArrowRight),
    UP(Icons.Default.KeyboardArrowUp),
    DOWN(Icons.Default.KeyboardArrowDown)
}

@Composable
fun VenCell(
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
                arrowDirection.icon,
                contentDescription = "箭头",
                modifier = Modifier.size(18.dp),
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
        if (border) Border()
    }
}