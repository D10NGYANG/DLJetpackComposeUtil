package com.d10ng.basicjetpackcomposeapp.model

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.d10ng.basicjetpackcomposeapp.BaseActivity
import com.d10ng.basicjetpackcomposeapp.dialog.builder.DialogBuilder
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
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AppViewModel(act) as T
        }
    }

    /** activity的弱引用 */
    val weakAct = WeakReference(act)

    /** 显示Toast */
    fun showToast(value: String, duration: Int = Toast.LENGTH_SHORT) {
        val act = weakAct.get()?: return
        launchMain { Toast.makeText(act.applicationContext, value, duration).show() }
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
    val dialogBuilder: MutableStateFlow<DialogBuilder?> = MutableStateFlow(null)
    fun showDialog(builder: DialogBuilder) {
        viewModelScope.launch(Dispatchers.IO) {
            dialogBuilder.emit(builder)
        }
    }
    fun hideDialog() {
        viewModelScope.launch(Dispatchers.IO) {
            dialogBuilder.emit(null)
        }
    }
}