package com.d10ng.basicjetpackcomposeapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.d10ng.basicjetpackcomposeapp.bean.*
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.compose.AppShape
import com.d10ng.basicjetpackcomposeapp.compose.AppText
import com.d10ng.text.string.toDString
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun LoadingDialog (
    isShow: Boolean,
    background: Color = Color(0x97454545),
    onDismiss:() -> Unit = {}
) {
    if (isShow) {
        Dialog(
            onDismissRequest = { onDismiss.invoke() },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Box(
                contentAlignment= Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .background(background, shape = AppShape.RC.v8)
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}

@Composable
fun DialogRack(
    isShow: Boolean,
    onDismiss: () -> Unit,
    properties: DialogProperties = DialogProperties(
        dismissOnBackPress = false,
        dismissOnClickOutside = false
    ),
    content: @Composable ColumnScope.() -> Unit
) {
    if (isShow) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = properties
        ) {
            DialogColumn(content = content)
        }
    }
}

@Composable
private fun DialogColumn(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(AppColor.System.surface, AppShape.RC.v16)
            .padding(25.dp),
        content = content
    )
}

@Composable
fun DialogTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = AppText.Bold.Title.v24,
        modifier = modifier
    )
}

@Composable
fun DialogMessage(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = AppText.Normal.Body.v14,
        modifier = modifier
    )
}

@Composable
fun BaseDialog(
    isShow: Boolean,
    builder: DialogBuilder,
    onDismiss:() -> Unit,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    DialogRack(isShow = isShow, onDismiss = onDismiss) {
        DialogTitle(
            text = builder.title,
            modifier = Modifier
                .align(builder.titleAlign)
        )
        if (builder.message.isNotEmpty()) {
            DialogMessage(
                text = builder.message,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(builder.messageAlign)
            )
        }
        content()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            builder.onClickCancel?.let {
                DialogCancelButton(
                    modifier = Modifier.weight(1f),
                    text = builder.cancelButton,
                    onClick = it
                )
            }
            if (builder.onClickCancel != null && builder.onClickSure != null) {
                Spacer(modifier = Modifier.width(16.dp))
            }
            builder.onClickSure?.let {
                DialogSureButton(
                    modifier = Modifier.weight(1f),
                    text = builder.sureButton,
                    onClick = it
                )
            }
        }
    }
}

@Composable
fun WarningDialog(
    isShow: Boolean,
    builder: WarningDialogBuilder,
    onDismiss:() -> Unit
) {
    BaseDialog(
        isShow = isShow,
        builder = DialogBuilder(
            title = builder.title,
            titleAlign = Alignment.CenterHorizontally,
            message = builder.message,
            messageAlign = Alignment.CenterHorizontally,
            sureButton = builder.buttonText,
            onClickSure = builder.onClickButton?: onDismiss
        ),
        onDismiss = onDismiss
    )
}

@Composable
fun InputDialog(
    isShow: Boolean,
    builder: InputDialogBuilder,
    onDismiss:() -> Unit
) {
    val inputValues = remember(builder) {
        mutableStateListOf<String>().apply {
            builder.inputs.forEach { item ->
                this.add(item.initValue)
            }
        }
    }

    val errorTexts = remember(builder) {
        mutableStateListOf<String>().apply {
            builder.inputs.forEach { _ ->
                this.add("")
            }
        }
    }

    BaseDialog(
        isShow = isShow,
        builder = DialogBuilder(
            title = builder.title,
            message = builder.message,
            sureButton = builder.sureButton,
            cancelButton = builder.cancelButton,
            onClickSure = {
                val results = builder.inputs.mapIndexed { index, input ->
                    input.verify.invoke(inputValues[index])
                }
                results.forEachIndexed { index, verify ->
                    errorTexts[index] = verify.errorText
                }
                if (results.find { !it.isOK } == null) {
                    builder.onClickSure.invoke(inputValues)
                }
            },
            onClickCancel = builder.onClickCancel
        ),
        onDismiss = onDismiss
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        builder.inputs.forEachIndexed { index, input ->
            DialogInput(
                input = input,
                value = inputValues[index],
                onValueChange = {
                    inputValues[index] = it
                },
                errorText = errorTexts[index]
            )
        }
    }
}

