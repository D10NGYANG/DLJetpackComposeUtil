package com.d10ng.compose.demo.pages

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.base.Button
import com.d10ng.compose.ui.base.ButtonType
import com.d10ng.compose.ui.base.CellTitle
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.compose.ui.show.ImagePreview
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * 图片查看器
 * @Author d10ng
 * @Date 2023/11/28 16:29
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ImagePreviewScreen(onBack: () -> Unit = {}) {
    // 图片路径
    var imagePath by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) scope.launch {
            var pendingFile: File? = null
            try {
                val file = withContext(Dispatchers.IO) {
                    val target = runCatching {
                        File.createTempFile("image-preview-", ".jpg", context.cacheDir)
                    }.getOrNull() ?: return@withContext null
                    pendingFile = target
                    runCatching {
                        requireNotNull(context.contentResolver.openInputStream(uri)).use { input ->
                            target.outputStream().use(input::copyTo)
                        }
                        target
                    }.getOrElse { target.delete(); null }
                }
                if (file != null) imagePath = file.path
                else UiViewModelManager.showToast("无法读取所选图片")
            } finally {
                if (pendingFile?.path != imagePath) pendingFile?.delete()
            }
        }
    }
    DisposableEffect(imagePath) {
        val path = imagePath
        onDispose { if (path.isNotEmpty()) File(path).delete() }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
            .navigationBarsPadding()
    ) {
        NavBar(title = "ImagePreview", onClickBack = { onBack() })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            CellTitle(title = "基础用法")
            CustomFlowRow {
                Button(text = "选择图片", type = ButtonType.PRIMARY) {
                    picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.SingleMimeType("image/jpeg")))
                }
            }
        }
    }
    if (imagePath.isNotEmpty()) {
        ImagePreview(path = imagePath) {
            imagePath = ""
        }
    }
}

@Preview
@Composable
private fun ImagePreviewScreenPreview() {
    ImagePreviewScreen()
}
