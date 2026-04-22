package com.d10ng.compose.ui.show

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.utils.next

/**
 * Popover 气泡弹出框
 * @Author d10ng
 * @Date 2023/9/18 14:19
 */

/**
 * Popover 气泡弹出框
 *
 * 基于 [Popup] 实现的气泡弹出框，自动根据锚点位置计算弹出方向，并渲染对应方向的三角形箭头。
 * 支持亮色与暗色两种主题，可通过 [content] 插槽自定义内容。
 *
 * @param expanded Boolean 是否展开显示气泡弹出框
 * @param onDismissRequest () -> Unit 请求关闭气泡的回调（如点击外部区域时触发）
 * @param offset DpOffset 气泡相对于锚点的偏移量，默认为 [DpOffset.Zero]
 * @param dark Boolean 是否使用暗色主题（深灰底色 + 白色文字），默认为 false（白色底色）
 * @param properties PopupProperties 弹出框窗口属性，默认为 `PopupProperties(focusable = true)`
 * @param content (@Composable () -> Unit) 气泡内部显示的自定义内容插槽
 */
@Composable
fun Popover(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    dark: Boolean = false,
    properties: PopupProperties = PopupProperties(focusable = true),
    content: @Composable () -> Unit
) {
    val expandedStates = remember { MutableTransitionState(false) }
    expandedStates.targetState = expanded

    if (expandedStates.currentState || expandedStates.targetState) {
        var placement by remember { mutableStateOf(PopoverPlacement.BottomCenter) }
        val density = LocalDensity.current
        val popupPositionProvider = remember(offset, density) {
            DropdownMenuPositionProvider(
                offset,
                density
            ) { parentBounds, menuBounds ->
                placement = calculatePlacement(parentBounds, menuBounds)
            }
        }

        val bgColor = remember(dark) {
            if (dark) Color.DarkGray else Color.White
        }
        Popup(
            onDismissRequest = onDismissRequest,
            popupPositionProvider = popupPositionProvider,
            properties = properties
        ) {
            PopoverContent(
                bgColor = bgColor,
                placement = placement,
                content = content
            )
        }
    }
}

/**
 * Popover 气泡弹出框纵向列表
 *
 * 将字符串集合渲染为纵向排列的列表项，相邻列表项之间自动添加分割线（最后一项除外）。
 * 通常作为 [Popover] 的 `content` 参数传入。
 *
 * @param value Set<String> 列表项文本集合，按集合迭代顺序依次渲染
 * @param dark Boolean 是否使用暗色主题，默认为 false
 * @param onClick (String) -> Unit 列表项点击回调，参数为被点击项的文本内容
 */
@Composable
fun PopoverColumnItems(
    value: Set<String>,
    dark: Boolean = false,
    onClick: (String) -> Unit
) {
    Column {
        value.forEachIndexed { index, text ->
            PopoverColumnItem(
                text = text,
                border = index != value.size - 1,
                dark = dark,
                onClick = { onClick(text) }
            )
        }
    }
}

/**
 * Popover 气泡弹出框纵向列表项
 *
 * 单个列表项，包含文本内容和可选的底部分割线，点击整行触发回调。
 * 文字与分割线颜色根据 [dark] 参数自动适配亮色/暗色主题。
 *
 * @param text String 列表项显示的文本内容
 * @param border Boolean 是否在列表项底部显示分割线，默认为 true
 * @param dark Boolean 是否使用暗色主题（白色文字 + 白色分割线），默认为 false
 * @param onClick () -> Unit 点击列表项时的回调
 */
@Composable
fun PopoverColumnItem(
    text: String,
    border: Boolean = true,
    dark: Boolean = false,
    onClick: () -> Unit
) {
    val contentColor = remember(dark) {
        if (dark) Color.White else AppColor.Neutral.title
    }
    val lineColor = remember(dark) {
        if (dark) Color.White.next(-0.5) else AppColor.Neutral.line
    }
    Box(
        modifier = Modifier
            .defaultMinSize(minWidth = 130.dp)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            style = AppText.Normal.Body.default,
            color = contentColor,
            modifier = Modifier.padding(16.dp)
        )
        if (border) {
            HorizontalDivider(
                modifier = Modifier.align(Alignment.BottomCenter),
                color = lineColor,
                thickness = 1.dp,
                paddingStart = 16.dp,
                paddingEnd = 16.dp,
            )
        }
    }
}

