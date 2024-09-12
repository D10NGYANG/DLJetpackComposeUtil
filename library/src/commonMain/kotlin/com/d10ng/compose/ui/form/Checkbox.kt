package com.d10ng.compose.ui.form

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.utils.next
import dljetpackcomposeutil.library.generated.resources.Res
import dljetpackcomposeutil.library.generated.resources.ic_round_check_24
import org.jetbrains.compose.resources.painterResource

/**
 * Checkbox 复选框
 * @Author d10ng
 * @Date 2023/11/16 02:34
 */

enum class CheckboxType(val shape: Shape) {
    // 方形
    SQUARE(AppShape.RC.v4),
    // 圆形
    CIRCLE(AppShape.RC.Cycle)
}

/**
 * 复选框
 * @param modifier Modifier 外部传入的修饰符
 * @param checked Boolean 是否选中
 * @param onCheckedChange Function1<Boolean, Unit> 选中状态切换
 * @param disabled Boolean 是否禁用
 * @param size Dp 大小
 * @param type CheckboxType 复选框类型
 * @param activeColor Color 选中颜色
 * @param inactiveColor Color 未选中颜色
 * @param disabledColor Color 禁用颜色
 */
@Composable
fun Checkbox(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit = {},
    disabled: Boolean = false,
    size: Dp = 22.dp,
    type: CheckboxType = CheckboxType.SQUARE,
    activeColor: Color = AppColor.Main.primary,
    inactiveColor: Color = AppColor.Neutral.tips,
    disabledColor: Color = AppColor.Neutral.hint,
) {
    val bgColor = remember(disabled, disabledColor, checked, activeColor, inactiveColor) {
        when {
            disabled && checked -> disabledColor
            disabled -> disabledColor.next(0.5)
            checked -> activeColor
            else -> Color.Transparent
        }
    }
    Box(modifier = modifier.size(size)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor, type.shape)
                .clip(type.shape)
                .then(
                    if (checked.not()) Modifier
                        .border(1.dp, if (disabled) disabledColor else inactiveColor, type.shape)
                    else Modifier
                )
                .clickable(disabled.not()) { onCheckedChange(checked.not()) },
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                val tintColor = remember {
                    if (disabled) disabledColor.next(0.5) else Color.White
                }
                Icon(
                    painter = painterResource(resource = Res.drawable.ic_round_check_24),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = tintColor,
                )
            }
        }
    }
}