package com.helsinkiwizard.cointoss.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Text

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Chip(
        label = { Text(text = text) },
        onClick = onClick,
        modifier = modifier,
    )
}

