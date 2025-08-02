package com.d10ng.compose.demo

import androidx.navigation.NavHostController
import kotlinx.serialization.Serializable

object Nav {

    @Serializable
    object HomeRoute

    @Serializable
    object ColorRoute

    @Serializable
    object TypographyRoute

    @Serializable
    object ShapeRoute

    @Serializable
    object ButtonRoute

    @Serializable
    object CellRoute

    @Serializable
    object ToastRoute

    @Serializable
    object CheckboxRoute

    @Serializable
    object FieldRoute

    @Serializable
    object SwitchRoute

    @Serializable
    object CheckButtonRoute

    @Serializable
    object StepperRoute

    @Serializable
    object SearchRoute

    @Serializable
    object DialogRoute

    @Serializable
    object SheetRoute

    @Serializable
    object NotifyRoute

    @Serializable
    object PullRefreshRoute

    @Serializable
    object TagRoute

    @Serializable
    object BadgeRoute

    @Serializable
    object AvatarRoute

    @Serializable
    object StepsRoute

    @Serializable
    object PopoverRoute

    @Serializable
    object NavBarRoute

    private var controller: NavHostController? = null

    fun init(navigator: NavHostController) {
        controller = navigator
    }

    fun instant(): NavHostController {
        while (controller == null) {
            // 等待初始化完成
        }
        return controller!!
    }
}