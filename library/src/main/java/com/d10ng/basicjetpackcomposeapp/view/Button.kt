package com.d10ng.basicjetpackcomposeapp.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.compose.AppShape
import com.d10ng.basicjetpackcomposeapp.compose.AppText

@Preview
@Composable
fun Button_Test() {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp)
    ) {
        NoPaddingButton(onClick = {  }) {
            Text(text = "无内间距按钮")
        }
        MiniButton("小按钮", {})
        Button(onClick = { }) {
            Text(text = "Button")
        }
        OutlinedButton(onClick = { }) {
            Text(text = "OutlinedButton")
        }
        Text(text = "TextButton", style = AppText.Bold.OnSecondary.v16)
        SolidButton {
            Text(text = "实心按钮")
        }
        SolidButtonWithText(text = "实心按钮带文本")
        HollowButton {
            Text(text = "空心按钮")
        }
        HollowButtonWithText(text = "空心按钮带文本")
        /*HollowButtonWithImageText(text = "空心按钮带文本和左侧图标", imageId = R.mipmap.ic_launcher)*/
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NoPaddingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    val contentColor by colors.contentColor(enabled)
    Surface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        color = colors.backgroundColor(enabled).value,
        contentColor = contentColor.copy(alpha = 1f),
        border = border,
        elevation = elevation?.elevation(enabled, interactionSource)?.value ?: 0.dp,
        interactionSource = interactionSource
    ) {
        CompositionLocalProvider(LocalContentAlpha provides contentColor.alpha) {
            ProvideTextStyle(
                value = MaterialTheme.typography.button
            ) {
                Row(
                    Modifier
                        .padding(contentPadding),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            }
        }
    }
}

@Composable
fun MiniButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 12.sp,
    fontColor: Color = AppColor.On.secondary,
    disabledFontColor: Color = AppColor.Text.hint,
    shape: Shape = AppShape.RC.v8,
    background: Color = AppColor.System.secondary,
    disabledBackground: Color = AppColor.System.secondaryVariant,
    enabled: Boolean = true,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
) {
    NoPaddingButton(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = background,
            disabledBackgroundColor = disabledBackground
        ),
        border = border,
        elevation = null,
        contentPadding = contentPadding
    ) {
        Text(
            text = text,
            style = AppText.Medium.OnSecondary.v12,
            color = if (enabled) fontColor else disabledFontColor,
            fontSize = fontSize
        )
    }
}

/**
 * 实心按钮
 * @param modifier Modifier
 * @param shape Shape
 * @param colors ButtonColors
 * @param enabled Boolean
 * @param onClick Function0<Unit>
 * @param content [@androidx.compose.runtime.Composable] [@kotlin.ExtensionFunctionType] Function1<RowScope, Unit>
 */
@Composable
fun SolidButton(
    modifier: Modifier = Modifier,
    shape: Shape = AppShape.RC.v4,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = AppColor.System.secondary,
        disabledBackgroundColor = AppColor.System.secondaryVariant,
        contentColor = AppColor.On.secondary,
        disabledContentColor = AppColor.On.secondary
    ),
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    content: @Composable RowScope.() -> Unit
) {
    Button(
        modifier = modifier,
        shape = shape,
        colors = colors,
        enabled = enabled,
        onClick = onClick,
        content = content,
        elevation = null
    )
}

/**
 * 实心按钮带文本
 * @param modifier Modifier
 * @param shape Shape
 * @param colors ButtonColors
 * @param enabled Boolean
 * @param onClick Function0<Unit>
 * @param text String
 */
@Composable
fun SolidButtonWithText(
    modifier: Modifier = Modifier,
    shape: Shape = AppShape.RC.v4,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = AppColor.System.secondary,
        disabledBackgroundColor = AppColor.System.secondaryVariant,
        contentColor = AppColor.On.secondary,
        disabledContentColor = AppColor.On.secondary
    ),
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    text: String
) {
    SolidButton(
        modifier = modifier,
        shape = shape,
        colors = colors,
        enabled = enabled,
        onClick = onClick
    ) {
        Text(text = text, color = colors.contentColor(enabled = enabled).value, style = AppText.Bold.OnSecondary.v16)
    }
}

