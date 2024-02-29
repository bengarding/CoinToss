package com.helsinkiwizard.core.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun CoinTossTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        typography = Typography,
        // For shapes, we generally recommend using the default Material Wear shapes which are
        // optimized for round and non-round devices.
        content = content
    )
}
