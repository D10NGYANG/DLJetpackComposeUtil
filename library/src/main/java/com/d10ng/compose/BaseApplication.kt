package com.d10ng.compose

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.d10ng.compose.model.AppViewModel
import kotlinx.coroutines.flow.MutableStateFlow

abstract class BaseApplication: Application() {

    /** 栈顶页面 */
    var topBaseActivityFlow: MutableStateFlow<BaseActivity?> = MutableStateFlow(null)
    var topActivityFlow: MutableStateFlow<Activity?> = MutableStateFlow(null)
    /** 页面列表 */
    val activityList: MutableList<Activity> = mutableListOf()

    override fun onCreate() {
        super.onCreate()

        // 监听页面生命周期
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                if (activity is BaseActivity) {
                    topBaseActivityFlow.value = activity
                }
                topActivityFlow.value = activity
                if (!activityList.contains(activity)) activityList.add(activity)
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {
                if (activity is BaseActivity && activity != topBaseActivityFlow.value) topBaseActivityFlow.value = activity
                if (activity != topActivityFlow.value) topActivityFlow.value = activity
            }

            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivityDestroyed(activity: Activity) {
                if (activity == topBaseActivityFlow.value) topBaseActivityFlow.value = null
                if (activity == topActivityFlow.value) topActivityFlow.value = null
                if (activityList.contains(activity)) activityList.remove(activity)
            }
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        })
    }

    /**
     * 获取Activity实例
     * @return T?
     */
    inline fun <reified T: Activity>getActivity(): T? {
        return activityList.find { it::class == T::class } as? T
    }

    /**
     * 获取最顶部的act的AppViewModel
     * @return AppViewModel?
     */
    fun getAppVM(): AppViewModel? {
        return topBaseActivityFlow.value?.app
    }
}