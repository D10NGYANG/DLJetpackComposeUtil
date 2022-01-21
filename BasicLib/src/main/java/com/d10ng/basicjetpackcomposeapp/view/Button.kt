package com.d10ng.basicjetpackcomposeapp.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.compose.AppText
import com.d10ng.basicjetpackcomposeapp.compose.Shapes

@Preview
@Composable
fun Button_Test() {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp)
    ) {
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
    shape: Shape = Shapes.small,
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
    shape: Shape = Shapes.small,
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
    shape: Shape = Shapes.small,
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
    shape: Shape = Shapes.small,
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
    shape: Shape = Shapes.small,
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