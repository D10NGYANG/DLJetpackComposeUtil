package com.d10ng.basicjetpackcomposeapp

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import java.lang.ref.WeakReference

open class BaseViewModel: ViewModel() {

    var weakAct: WeakReference<BaseActivity> = WeakReference(null)
        private set
    var controller: NavHostController? = null
        private set

    open fun init(act: BaseActivity, controller: NavHostController) {
        init(act)
        init(controller)
    }

    open fun init(act: BaseActivity) {
        if (weakAct.get() == null) weakAct = WeakReference(act)
    }

    open fun init(controller: NavHostController) {
        this.controller = controller
    }
}