package com.d10ng.basicjetpackcomposeapp.dialog.builder

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.d10ng.basicjetpackcomposeapp.R
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.compose.AppText
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode

data class RadioDialogBuilder(
    var title: String = "请选择",
    var message: String = "",
    var map: Map<String, Any>,
    var select: String,
    var customItemView: @Composable (Boolean, Pair<String, Any>, () -> Unit) -> Unit = { isSelect, info, onClick ->
        DefaultItemView(isSelect, info, onClick)
    },
    var isRow: Boolean = false,
    var mainAxisSize: SizeMode = SizeMode.Wrap,
    var mainAxisAlignment: FlowMainAxisAlignment = FlowMainAxisAlignment.Start,
    var mainAxisSpacing: Dp = 0.dp,
    var crossAxisAlignment: FlowCrossAxisAlignment = FlowCrossAxisAlignment.Start,
    var crossAxisSpacing: Dp = 0.dp,
    var lastLineMainAxisAlignment: FlowMainAxisAlignment = mainAxisAlignment,
    var onSelect: (Pair<String, Any>) -> Unit
): DialogBuilder() {
    companion object{
        @Composable
        fun DefaultItemView(isSelect: Boolean, info: Pair<String, Any>, onClick: () -> Unit) {
            val iconId = remember(isSelect) {
                if (isSelect) R.drawable.ic_baseline_radio_button_checked_24
                else R.drawable.ic_baseline_radio_button_unchecked_24
            }
            val color = remember(isSelect) {
                if (isSelect) AppColor.System.secondary
                else AppColor.Text.body
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable { onClick.invoke() }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = info.first,
                    style = AppText.Normal.Body.v14,
                    color = color
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = if (isSelect) "选择" else "未选择",
                    modifier = Modifier.size(18.dp),
                    tint = color
                )
            }
        }
    }

    @Composable
    override fun Build() {
        BaseDialogBuilder(
            title = title,
            message = message
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            if (isRow) {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    mainAxisSize = mainAxisSize,
                    mainAxisAlignment = mainAxisAlignment,
                    mainAxisSpacing = mainAxisSpacing,
                    crossAxisAlignment = crossAxisAlignment,
                    crossAxisSpacing = crossAxisSpacing,
                    lastLineMainAxisAlignment = lastLineMainAxisAlignment
                ) {
                    map.forEach { map ->
                        customItemView(map.key == select, map.toPair()) {
                            onSelect.invoke(map.toPair())
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    map.forEach { map ->
                        customItemView(map.key == select, map.toPair()) {
                            onSelect.invoke(map.toPair())
                        }
                    }
                }
            }
        }.Build()
    }
}
