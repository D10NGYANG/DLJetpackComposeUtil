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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Divider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.d10ng.compose.R
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.datelib.curTime
import com.d10ng.datelib.isToday
import com.d10ng.datelib.isYesterday
import com.d10ng.datelib.toDateStr

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

@Preview(showBackground = true)
@Composable
private fun ListItem_Test() {
    Column(modifier = Modifier.fillMaxWidth()) {
        ListItem(
            iconId = R.drawable.ic_baseline_admin_panel_settings_24,
            iconSizeDp = 32.dp,
            title = "测试标题",
            note = "描述信息ABCDEFG1234567。",
            right = "版本1.2.3(11)"
        )
        Divider()
        ListItem(
            iconId = R.drawable.ic_baseline_admin_panel_settings_24,
            title = "测试标题",
            note = "描述信息ABCDEFG1234567。",
            right = "版本1.2.3(11)"
        )
        Divider()
        ListItem(
            iconId = R.drawable.ic_baseline_admin_panel_settings_24,
            title = "测试标题",
            right = "版本1.2.3(11)"
        )
        Divider()
        ListItem(
            iconId = R.drawable.ic_baseline_admin_panel_settings_24,
            title = "测试标题",
            note = "描述信息ABCDEFG1234567。"
        )
        Divider()
        ListItem(
            iconId = R.drawable.ic_baseline_admin_panel_settings_24,
            title = "测试标题",
            right = "版本1.2.3(11)",
            isShowArrow = false
        )
        Divider()
        ListItem(
            title = "测试标题",
            right = "版本1.2.3(11)"
        )
        Divider()
        ListItem(
            title = "测试标题",
            note = "描述信息ABCDEFG1234567。"
        )
        Divider()
        ListItem(
            title = "测试标题",
            isShowTitleStar = true,
            right = "版本1.2.3(11)",
            isShowArrow = false
        )
        Divider()
    }
}

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    iconId: Int? = null,
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
            if (iconId != null) {
                Image(
                    painter = painterResource(id = iconId),
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
                modifier = Modifier.padding(start = if (iconId != null) 16.dp else 0.dp)
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
                            if (iconId != null && isShowTitleStar) 26.dp
                            else if (iconId != null) 16.dp
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
                    painter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_right_24),
                    contentDescription = title,
                    tint = AppColor.On.background
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun ListInputItem_Test() {
    Column(modifier = Modifier.fillMaxWidth()) {
        ListInputItem(
            title = "我是标题",
            value = "",
            onValueChange = {}
        )
        Divider()
        ListInputItem(
            title = "我是标题",
            isShowTitleStar = true,
            value = "",
            onValueChange = {}
        )
        Divider()
        ListInputItem(
            title = "我是标题",
            isShowTitleStar = true,
            note = "描述信息",
            value = "111",
            onValueChange = {}
        )
        Divider()
        ListInputItem(
            title = "我是标题",
            isShowTitleStar = true,
            placeholder = "占位显示内容",
            value = "",
            onValueChange = {}
        )
        Divider()
        ListInputItem(
            title = "我是标题",
            isShowTitleStar = true,
            prefix = "$",
            value = "1",
            onValueChange = {}
        )
        Divider()
        ListInputItem(
            title = "我是标题",
            isShowTitleStar = true,
            suffix = "元",
            value = "1",
            onValueChange = {}
        )
        Divider()
        ListInputItem(
            title = "密码        ",
            isShowTitleStar = true,
            isPassword = true,
            value = "1111111",
            onValueChange = {}
        )
        Divider()
        ListInputItem(
            title = "我是标题",
            isShowTitleStar = true,
            suffix = "元",
            value = "1",
            error = "请输入正确的金额",
            onValueChange = {}
        )
        Divider()
        ListInputItem(
            title = "我是标题",
            isShowTitleStar = true,
            value = "1asdgyasdgashdgahsdgajhdgjhadgjhadgjahgdhjasgdashdgsafdadhfgaiusjkfvajhvfahsdgashdgashdgashdgashjdgashjdgashjd",
            maxLines = 5,
            error = "请输入正确的金额",
            onValueChange = {}
        )
        Divider()
    }
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
    iconId: Int? = null,
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
    inputBackgroundColor: Color = AppColor.System.divider,
    inputBackgroundShape: Shape = AppShape.RC.v4,
    onClickNext: () -> Unit = {},
    onFocus: () -> Unit = {},
    onNoFocus: () -> Unit = {}
) {
    ListItem(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        iconId = iconId,
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

@Preview(showBackground = true)
@Composable
private fun ListMenuItem_Test() {
    Column(modifier = Modifier.fillMaxWidth()) {
        ListMenuItem(
            title = "我是标题",
            value = null,
            menus = listOf(ListItemMenu("item 1", 1))
        )
        Divider()
        ListMenuItem(
            title = "我是标题",
            value = 1,
            menus = listOf(ListItemMenu("item 1", 1)),
            inputWidth = 200.dp
        )
        Divider()
    }
}

@Composable
fun ListMenuItem(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    iconId: Int? = null,
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
    inputBackgroundColor: Color = AppColor.System.divider,
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
        iconId = iconId,
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
                        painter = painterResource(id = if (isShowMenu) R.drawable.ic_baseline_arrow_drop_up_24 else R.drawable.ic_baseline_arrow_drop_down_24) ,
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

@Preview(showBackground = true)
@Composable
private fun ListChatItem_Test() {
    Column(modifier = Modifier.fillMaxWidth()) {
        ListChatItem(
            icon = {
                NameAvatar(name = "杨")
            },
            badgeNumber = 9,
            name = "杨迪龙",
            message = "这是一条测试消息。很长的消息，非常长的中文消息。你能收到吗？",
            time = 1646014315258,
        )
    }
}

@Composable
fun ListChatItem(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    icon: @Composable BoxScope.() -> Unit = {},
    badgeNumber: Int = 0,
    name: String,
    nameStyle: TextStyle = AppText.Normal.Title.v16,
    message: String = "",
    messageStyle: TextStyle = AppText.Normal.Body.v12,
    time: Long = curTime,
    timeStyle: TextStyle = AppText.Normal.Hint.v12,
    onClick: () -> Unit = {}
) {
    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp, 10.dp),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        onClick = onClick,
        icon = {
            Box(
                modifier = Modifier
                    .wrapContentSize()
            ) {
                icon()
                if (badgeNumber > 0) {
                    BadgeText(
                        modifier = Modifier.align(Alignment.TopEnd),
                        number = badgeNumber,
                        size = 22.dp
                    )
                }
            }
        },
        title = {
            Row(
                modifier = Modifier.padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = name, style = nameStyle)
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f))
                Text(
                    text = when {
                        time.isToday() -> time.toDateStr("HH:mm")
                        time.isYesterday() -> "昨天 ${time.toDateStr("HH:mm")}"
                        else -> time.toDateStr("yyyy-MM-dd HH:mm")
                    },
                    style = timeStyle
                )
            }
        },
        note = {
            Text(
                text = message,
                style = messageStyle,
                modifier = Modifier
                    .padding(start = 16.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}