package com.d10ng.basicjetpackcomposeapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.d10ng.basicjetpackcomposeapp.bean.NormalDialogBuilder
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.compose.AppText
import com.d10ng.basicjetpackcomposeapp.compose.AppTheme
import com.d10ng.basicjetpackcomposeapp.model.AppViewModel
import com.d10ng.basicjetpackcomposeapp.view.SolidButtonWithText
import com.d10ng.coroutines.launchIO
import com.google.accompanist.insets.statusBarsHeight
import kotlinx.coroutines.delay

class MainActivity : BaseActivity() {

    val app: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(app = app) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsHeight()
                        .background(AppColor.System.primaryVariant))
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(AppColor.System.primary)
                        .padding(16.dp, 8.dp)
                    ) {
                        Text(text = "首页", style = AppText.Bold.OnPrimary.v18)
                    }

                    LazyColumn(modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(16.dp)
                    ) {
                        item {
                            SolidButtonWithText(text = "显示加载中弹窗", onClick = {
                                app.startLoading()
                                launchIO {
                                    delay(1000L)
                                    app.cancelLoading()
                                }
                            })
                        }

                        item {
                            SolidButtonWithText(text = "显示普通弹窗", onClick = {
                                app.buildNormalDialog(NormalDialogBuilder(
                                    title = "弹窗",
                                    message = "注意，这是一个弹窗，提醒用户某些信息。",
                                    sureButton = "确定",
                                    cancelButton = "取消",
                                    onClickButton = { w ->
                                        if (w == 1) {
                                            app.showSnackBar("用户点击了[确定]")
                                            app.dismissNormalDialog()
                                        } else {
                                            app.showSnackBar("用户点击了[取消]")
                                            app.dismissNormalDialog()
                                        }
                                    }
                                ))
                                app.showNormalDialog()
                            })
                        }
                    }
                }
            }
        }
    }
}