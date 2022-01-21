package com.d10ng.basicjetpackcomposeapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.d10ng.basicjetpackcomposeapp.bean.NormalDialogBuilder
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.compose.AppText
import com.d10ng.basicjetpackcomposeapp.compose.AppTheme
import com.d10ng.basicjetpackcomposeapp.model.AppViewModel
import com.d10ng.basicjetpackcomposeapp.view.HollowButtonWithText
import com.d10ng.basicjetpackcomposeapp.view.SolidButtonWithText
import com.d10ng.coroutines.launchIO
import com.google.accompanist.insets.statusBarsHeight
import kotlinx.coroutines.delay

class MainActivity : BaseActivity() {

    val app: AppViewModel by viewModels(factoryProducer = { AppViewModel.Factory(this) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isShow by remember {
                mutableStateOf(false)
            }
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

                        item {
                            SolidButtonWithText(text = "显示底部弹窗", onClick = {
                                isShow = true
                            })
                        }
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    if (isShow) {
                        AlertDialog(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            onDismissRequest = {
                                isShow = false
                            },
                            buttons = {
                                HollowButtonWithText(text = "关闭", onClick = { isShow = false })
                            }
                        )
                    }
                }
            }
        }
    }
}