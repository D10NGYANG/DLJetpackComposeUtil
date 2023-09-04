package com.d10ng.compose.dialog.builder

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.d10ng.compose.R
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppText

data class RadioDialogBuilder(
    var title: String = "提示",
    var titleAlign: Alignment.Horizontal = Alignment.Start,
    var titleColor: Color = AppColor.Neutral.title,
    var message: String = "",
    var messageAlign: Alignment.Horizontal = Alignment.Start,
    var messageColor: Color = AppColor.Neutral.body,
    var map: Map<String, Any>,
    var select: String,
    var customItemView: @Composable (Boolean, Pair<String, Any>, () -> Unit) -> Unit = { isSelect, info, onClick ->
        DefaultItemView(isSelect, info, onClick)
    },
    var isRow: Boolean = false,
    var verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    var horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceAround,
    var maxItemsInEachColumn: Int = Int.MAX_VALUE,
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
                if (isSelect) AppColor.Main.primary
                else AppColor.Neutral.body
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

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    override fun Build() {
        BaseDialogBuilder(
            title = title,
            titleAlign = titleAlign,
            titleColor = titleColor,
            message = message,
            messageAlign = messageAlign,
            messageColor = messageColor,
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            if (isRow) {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalArrangement = verticalArrangement,
                    horizontalArrangement = horizontalArrangement,
                    maxItemsInEachRow = maxItemsInEachColumn
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
