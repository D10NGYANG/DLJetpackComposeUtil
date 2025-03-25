package com.d10ng.compose.ui.sheet.builder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.form.TimePicker
import com.d10ng.compose.ui.form.TimePickerMode
import com.d10ng.compose.ui.form.hour
import com.d10ng.compose.ui.form.minute
import com.d10ng.compose.ui.form.second
import com.d10ng.compose.ui.sheet.SheetColumn
import com.d10ng.datelib.createSystemLocalDateTime
import com.d10ng.datelib.nowTimestamp
import kotlinx.coroutines.CoroutineScope

/**
 * 时间选择器构建器
 * @Author d10ng
 * @Date 2023/9/12 11:23
 */
class TimePickerSheetBuilder(
    // 标题
    private val title: String = "请选择",
    // 选中的时间
    private var value: Int = 0,
    // 开始时间
    private val start: Int = 0,
    // 结束时间，包含
    private val endInclude: Int = 86399,
    // 文本样式
    private val textStyle: TextStyle = AppText.Normal.Title.default,
    // 选择器模式
    private val mode: TimePickerMode = TimePickerMode.HMS,
    // 选项文本
    private val itemText: (Int, String) -> String = { _, item -> item },
    // 取消文本
    private val cancelText: String = "取消",
    // 确定文本
    private val confirmText: String = "确定",
    // 取消按钮点击事件，返回true则隐藏弹窗
    private val onCancelClick: suspend CoroutineScope.() -> Boolean = { true },
    // 确定按钮点击事件，返回true则隐藏弹窗
    private val onConfirmClick: suspend CoroutineScope.(Int, List<Int>) -> Boolean = { _, _ -> true },
) : SheetBuilder() {

    /**
     * 设置选择当前时间
     * > 仅在未设置value，并且触发弹窗展示前有效
     * @param timestamp Long 时间戳，默认当前时间，单位毫秒
     */
    fun setCurrentTime(timestamp: Long = nowTimestamp()) {
        val datetime = createSystemLocalDateTime(timestamp)
        this.value = datetime.hour * 3600 + datetime.minute * 60 + datetime.second
    }

    @Composable
    override fun Build() {
        var selected by remember(value) {
            mutableIntStateOf(value)
        }
        var selectedList by remember(value) {
            mutableStateOf(listOf(value.hour(), value.minute(), value.second()))
        }
        SheetColumn {
            // 标题栏
            TitleBar(
                title = title,
                cancelText = cancelText,
                confirmText = confirmText,
                onCancelClick = onCancelClick,
                onConfirmClick = { onConfirmClick(selected, selectedList) }
            )
            // 选项
            TimePicker(
                value = selected,
                onValueChange = { s, l ->
                    selected = s
                    selectedList = l
                },
                start = start,
                endInclude = endInclude,
                textStyle = textStyle,
                mode = mode,
                itemText = itemText
            )
        }
    }
}