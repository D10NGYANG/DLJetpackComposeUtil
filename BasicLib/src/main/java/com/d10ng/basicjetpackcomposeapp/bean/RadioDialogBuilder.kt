package com.d10ng.basicjetpackcomposeapp.bean

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.d10ng.basicjetpackcomposeapp.R
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.compose.AppText

data class RadioDialogBuilder(
    var title: String = "请选择",
    var message: String = "",
    var map: Map<String, Any>,
    var select: String,
    var customItemView: @Composable (Boolean, Pair<String, Any>, () -> Unit) -> Unit = { isSelect, info, onClick ->
        DefaultItemView(isSelect, info, onClick)
    },
    var isRow: Boolean = false,
    var onSelect: (Pair<String, Any>) -> Unit
) {
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
}