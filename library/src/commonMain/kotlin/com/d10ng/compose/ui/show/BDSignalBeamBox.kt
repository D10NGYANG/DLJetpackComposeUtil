package com.d10ng.compose.ui.show

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.defaultPaddingSize
import kotlin.math.max

/**
 * 北斗信号强度展示
 * @Author d10ng
 * @Date 2023/9/12 14:27
 */

/**
 * 北斗信号强度展示
 *
 * 以竖向柱状图的形式展示多颗北斗卫星的信号强度，背景网格线标注「强/中/弱」三个参考等级。
 * 至少展示 10 个信号柱（不足时以强度 0 补齐），每个柱子顶部显示卫星序号。
 * 底部附带使用提示文本。
 *
 * @param beams List<Pair<Int, Int>> 卫星信号列表，每项为 (卫星序号, 信号强度)；
 *   信号强度范围为 0～60，不足 10 项时自动补齐至 10 项显示
 * @param color Color 信号柱的填充颜色，默认为 [AppColor.Main.primary]
 * @param title String 顶部居中标题文本，默认为"北斗通讯卫星信号"
 * @param tips String 底部使用提示文本，默认为北斗卫星使用建议说明
 */
@Composable
fun BDSignalBeamBox(
    beams: List<Pair<Int, Int>>,
    color: Color = AppColor.Main.primary,
    title: String = "北斗通讯卫星信号",
    tips: String = "遮挡物会影响卫星信号的接收，使用时应将终端置于室外空旷开阔的地方，并将天线区朝南。北斗卫星在赤道上空，高纬度地区可再倾斜适当角度以获取更好的卫星信号。"
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        // 标题
        Title(text = title)
        // 信号展示区域
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            LineLevelView()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(start = 45.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                val size = max(beams.size, 10)
                (0 until size).forEach { index ->
                    val value = if (beams.size <= index) 0 to 0 else beams[index]
                    BeamItem(index = value.first, value = value.second, color = color)
                }
            }
        }
        // 提示文本
        Text(
            text = tips,
            style = AppText.Normal.Body.mini,
            modifier = Modifier
                .fillMaxWidth()
                .padding(defaultPaddingSize)
        )
    }
}

@Composable
private fun Title(
    text: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(defaultPaddingSize),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = AppText.Normal.Title.default
        )
    }
}

@Composable
private fun BoxText(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        modifier = modifier.size(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = AppText.Normal.Tips.mini
        )
    }
}

@Composable
private fun CustomDivider(
    modifier: Modifier = Modifier
) {
    HorizontalDivider(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp),
        color = AppColor.Neutral.line
    )
}

@Composable
private fun LineLevelView() {
    BoxText(
        text = "强",
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp)
    )

    CustomDivider(
        modifier = Modifier
            .padding(start = 45.dp, top = 26.dp)
    )

    BoxText(
        text = "中",
        modifier = Modifier
            .padding(top = 78.dp, start = 16.dp)
    )

    CustomDivider(
        modifier = Modifier
            .padding(start = 45.dp, top = 88.dp)
    )

    BoxText(
        text = "弱",
        modifier = Modifier
            .padding(top = 109.dp, start = 16.dp)
    )

    CustomDivider(
        modifier = Modifier
            .padding(start = 45.dp, top = 119.dp)
    )

    CustomDivider(
        modifier = Modifier
            .padding(top = 150.dp)
    )
}

@Composable
private fun BeamItem(
    index: Int,
    value: Int,
    color: Color
) {
    Column(
        modifier = Modifier
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BoxText(text = index.toString())
        Progress(
            progress = value / 60f,
            modifier = Modifier
                .weight(1f)
                .width(12.dp)
                .padding(top = 6.dp),
            color = color
        )
    }
}

@Composable
private fun Progress(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color
) {
    Box(
        modifier = modifier
            .background(AppColor.Neutral.line),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(progress)
                .background(color)
        )
    }
}

@Preview
@Composable
fun PreviewBDSignalBeamBox() {
    BDSignalBeamBox(
        beams = listOf(
            1 to 45,
            2 to 30,
            3 to 58,
            4 to 12,
            5 to 0,
            6 to 50,
            7 to 22,
            8 to 60,
            9 to 38,
            10 to 5
        )
    )
}

@Preview
@Composable
fun PreviewBDSignalBeamBoxEmpty() {
    // 空列表，自动补齐10个信号柱（强度均为0）
    BDSignalBeamBox(beams = emptyList())
}
