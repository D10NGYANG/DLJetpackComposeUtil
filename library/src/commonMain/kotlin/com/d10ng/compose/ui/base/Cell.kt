package com.d10ng.compose.ui.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.resources.Res
import com.d10ng.compose.resources.ic_round_forward_16
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.defaultPaddingSize
import com.d10ng.compose.ui.show.HorizontalDivider
import org.jetbrains.compose.resources.painterResource

/**
 * 单元格组件
 * @Author d10ng
 * @Date 2023/9/4 10:07
 */

/**
 * 单元格标题
 * 通常用于 [CellGroup] 上方，作为分组的描述性标题文字
 * @param modifier Modifier 修饰符
 * @param title String 标题文字
 */
@Composable
fun CellTitle(
    modifier: Modifier = Modifier,
    title: String
) {
    Text(
        text = title,
        style = AppText.Normal.Tips.default,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier.padding(defaultPaddingSize, 12.dp)
    )
}

/**
 * 单元格组
 * 将一组 [Cell] 包裹在一起，可选择是否显示分组标题、上下边框及圆角卡片风格
 * @param modifier Modifier 修饰符
 * @param title String 分组标题，为空时不显示，默认为空
 * @param inset Boolean 是否为圆角卡片风格，开启后内容区域带圆角且左右留有间距，默认 false
 * @param border Boolean 是否显示上下边框，仅在 [inset] 为 false 时生效，默认 true
 * @param bgColor Color 内容区域的背景色，默认为 MaterialTheme.colorScheme.surfaceContainerLowest
 * @param content @Composable ColumnScope.() -> Unit 子内容，通常由多个 [Cell] 组成
 */
@Composable
fun CellGroup(
    modifier: Modifier = Modifier,
    title: String = "",
    inset: Boolean = false,
    border: Boolean = true,
    bgColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        if (title.isNotEmpty()) {
            CellTitle(title = title)
        }
        val shape = if (inset) AppShape.RC.v8 else AppShape.RC.v0
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (inset) defaultPaddingSize else 0.dp)
                .background(bgColor, shape)
        ) {
            if (border && !inset) HorizontalDivider()
            content()
            if (border && !inset) HorizontalDivider()
        }
    }
}

/**
 * 单元格箭头方向
 * 控制 [Cell] 右侧箭头图标的旋转方向，仅在 [Cell] 的 link 参数为 true 时显示
 */
enum class CellArrowDirection(val degrees: Float) {
    // 向左
    LEFT(180f),
    // 向右（默认）
    RIGHT(0f),
    // 向上
    UP(-90f),
    // 向下
    DOWN(90f)
}

/**
 * 单元格
 * 列表中的基础行单元，支持标题、右侧内容、描述信息、图标、箭头链接及必填标记等
 * @param modifier Modifier 修饰符
 * @param title String 左侧标题文字
 * @param value String 右侧内容文字，为空时不显示，默认为空
 * @param label String 标题下方的描述信息，为空时不显示，默认为空
 * @param icon @Composable (() -> Unit)? 标题左侧图标，为 null 时不显示，默认 null
 * @param border Boolean 是否在底部显示分割线，默认 true
 * @param link Boolean 是否显示右侧箭头并将整行作为可点击区域，需同时设置 [onClick]，默认 false
 * @param required Boolean 是否在标题左侧显示红色必填星号 (*)，默认 false
 * @param arrowDirection CellArrowDirection 右侧箭头的方向，仅在 [link] 为 true 时生效，默认 [CellArrowDirection.RIGHT]
 * @param onClick (() -> Unit)? 整行点击回调，为 null 时不响应点击，默认 null
 * @param afterTitle @Composable (() -> Unit)? 紧跟在标题文字后面插入的自定义内容，默认 null
 * @param afterValue @Composable (() -> Unit)? 紧跟在右侧内容后、箭头前插入的自定义内容，默认 null
 */
