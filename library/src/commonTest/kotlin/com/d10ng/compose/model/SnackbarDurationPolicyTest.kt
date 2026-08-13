package com.d10ng.compose.model

import androidx.compose.material3.SnackbarDuration
import kotlin.test.Test
import kotlin.test.assertEquals

class SnackbarDurationPolicyTest {

    @Test
    fun shortToastDurationUsesShortSnackbar() {
        assertEquals(SnackbarDuration.Short, resolveSnackbarDuration(2_500L))
        assertEquals(SnackbarDuration.Short, resolveSnackbarDuration(4_000L))
    }

    @Test
    fun longToastDurationUsesLongSnackbar() {
        assertEquals(SnackbarDuration.Long, resolveSnackbarDuration(4_001L))
        assertEquals(SnackbarDuration.Long, resolveSnackbarDuration(10_000L))
    }
}
