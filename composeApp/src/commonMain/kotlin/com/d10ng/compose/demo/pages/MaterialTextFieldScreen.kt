package com.d10ng.compose.demo.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.navigation.NavBar

@Composable
fun MaterialTextFieldScreen(onBack: () -> Unit = {}) {
    var basicValue by remember { mutableStateOf("") }
    var emailValue by remember { mutableStateOf("invalid-email") }
    var passwordValue by remember { mutableStateOf("compose123") }
    var passwordVisible by remember { mutableStateOf(false) }
    var amountValue by remember { mutableStateOf("128") }
    var notesValue by remember {
        mutableStateOf("这是一段用于检查窄屏换行和多行编辑体验的较长文本。")
    }
    val emailSupportingText = when {
        emailValue.isBlank() -> "请输入邮箱"
        '@' !in emailValue -> "邮箱格式不正确"
        else -> "邮箱格式有效"
    }
    val emailError = emailValue.isBlank() || '@' !in emailValue

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
    ) {
        NavBar(title = "Material TextField", onClickBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            TextFieldSection("Filled 与 Outlined") {
                TextField(
                    value = basicValue,
                    onValueChange = { basicValue = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("姓名") },
                    placeholder = { Text("请输入姓名") },
                    supportingText = { Text("Filled 默认样式") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = emailValue,
                    onValueChange = { emailValue = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("邮箱") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null)
                    },
                    supportingText = {
                        Text(emailSupportingText)
                    },
                    isError = emailError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true
                )
            }

            TextFieldSection("密码与内容槽") {
                OutlinedTextField(
                    value = passwordValue,
                    onValueChange = { passwordValue = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("密码") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null)
                    },
                    trailingIcon = {
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
                    },
                    visualTransformation = if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true
                )
                TextField(
                    value = amountValue,
                    onValueChange = { amountValue = it.filter(Char::isDigit) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("金额") },
                    prefix = { Text("CNY ") },
                    suffix = { Text(".00") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }

            TextFieldSection("只读与禁用") {
                OutlinedTextField(
                    value = "只读内容可以选择和复制",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("只读") },
                    readOnly = true,
                    singleLine = true
                )
                OutlinedTextField(
                    value = "禁用内容不可编辑",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("禁用") },
                    enabled = false,
                    singleLine = true
                )
            }

            TextFieldSection("多行与长文本") {
                OutlinedTextField(
                    value = notesValue,
                    onValueChange = { notesValue = it.take(120) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("备注") },
                    supportingText = { Text("${notesValue.length}/120") },
                    minLines = 3,
                    maxLines = 5
                )
            }
        }
    }
}

@Composable
private fun TextFieldSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        content()
    }
}
