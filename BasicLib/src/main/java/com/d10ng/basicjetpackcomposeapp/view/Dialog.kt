package com.d10ng.basicjetpackcomposeapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.d10ng.basicjetpackcomposeapp.bean.DialogBuilder
import com.d10ng.basicjetpackcomposeapp.bean.InputDialogBuilder
import com.d10ng.basicjetpackcomposeapp.bean.WarningDialogBuilder
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.compose.AppShape
import com.d10ng.basicjetpackcomposeapp.compose.AppText

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
fun WarningDialog(
    isShow: Boolean,
    builder: WarningDialogBuilder,
    onDismiss:() -> Unit
) {
    DialogRack(isShow = isShow, onDismiss = onDismiss) {
        DialogTitle(
            text = builder.title,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        DialogMessage(
            text = builder.message,
            modifier = Modifier
                .padding(top = 16.dp)
                .align(Alignment.CenterHorizontally)
        )
        DialogSureButton(
            modifier = Modifier
                .padding(top = 16.dp),
            text = builder.buttonText,
            onClick = builder.onClickButton?: onDismiss
        )
    }
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
                .align(Alignment.Start)
        )
        DialogMessage(
            text = builder.message,
            modifier = Modifier
                .padding(top = 16.dp)
                .align(Alignment.Start)
        )
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
fun InputDialog(
    isShow: Boolean,
    builder: InputDialogBuilder,
    onDismiss:() -> Unit,
    content: @Composable ColumnScope.() -> Unit = {}
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
            builder.title,
            builder.message,
            builder.sureButton,
            builder.cancelButton,
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
        content()
    }
}