package com.d10ng.basicjetpackcomposeapp.model

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.d10ng.basicjetpackcomposeapp.BaseActivity
import com.d10ng.basicjetpackcomposeapp.bean.*
import com.d10ng.coroutines.launchIO
import com.d10ng.coroutines.launchMain
import kotlinx.coroutines.delay
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
        Toast.makeText(act, value, duration).show()
    }

    /**
     * ---------------------------------------------------------------------------------------------
     * 错误提示
     */
    val isShowError = MutableLiveData(false)
    val errorText = MutableLiveData("")
    fun showError(value: String) {
        launchMain {
            errorText.value = value
            launchIO {
                isShowError.postValue(true)
                delay(3000)
                isShowError.postValue(false)
            }
        }
    }
    /** ----------------------------------------------------------------------------------------- */
    /**
     * ---------------------------------------------------------------------------------------------
     * loading
     */
    val isShowLoading = MutableLiveData(false)
    fun showLoading() {
        isShowLoading.postValue(true)
    }
    fun hideLoading() {
        isShowLoading.postValue(false)
    }
    /** ----------------------------------------------------------------------------------------- */

    /**
     * ---------------------------------------------------------------------------------------------
     * 警告
     */
    val isShowWarning = MutableLiveData(false)
    val warningBuilder: MutableLiveData<WarningDialogBuilder?> = MutableLiveData(null)
    fun showWarningDialog(builder: WarningDialogBuilder) {
        warningBuilder.postValue(builder)
        isShowWarning.postValue(true)
    }
    fun hideWarningDialog() {
        isShowWarning.postValue(false)
    }
    /** ----------------------------------------------------------------------------------------- */

    /**
     * ---------------------------------------------------------------------------------------------
     * 提示弹窗
     */
    val isShowDialog = MutableLiveData(false)
    val dialogBuilder: MutableLiveData<DialogBuilder?> = MutableLiveData(null)
    fun showDialog(builder: DialogBuilder) {
        dialogBuilder.postValue(builder)
        isShowDialog.postValue(true)
    }
    fun hideDialog() {
        isShowDialog.postValue(false)
    }
    /** ----------------------------------------------------------------------------------------- */

    /**
     * ---------------------------------------------------------------------------------------------
     * 输入框弹窗
     */
    val isShowInputDialog = MutableLiveData(false)
    val inputDialogBuilder: MutableLiveData<InputDialogBuilder?> = MutableLiveData(null)
    fun showInputDialog(builder: InputDialogBuilder) {
        inputDialogBuilder.postValue(builder)
        isShowInputDialog.postValue(true)
    }
    fun hideInputDialog() {
        isShowInputDialog.postValue(false)
    }
    /** ----------------------------------------------------------------------------------------- */

    /**
     * ---------------------------------------------------------------------------------------------
     * 单选弹窗
     */
    val isShowRadioDialog = MutableLiveData(false)
    val radioDialogBuilder: MutableLiveData<RadioDialogBuilder?> = MutableLiveData(null)
    fun showRadioDialog(builder: RadioDialogBuilder) {
        radioDialogBuilder.postValue(builder)
        isShowRadioDialog.postValue(true)
    }
    fun hideRadioDialog() {
        isShowRadioDialog.postValue(false)
    }
    /** ----------------------------------------------------------------------------------------- */

    /**
     * ---------------------------------------------------------------------------------------------
     * 日期选择弹窗
     */
    val isShowDatePickerDialog = MutableLiveData(false)
    val datePickerDialogBuilder: MutableLiveData<DatePickerDialogBuilder?> = MutableLiveData(null)
    fun showDatePickerDialog(builder: DatePickerDialogBuilder) {
        datePickerDialogBuilder.postValue(builder)
        isShowDatePickerDialog.postValue(true)
    }
    fun hideDatePickerDialog() {
        isShowDatePickerDialog.postValue(false)
    }
    /** ----------------------------------------------------------------------------------------- */

    /**
     * ---------------------------------------------------------------------------------------------
     * 时间选择弹窗
     */
    val isShowTimePickerDialog = MutableLiveData(false)
    val timePickerDialogBuilder: MutableLiveData<TimePickerDialogBuilder?> = MutableLiveData(null)
    fun showTimePickerDialog(builder: TimePickerDialogBuilder) {
        timePickerDialogBuilder.postValue(builder)
        isShowTimePickerDialog.postValue(true)
    }
    fun hideTimePickerDialog() {
        isShowTimePickerDialog.postValue(false)
    }
    /** ----------------------------------------------------------------------------------------- */

    /**
     * ---------------------------------------------------------------------------------------------
     * 进度弹窗
     */
    val isShowProgressDialog = MutableLiveData(false)
    val progressDialogBuilder: MutableLiveData<ProgressDialogBuilder?> = MutableLiveData(null)
    fun showProgressDialog(builder: ProgressDialogBuilder) {
        progressDialogBuilder.postValue(builder)
        isShowProgressDialog.postValue(true)
    }
    fun updateProgressDialog(progress: Long) {
        val builder = progressDialogBuilder.value?: return
        progressDialogBuilder.postValue(builder.copy(progress = progress))
    }
    fun updateProgressDialog(title: String?, message: String?) {
        val builder = progressDialogBuilder.value?: return
        progressDialogBuilder.postValue(builder.copy(title = title?: builder.title, message = message?: builder.message))
    }
    fun hideProgressDialog() {
        isShowProgressDialog.postValue(false)
    }
    /** ----------------------------------------------------------------------------------------- */
}