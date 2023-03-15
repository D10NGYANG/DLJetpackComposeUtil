package com.d10ng.compose.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.d10ng.compose.ui.AppText
import com.d10ng.datelib.*
import com.d10ng.compose.utils.up2Length

@Composable
fun IntNumberPicker(
    modifier: Modifier = Modifier,
    label: (Int) -> String = {
        it.toString()
    },
    value: Int,
    onValueChange: (Int) -> Unit,
    dividersColor: Color = Color.Transparent,
    start: Int = Int.MIN_VALUE,
    endInclude: Int = Int.MAX_VALUE,
    step: Int = 1,
    textStyle: TextStyle = AppText.Normal.Title.v16,
) {
    val list = remember(start, endInclude, step) {
        val ls = mutableListOf<Int>()
        for (i in start .. endInclude step step) {
            ls.add(i)
        }
        ls
    }
    ListItemPicker(
        modifier = modifier,
        label = label,
        value = value,
        onValueChange = onValueChange,
        dividersColor = dividersColor,
        list = list,
        textStyle = textStyle
    )
}

@Composable
fun DoubleNumberPicker(
    modifier: Modifier = Modifier,
    label: (Double) -> String = {
        it.toString()
    },
    value: Double,
    onValueChange: (Double) -> Unit,
    dividersColor: Color = Color.Transparent,
    start: Double = Double.MIN_VALUE,
    endInclude: Double = Double.MAX_VALUE,
    step: Double = 1.0,
    textStyle: TextStyle = AppText.Normal.Title.v16,
) {
    val list = remember(start, endInclude, step) {
        val ls = mutableListOf<Double>()
        var index = start
        while (index <= endInclude) {
            ls.add(index)
            index += step
        }
        ls
    }
    ListItemPicker(
        modifier = modifier,
        label = label,
        value = value,
        onValueChange = onValueChange,
        dividersColor = dividersColor,
        list = list,
        textStyle = textStyle
    )
}

@Composable
fun YearPicker(
    modifier: Modifier = Modifier,
    label: (Int) -> String = {
        it.toString().up2Length(4)
    },
    value: Int,
    onValueChange: (Int) -> Unit,
    dividersColor: Color = Color.Transparent,
    start: Int = 1900,
    endInclude: Int = curYear,
    textStyle: TextStyle = AppText.Normal.Title.v16,
) {
    IntNumberPicker(
        modifier = modifier,
        label = label,
        value = value,
        onValueChange = onValueChange,
        dividersColor = dividersColor,
        start = start,
        endInclude = endInclude,
        step = 1,
        textStyle = textStyle
    )
}

@Composable
fun MonthPicker(
    modifier: Modifier = Modifier,
    label: (Int) -> String = {
        it.toString().up2Length(2)
    },
    value: Int,
    onValueChange: (Int) -> Unit,
    dividersColor: Color = Color.Transparent,
    start: Int = 1,
    endInclude: Int = 12,
    textStyle: TextStyle = AppText.Normal.Title.v16,
) {
    IntNumberPicker(
        modifier = modifier,
        label = label,
        value = value,
        onValueChange = onValueChange,
        dividersColor = dividersColor,
        start = start,
        endInclude = endInclude,
        step = 1,
        textStyle = textStyle
    )
}

@Composable
fun DayPicker(
    modifier: Modifier = Modifier,
    label: (Int) -> String = {
        it.toString().up2Length(2)
    },
    value: Int,
    onValueChange: (Int) -> Unit,
    dividersColor: Color = Color.Transparent,
    start: Int = 1,
    endInclude: Int = getDaysOfMonth(),
    textStyle: TextStyle = AppText.Normal.Title.v16,
) {
    IntNumberPicker(
        modifier = modifier,
        label = label,
        value = value,
        onValueChange = onValueChange,
        dividersColor = dividersColor,
        start = start,
        endInclude = endInclude,
        step = 1,
        textStyle = textStyle
    )
}

