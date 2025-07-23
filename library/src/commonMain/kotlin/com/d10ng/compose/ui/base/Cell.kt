package com.d10ng.compose.ui.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import com.d10ng.compose.resources.Res
import com.d10ng.compose.resources.ic_round_forward_16
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.defaultPaddingSize
import com.d10ng.compose.ui.show.HorizontalDivider
import org.jetbrains.compose.resources.painterResource
import tech.annexflow.constraintlayout.compose.ConstraintLayout

/**
 * 单元格组件
 * @Author d10ng
 * @Date 2023/9/4 10:07
 */

/**
 * 单元格标题
 * @param modifier Modifier
 * @param title String 标题
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
 * @param link Boolean 是否展示右侧箭头并开启点击反馈
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
 * @param modifier Modifier 修饰符
 * @param divider Boolean 是否显示分割线
 * @param dividerPaddingValues PaddingValues 分割线内边距
 * @param contentPaddingValues PaddingValues 内容内边距
 * @param content [@androidx.compose.runtime.Composable] [@kotlin.ExtensionFunctionType] Function1<RowScope, Unit> 内容
 */
@Composable
fun CellRow(
    modifier: Modifier = Modifier,
    divider: Boolean = true,
    dividerPaddingValues: PaddingValues = PaddingValues(horizontal = defaultPaddingSize),
    contentPaddingValues: PaddingValues = PaddingValues(0.dp),
    content: @Composable RowScope.() -> Unit
) {
    ConstraintLayout(
        modifier = modifier.fillMaxWidth()
    ) {
        val (row, div) = createRefs()
        Row(
            modifier = Modifier
                .constrainAs(row) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(contentPaddingValues),
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
        }
        if (divider) {
            HorizontalDivider(
                modifier = Modifier
                    .constrainAs(div) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                paddingStart = dividerPaddingValues.calculateStartPadding(LocalLayoutDirection.current),
                paddingEnd = dividerPaddingValues.calculateEndPadding(LocalLayoutDirection.current)
            )
        }
    }
}