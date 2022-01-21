package com.d10ng.basicjetpackcomposeapp.model

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.d10ng.basicjetpackcomposeapp.BaseActivity
import com.d10ng.basicjetpackcomposeapp.bean.NormalDialogBuilder
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

    /** 是否显示加载中弹窗 */
    val isShowLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    /** 显示加载中弹窗 */
    fun startLoading() {
        isShowLoading.postValue(true)
    }

    /** 隐藏加载中弹窗 */
    fun cancelLoading() {
        isShowLoading.postValue(false)
    }

    /** 普通弹窗构造器 */
    val normalDialogBuilder: MutableLiveData<NormalDialogBuilder?> = MutableLiveData(null)

    /** 构造普通弹窗 */
    fun buildNormalDialog(builder: NormalDialogBuilder) {
        normalDialogBuilder.value = builder
    }

    /** 是否显示普通弹窗 */
    val isShowNormalDialog = MutableLiveData(false)

    /** 显示普通弹窗 */
    fun showNormalDialog() {
        if (normalDialogBuilder.value == null) {
            isShowNormalDialog.postValue(false)
        } else {
            isShowNormalDialog.postValue(true)
        }
    }

    /** 隐藏普通弹窗 */
    fun dismissNormalDialog() {
        isShowNormalDialog.postValue(false)
    }

    /** 是否显示提示 */
    val isShowSnackBar = MutableLiveData(false)

    /** 提示内容 */
    val snackBarValue = MutableLiveData("")

    /** 显示提示 */
    fun showSnackBar(value: String) {
        launchMain {
            snackBarValue.value = value
            launchIO {
                isShowSnackBar.postValue(true)
                delay(3000)
                isShowSnackBar.postValue(false)
            }
        }
    }
}