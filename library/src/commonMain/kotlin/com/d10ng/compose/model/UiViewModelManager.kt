package com.d10ng.compose.model

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.d10ng.compose.ui.base.ToastPosition
import com.d10ng.compose.ui.dialog.builder.DialogBuilder
import com.d10ng.compose.ui.feedback.NotifyType
import com.d10ng.compose.ui.sheet.builder.SheetBuilder
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * UI ViewModel 管理器
 * @Author d10ng
 * @Date 2023/9/4 10:24
 */
object UiViewModelManager : IUiViewModel {

    private val models = mutableListOf<UiViewModel>()

    @Composable
    fun Init(uiViewModel: UiViewModel = viewModel { UiViewModel() }) {
        LaunchedEffect(uiViewModel) {
            models.add(uiViewModel)
        }
        DisposableEffect(uiViewModel) {
            onDispose {
                models.remove(uiViewModel)
            }
        }
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            uiViewModel.Init()
        }
    }

    override fun showToast(msg: String, duration: Long, position: ToastPosition) {
        models.forEach { it.showToast(msg, duration, position) }
    }

    override fun showSuccessToast(msg: String, duration: Long) {
        models.forEach { it.showSuccessToast(msg, duration) }
    }

    override fun showFailToast(msg: String, duration: Long) {
        models.forEach { it.showFailToast(msg, duration) }
    }

    override fun showLoading(text: String) {
        models.forEach { it.showLoading(text) }
    }

    override fun hideLoading() {
        models.forEach { it.hideLoading() }
    }

    override fun showNotify(type: NotifyType, text: String, duration: Long) {
        models.forEach { it.showNotify(type, text, duration) }
    }

    override fun showErrorNotify(text: String, duration: Long) {
        models.forEach { it.showErrorNotify(text, duration) }
    }

    override fun showSheet(builder: SheetBuilder) {
        models.forEach { it.showSheet(builder) }
    }

    override fun hideSheet(builder: SheetBuilder) {
        models.forEach { it.hideSheet(builder) }
    }

    override fun hideAllSheet() {
        models.forEach { it.hideAllSheet() }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun showDialog(builder: DialogBuilder): String {
        val id = Uuid.random().toHexString()
        models.forEach { it.showDialog(id, builder) }
        return id
    }

    fun updateDialog(id: String, builder: DialogBuilder) {
        models.forEach { it.updateDialog(id, builder) }
    }

    fun hideDialog(id: String) {
        models.forEach { it.hideDialog(id) }
    }
}