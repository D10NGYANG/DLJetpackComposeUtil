package com.d10ng.basicjetpackcomposeapp

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.MutableLiveData
import com.d10ng.applib.view.lockScreenOrientation
import com.d10ng.basicjetpackcomposeapp.model.AppViewModel
import com.d10ng.basicjetpackcomposeapp.utils.KeyInstant
import com.d10ng.coroutines.launchMain
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

abstract class BaseActivity: AppCompatActivity() {

    val app: AppViewModel by viewModels(factoryProducer = { AppViewModel.Factory(this) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 沉浸式状态栏
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // 固定屏幕方向
        if (isNeedLockScreenOrientation()) {
            lockScreenOrientation(isLockScreenVerticalOrientation())
        }
    }

    /**
     * 是否需要固定屏幕方向
     * @return Boolean
     */
    open fun isNeedLockScreenOrientation(): Boolean = true

    /**
     * 是否锁定屏幕竖向
     * @return Boolean
     */
    open fun isLockScreenVerticalOrientation(): Boolean = true

    /**
     * 获取最底层View
     * @return View?
     */
    open fun getRootView(): View? {
        return (this.findViewById(android.R.id.content) as ViewGroup).getChildAt(0)
    }

    /** 请求权限任务 */
    private val reqPermissionJob = mutableMapOf<Int, MutableLiveData<Boolean?>>()

    /**
     * 请求权限
     * @param permissions Array<out String>
     * @param success Function0<Unit>
     * @param fail Function0<Unit>
     */
    fun reqPermissions(
        permissions: Array<out String>,
        success: () -> Unit = {},
        fail: () -> Unit = {}
    ) {
        val tag = KeyInstant.new()
        val live = MutableLiveData<Boolean?>(null)
        reqPermissionJob[tag] = live
        ActivityCompat.requestPermissions(this, permissions, tag)
        val observer: (Boolean?) -> Unit = { bool ->
            if (bool != null) {
                if (bool) {
                    success.invoke()
                } else {
                    fail.invoke()
                }
            }
        }
        live.observe(this, observer)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (reqPermissionJob.contains(requestCode)) {
            reqPermissionJob[requestCode]?.postValue(grantResults.all { it == PackageManager.PERMISSION_GRANTED })
        }
    }

    /**
     * 检查权限
     * @param permissions Array<out String>
     * @return Boolean
     */
    suspend fun checkPermissions(permissions: Array<out String>): Boolean {
        if (hasPermissions(permissions)) return true
        return suspendCoroutine { cont ->
            launchMain {
                reqPermissions(permissions, success = {
                    cont.resume(true)
                }, fail = {
                    cont.resume(false)
                })
            }
        }
    }
}

/**
 * 判断权限
 * @receiver Context
 * @param permissions Array<out String>
 * @return Boolean
 */
fun Context.hasPermissions(permissions: Array<out String>) = permissions.all {
    ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
}