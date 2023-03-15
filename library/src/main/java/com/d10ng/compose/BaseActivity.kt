package com.d10ng.compose

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.d10ng.compose.model.AppViewModel

abstract class BaseActivity: ComponentActivity() {

    val app: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 沉浸式状态栏
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // 固定屏幕方向
        if (isNeedLockScreenOrientation()) {
            lockScreenOrientation(isLockScreenVerticalOrientation())
        }
    }

    /**
     * 是否需要固定屏幕方向
     * @return Boolean
     */
    open fun isNeedLockScreenOrientation(): Boolean = true

    /**
     * 是否锁定屏幕竖向
     * @return Boolean
     */
    open fun isLockScreenVerticalOrientation(): Boolean = true

    /**
     * 锁定屏幕方向
     * - 除了在Activity中设置当前方法以外还需要，在主题中设置以下内容
     * <resources>
     *     <style name="AppTheme" parent="Theme.MaterialComponents.Light.NoActionBar">
     *         <!-- 锁定布局在发生以下改变时，不重置状态 -->
     *         <item name="android:configChanges">orientation|keyboardHidden|screenSize|locale</item>
     *     </style>
     * </resources>
     * - 还需要在AndroidManifest.xml中为您的activity设置以下内容
     * <activity
     *     android:name=".XActivity"
     *     android:screenOrientation="locked" />
     * @receiver Activity
     * @param isVertical Boolean 是否为竖向
     */
    private fun lockScreenOrientation(isVertical: Boolean = true) {
        val orientation = if (isVertical) ActivityInfo.SCREEN_ORIENTATION_PORTRAIT else ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        if (requestedOrientation != orientation) requestedOrientation = orientation
    }
}