package com.d10ng.compose.ui.sheet

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SheetDismissPolicyTest {

    @Test
    fun dismissibleSheetUsesCompatibilityBackHandlerWhileEntering() {
        val policy = resolveSheetDismissPolicy(
            userDismissEnabled = true,
            visible = true,
            sheetStateVisible = false,
        )

        assertTrue(policy.dismissOnScrimClick)
        assertTrue(policy.gesturesEnabled)
        assertFalse(policy.officialBackHandlerEnabled)
        assertTrue(policy.compatibilityBackHandlerEnabled)
        assertTrue(policy.dismissOnCompatibilityBack)
    }

    @Test
    fun dismissibleSheetUsesOfficialBackHandlerAfterEntering() {
        val policy = resolveSheetDismissPolicy(
            userDismissEnabled = true,
            visible = true,
            sheetStateVisible = true,
        )

        assertTrue(policy.dismissOnScrimClick)
        assertTrue(policy.gesturesEnabled)
        assertTrue(policy.officialBackHandlerEnabled)
        assertFalse(policy.compatibilityBackHandlerEnabled)
        assertFalse(policy.dismissOnCompatibilityBack)
    }

    @Test
    fun exitingSheetBlocksUserInputWithoutDismissingAgain() {
        val policy = resolveSheetDismissPolicy(
            userDismissEnabled = true,
            visible = false,
            sheetStateVisible = true,
        )

        assertFalse(policy.dismissOnScrimClick)
        assertFalse(policy.gesturesEnabled)
        assertFalse(policy.officialBackHandlerEnabled)
        assertTrue(policy.compatibilityBackHandlerEnabled)
        assertFalse(policy.dismissOnCompatibilityBack)
    }

    @Test
    fun forcedSheetBlocksEveryUserDismissPath() {
        val policy = resolveSheetDismissPolicy(
            userDismissEnabled = false,
            visible = true,
            sheetStateVisible = true,
        )

        assertFalse(policy.dismissOnScrimClick)
        assertFalse(policy.gesturesEnabled)
        assertFalse(policy.officialBackHandlerEnabled)
        assertTrue(policy.compatibilityBackHandlerEnabled)
        assertFalse(policy.dismissOnCompatibilityBack)
    }

    @Test
    fun removedSheetRegistersNoHandlers() {
        val policy = resolveSheetDismissPolicy(
            userDismissEnabled = true,
            visible = false,
            sheetStateVisible = false,
        )

        assertFalse(policy.dismissOnScrimClick)
        assertFalse(policy.gesturesEnabled)
        assertFalse(policy.officialBackHandlerEnabled)
        assertFalse(policy.compatibilityBackHandlerEnabled)
        assertFalse(policy.dismissOnCompatibilityBack)
    }
}
