package com.d10ng.compose.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d10ng.compose.ui.base.Toast
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * 弹窗、toast、snackBar等组件的管理模型
 * @Author d10ng
 * @Date 2023/9/4 10:10
 */
class UiViewModel : ViewModel() {

    private val toastFlow = MutableStateFlow<String?>(null)
    private var cancelToastJob: Job? = null

    fun showToast(msg: String, duration: Long = 1500) {
        toastFlow.value = msg
        cancelToastJob?.cancel()
        cancelToastJob = viewModelScope.launch {
            delay(duration)
            toastFlow.value = null
        }
    }

    @Composable
    fun Init() {
        val toast by toastFlow.collectAsState()
        if (toast != null) {
            Toast(text = toast!!)
        }
    }
}