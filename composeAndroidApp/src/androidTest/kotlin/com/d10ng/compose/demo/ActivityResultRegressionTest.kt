package com.d10ng.compose.demo

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.SystemClock
import android.provider.MediaStore
import android.view.MotionEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.accessibilityservice.AccessibilityService
import androidx.activity.compose.setContent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import com.d10ng.app.managers.ActivityManager
import com.d10ng.app.managers.PhotoManager
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.sheet.builder.RadioSheetBuilder
import com.d10ng.compose.ui.show.ImagePreview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.hamcrest.Matchers.anyOf
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

@RunWith(AndroidJUnit4::class)
class ActivityResultRegressionTest {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()

    @Test
    fun forcedSheetBlocksNativeBackAndScrimButAllowsCodeDismissal() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            waitForText("DLJetpackComposeUtil", true)
            val builder = RadioSheetBuilder(
                title = "Forced sheet validation",
                items = linkedSetOf("Validation option")
            ).apply { clickOutsideDismiss = false }
            scenario.onActivity { UiViewModelManager.showSheet(builder) }
            try {
                waitForText("Forced sheet validation", true)
                assertTrue(instrumentation.uiAutomation.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK))
                SystemClock.sleep(500)
                waitForText("Forced sheet validation", true)
                val metrics = instrumentation.targetContext.resources.displayMetrics
                tap(metrics.widthPixels / 2f, metrics.heightPixels / 4f)
                SystemClock.sleep(500)
                waitForText("Forced sheet validation", true)
                scenario.onActivity { builder.dismiss() }
                waitForText("Forced sheet validation", false)
                waitForText("DLJetpackComposeUtil", true)
            } finally {
                scenario.onActivity { builder.dismiss() }
            }
        }
    }

    private fun waitForText(text: String, present: Boolean) {
        val deadline = SystemClock.uptimeMillis() + 10_000
        while (SystemClock.uptimeMillis() < deadline) {
            val found = containsText(instrumentation.uiAutomation.rootInActiveWindow, text)
            if (found == present) return
            SystemClock.sleep(100)
        }
        fail("Expected text '$text' present=$present")
    }

    private fun containsText(node: AccessibilityNodeInfo?, text: String): Boolean {
        if (node == null) return false
        if (node.text?.toString() == text || node.contentDescription?.toString() == text) return true
        return (0 until node.childCount).any { containsText(node.getChild(it), text) }
    }

    private fun tap(x: Float, y: Float) {
        val now = SystemClock.uptimeMillis()
        for (action in listOf(MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP)) {
            val event = MotionEvent.obtain(now, SystemClock.uptimeMillis(), action, x, y, 0)
            try {
                instrumentation.sendPointerSync(event)
            } finally {
                event.recycle()
            }
        }
    }

    @Test
    fun recreationRemovesDestroyedActivityLaunchers() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            lateinit var oldActivity: MainActivity
            scenario.onActivity { oldActivity = it }
            scenario.recreate()
            scenario.onActivity { current ->
                assertNotSame(oldActivity, current)
                assertSame(current, ActivityManager.top())
                assertFalse(ActivityManager.listFlow.value.contains(oldActivity))
                // 2.7.1修复的是私有映射残留，直接检查才能防止“新launcher可用但旧引用仍泄漏”。
                val field = ActivityManager::class.java.getDeclaredField("launcherMap")
                field.isAccessible = true
                val launchers = field.get(ActivityManager) as Map<*, *>
                assertFalse(launchers.containsKey(oldActivity))
                assertTrue(launchers.containsKey(current))
            }
        }
    }

    @Test
    @SdkSuppress(minSdkVersion = 29)
    fun photoResultIsReadableBeforeAndAfterActivityRecreation() {
        val context = instrumentation.targetContext
        val resolver = context.contentResolver
        val name = "dlcompose-validation-${System.nanoTime()}.jpg"
        val uri = requireNotNull(resolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, name)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/DLComposeValidation")
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        ))
        try {
            val bitmap = Bitmap.createBitmap(32, 32, Bitmap.Config.ARGB_8888)
            try {
                bitmap.eraseColor(Color.GREEN)
                requireNotNull(resolver.openOutputStream(uri)).use {
                    assertTrue(bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it))
                }
            } finally {
                bitmap.recycle()
            }
            resolver.update(uri, ContentValues().apply {
                put(MediaStore.Images.Media.IS_PENDING, 0)
            }, null, null)
            Intents.init()
            try {
                intending(anyOf(
                    hasAction(Intent.ACTION_PICK),
                    hasAction(Intent.ACTION_OPEN_DOCUMENT),
                    hasAction(MediaStore.ACTION_PICK_IMAGES)
                )).respondWith(ActivityResult(Activity.RESULT_OK, Intent().setData(uri)))
                ActivityScenario.launch(MainActivity::class.java).use { scenario ->
                    repeat(3) { attempt ->
                        if (attempt > 0) scenario.recreate()
                        instrumentation.waitForIdleSync()
                        val path = runBlocking {
                            withTimeout(15_000) { PhotoManager.pick() }
                        }
                        assertNotNull("Photo result missing after recreation $attempt", path)
                        val decoded = requireNotNull(BitmapFactory.decodeFile(path))
                        try {
                            assertEquals(32, decoded.width)
                            assertEquals(32, decoded.height)
                            assertTrue(Color.green(decoded.getPixel(16, 16)) > 240)
                        } finally {
                            decoded.recycle()
                        }
                        val dismissed = AtomicBoolean(false)
                        scenario.onActivity { activity ->
                            activity.setContent {
                                ImagePreview(requireNotNull(path)) { dismissed.set(true) }
                            }
                        }
                        val deadline = SystemClock.uptimeMillis() + 5_000
                        var rendered = false
                        while (!rendered && SystemClock.uptimeMillis() < deadline) {
                            val screen = requireNotNull(instrumentation.uiAutomation.takeScreenshot())
                            try {
                                val pixel = screen.getPixel(screen.width / 2, screen.height / 2)
                                rendered = Color.green(pixel) > 240 && Color.red(pixel) < 20
                                if (rendered) {
                                    if (attempt == 2) {
                                        File(context.getExternalFilesDir(null), "photo-preview.png")
                                            .outputStream().use { screen.compress(Bitmap.CompressFormat.PNG, 100, it) }
                                    }
                                    tap(screen.width / 2f, screen.height / 2f)
                                }
                            } finally {
                                screen.recycle()
                            }
                            SystemClock.sleep(100)
                        }
                        assertTrue("Selected image was not rendered", rendered)
                        instrumentation.waitForIdleSync()
                        // 图片预览同时支持双击，单击回调需等待双击判定窗口结束。
                        val tapDeadline = SystemClock.uptimeMillis() + 2_000
                        while (!dismissed.get() && SystemClock.uptimeMillis() < tapDeadline) {
                            SystemClock.sleep(50)
                        }
                        assertTrue("Image tap did not call onDismiss", dismissed.get())
                    }
                }
            } finally {
                Intents.release()
            }
        } finally {
            resolver.delete(uri, null, null)
            File(context.cacheDir, name).delete()
        }
    }

    @Test
    fun realSystemPickerCanOpenAndCancelAfterRecreation() = runBlocking {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.recreate()
            instrumentation.waitForIdleSync()
            val result = async(Dispatchers.Default) {
                withTimeout(20_000) { PhotoManager.pick() }
            }
            val automation = instrumentation.uiAutomation
            try {
                val deadline = SystemClock.uptimeMillis() + 10_000
                var pickerPackage: String? = null
                while (SystemClock.uptimeMillis() < deadline) {
                    pickerPackage = automation.rootInActiveWindow?.packageName?.toString()
                    if (pickerPackage != null && pickerPackage != instrumentation.targetContext.packageName) break
                    SystemClock.sleep(100)
                }
                assertNotNull("System picker did not expose a window", pickerPackage)
                assertNotEquals(instrumentation.targetContext.packageName, pickerPackage)
                assertTrue(automation.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK))
                assertNull(withTimeout(10_000) { result.await() })
            } finally {
                result.cancel()
            }
        }
    }
}