/**
 * 空心按钮
 * @param modifier Modifier
 * @param shape Shape
 * @param colors ButtonColors
 * @param border BorderStroke
 * @param enabled Boolean
 * @param onClick Function0<Unit>
 * @param content [@androidx.compose.runtime.Composable] [@kotlin.ExtensionFunctionType] Function1<RowScope, Unit>
 */
@Composable
fun HollowButton(
    modifier: Modifier = Modifier,
    shape: Shape = AppShape.RC.v4,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = Color.Transparent,
        disabledBackgroundColor = Color.Transparent,
        contentColor = AppColor.System.secondary,
        disabledContentColor = AppColor.System.secondary
    ),
    border: BorderStroke = BorderStroke(1.dp, AppColor.System.secondary),
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    content: @Composable RowScope.() -> Unit
) {
    OutlinedButton(
        modifier = modifier,
        shape = shape,
        colors = colors,
        border = border,
        enabled = enabled,
        onClick = onClick,
        content = content
    )
}

/**
 * 空心按钮带文本
 * @param modifier Modifier
 * @param shape Shape
 * @param colors ButtonColors
 * @param border BorderStroke
 * @param enabled Boolean
 * @param onClick Function0<Unit>
 * @param text String
 */
@Composable
fun HollowButtonWithText(
    modifier: Modifier = Modifier,
    shape: Shape = AppShape.RC.v4,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = Color.Transparent,
        disabledBackgroundColor = Color.Transparent,
        contentColor = AppColor.System.secondary,
        disabledContentColor = AppColor.System.secondary
    ),
    border: BorderStroke = BorderStroke(1.dp, AppColor.System.secondary),
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    text: String
) {
    HollowButton(
        modifier = modifier,
        shape = shape,
        colors = colors,
        border = border,
        enabled = enabled,
        onClick = onClick
    ) {
        Text(text = text, color = colors.contentColor(enabled = enabled).value, style = AppText.Bold.Secondary.v16)
    }
}

/**
 * 空心按钮带文本和左侧图标
 * @param modifier Modifier
 * @param shape Shape
 * @param colors ButtonColors
 * @param border BorderStroke
 * @param enabled Boolean
 * @param onClick Function0<Unit>
 * @param imageId Int
 * @param text String
 */
@Composable
fun HollowButtonWithImageText(
    modifier: Modifier = Modifier,
    shape: Shape = AppShape.RC.v4,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = Color.Transparent,
        disabledBackgroundColor = Color.Transparent,
        contentColor = AppColor.System.secondary,
        disabledContentColor = AppColor.System.secondary
    ),
    border: BorderStroke = BorderStroke(1.dp, AppColor.System.secondary),
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    imageId: Int,
    text: String
) {
    HollowButton(
        modifier = modifier,
        shape = shape,
        colors = colors,
        border = border,
        enabled = enabled,
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = imageId),
                contentDescription = text,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(18.dp),
                alignment = Alignment.Center,
                colorFilter = ColorFilter.tint(colors.contentColor(enabled = enabled).value)
            )
            Text(text = text, color = colors.contentColor(enabled = enabled).value, style = AppText.Bold.Secondary.v16)
        }
    }
}

@Composable
fun DialogSureButton(
    modifier: Modifier = Modifier,
    text: String = "确定",
    color: Color = AppColor.On.secondary,
    backgroundColor: Color = AppColor.System.secondary,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = AppShape.RC.v8,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            disabledBackgroundColor = backgroundColor,
            contentColor = color,
            disabledContentColor = color
        ),
        elevation = null,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            style = AppText.Normal.OnSecondary.v14,
            color = color
        )
    }
}

@Composable
fun DialogCancelButton(
    modifier: Modifier = Modifier,
    text: String = "取消",
    color: Color = AppColor.Text.body,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        shape = AppShape.RC.v8,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            disabledBackgroundColor = Color.Transparent,
            contentColor = color,
            disabledContentColor = color
        ),
        border = BorderStroke(1.dp, color),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            style = AppText.Normal.Body.v14,
            color = color
        )
    }
}