@Composable
fun Cell(
    modifier: Modifier = Modifier,
    title: String,
    value: String = "",
    label: String = "",
    icon: @Composable (() -> Unit)? = null,
    border: Boolean = true,
    link: Boolean = false,
    required: Boolean = false,
    arrowDirection: CellArrowDirection = CellArrowDirection.RIGHT,
    onClick: (() -> Unit)? = null,
    afterTitle: @Composable (() -> Unit)? = null,
    afterValue: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(horizontal = defaultPaddingSize),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = defaultPaddingSize),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.invoke()
            if (required) {
                Text(
                    text = "*",
                    style = AppText.Normal.Error.default,
                    modifier = Modifier.padding(start = if (icon != null) 8.dp else 0.dp)
                )
            }
            Text(
                text = title,
                style = AppText.Normal.Title.default,
                modifier = Modifier
                    .padding(start = if (icon != null && !required) 8.dp else 0.dp)
            )
            afterTitle?.invoke()
            Spacer(modifier = Modifier.weight(1f))
            if (value.isNotEmpty()) {
                SelectionContainer {
                    Text(
                        text = value,
                        style = AppText.Normal.Body.default
                    )
                }
            }
            afterValue?.invoke()
            if (link) {
                Image(
                    painter = painterResource(resource = Res.drawable.ic_round_forward_16),
                    contentDescription = "箭头",
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(18.dp)
                        .rotate(arrowDirection.degrees),
                    colorFilter = ColorFilter.tint(AppColor.Neutral.body),
                    contentScale = ContentScale.Inside
                )
            }
        }
        if (label.isNotEmpty()) {
            SelectionContainer {
                Text(
                    text = label,
                    style = AppText.Normal.Tips.small,
                    modifier = Modifier.padding(bottom = defaultPaddingSize)
                )
            }
        }
        if (border) HorizontalDivider()
    }
}

/**
 * 单元格行
 * 通用的带分割线行容器，可在 [CellGroup] 内使用，适合自定义行内布局
 * 分割线叠加在内容底部，不会额外增加行高
 * @param modifier Modifier 修饰符
 * @param divider Boolean 是否在底部显示分割线，默认 true
 * @param dividerPaddingValues PaddingValues 分割线的水平内边距（左右缩进），默认左右各 [defaultPaddingSize]
 * @param contentPaddingValues PaddingValues 行内容的内边距，默认无内边距
 * @param content @Composable RowScope.() -> Unit 行内子内容
 */
@Composable
fun CellRow(
    modifier: Modifier = Modifier,
    divider: Boolean = true,
    dividerPaddingValues: PaddingValues = PaddingValues(horizontal = defaultPaddingSize),
    contentPaddingValues: PaddingValues = PaddingValues(0.dp),
    content: @Composable RowScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPaddingValues),
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
        }
        if (divider) {
            HorizontalDivider(
                modifier = Modifier.align(Alignment.BottomCenter),
                paddingStart = dividerPaddingValues.calculateStartPadding(LocalLayoutDirection.current),
                paddingEnd = dividerPaddingValues.calculateEndPadding(LocalLayoutDirection.current)
            )
        }
    }
}

@Preview
@Composable
fun PreviewCellTitle() {
    Column(modifier = Modifier.background(Color(0xFFF5F5F5))) {
        CellTitle(title = "分组标题")
    }
}

@Preview
@Composable
fun PreviewCellGroup() {
    Column(modifier = Modifier.background(Color(0xFFF5F5F5))) {
        // 基本用法
        CellGroup(title = "基本用法") {
            Cell(title = "单元格", value = "内容")
            Cell(title = "单元格", value = "内容", label = "描述信息")
        }
        // 无边框
        CellGroup(title = "无边框", border = false) {
            Cell(title = "单元格", value = "内容")
            Cell(title = "单元格", value = "内容")
        }
        // 圆角卡片风格
        CellGroup(title = "圆角卡片(inset)", inset = true) {
            Cell(title = "单元格", value = "内容")
            Cell(title = "单元格", value = "内容")
        }
        // 圆角卡片 + 无边框
        CellGroup(title = "圆角卡片 + 无边框", inset = true, border = false) {
            Cell(title = "单元格", value = "内容")
            Cell(title = "单元格", value = "内容")
        }
    }
}

@Preview
@Composable
fun PreviewCell() {
    Column(modifier = Modifier.background(Color(0xFFF5F5F5))) {
        Cell(title = "单元格", value = "内容")
        Cell(title = "带箭头", link = true, onClick = {})
        Cell(title = "必填项", required = true, value = "内容")
        Cell(title = "带描述", value = "内容", label = "这是一段描述信息")
        Cell(title = "向上箭头", link = true, arrowDirection = CellArrowDirection.UP, onClick = {})
        Cell(title = "禁用点击", value = "无点击事件")
    }
}

@Preview
@Composable
fun PreviewCellRow() {
    Column(modifier = Modifier.background(Color(0xFFF5F5F5))) {
        CellGroup {
            CellRow(
                contentPaddingValues = PaddingValues(horizontal = defaultPaddingSize, vertical = 12.dp)
            ) {
                Text(text = "左侧内容", style = AppText.Normal.Title.default)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "右侧内容", style = AppText.Normal.Body.default)
            }
        }
    }
}
