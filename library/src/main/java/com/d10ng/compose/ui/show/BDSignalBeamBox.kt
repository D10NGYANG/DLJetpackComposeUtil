package com.d10ng.compose.ui.show

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppText
import kotlin.math.max

/**
 * 北斗信号强度展示
 * @Author d10ng
 * @Date 2023/9/12 14:27
 */

@Preview
@Composable
private fun BDSignalBeamBoxTest() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        BDSignalBeamBox(
            beams = listOf(
                1 to 15,
                2 to 30,
                3 to 45,
                4 to 60,
                5 to 14,
                6 to 44,
                7 to 12,
                8 to 5,
                9 to 38,
                10 to 1
            )
        )
    }
}

/**
 * 北斗信号强度展示
 * @param beams List<Pair<Int, Int>> 信号强度列表，第一个参数为序号，第二个参数为强度值，范围0-60
 */
@Composable
fun BDSignalBeamBox(
    beams: List<Pair<Int, Int>>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        // 标题
        Title()
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
                    BeamItem(index = value.first, value = value.second)
                }
            }
        }
        // 提示文本
        Text(
            text = "遮挡物会影响卫星信号的接收，使用时应将终端置于室外空旷开阔的地方，并将天线区朝南。北斗卫星在赤道上空，高纬度地区可再倾斜适当角度以获取更好的卫星信号。",
            style = AppText.Normal.Body.mini,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Composable
private fun Title() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "北斗通讯卫星信号",
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
    Divider(
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
    value: Int
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
                .padding(top = 6.dp)
        )
    }
}

@Composable
private fun Progress(
    progress: Float,
    modifier: Modifier = Modifier
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
                .background(AppColor.Main.primary)
        )
    }
}