package com.d10ng.compose.ui.sheet

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheet
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scrim
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.sheet.builder.SheetBuilder
import com.d10ng.compose.utils.BackHandler

/**
 * 底部弹窗容器
 *
 * 使用 Material 3 官方同树 [BottomSheet] 和 [Scrim] 构建全屏覆盖层，不会创建独立 Dialog 窗口。
 * [SheetBuilder.clickOutsideDismiss] 统一控制所有用户发起的关闭入口：遮罩点击、下滑手势和系统返回。
 * 配置为 `false` 时会消费系统返回但保持弹窗展示；代码仍可调用 [SheetBuilder.dismiss] 主动关闭。
 *
 * @param builder [SheetBuilder] 弹窗构建器实例，负责提供弹窗内容和控制弹窗的显隐状态
 * @Author d10ng
 * @Date 2023/9/8 18:00
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Sheet(
    builder: SheetBuilder
) {
    val visible by builder.visibleFlow.collectAsState()
    val userDismissEnabled = builder.clickOutsideDismiss
    val sheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded)
    )
    val scrimAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "SheetScrimAlpha"
    )
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(builder) {
        // 展示 Sheet 时收起虚拟键盘，并清除底层输入框焦点，避免键盘遮挡或再次弹出。
        focusManager.clearFocus(force = true)
        keyboardController?.hide()
    }
    LaunchedEffect(visible) {
        if (visible) {
            sheetState.show()
        } else {
            sheetState.hide()
            UiViewModelManager.hideSheet(builder)
        }
    }
    val dismissPolicy = resolveSheetDismissPolicy(
        userDismissEnabled = userDismissEnabled,
        visible = visible,
        sheetStateVisible = sheetState.isVisible,
    )
    // 官方返回处理器尚未接管或已经退出时，主动拦截事件，避免返回穿透到底层页面。
    BackHandler(enabled = dismissPolicy.compatibilityBackHandlerEnabled) {
        if (dismissPolicy.dismissOnCompatibilityBack) builder.dismiss()
    }
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val sheetMaxHeight = (maxHeight - builder.topMargin()).coerceAtLeast(0.dp)
        Scrim(
            contentDescription = if (dismissPolicy.dismissOnScrimClick) "关闭弹窗" else null,
            modifier = if (dismissPolicy.dismissOnScrimClick) {
                Modifier
            } else {
                Modifier.clearAndSetSemantics { }
            },
            onClick = {
                if (dismissPolicy.dismissOnScrimClick) builder.dismiss()
            },
            alpha = { scrimAlpha },
            color = Color.Black.copy(alpha = 0.4f)
        )
        BottomSheet(
            state = sheetState,
            onDismissRequest = builder::dismiss,
            maxWidth = Dp.Unspecified,
            gesturesEnabled = dismissPolicy.gesturesEnabled,
            backHandlerEnabled = dismissPolicy.officialBackHandlerEnabled,
            dragHandle = null,
            contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
            shape = RectangleShape,
            containerColor = Color.Transparent,
            contentColor = LocalContentColor.current,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = sheetMaxHeight)
            ) {
                builder.Build()
            }
        }
    }
}

/**
 * 底部弹窗内容盒子
 *
 * 为弹窗内容提供统一的白色圆角背景容器，使用 [BoxScope] 布局。
 * 默认顶部左右圆角 12dp，适合作为底部弹窗最外层容器使用。
 *
 * @param color Color 容器背景色，默认白色
 * @param shape RoundedCornerShape 容器圆角形状，默认顶部左右 12dp 圆角
 * @param content [@Composable] BoxScope 内容插槽
 */
@Composable
fun SheetBox(
    color: Color = Color.White,
    shape: RoundedCornerShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color, shape)
            .clip(shape),
    ) {
        content()
    }
}

/**
 * 底部弹窗内容列布局
 *
 * 基于 [SheetBox] 的纵向布局容器，内部使用 [Column] 排列子组件，
 * 并自动应用 [navigationBarsPadding] 以适配系统导航栏。
 * 适用于弹窗内容需要纵向排列的场景（如标题栏 + 选择器）。
 *
 * @param color Color 容器背景色，默认白色
 * @param shape RoundedCornerShape 容器圆角形状，默认顶部左右 12dp 圆角
 * @param content [@Composable] ColumnScope 内容插槽
 */
@Composable
fun SheetColumn(
    color: Color = Color.White,
    shape: RoundedCornerShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    SheetBox(
        color = color,
        shape = shape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSheetBox() {
    Box(modifier = Modifier.background(Color(0xFFEEEEEE))) {
        SheetBox {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "SheetBox 内容区域", style = AppText.Normal.Title.default)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSheetColumn() {
    Box(modifier = Modifier.background(Color(0xFFEEEEEE))) {
        SheetColumn {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "标题", style = AppText.Bold.Title.large)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "SheetColumn 内容区域", style = AppText.Normal.Title.default)
            }
        }
    }
}
