package com.d10ng.compose.startup

import android.app.Application
import android.content.Context
import androidx.startup.Initializer
import com.d10ng.compose.model.UiViewModelManager

/**
 * 启动初始化
 * @Author d10ng
 * @Date 2023/9/4 10:22
 */
class StartupInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        UiViewModelManager.init(context as Application)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}