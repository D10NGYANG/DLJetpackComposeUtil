package com.d10ng.compose.model

import androidx.compose.material3.SnackbarDuration

internal fun resolveSnackbarDuration(durationMillis: Long): SnackbarDuration =
    if (durationMillis <= 4_000L) SnackbarDuration.Short else SnackbarDuration.Long