// 弹出位置
enum class PopoverPlacement(val shape: (Density) -> GenericShape) {
    // 下方左侧
    BottomStart(
        { density ->
            getShape(
                density,
                { _ -> Point(getSize(34.5.dp, density), 0f) },
                { _ -> Point(getSize(27.dp, density), getSize(10.dp, density)) },
                { _ -> Point(getSize(42.dp, density), getSize(10.dp, density)) }
            )
        }
    ),
    // 下方中间
    BottomCenter(
        { density ->
            getShape(
                density,
                { size -> Point(size.width / 2, 0f) },
                { size -> Point(size.width / 2 - getSize(7.5.dp, density), getSize(10.dp, density)) },
                { size -> Point(size.width / 2 + getSize(7.5.dp, density), getSize(10.dp, density)) }
            )
        }
    ),
    // 下方右侧
    BottomEnd(
        { density ->
            getShape(
                density,
                { size -> Point(size.width - getSize(34.5.dp, density), 0f) },
                { size -> Point(size.width - getSize(42.dp, density), getSize(10.dp, density)) },
                { size -> Point(size.width - getSize(27.dp, density), getSize(10.dp, density)) }
            )
        }
    ),
    // 上方左侧
    TopStart(
        { density ->
            getShape(
                density,
                { size -> Point(getSize(34.5.dp, density), size.height) },
                { size -> Point(getSize(27.dp, density), size.height - getSize(10.dp, density)) },
                { size -> Point(getSize(42.dp, density), size.height - getSize(10.dp, density)) }
            )
        }
    ),
    // 上方中间
    TopCenter(
        { density ->
            getShape(
                density,
                { size -> Point(size.width / 2, size.height) },
                { size -> Point(size.width / 2 - getSize(7.5.dp, density), size.height - getSize(10.dp, density)) },
                { size -> Point(size.width / 2 + getSize(7.5.dp, density), size.height - getSize(10.dp, density)) }
            )
        }
    ),
    // 上方右侧
    TopEnd(
        { density ->
            getShape(
                density,
                { size -> Point(size.width - getSize(34.5.dp, density), size.height) },
                { size -> Point(size.width - getSize(42.dp, density), size.height - getSize(10.dp, density)) },
                { size -> Point(size.width - getSize(27.dp, density), size.height - getSize(10.dp, density)) }
            )
        }
    ),
}

private data class Point(val x: Float, val y: Float)
private fun getShape(density: Density, top: (Size) -> Point, start: (Size) -> Point, end: (Size) -> Point): GenericShape {
    return GenericShape { size, _ ->
        val topPoint = top(size)
        val startPoint = start(size)
        val endPoint = end(size)
        // 画三角形
        moveTo(topPoint.x, topPoint.y)
        lineTo(endPoint.x, endPoint.y)
        lineTo(startPoint.x, startPoint.y)
        lineTo(topPoint.x, topPoint.y)
        // 画矩形
        val margin = getSize(10.dp, density)
        val radius = getSize(8.dp, density)
        addRoundRect(RoundRect(
            left = margin,
            top = margin,
            right = size.width - margin,
            bottom = size.height - margin,
            cornerRadius = CornerRadius(radius, radius)
        ))
        close()
    }
}
private fun getSize(value: Dp, density: Density) = run { with(density) { value.roundToPx().toFloat() } }

// Size defaults.
private val MenuVerticalMargin = 0.dp

@Composable
private fun PopoverContent(
    placement: PopoverPlacement,
    bgColor: Color = Color.White,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val shape = remember(placement, density) {
        placement.shape(density)
    }
    Surface(
        shape = shape,
        color = bgColor,
        tonalElevation = 1.dp,
        shadowElevation = 1.dp
    ) {
        Box(
            modifier = Modifier.padding(10.dp)
        ) {
            content()
        }
    }
}

