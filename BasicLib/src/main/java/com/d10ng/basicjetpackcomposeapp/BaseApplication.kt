package com.d10ng.basicjetpackcomposeapp

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.MutableLiveData

abstract class BaseApplication: Application() {

    /** 栈顶页面 */
    var topActivityLiveData: MutableLiveData<BaseActivity?> = MutableLiveData(null)
    /** 页面列表 */
    val activityList: MutableList<BaseActivity> = mutableListOf()

    override fun onCreate() {
        super.onCreate()

        // 监听页面生命周期
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                if (activity is BaseActivity) {
                    topActivityLiveData.postValue(activity)
                    if (!activityList.contains(activity)) activityList.add(activity)
                }
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {
                if (activity is BaseActivity) topActivityLiveData.postValue(activity)
            }

            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivityDestroyed(activity: Activity) {
                if (activity == topActivityLiveData.value) topActivityLiveData.postValue(null)
                if (activityList.contains(activity)) activityList.remove(activity)
            }
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        })
    }
}