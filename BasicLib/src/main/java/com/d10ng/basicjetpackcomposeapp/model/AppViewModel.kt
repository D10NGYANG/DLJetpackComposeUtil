package com.d10ng.basicjetpackcomposeapp.model

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.d10ng.basicjetpackcomposeapp.BaseActivity
import com.d10ng.basicjetpackcomposeapp.bean.*
import com.d10ng.coroutines.launchMain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

/**
 * APP公共数据
 * @Author: D10NG
 * @Time: 2021/9/25 11:14 上午
 */
class AppViewModel(act: BaseActivity): ViewModel() {

    class Factory(private val act: BaseActivity): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AppViewModel(act) as T
        }
    }

    /** activity的弱引用 */
    val weakAct = WeakReference(act)

    /** 显示Toast */
    fun showToast(value: String, duration: Int = Toast.LENGTH_SHORT) {
        val act = weakAct.get()?: return
        launchMain { Toast.makeText(act, value, duration).show() }
    }

    /**
     * ---------------------------------------------------------------------------------------------
     * 错误提示
     */
    val isShowError = MutableStateFlow(false)
    val errorText = MutableStateFlow("")
    fun showError(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            errorText.emit(value)
            isShowError.emit(true)
            delay(3000)
            isShowError.emit(false)
        }
    }
    /** ----------------------------------------------------------------------------------------- */
    /**
     * ---------------------------------------------------------------------------------------------
     * loading
     */
    val isShowLoading = MutableStateFlow(false)
    fun showLoading() {
        isShowLoading.update { true }
    }
    fun hideLoading() {
        isShowLoading.update { false }
    }
    /** ----------------------------------------------------------------------------------------- */

    /**
     * ---------------------------------------------------------------------------------------------
     * 警告
     */
    val isShowWarning = MutableStateFlow(false)
    val warningBuilder: MutableStateFlow<WarningDialogBuilder?> = MutableStateFlow(null)
    fun showWarningDialog(builder: WarningDialogBuilder) {
        viewModelScope.launch(Dispatchers.IO) {
            warningBuilder.emit(builder)
            isShowWarning.emit(true)
        }
    }
    fun hideWarningDialog() {
        viewModelScope.launch(Dispatchers.IO) {
            isShowWarning.emit(false)
        }
    }
    /** ----------------------------------------------------------------------------------------- */

    /**
     * ---------------------------------------------------------------------------------------------
     * 提示弹窗
     */
    val isShowDialog = MutableStateFlow(false)
    val dialogBuilder: MutableStateFlow<DialogBuilder?> = MutableStateFlow(null)
    fun showDialog(builder: DialogBuilder) {
        viewModelScope.launch(Dispatchers.IO) {
            dialogBuilder.emit(builder)
            isShowDialog.emit(true)
        }
    }
    fun hideDialog() {
        viewModelScope.launch(Dispatchers.IO) {
            isShowDialog.emit(false)
        }
    }
    /** ----------------------------------------------------------------------------------------- */

    /**
     * ---------------------------------------------------------------------------------------------
     * 输入框弹窗
     */
    val isShowInputDialog = MutableStateFlow(false)
    val inputDialogBuilder: MutableStateFlow<InputDialogBuilder?> = MutableStateFlow(null)
    fun showInputDialog(builder: InputDialogBuilder) {
        viewModelScope.launch(Dispatchers.IO) {
            inputDialogBuilder.emit(builder)
            isShowInputDialog.emit(true)
        }
    }
    fun hideInputDialog() {
        viewModelScope.launch(Dispatchers.IO) {
            isShowInputDialog.emit(false)
        }
    }
    /** ----------------------------------------------------------------------------------------- */

    /**
     * ---------------------------------------------------------------------------------------------
     * 单选弹窗
     */
    val isShowRadioDialog = MutableStateFlow(false)
    val radioDialogBuilder: MutableStateFlow<RadioDialogBuilder?> = MutableStateFlow(null)
    fun showRadioDialog(builder: RadioDialogBuilder) {
        viewModelScope.launch(Dispatchers.IO) {
            radioDialogBuilder.emit(builder)
            isShowRadioDialog.emit(true)
        }
    }
    fun hideRadioDialog() {
        viewModelScope.launch(Dispatchers.IO) {
            isShowRadioDialog.emit(false)
        }
    }
    /** ----------------------------------------------------------------------------------------- */

    /**
     * ---------------------------------------------------------------------------------------------
     * 日期选择弹窗
     */
    val isShowDatePickerDialog = MutableStateFlow(false)
    val datePickerDialogBuilder: MutableStateFlow<DatePickerDialogBuilder?> = MutableStateFlow(null)
    fun showDatePickerDialog(builder: DatePickerDialogBuilder) {
        viewModelScope.launch(Dispatchers.IO) {
            datePickerDialogBuilder.emit(builder)
            isShowDatePickerDialog.emit(true)
        }
    }
    fun hideDatePickerDialog() {
        viewModelScope.launch(Dispatchers.IO) {
            isShowDatePickerDialog.emit(false)
        }
    }
    /** ----------------------------------------------------------------------------------------- */

    /**
     * ---------------------------------------------------------------------------------------------
     * 时间选择弹窗
     */
    val isShowTimePickerDialog = MutableStateFlow(false)
    val timePickerDialogBuilder: MutableStateFlow<TimePickerDialogBuilder?> = MutableStateFlow(null)
    fun showTimePickerDialog(builder: TimePickerDialogBuilder) {
        viewModelScope.launch(Dispatchers.IO) {
            timePickerDialogBuilder.emit(builder)
            isShowTimePickerDialog.emit(true)
        }
    }
    fun hideTimePickerDialog() {
        viewModelScope.launch(Dispatchers.IO) {
            isShowTimePickerDialog.emit(false)
        }
    }
    /** ----------------------------------------------------------------------------------------- */

    /**
     * ---------------------------------------------------------------------------------------------
     * 进度弹窗
     */
    val isShowProgressDialog = MutableStateFlow(false)
    val progressDialogBuilder: MutableStateFlow<ProgressDialogBuilder?> = MutableStateFlow(null)
    fun showProgressDialog(builder: ProgressDialogBuilder) {
        viewModelScope.launch(Dispatchers.IO) {
            progressDialogBuilder.emit(builder)
            isShowProgressDialog.emit(true)
        }
    }
    fun updateProgressDialog(progress: Long) {
        val builder = progressDialogBuilder.value?: return
        viewModelScope.launch(Dispatchers.IO) {
            progressDialogBuilder.emit(builder.copy(progress = progress))
        }
    }
    fun updateProgressDialog(title: String?, message: String?) {
        val builder = progressDialogBuilder.value?: return
        viewModelScope.launch(Dispatchers.IO) {
            progressDialogBuilder.emit(builder.copy(title = title?: builder.title, message = message?: builder.message))
        }
    }
    fun hideProgressDialog() {
        viewModelScope.launch(Dispatchers.IO) {
            isShowProgressDialog.emit(false)
        }
    }
    /** ----------------------------------------------------------------------------------------- */

    /**
     * ---------------------------------------------------------------------------------------------
     * 成功或失败弹窗
     */
    val isShowSuccessOrFalseDialog = MutableStateFlow(false)
    val successOrFalseDialogBuilder: MutableStateFlow<SuccessOrFalseDialogBuilder?> = MutableStateFlow(null)
    fun showSuccessOrFalseDialog(builder: SuccessOrFalseDialogBuilder) {
        viewModelScope.launch(Dispatchers.IO) {
            successOrFalseDialogBuilder.emit(builder)
            isShowSuccessOrFalseDialog.emit(true)
        }
    }
    fun hideSuccessOrFalseDialog() {
        viewModelScope.launch(Dispatchers.IO) {
            isShowSuccessOrFalseDialog.emit(false)
        }
    }
    /** ----------------------------------------------------------------------------------------- */
}