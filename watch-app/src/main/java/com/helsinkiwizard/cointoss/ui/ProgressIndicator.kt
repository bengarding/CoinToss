package com.helsinkiwizard.cointoss.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import com.helsinkiwizard.core.theme.Four

private const val PROGRESS_INDICATOR_FRACTION = .25f

@Composable
internal fun ProgressIndicator() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            trackColor = MaterialTheme.colors.primary,
            indicatorColor = MaterialTheme.colors.onPrimary,
            strokeWidth = Four,
            modifier = Modifier.fillMaxSize(PROGRESS_INDICATOR_FRACTION)
        )
    }
}