@Composable
fun RadioDialog(
    isShow: Boolean,
    builder: RadioDialogBuilder,
    onDismiss:() -> Unit
) {
    BaseDialog(
        isShow = isShow,
        builder = DialogBuilder(
            title = builder.title,
            message = builder.message
        ),
        onDismiss = onDismiss
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        if (builder.isRow) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                mainAxisAlignment = FlowMainAxisAlignment.Center
            ) {
                builder.map.forEach { map ->
                    builder.customItemView(map.key == builder.select, map.toPair()) {
                        builder.onSelect.invoke(map.toPair())
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                builder.map.forEach { map ->
                    builder.customItemView(map.key == builder.select, map.toPair()) {
                        builder.onSelect.invoke(map.toPair())
                    }
                }
            }
        }
    }
}

@Composable
fun DatePickerDialog(
    isShow: Boolean,
    builder: DatePickerDialogBuilder,
    onDismiss:() -> Unit
) {
    var value by remember(builder) {
        mutableStateOf(builder.initValue)
    }
    BaseDialog(
        isShow = isShow,
        builder = DialogBuilder(
            title = builder.title,
            message = builder.message,
            sureButton = builder.sureButton,
            cancelButton = builder.cancelButton,
            onClickSure = {
                builder.onClickSure.invoke(value)
            },
            onClickCancel = builder.onClickCancel
        ),
        onDismiss = onDismiss
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        DatePicker(
            value = value,
            onValueChange = {
                value = it
            },
            start = builder.start,
            endInclude = builder.endInclude
        )
    }
}

@Composable
fun TimePickerDialog(
    isShow: Boolean,
    builder: TimePickerDialogBuilder,
    onDismiss:() -> Unit
) {
    var hour by remember(builder) {
        mutableStateOf(builder.hour)
    }
    var minute by remember(builder) {
        mutableStateOf(builder.minute)
    }
    var second by remember(builder) {
        mutableStateOf(builder.second)
    }
    BaseDialog(
        isShow = isShow,
        builder = DialogBuilder(
            title = builder.title,
            message = builder.message,
            sureButton = builder.sureButton,
            cancelButton = builder.cancelButton,
            onClickSure = {
                builder.onClickSure.invoke(hour, minute, second)
            },
            onClickCancel = builder.onClickCancel
        ),
        onDismiss = onDismiss
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        TimePicker(
            hour = hour,
            minute = minute,
            second = second,
            isShowSecond = builder.isShowSecond,
            onValueChange = { h,m,s ->
                hour = h
                minute = m
                second = s
            }
        )
    }
}

@Composable
fun ProgressDialog(
    isShow: Boolean,
    builder: ProgressDialogBuilder,
    onDismiss:() -> Unit
) {
    println("刷新 ProgressDialog $builder")
    BaseDialog(
        isShow = isShow,
        builder = DialogBuilder(
            title = builder.title,
            titleAlign = Alignment.CenterHorizontally,
            message = builder.message,
            messageAlign = Alignment.CenterHorizontally
        ),
        onDismiss = onDismiss
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        if (builder.isShowProgressText) {
            Text(
                text = when(builder.progressTextStyle) {
                    ProgressDialogBuilder.ProgressTextStyle.PROGRESS_AND_MAX -> {
                        "${builder.progress}/${builder.max}"
                    }
                    ProgressDialogBuilder.ProgressTextStyle.PERCENTAGE -> {
                        "${((builder.progress * 100.0) / builder.max).toInt()}%"
                    }
                },
                style = AppText.Normal.Body.v16,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        LinearProgressIndicator(
            progress = (builder.progress * 1.0f) / builder.max,
            modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(top = 16.dp),
            color = AppColor.System.secondary,
            backgroundColor = AppColor.System.divider
        )
    }
}