@Composable
fun DatePicker(
    value: Long,
    onValueChange: (Long) -> Unit,
    start: Long = getDateBy(1900, 1, 1, 0, 0, 0, 0),
    endInclude: Long = curTime,
    dividersColor: Color = Color.Transparent,
    textStyle: TextStyle = AppText.Normal.Title.v16,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        YearPicker(
            value = value.getDateYear(), 
            onValueChange = {
                onValueChange.invoke(value.setDateYear(it).coerceAtLeast(start).coerceAtMost(endInclude))
            },
            dividersColor = dividersColor,
            start = start.getDateYear(),
            endInclude = endInclude.getDateYear(),
            textStyle = textStyle
        )
        Text(text = "年", style = AppText.Normal.Body.v14)
        MonthPicker(
            value = value.getDateMonth(),
            onValueChange = {
                onValueChange.invoke(value.setDateMonth(it).coerceAtLeast(start).coerceAtMost(endInclude))
            },
            dividersColor = dividersColor,
            start = if (start.getDateYear() == value.getDateYear()) start.getDateMonth() else 1,
            endInclude = if (endInclude.getDateYear() == value.getDateYear()) endInclude.getDateMonth() else 12,
            textStyle = textStyle
        )
        Text(text = "月", style = AppText.Normal.Body.v14)
        DayPicker(
            value = value.getDateDay(),
            onValueChange = {
                onValueChange.invoke(value.setDateDay(it).coerceAtLeast(start).coerceAtMost(endInclude))
            },
            dividersColor = dividersColor,
            start = if (start.getDateYear() == value.getDateYear() && start.getDateMonth() == value.getDateMonth()) start.getDateDay() else 1,
            endInclude = if (endInclude.getDateYear() == value.getDateYear() && endInclude.getDateMonth() == value.getDateMonth()) endInclude.getDateDay() else getDaysOfMonth(value.getDateYear(), value.getDateMonth()),
            textStyle = textStyle
        )
        Text(text = "日", style = AppText.Normal.Body.v14)
    }
}

@Composable
fun HourPicker(
    modifier: Modifier = Modifier,
    label: (Int) -> String = {
        it.toString().up2Length(2)
    },
    value: Int,
    onValueChange: (Int) -> Unit,
    dividersColor: Color = Color.Transparent,
    start: Int = 0,
    endInclude: Int = 23,
    textStyle: TextStyle = AppText.Normal.Title.v16,
) {
    IntNumberPicker(
        modifier = modifier,
        label = label,
        value = value,
        onValueChange = onValueChange,
        dividersColor = dividersColor,
        start = start,
        endInclude = endInclude,
        step = 1,
        textStyle = textStyle
    )
}

@Composable
fun MinutePicker(
    modifier: Modifier = Modifier,
    label: (Int) -> String = {
        it.toString().up2Length(2)
    },
    value: Int,
    onValueChange: (Int) -> Unit,
    dividersColor: Color = Color.Transparent,
    start: Int = 0,
    endInclude: Int = 59,
    textStyle: TextStyle = AppText.Normal.Title.v16,
) {
    IntNumberPicker(
        modifier = modifier,
        label = label,
        value = value,
        onValueChange = onValueChange,
        dividersColor = dividersColor,
        start = start,
        endInclude = endInclude,
        step = 1,
        textStyle = textStyle
    )
}

@Composable
fun SecondPicker(
    modifier: Modifier = Modifier,
    label: (Int) -> String = {
        it.toString().up2Length(2)
    },
    value: Int,
    onValueChange: (Int) -> Unit,
    dividersColor: Color = Color.Transparent,
    start: Int = 0,
    endInclude: Int = 59,
    textStyle: TextStyle = AppText.Normal.Title.v16,
) {
    IntNumberPicker(
        modifier = modifier,
        label = label,
        value = value,
        onValueChange = onValueChange,
        dividersColor = dividersColor,
        start = start,
        endInclude = endInclude,
        step = 1,
        textStyle = textStyle
    )
}

@Composable
fun TimePicker(
    hour: Int,
    minute: Int,
    second: Int,
    isShowSecond: Boolean = true,
    onValueChange: (Int, Int, Int) -> Unit,
    dividersColor: Color = Color.Transparent,
    textStyle: TextStyle = AppText.Normal.Title.v16,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HourPicker(
            value = hour,
            onValueChange = {
                onValueChange.invoke(it, minute, second)
            },
            dividersColor = dividersColor,
            textStyle = textStyle
        )
        Text(text = ":", style = AppText.Normal.Body.v14)
        MinutePicker(
            value = minute,
            onValueChange = {
                onValueChange.invoke(hour, it ,second)
            },
            dividersColor = dividersColor,
            textStyle = textStyle
        )
        Text(text = ":", style = AppText.Normal.Body.v14)
        if (isShowSecond) {
            SecondPicker(
                value = second,
                onValueChange = {
                    onValueChange.invoke(hour, minute, it)
                },
                dividersColor = dividersColor,
                textStyle = textStyle
            )
        }
    }
}