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
import com.d10ng.compose.ui.dialog.Dialog
import com.d10ng.compose.ui.dialog.builder.DialogBuilder
import com.d10ng.compose.ui.feedback.Notify
import com.d10ng.compose.ui.feedback.NotifyType
import com.d10ng.compose.ui.sheet.Sheet
import com.d10ng.compose.ui.sheet.builder.SheetBuilder
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
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

    // notify
    private val notifyFlow = MutableStateFlow("")
    private val notifyTypeFlow = MutableStateFlow(NotifyType.Primary)
    private var cancelNotifyJob: Job? = null

    // dialog
    private val dialogBuilderMapFlow = MutableStateFlow(mapOf<Int, DialogBuilder>())

    // sheet
    private val sheetBuilderListFlow = MutableStateFlow(listOf<SheetBuilder>())

    @Composable
    fun Init() {
        val sheetBuilders by sheetBuilderListFlow.collectAsState()
        sheetBuilders.forEach { Sheet(builder = it) }

        val dialogBuilderMap by dialogBuilderMapFlow.collectAsState()
        dialogBuilderMap.forEach { (id, builder) ->
            Dialog(id = id, builder = builder)
        }

        val notify by notifyFlow.collectAsState()
        val notifyType by notifyTypeFlow.collectAsState()
        if (notify.isNotEmpty()) {
            Notify(text = notify, type = notifyType)
        }

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

    override fun showNotify(type: NotifyType, text: String, duration: Long) {
        notifyFlow.value = text
        notifyTypeFlow.value = type
        cancelNotifyJob?.cancel()
        cancelNotifyJob = viewModelScope.launch {
            delay(duration)
            notifyFlow.value = ""
        }
    }

    override fun showErrorNotify(text: String, duration: Long) {
        showNotify(NotifyType.Error, text, duration)
    }

    override fun showSheet(builder: SheetBuilder) {
        sheetBuilderListFlow.update { list ->
            list + builder
        }
    }

    override fun hideSheet(builder: SheetBuilder) {
        sheetBuilderListFlow.update { list ->
            list.filter { it != builder }
        }
    }

    override fun hideAllSheet() {
        sheetBuilderListFlow.value = listOf()
    }

    fun showDialog(id: Int, builder: DialogBuilder) {
        dialogBuilderMapFlow.update { map ->
            map.toMutableMap().apply {
                put(id, builder)
            }
        }
    }

    fun updateDialog(id: Int, builder: DialogBuilder) {
        if (dialogBuilderMapFlow.value.containsKey(id)) showDialog(id, builder)
    }

    fun hideDialog(id: Int) {
        dialogBuilderMapFlow.update { map ->
            map.filterKeys { it != id }
        }
    }
}