package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.compose.view.Input

@Composable
fun InputScreen(onBack: () -> Unit = {}) {
    var basicValue by remember { mutableStateOf("") }
    var focusValue by remember { mutableStateOf("") }
    var requestFocus by remember { mutableStateOf(false) }
    var focusStatus by remember { mutableStateOf("未聚焦") }
    var passwordValue by remember { mutableStateOf("compose123") }
    var passwordVisible by remember { mutableStateOf(false) }
    var imeStatus by remember { mutableStateOf("等待键盘操作") }
    var notesValue by remember { mutableStateOf("用于检查窄屏换行和多行编辑体验的较长文本。") }
    var centeredValue by remember { mutableStateOf("居中文本") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        NavBar(title = "Input 输入原语", onClickBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            InputSection("基础输入与占位符") {
                InputSurface {
                    Input(
                        value = basicValue,
                        onValueChange = { basicValue = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { contentDescription = "基础输入" },
                        placeholder = "请输入内容",
                        singleLine = true
                    )
                }
                InputStatus("当前内容：${basicValue.ifEmpty { "空" }}")
            }

            InputSection("焦点控制") {
                InputSurface {
                    Input(
                        value = focusValue,
                        onValueChange = { focusValue = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { contentDescription = "焦点输入" }
                            .onFocusChanged { state ->
                                focusStatus = if (state.isFocused) "已聚焦" else "已失焦"
                                if (state.isFocused) requestFocus = false
                            },
                        placeholder = "点击按钮后聚焦",
                        singleLine = true,
                        isFocus = requestFocus
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { requestFocus = true }) {
                        Text("请求焦点")
                    }
                    InputStatus(focusStatus)
                }
            }

            InputSection("只读与禁用") {
                InputSurface {
                    Input(
                        value = "只读内容可以选择",
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { contentDescription = "只读输入" },
                        readOnly = true,
                        singleLine = true
                    )
                }
                InputSurface(containerColor = MaterialTheme.colorScheme.surfaceVariant) {
                    Input(
                        value = "禁用内容不可编辑",
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { contentDescription = "禁用输入" },
                        enabled = false,
                        singleLine = true
                    )
                }
            }

            InputSection("密码与键盘动作") {
                InputSurface {
                    Row {
                        Input(
                            value = passwordValue,
                            onValueChange = { passwordValue = it },
                            modifier = Modifier
                                .weight(1f)
                                .semantics { contentDescription = "密码输入" },
                            visualTransformation = if (passwordVisible) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(onDone = {
                                imeStatus = "已触发 Done"
                            }),
                            singleLine = true
                        )
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) {
                                    Icons.Default.VisibilityOff
                                } else {
                                    Icons.Default.Visibility
                                },
                                contentDescription = if (passwordVisible) "隐藏密码" else "显示密码"
                            )
                        }
                    }
                }
                InputStatus(imeStatus)
            }

            InputSection("多行与长度边界") {
                InputSurface {
                    Input(
                        value = notesValue,
                        onValueChange = { notesValue = it.take(80) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { contentDescription = "多行备注" },
                        placeholder = "请输入备注",
                        minLines = 3,
                        maxLines = 4
                    )
                }
                InputStatus("${notesValue.length}/80")
            }

            InputSection("文本对齐与自定义样式") {
                InputSurface(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    borderColor = MaterialTheme.colorScheme.secondary
                ) {
                    Input(
                        value = centeredValue,
                        onValueChange = { centeredValue = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { contentDescription = "居中样式输入" },
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontSize = 18.sp
                        ),
                        placeholder = "居中占位符",
                        placeholderStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 18.sp
                        ),
                        textAlign = TextAlign.Center,
                        cursorColor = MaterialTheme.colorScheme.secondary,
                        singleLine = true
                    )
                }
            }
        }
    }
}

@Composable
private fun InputSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        content()
    }
}

@Composable
private fun InputSurface(
    containerColor: Color = MaterialTheme.colorScheme.surface,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(containerColor, RoundedCornerShape(4.dp))
            .border(1.dp, borderColor, RoundedCornerShape(4.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        content()
    }
}

@Composable
private fun InputStatus(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
