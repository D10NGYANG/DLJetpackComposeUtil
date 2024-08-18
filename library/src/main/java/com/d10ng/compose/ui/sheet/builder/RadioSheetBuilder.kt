package com.d10ng.compose.ui.sheet.builder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.form.RadioCell
import com.d10ng.compose.ui.sheet.SheetColumn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * 单选面板构建器
 * @Author d10ng
 * @Date 2023/9/8 18:08
 */
class RadioSheetBuilder<T>(
    // 标题
    private val title: String = "请选择",
    // 选项
    private val items: Set<T>,
    // 选项文本
    private val itemText: (T) -> String = { it.toString() },
    // 选中的项
    private val selectedItem: T = items.first(),
    // 是否显示确定和取消按钮
    private val showButton: Boolean = true,
    // 取消文本
    private val cancelText: String = "取消",
    // 确定文本
    private val confirmText: String = "确定",
    // 取消按钮点击事件，返回true则隐藏弹窗
    private val onCancelClick: suspend CoroutineScope.() -> Boolean = { true },
    // 确定按钮点击事件，返回true则隐藏弹窗
    private val onConfirmClick: suspend CoroutineScope.(T) -> Boolean = { true },
): SheetBuilder() {

    @Composable
    override fun Build() {
        var selected by remember(selectedItem) {
            mutableStateOf(selectedItem)
        }
        SheetColumn {
            // 标题栏
            if (showButton) {
                TitleBar(
                    title = title,
                    cancelText = cancelText,
                    confirmText = confirmText,
                    onCancelClick = onCancelClick,
                    onConfirmClick = { onConfirmClick(selected) }
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        style = AppText.Bold.Title.large,
                        textAlign = TextAlign.Center
                    )
                }
            }
            // 选项
            val scope = rememberCoroutineScope()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
            ) {
                items.forEachIndexed { index, item ->
                    RadioCell(
                        label = itemText(item),
                        selected = selected == item,
                        onClick = {
                            selected = item
                            if (showButton.not()) {
                                scope.launch { if (onConfirmClick(item)) dismiss() }
                            }
                        },
                        border = index != items.size - 1
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun BuildTest() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray),
        contentAlignment = Alignment.BottomCenter
    ) {
        val builder = RadioSheetBuilder(items = setOf("1", "2"), showButton = false)
        builder.Build()
    }
}