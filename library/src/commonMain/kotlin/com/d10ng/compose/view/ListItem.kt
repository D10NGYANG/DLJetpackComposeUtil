package com.d10ng.compose.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import dljetpackcomposeutil.library.generated.resources.Res
import dljetpackcomposeutil.library.generated.resources.ic_baseline_arrow_drop_down_24
import dljetpackcomposeutil.library.generated.resources.ic_baseline_arrow_drop_up_24
import dljetpackcomposeutil.library.generated.resources.ic_baseline_keyboard_arrow_right_24
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    onClick: () -> Unit = {},
    icon: @Composable RowScope.() -> Unit = {},
    title: @Composable ColumnScope.() -> Unit = {},
    note: @Composable ColumnScope.() -> Unit = {},
    center: @Composable BoxScope.() -> Unit = {},
    right: @Composable RowScope.() -> Unit = {},
    end: @Composable RowScope.() -> Unit = {},
) {
    Row(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .clickable { onClick.invoke() },
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        icon()
        Column {
            title()
            note()
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            content = center
        )
        right()
        end()
    }
}

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    iconResource: DrawableResource? = null,
    iconSizeDp: Dp = 20.dp,
    iconColorFilter: ColorFilter? = null,
    title: String,
    titleStyle: TextStyle = AppText.Normal.Title.v16,
    isShowTitleStar: Boolean = false,
    note: String = "",
    noteStyle: TextStyle = AppText.Normal.Body.v12,
    right: String = "",
    rightStyle: TextStyle = AppText.Normal.Body.v14,
    isShowArrow: Boolean = true,
    onClick: () -> Unit = {},
    center: @Composable BoxScope.() -> Unit = {},
) {
    ListItem(
        modifier = modifier.padding(16.dp, 6.dp),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        onClick = onClick,
        icon = {
            if (iconResource != null) {
                Image(
                    painter = painterResource(resource = iconResource),
                    contentDescription = title,
                    modifier = Modifier
                        .size(iconSizeDp),
                    contentScale = ContentScale.Fit,
                    colorFilter = iconColorFilter
                )
            }
        },
        title = {
            Row(
                modifier = Modifier.padding(start = if (iconResource != null) 16.dp else 0.dp)
            ) {
                if (isShowTitleStar) {
                    Text(text = "*", style = titleStyle, color = Color.Red,
                        modifier = Modifier.width(10.dp))
                }
                Text(text = title, style = titleStyle)
            }
        },
        note = {
            if (note.isNotEmpty()) {
                Text(
                    text = note,
                    style = noteStyle,
                    modifier = Modifier
                        .padding(start =
                            if (iconResource != null && isShowTitleStar) 26.dp
                            else if (iconResource != null) 16.dp
                            else if (isShowTitleStar) 10.dp
                            else 0.dp
                        )
                )
            }
        },
        center = center,
        right = {
            if (right.isNotEmpty()) {
                Text(
                    text = right,
                    style = rightStyle,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        },
        end = {
            if (isShowArrow) {
                Icon(
                    painter = painterResource(resource = Res.drawable.ic_baseline_keyboard_arrow_right_24),
                    contentDescription = title,
                    tint = AppColor.Neutral.tips
                )
            }
        }
    )
}

/** 下拉菜单信息 */
data class ListItemMenu(
    var show: String = "",
    var data: Any? = null
)

@Composable
fun ListInputItem(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    iconResource: DrawableResource? = null,
    iconSizeDp: Dp = 20.dp,
    iconColorFilter: ColorFilter? = null,
    title: String,
    titleStyle: TextStyle = AppText.Normal.Title.v16,
    isShowTitleStar: Boolean = false,
    note: String = "",
    noteStyle: TextStyle = AppText.Normal.Body.v12,
    value: String,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = AppText.Normal.Body.v14,
    placeholder: String = "",
    placeholderStyle: TextStyle = AppText.Normal.Hint.v14,
    prefix: String = "",
    suffix: String = "",
    error: String = "",
    menus: List<ListItemMenu> = emptyList(),
    noMatchMenuTips: String = "无符合选项",
    onMenuSelect: (ListItemMenu) -> Unit = {},
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    isFocus: Boolean = false,
    maxLines: Int = 1,
    inputWidth: Dp? = null,
    inputBackgroundColor: Color = AppColor.Neutral.line,
    inputBackgroundShape: Shape = AppShape.RC.v4,
    onClickNext: () -> Unit = {},
    onFocus: () -> Unit = {},
    onNoFocus: () -> Unit = {}
) {
    ListItem(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        iconResource = iconResource,
        iconSizeDp = iconSizeDp,
        iconColorFilter = iconColorFilter,
        title = title,
        titleStyle = titleStyle,
        isShowTitleStar = isShowTitleStar,
        note = note,
        noteStyle = noteStyle,
        isShowArrow = false
    ) {
        MenuInput(
            modifier = Modifier
                .padding(start = 16.dp),
            value = value,
            onValueChange = onValueChange,
            textStyle = textStyle,
            placeholder = placeholder,
            placeholderStyle = placeholderStyle,
            prefix = prefix,
            suffix = suffix,
            error = error ,
            menus = menus,
            noMatchMenuTips = noMatchMenuTips,
            onMenuSelect = onMenuSelect,
            keyboardType = keyboardType,
            isPassword = isPassword,
            isFocus = isFocus,
            maxLines = maxLines,
            inputWidth = inputWidth,
            inputBackgroundColor = inputBackgroundColor,
            inputBackgroundShape = inputBackgroundShape,
            onClickNext = onClickNext,
            onFocus = onFocus,
            onNoFocus = onNoFocus
        )
    }
}

@Composable
fun ListMenuItem(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    iconResource: DrawableResource? = null,
    iconSizeDp: Dp = 20.dp,
    iconColorFilter: ColorFilter? = null,
    title: String,
    titleStyle: TextStyle = AppText.Normal.Title.v16,
    isShowTitleStar: Boolean = false,
    note: String = "",
    noteStyle: TextStyle = AppText.Normal.Body.v12,
    value: Any?,
    textStyle: TextStyle = AppText.Medium.Body.v14,
    placeholder: String = "请选择",
    placeholderStyle: TextStyle = AppText.Normal.Hint.v14,
    inputWidth: Dp? = null,
    inputBackgroundColor: Color = AppColor.Neutral.line,
    inputBackgroundShape: Shape = AppShape.RC.v4,
    menus: List<ListItemMenu> = emptyList(),
    onMenuSelect: (ListItemMenu) -> Unit = {},
) {
    var isShowMenu by remember {
        mutableStateOf(false)
    }
    val select = remember(value, menus) {
        menus.find { it.data == value }
    }
    val inputModifier = remember(inputWidth) {
        if (inputWidth == null) Modifier.fillMaxWidth()
        else Modifier.width(inputWidth)
    }

    ListItem(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        iconResource = iconResource,
        iconSizeDp = iconSizeDp,
        iconColorFilter = iconColorFilter,
        title = title,
        titleStyle = titleStyle,
        isShowTitleStar = isShowTitleStar,
        note = note,
        noteStyle = noteStyle,
        isShowArrow = false
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .weight(1f))
            Column(
                modifier = inputModifier
                    .padding(start = if (inputWidth == null) 16.dp else 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(inputBackgroundColor, inputBackgroundShape)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Input(
                        value = select?.show?: placeholder,
                        onValueChange = {},
                        textStyle = if (select == null) placeholderStyle else textStyle,
                        enabled = false,
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clickable {
                                isShowMenu = !isShowMenu
                            }
                    )
                    Icon(
                        painter = painterResource(resource = if (isShowMenu) Res.drawable.ic_baseline_arrow_drop_up_24 else Res.drawable.ic_baseline_arrow_drop_down_24) ,
                        contentDescription = if (isShowMenu) "点击隐藏" else "点击显示",
                        tint = textStyle.color,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable { isShowMenu = !isShowMenu }
                    )
                }
                DropdownMenu(
                    expanded = isShowMenu,
                    onDismissRequest = { isShowMenu = false },
                    modifier = Modifier.heightIn(max = 200.dp)
                ) {
                    menus.forEach { item ->
                        DropdownMenuItem(onClick = {
                            onMenuSelect.invoke(item)
                            isShowMenu = false
                        }, text = {
                            Text(item.show, style = textStyle)
                        })
                    }
                }
            }
        }
    }
}