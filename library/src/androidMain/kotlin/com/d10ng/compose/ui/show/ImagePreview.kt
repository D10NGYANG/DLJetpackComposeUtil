package com.d10ng.compose.ui.show

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import java.io.File

/**
 * 图片查看器
 * @Author d10ng
 * @Date 2023/11/28 16:02
 */

/**
 * 图片查看器
 * @param path String 图片路径
 * @param onDismiss Function0<Unit>
 */
@Composable
fun ImagePreview(
    path: String,
    onDismiss: () -> Unit
) {
    val bitmap = remember(path) {
        File(path).let {
            if (it.exists()) {
                runCatching { BitmapFactory.decodeFile(it.absolutePath) }.getOrNull()
            } else null
        }?.asImageBitmap()
    }

    Surface(
        color = Color.DarkGray,
        modifier = Modifier.fillMaxSize()
    ) {
        val previewModifier = Modifier
            .fillMaxSize()
            .imagePreviewGestures(onDismiss)
        if (bitmap != null) {
            Image(
                bitmap = bitmap,
                contentDescription = null,
                modifier = previewModifier
            )
        } else {
            Box(modifier = previewModifier)
        }
    }
}

private fun Modifier.imagePreviewGestures(
    onDismiss: () -> Unit
): Modifier = composed {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val currentOnDismiss by rememberUpdatedState(onDismiss)
    val transformableState = rememberTransformableState { _, zoomChange, offsetChange, _ ->
        val newScale = (zoomChange * scale).coerceIn(1f, 5f)
        scale = newScale
        offset = if (newScale == 1f) Offset.Zero else offset + offsetChange
    }

    transformable(state = transformableState)
        .graphicsLayer(
            scaleX = scale,
            scaleY = scale,
            translationX = offset.x,
            translationY = offset.y
        )
        .pointerInput(Unit) {
            detectTapGestures(
                onDoubleTap = {
                    scale = 1f
                    offset = Offset.Zero
                },
                onTap = { currentOnDismiss() }
            )
        }
}
