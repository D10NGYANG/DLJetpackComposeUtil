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

    // ---- Toast ----
    // 使用数据类聚合 toast 状态，避免多 Flow 分步赋值产生的中间状态帧
    private data class ToastState(
        val msg: String?,                          // null 表示不显示
        val position: ToastPosition = ToastPosition.Center,
        val type: ToastType = ToastType.Normal
    )
    private val toastStateFlow = MutableStateFlow(ToastState(null))
    private var cancelToastJob: Job? = null

    // ---- Loading ----
    private data class LoadingState(val visible: Boolean, val text: String = "")
    private val loadingStateFlow = MutableStateFlow(LoadingState(false))

    // ---- Notify ----
    private data class NotifyState(
        val text: String?,                         // null 表示不显示
        val type: NotifyType = NotifyType.Primary
    )
    private val notifyStateFlow = MutableStateFlow(NotifyState(null))
    private var cancelNotifyJob: Job? = null

    // ---- Dialog ----
    private val dialogBuilderMapFlow = MutableStateFlow(mapOf<String, DialogBuilder>())

    // ---- Sheet ----
    private val sheetBuilderListFlow = MutableStateFlow(listOf<SheetBuilder>())

    @Composable
    fun Init() {
        SheetLayer()
        DialogLayer()
        NotifyLayer()
        ToastLayer()
        LoadingLayer()
    }

    /** 独立的 Sheet 层，仅在 sheetBuilderListFlow 变化时重组此层 */
    @Composable
    private fun SheetLayer() {
        val sheetBuilders by sheetBuilderListFlow.collectAsState()
        sheetBuilders.forEach { Sheet(builder = it) }
    }

    /** 独立的 Dialog 层，仅在 dialogBuilderMapFlow 变化时重组此层 */
    @Composable
    private fun DialogLayer() {
        val dialogBuilderMap by dialogBuilderMapFlow.collectAsState()
        dialogBuilderMap.forEach { (id, builder) ->
            Dialog(id = id, builder = builder)
        }
    }

    /** 独立的 Notify 层 */
    @Composable
    private fun NotifyLayer() {
        val state by notifyStateFlow.collectAsState()
        val text = state.text
        if (text != null) {
            Notify(text = text, type = state.type)
        }
    }

    /** 独立的 Toast 层 */
    @Composable
    private fun ToastLayer() {
        val state by toastStateFlow.collectAsState()
        val msg = state.msg
        if (msg != null) {
            Toast(text = msg, position = state.position, type = state.type)
        }
    }

    /** 独立的 Loading 层 */
    @Composable
    private fun LoadingLayer() {
        val state by loadingStateFlow.collectAsState()
        if (state.visible) {
            LoadingToast(text = state.text)
        }
    }

    // ---- 内部统一展示 Toast ----
    private fun showToastInternal(msg: String, duration: Long, position: ToastPosition, type: ToastType) {
        // 原子更新：一次赋值，避免分步更新产生中间状态帧
        toastStateFlow.value = ToastState(msg, position, type)
        cancelToastJob?.cancel()
        cancelToastJob = viewModelScope.launch {
            delay(duration)
            toastStateFlow.value = ToastState(null)
        }
    }

    override fun showToast(msg: String, duration: Long, position: ToastPosition) {
        showToastInternal(msg, duration, position, ToastType.Normal)
    }

    override fun showSuccessToast(msg: String, duration: Long) {
        showToastInternal(msg, duration, ToastPosition.Center, ToastType.Success)
    }

    override fun showFailToast(msg: String, duration: Long) {
        showToastInternal(msg, duration, ToastPosition.Center, ToastType.Fail)
    }

    override fun showLoading(text: String) {
        loadingStateFlow.value = LoadingState(true, text)
    }

    override fun hideLoading() {
        loadingStateFlow.value = LoadingState(false)
    }

    override fun showNotify(type: NotifyType, text: String, duration: Long) {
        notifyStateFlow.value = NotifyState(text, type)
        cancelNotifyJob?.cancel()
        cancelNotifyJob = viewModelScope.launch {
            delay(duration)
            notifyStateFlow.value = NotifyState(null)
        }
    }

    override fun showErrorNotify(text: String, duration: Long) {
        showNotify(NotifyType.Error, text, duration)
    }

    override fun showPersistentNotify(type: NotifyType, text: String) {
        // 取消可能存在的自动隐藏任务，保持持续展示
        cancelNotifyJob?.cancel()
        cancelNotifyJob = null
        notifyStateFlow.value = NotifyState(text, type)
    }

    override fun hideNotify() {
        cancelNotifyJob?.cancel()
        cancelNotifyJob = null
        notifyStateFlow.value = NotifyState(null)
    }

    override fun showSheet(builder: SheetBuilder) {
        sheetBuilderListFlow.update { it + builder }
    }

    override fun hideSheet(builder: SheetBuilder) {
        sheetBuilderListFlow.update { it.filter { item -> item != builder } }
    }

    override fun hideAllSheet() {
        sheetBuilderListFlow.value = listOf()
    }

    fun showDialog(id: String, builder: DialogBuilder) {
        dialogBuilderMapFlow.update { it.toMutableMap().apply { put(id, builder) } }
    }

    fun updateDialog(id: String, builder: DialogBuilder) {
        if (dialogBuilderMapFlow.value.containsKey(id)) showDialog(id, builder)
    }

    fun hideDialog(id: String) {
        dialogBuilderMapFlow.update { it.filterKeys { key -> key != id } }
    }
}