@Immutable
private data class DropdownMenuPositionProvider(
    val contentOffset: DpOffset,
    val density: Density,
    val onPositionCalculated: (IntRect, IntRect) -> Unit = { _, _ -> }
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        // The min margin above and below the menu, relative to the screen.
        val verticalMargin = with(density) { MenuVerticalMargin.roundToPx() }
        // The content offset specified using the dropdown offset parameter.
        val contentOffsetX = with(density) { contentOffset.x.roundToPx() }
        val contentOffsetY = with(density) { contentOffset.y.roundToPx() }

        // Compute horizontal position.
        val toHCenter = anchorBounds.left + anchorBounds.width / 2 - popupContentSize.width / 2 + contentOffsetX
        val toRight = anchorBounds.left + contentOffsetX - with(density) { 10.dp.roundToPx() }
        val toLeft = anchorBounds.right - contentOffsetX - popupContentSize.width + with(density) { 10.dp.roundToPx() }
        val toDisplayRight = windowSize.width - popupContentSize.width
        val toDisplayLeft = 0
        val x = if (layoutDirection == LayoutDirection.Ltr) {
            sequenceOf(
                toHCenter,
                toRight,
                toLeft,
                // If the anchor gets outside of the window on the left, we want to position
                // toDisplayLeft for proximity to the anchor. Otherwise, toDisplayRight.
                if (anchorBounds.left >= 0) toDisplayRight else toDisplayLeft
            )
        } else {
            sequenceOf(
                toHCenter,
                toLeft,
                toRight,
                // If the anchor gets outside of the window on the right, we want to position
                // toDisplayRight for proximity to the anchor. Otherwise, toDisplayLeft.
                if (anchorBounds.right <= windowSize.width) toDisplayLeft else toDisplayRight
            )
        }.firstOrNull {
            it >= 0 && it + popupContentSize.width <= windowSize.width
        } ?: toLeft

        // Compute vertical position.
        val toBottom = maxOf(anchorBounds.bottom + contentOffsetY, verticalMargin)
        val toTop = anchorBounds.top - contentOffsetY - popupContentSize.height
        val toCenter = anchorBounds.top - popupContentSize.height / 2
        val toDisplayBottom = windowSize.height - popupContentSize.height - verticalMargin
        val y = sequenceOf(toBottom, toTop, toCenter, toDisplayBottom).firstOrNull {
            it >= verticalMargin &&
                    it + popupContentSize.height <= windowSize.height - verticalMargin
        } ?: toTop

        onPositionCalculated(
            anchorBounds,
            IntRect(x, y, x + popupContentSize.width, y + popupContentSize.height)
        )
        return IntOffset(x, y)
    }
}

private fun calculatePlacement(
    parentBounds: IntRect,
    menuBounds: IntRect
): PopoverPlacement {
    if (parentBounds.top >= menuBounds.bottom) {
        // Parent is above the menu.
        return if (parentBounds.topCenter.x == menuBounds.topCenter.x) {
            PopoverPlacement.TopCenter
        } else if (parentBounds.topCenter.x > menuBounds.topCenter.x) {
            PopoverPlacement.TopEnd
        } else {
            PopoverPlacement.TopStart
        }
    } else {
        // Parent is overlapping the menu vertically.
        return if (parentBounds.topCenter.x == menuBounds.topCenter.x) {
            PopoverPlacement.BottomCenter
        } else if (parentBounds.topCenter.x > menuBounds.topCenter.x) {
            PopoverPlacement.BottomEnd
        } else {
            PopoverPlacement.BottomStart
        }
    }
}

@Preview
@Composable
private fun PreviewPopoverColumnItems() {
    Column {
        // 亮色
        Box(modifier = Modifier.background(Color.White)) {
            PopoverColumnItems(
                value = linkedSetOf("选项一", "选项二", "选项三"),
                onClick = {}
            )
        }
        // 暗色
        Box(modifier = Modifier.background(Color.DarkGray)) {
            PopoverColumnItems(
                value = linkedSetOf("选项一", "选项二", "选项三"),
                dark = true,
                onClick = {}
            )
        }
    }
}

@Preview
@Composable
private fun PreviewPopoverColumnItem() {
    Column {
        // 有边框 - 亮色
        Box(modifier = Modifier.background(Color.White)) {
            PopoverColumnItem(text = "有边框", border = true, onClick = {})
        }
        // 无边框 - 亮色
        Box(modifier = Modifier.background(Color.White)) {
            PopoverColumnItem(text = "无边框", border = false, onClick = {})
        }
        // 有边框 - 暗色
        Box(modifier = Modifier.background(Color.DarkGray)) {
            PopoverColumnItem(text = "暗色有边框", border = true, dark = true, onClick = {})
        }
    }
}

@Preview
@Composable
private fun PreviewPopoverContent() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // 下方居中气泡
        PopoverContentPreview(placement = PopoverPlacement.BottomCenter, dark = false)
        // 上方居中气泡
        PopoverContentPreview(placement = PopoverPlacement.TopCenter, dark = false)
        // 暗色气泡
        PopoverContentPreview(placement = PopoverPlacement.BottomCenter, dark = true)
    }
}

@Composable
private fun PopoverContentPreview(placement: PopoverPlacement, dark: Boolean) {
    val bgColor = if (dark) Color.DarkGray else Color.White
    PopoverContent(placement = placement, bgColor = bgColor) {
        PopoverColumnItems(
            value = linkedSetOf("选项一", "选项二", "选项三"),
            dark = dark,
            onClick = {}
        )
    }
}

