package com.d10ng.compose.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d10ng.compose.ui.base.LoadingToast
import com.d10ng.compose.ui.base.Toast
import com.d10ng.compose.ui.base.ToastPosition
import com.d10ng.compose.ui.base.ToastType
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * 弹窗、toast、snackBar等组件的管理模型
 * @Author d10ng
 * @Date 2023/9/4 10:10
 */
class UiViewModel : ViewModel(), IUiViewModel {

    // toast
    private val toastFlow = MutableStateFlow("")
    private val positionFlow = MutableStateFlow(ToastPosition.Center)
    private val toastTypeFlow = MutableStateFlow(ToastType.Normal)
    private var cancelToastJob: Job? = null

    // loading
    private val loadingFlow = MutableStateFlow(false)
    private val loadingTextFlow = MutableStateFlow("")

    @Composable
    fun Init() {
        val toast by toastFlow.collectAsState()
        val position by positionFlow.collectAsState()
        val toastType by toastTypeFlow.collectAsState()
        if (toast.isNotEmpty()) {
            Toast(text = toast, position = position, type = toastType)
        }

        val loading by loadingFlow.collectAsState()
        val loadingText by loadingTextFlow.collectAsState()
        if (loading) {
            LoadingToast(text = loadingText)
        }
    }

    override fun showToast(msg: String, duration: Long, position: ToastPosition) {
        toastFlow.value = msg
        positionFlow.value = position
        toastTypeFlow.value = ToastType.Normal
        cancelToastJob?.cancel()
        cancelToastJob = viewModelScope.launch {
            delay(duration)
            toastFlow.value = ""
        }
    }

    override fun showSuccessToast(msg: String, duration: Long) {
        toastFlow.value = msg
        toastTypeFlow.value = ToastType.Success
        cancelToastJob?.cancel()
        cancelToastJob = viewModelScope.launch {
            delay(duration)
            toastFlow.value = ""
        }
    }

    override fun showFailToast(msg: String, duration: Long) {
        toastFlow.value = msg
        toastTypeFlow.value = ToastType.Fail
        cancelToastJob?.cancel()
        cancelToastJob = viewModelScope.launch {
            delay(duration)
            toastFlow.value = ""
        }
    }

    override fun showLoading(text: String) {
        loadingTextFlow.value = text
        loadingFlow.value = true
    }

    override fun hideLoading() {
        loadingFlow.value = false
    }
}