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
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.d10ng.compose.resources.Res
import com.d10ng.compose.resources.ic_baseline_arrow_drop_down_24
import com.d10ng.compose.resources.ic_baseline_arrow_drop_up_24
import com.d10ng.compose.resources.ic_baseline_keyboard_arrow_right_24
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    onClick: (() -> Unit)? = null,
    icon: @Composable RowScope.() -> Unit = {},
    title: @Composable ColumnScope.() -> Unit = {},
    note: @Composable ColumnScope.() -> Unit = {},
    center: @Composable BoxScope.() -> Unit = {},
    right: @Composable RowScope.() -> Unit = {},
    end: @Composable RowScope.() -> Unit = {},
    enabled: Boolean = true,
) {
    ListItemLayout(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        enabled = enabled,
        onClick = onClick,
        icon = icon,
        title = title,
        note = note,
        center = center,
        right = right,
        end = end
    )
}

@Composable
private fun ListItemLayout(
    modifier: Modifier,
    horizontalArrangement: Arrangement.Horizontal,
    verticalAlignment: Alignment.Vertical,
    enabled: Boolean,
    onClick: (() -> Unit)?,
    icon: @Composable RowScope.() -> Unit,
    title: @Composable ColumnScope.() -> Unit,
    note: @Composable ColumnScope.() -> Unit,
    center: @Composable BoxScope.() -> Unit,
    right: @Composable RowScope.() -> Unit,
    end: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .then(
                if (onClick == null) Modifier
                else Modifier.clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            ),
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
    enabled: Boolean = true,
) {
    ListItemContent(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        enabled = enabled,
        onClick = onClick,
        iconResource = iconResource,
        iconSizeDp = iconSizeDp,
        iconColorFilter = iconColorFilter,
        title = title,
        titleStyle = titleStyle,
        isShowTitleStar = isShowTitleStar,
        note = note,
        noteStyle = noteStyle,
        right = right,
        rightStyle = rightStyle,
        isShowArrow = isShowArrow,
        center = center
    )
}

@Composable
private fun ListItemContent(
    modifier: Modifier,
    horizontalArrangement: Arrangement.Horizontal,
    verticalAlignment: Alignment.Vertical,
    enabled: Boolean,
    onClick: (() -> Unit)?,
    iconResource: DrawableResource?,
    iconSizeDp: Dp,
    iconColorFilter: ColorFilter?,
    title: String,
    titleStyle: TextStyle,
    isShowTitleStar: Boolean,
    note: String,
    noteStyle: TextStyle,
    right: String,
    rightStyle: TextStyle,
    isShowArrow: Boolean,
    center: @Composable BoxScope.() -> Unit,
) {
    ListItemLayout(
        modifier = modifier.padding(16.dp, 6.dp),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        enabled = enabled,
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
    onNoFocus: () -> Unit = {},
    enabled: Boolean = true,
    readOnly: Boolean = false
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
        isShowArrow = false,
        center = {
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
                error = error,
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
                onNoFocus = onNoFocus,
                enabled = enabled,
                readOnly = readOnly
            )
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
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

    ListItemContent(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        enabled = true,
        onClick = null,
        iconResource = iconResource,
        iconSizeDp = iconSizeDp,
        iconColorFilter = iconColorFilter,
        title = title,
        titleStyle = titleStyle,
        isShowTitleStar = isShowTitleStar,
        note = note,
        noteStyle = noteStyle,
        right = "",
        rightStyle = AppText.Normal.Body.v14,
        isShowArrow = false
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .weight(1f))
            ExposedDropdownMenuBox(
                expanded = isShowMenu,
                onExpandedChange = { isShowMenu = it },
                modifier = inputModifier
                    .padding(start = if (inputWidth == null) 16.dp else 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                        .background(inputBackgroundColor, inputBackgroundShape)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = select?.show ?: placeholder,
                        style = if (select == null) placeholderStyle else textStyle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                    Icon(
                        painter = painterResource(resource = if (isShowMenu) Res.drawable.ic_baseline_arrow_drop_up_24 else Res.drawable.ic_baseline_arrow_drop_down_24) ,
                        contentDescription = null,
                        tint = textStyle.color,
                        modifier = Modifier
                            .padding(start = 8.dp)
                    )
                }
                ExposedDropdownMenu(
                    expanded = isShowMenu,
                    onDismissRequest = { isShowMenu = false },
                    modifier = Modifier.heightIn(max = 200.dp)
                ) {
                    if (menus.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 48.dp)
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = placeholder,
                                style = placeholderStyle,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
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
}
