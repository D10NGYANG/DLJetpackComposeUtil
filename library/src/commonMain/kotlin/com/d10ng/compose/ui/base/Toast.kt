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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.d10ng.compose.resources.Res
import com.d10ng.compose.resources.ic_false_102
import com.d10ng.compose.resources.ic_success_102
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * 轻提示显示位置
 * 仅对 [ToastType.Normal] 文字提示生效，图标类提示固定居中显示
 */
enum class ToastPosition(val contentAlignment: Alignment) {
    // 顶部显示
    Top(Alignment.TopCenter),
    // 居中显示（默认）
    Center(Alignment.Center),
    // 底部显示
    Bottom(Alignment.BottomCenter)
}

/**
 * 轻提示类型
 * [Normal] 为纯文字提示，[Success] 和 [Fail] 为带图标的提示，固定居中展示
 */
enum class ToastType(val iconResource: DrawableResource?) {
    // 普通文字提示，无图标
    Normal(null),
    // 成功提示，显示对勾图标
    Success(Res.drawable.ic_success_102),
    // 失败提示，显示叉号图标
    Fail(Res.drawable.ic_false_102)
}

/**
 * 轻提示入口函数
 * 根据 [type] 自动选择纯文字提示（[NormalToast]）或带图标提示（[IconToast]）
 * @param text String 提示文本
 * @param position ToastPosition 显示位置，仅在 [type] 为 [ToastType.Normal] 时生效，默认居中
 * @param forbidClick Boolean 是否禁止穿透点击，仅在 [type] 非 [ToastType.Normal] 时生效，默认 false
 * @param type ToastType 提示类型，默认 [ToastType.Normal]
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
                tint = MaterialTheme.colorScheme.surfaceContainerLowest,
                modifier = Modifier.size(50.dp)
            )
        })
    }
}

/**
 * 纯文字轻提示
 * 在半透明圆角背景上显示文字，可通过 [position] 控制垂直位置
 * @param text String 提示文本
 * @param position ToastPosition 显示位置，默认居中 [ToastPosition.Center]
 */
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
                    .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.7f), shape = AppShape.RC.v8)
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Text(text = text, style = AppText.Normal.Surface.default)
            }
        }
    }
}

/**
 * 带图标的轻提示
 * 在居中的 150×150dp 半透明圆角面板上展示图标和可选文字
 * @param text String 图标下方的文字，为空时不显示，默认为空
 * @param forbidClick Boolean 是否拦截并消费外部触摸事件（防止误操作），默认 false
 * @param icon @Composable () -> Unit 顶部展示的图标内容
 */
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
                .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.7f), shape = AppShape.RC.v8),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            icon()
            if (text.isNotEmpty()) Text(
                text = text,
                style = AppText.Normal.Surface.default,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
    if (forbidClick) {
        val state = rememberNavigationEventState(NavigationEventInfo.None)
        NavigationBackHandler(state, true) {}
    }
}

/**
 * 加载中提示
 * 在居中面板上展示圆形进度动画，默认禁止外部点击穿透，常用于网络请求等耗时操作
 * @param text String 进度动画下方的文字说明，为空时不显示，默认为空
 */
@Composable
fun LoadingToast(
    text: String = "",
) {
    IconToast(text = text, forbidClick = true, icon = {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            strokeCap = StrokeCap.Round,
            strokeWidth = 1.5.dp
        )
    })
}

@Preview
@Composable
fun PreviewNormalToast() {
    Box(modifier = Modifier.fillMaxSize()) {
        NormalToast(text = "这是一条提示信息", position = ToastPosition.Center)
    }
}

@Preview
@Composable
fun PreviewNormalToastTop() {
    Box(modifier = Modifier.fillMaxSize()) {
        NormalToast(text = "顶部提示信息", position = ToastPosition.Top)
    }
}

@Preview
@Composable
fun PreviewNormalToastBottom() {
    Box(modifier = Modifier.fillMaxSize()) {
        NormalToast(text = "底部提示信息", position = ToastPosition.Bottom)
    }
}

@Preview
@Composable
fun PreviewToastSuccess() {
    Box(modifier = Modifier.fillMaxSize()) {
        Toast(text = "操作成功", type = ToastType.Success)
    }
}

@Preview
@Composable
fun PreviewToastFail() {
    Box(modifier = Modifier.fillMaxSize()) {
        Toast(text = "操作失败", type = ToastType.Fail)
    }
}

@Preview
@Composable
fun PreviewLoadingToast() {
    Box(modifier = Modifier.fillMaxSize()) {
        LoadingToast(text = "加载中...")
    }
}
