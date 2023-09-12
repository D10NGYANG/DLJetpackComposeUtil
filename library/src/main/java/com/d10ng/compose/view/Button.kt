package com.d10ng.compose.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText

@Preview
@Composable
fun Button_Test() {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp)
    ) {
        MiniButton("小按钮", {})
        Button(onClick = { }) {
            Text(text = "Button")
        }
        OutlinedButton(onClick = { }) {
            Text(text = "OutlinedButton")
        }
        Text(text = "TextButton", style = AppText.Bold.White.v16)
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

@Composable
fun MiniButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 12.sp,
    fontColor: Color = Color.White,
    disabledFontColor: Color = AppColor.Neutral.hint,
    shape: Shape = AppShape.RC.v8,
    background: Color = AppColor.Main.primary,
    disabledBackground: Color = AppColor.Main.primary,
    enabled: Boolean = true,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = background,
            disabledContainerColor = disabledBackground
        ),
        border = border,
        elevation = null,
        contentPadding = contentPadding
    ) {
        Text(
            text = text,
            style = AppText.Medium.White.v12,
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
        containerColor = AppColor.Main.primary,
        disabledContainerColor = AppColor.Main.primary,
        contentColor = Color.White,
        disabledContentColor = Color.White
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
        containerColor = AppColor.Main.primary,
        disabledContainerColor = AppColor.Main.primary,
        contentColor = Color.White,
        disabledContentColor = Color.White
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
        Text(
            text = text,
            style = AppText.Bold.White.v16
        )
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
        containerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        contentColor = AppColor.Main.primary,
        disabledContentColor = AppColor.Main.primary
    ),
    border: BorderStroke = BorderStroke(1.dp, AppColor.Main.primary),
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
        containerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        contentColor = AppColor.Main.primary,
        disabledContentColor = AppColor.Main.primary
    ),
    border: BorderStroke = BorderStroke(1.dp, AppColor.Main.primary),
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
        Text(
            text = text,
            style = AppText.Bold.Primary.v16
        )
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
        containerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        contentColor = AppColor.Main.primary,
        disabledContentColor = AppColor.Main.primary
    ),
    border: BorderStroke = BorderStroke(1.dp, AppColor.Main.primary),
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
                alignment = Alignment.Center
            )
            Text(
                text = text,
                style = AppText.Bold.Primary.v16
            )
        }
    }
}