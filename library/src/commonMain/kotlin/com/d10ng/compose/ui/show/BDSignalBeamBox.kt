package com.d10ng.compose.ui.show

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.defaultPaddingSize
import kotlin.math.max

private const val MAX_SIGNAL_STRENGTH = 60
private const val MIN_VISIBLE_BEAMS = 10
private val BeamItemWidth = 28.dp
private val LevelAxisWidth = 45.dp

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
 *   信号强度会限制在 0～60，不足 10 项时以无编号的空柱补齐，超过 10 项时可横向滚动
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
    val normalizedBeams = beams.map { (satelliteId, strength) ->
        satelliteId to strength.coerceIn(0, MAX_SIGNAL_STRENGTH)
    }
    val signalSummary = if (normalizedBeams.isEmpty()) {
        "暂无卫星信号"
    } else {
        val strengths = normalizedBeams.map { it.second }
        "${normalizedBeams.size} 路卫星信号，最弱 ${strengths.min()}，最强 ${strengths.max()}"
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .semantics {
                contentDescription = "北斗卫星信号图"
                stateDescription = signalSummary
            }
    ) {
        // 标题
        Title(text = title)
        // 信号展示区域
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth(),
        ) {
            LineLevelView()
            val visibleSize = max(normalizedBeams.size, MIN_VISIBLE_BEAMS)
            val viewportWidth = (maxWidth - LevelAxisWidth).coerceAtLeast(0.dp)
            Box(
                modifier = Modifier
                    .padding(start = LevelAxisWidth)
                    .width(viewportWidth)
                    .horizontalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier
                        .width((BeamItemWidth * visibleSize).coerceAtLeast(viewportWidth))
                        .height(150.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    repeat(visibleSize) { index ->
                        val beam = normalizedBeams.getOrNull(index)
                        BeamItem(
                            satelliteId = beam?.first,
                            value = beam?.second ?: 0,
                            color = color
                        )
                    }
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
    satelliteId: Int?,
    value: Int,
    color: Color
) {
    Column(
        modifier = Modifier
            .width(BeamItemWidth)
            .fillMaxHeight()
            .then(
                if (satelliteId == null) {
                    Modifier.clearAndSetSemantics { }
                } else {
                    Modifier.clearAndSetSemantics {
                        contentDescription = "卫星 $satelliteId，信号 $value/$MAX_SIGNAL_STRENGTH"
                    }
                }
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BoxText(text = satelliteId?.toString().orEmpty())
        Progress(
            progress = value.toFloat() / MAX_SIGNAL_STRENGTH,
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
