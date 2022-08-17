package com.d10ng.basicjetpackcomposeapp.demo

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.d10ng.basicjetpackcomposeapp.BaseActivity
import com.d10ng.basicjetpackcomposeapp.BaseComposeScreenObject
import com.d10ng.basicjetpackcomposeapp.view.SolidButtonWithText
import com.d10ng.basicjetpackcomposeapp.view.TitleBar
import com.d10ng.datelib.curTime
import com.d10ng.datelib.toDateStr
import com.google.accompanist.navigation.animation.composable
import kotlinx.coroutines.flow.MutableStateFlow
import java.lang.ref.WeakReference
import java.util.*
import kotlin.concurrent.schedule

object NavigationOneScreenObj: BaseComposeScreenObject("NavigationOneScreen") {
    @OptIn(ExperimentalAnimationApi::class)
    override fun composable(
        builder: NavGraphBuilder,
        controller: NavHostController,
        act: BaseActivity
    ) {
        builder.composable(name) {
            NavigationOneScreen(act, controller)
        }
    }
}

class NavigationOneScreenModel(
    /*act: BaseActivity,
    private val controller: NavHostController*/
): ViewModel() {
    /*class Factory(
        private val act: BaseActivity,
        private val controller: NavHostController
    ): ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = NavigationOneScreenModel(act, controller) as T
    }*/

    private var weakAct: WeakReference<BaseActivity> = WeakReference(null)
    private var controller: NavHostController? = null

    val timeTextFlow = MutableStateFlow(curTime.toDateStr())

    private val timer = Timer()

    init {
        timer.schedule(1000L, 1000L) {
            timeTextFlow.value = curTime.toDateStr()
        }
    }

    override fun onCleared() {
        timer.cancel()
        super.onCleared()
    }

    fun init(act: BaseActivity, controller: NavHostController) {
        if (weakAct.get() == null) {
            weakAct = WeakReference(act)
        }
        this.controller = controller
    }

    fun onClickBack() {
        weakAct.get()?.finish()
    }

    fun onClickScreen2() {
        controller?.apply {
            NavigationTwoScreenObj.go(this, timeTextFlow.value)
        }
    }
}

@Composable
fun NavigationOneScreen(
    act: BaseActivity,
    controller: NavHostController
) {

    val model: NavigationOneScreenModel = viewModel()
    model.init(act, controller)
    val timeText by model.timeTextFlow.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        TitleBar(value = "页面1", onClickBack = {
            model.onClickBack()
        })

        SolidButtonWithText(text = "跳转页面2", onClick = {
            model.onClickScreen2()
        })

        Text(text = timeText)
    }
}