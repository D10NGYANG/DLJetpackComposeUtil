package com.d10ng.compose

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.d10ng.compose.model.AppViewModel

abstract class BaseActivity: ComponentActivity() {

    val app: AppViewModel by viewModels()
}