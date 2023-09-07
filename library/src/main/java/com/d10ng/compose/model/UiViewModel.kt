package com.d10ng.compose.model

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d10ng.compose.dialog.Dialog
import com.d10ng.compose.dialog.builder.DialogBuilder
import com.d10ng.compose.ui.base.LoadingToast
import com.d10ng.compose.ui.base.Toast
import com.d10ng.compose.ui.base.ToastPosition
import com.d10ng.compose.ui.base.ToastType
import com.d10ng.compose.view.ErrorBar
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

    // error
    private val errorFlow = MutableStateFlow("")
    private var cancelErrorJob: Job? = null

    // dialog
    private val dialogBuilderFlow: MutableStateFlow<DialogBuilder?> = MutableStateFlow(null)
    private val dialogBuilderMapFlow = MutableStateFlow(mapOf<Int, com.d10ng.compose.ui.dialog.builder.DialogBuilder>())

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

        val error by errorFlow.collectAsState()
        Box(modifier = Modifier.fillMaxWidth()) {
            ErrorBar(error.isNotEmpty(), error)
        }

        val dialogBuilder by dialogBuilderFlow.collectAsState()
        dialogBuilder?.let {
            Dialog(
                onDismiss = {
                    if (it.isClickOutsideDismiss) {
                        hideDialog()
                    }
                },
                contentAlignment = it.contentAlignment
            ) {
                it.Build()
            }
        }

        val dialogBuilderMap by dialogBuilderMapFlow.collectAsState()
        dialogBuilderMap.forEach { (id, builder) ->
            println("test, buildDialog = $id")
            com.d10ng.compose.ui.dialog.Dialog(id = id, builder = builder)
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

    override fun showError(msg: String) {
        errorFlow.value = msg
        cancelErrorJob?.cancel()
        cancelErrorJob = viewModelScope.launch {
            delay(3000)
            errorFlow.value = ""
        }
    }

    override fun showDialog(builder: DialogBuilder) {
        dialogBuilderFlow.value = builder
    }

    override fun hideDialog() {
        dialogBuilderFlow.value = null
    }

    fun showDialog(builder: com.d10ng.compose.ui.dialog.builder.DialogBuilder, id: Int) {
        val map = dialogBuilderMapFlow.value.toMutableMap()
        map[id] = builder
        dialogBuilderMapFlow.value = map
    }

    fun hideDialog(id: Int) {
        val map = dialogBuilderMapFlow.value.toMutableMap()
        map.remove(id)
        dialogBuilderMapFlow.value = map
    }
}