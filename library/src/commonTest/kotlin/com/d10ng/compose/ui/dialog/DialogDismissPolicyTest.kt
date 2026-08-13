package com.d10ng.compose.ui.dialog

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DialogDismissPolicyTest {

    @Test
    fun dismissibleBuilderEnablesBackAndOutsideDismiss() {
        val policy = resolveDialogDismissPolicy(clickOutsideDismiss = true)

        assertTrue(policy.dismissOnBackPress)
        assertTrue(policy.dismissOnClickOutside)
    }

    @Test
    fun forcedBuilderBlocksBackAndOutsideDismiss() {
        val policy = resolveDialogDismissPolicy(clickOutsideDismiss = false)

        assertFalse(policy.dismissOnBackPress)
        assertFalse(policy.dismissOnClickOutside)
    }
}
