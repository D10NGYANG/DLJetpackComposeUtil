package com.d10ng.compose.ui.base

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.d10ng.compose.resources.Res
import com.d10ng.compose.resources.ic_false_102
import com.d10ng.compose.resources.ic_success_102
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.utils.BackHandler
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * 轻提示组件
 * @Author d10ng
 * @Date 2023/9/4 10:07
 */

enum class ToastPosition(val contentAlignment: Alignment) {
    Top(Alignment.TopCenter),
    Center(Alignment.Center),
    Bottom(Alignment.BottomCenter)
}

enum class ToastType(val iconResource: DrawableResource?) {
    Normal(null),
    Success(Res.drawable.ic_success_102),
    Fail(Res.drawable.ic_false_102)
}

/**
 * 轻提示
 * @param text String 提示文本
 * @param position ToastPosition 显示位置
 * @param forbidClick Boolean 是否禁止点击
 * @param type ToastType 提示类型
 */
@Composable
fun Toast(
    text: String,
    position: ToastPosition = ToastPosition.Center,
    forbidClick: Boolean = false,
    type: ToastType = ToastType.Normal
) {
    if (type == ToastType.Normal) {
        NormalToast(text = text, position = position)
    } else {
        IconToast(text = text, forbidClick = forbidClick, icon = {
            Icon(
                painter = painterResource(resource = type.iconResource!!),
                contentDescription = type.name,
                tint = Color.White,
                modifier = Modifier.size(50.dp)
            )
        })
    }
}

@Composable
fun NormalToast(
    text: String,
    position: ToastPosition = ToastPosition.Center
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(0.7f),
            contentAlignment = position.contentAlignment
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.7f), shape = AppShape.RC.v8)
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun IconToast(
    text: String,
    forbidClick: Boolean = false,
    icon: @Composable () -> Unit,
) {
    var modifier = Modifier
        .fillMaxSize()
    if (forbidClick) {
        modifier = modifier.pointerInput(Unit) {
            // 拦截外部的点击
            detectTapGestures { }
        }
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .size(150.dp)
                .background(Color.Black.copy(alpha = 0.7f), shape = AppShape.RC.v8),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            icon()
            if (text.isNotEmpty()) Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
    if (forbidClick) {
        BackHandler(true) {}
    }
}

@Composable
fun LoadingToast(
    text: String = "",
) {
    IconToast(text = text, forbidClick = true, icon = {
        CircularProgressIndicator(
            color = Color.White,
            strokeCap = StrokeCap.Round,
            strokeWidth = 1.5.dp
        )
    })
}