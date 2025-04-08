package com.helsinkiwizard.cointoss.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.helsinkiwizard.core.theme.Twelve

@Composable
internal fun PrimaryChip(
    text: String,
    onClick: () -> Unit,
    secondaryLabel: String? = null,
    icon: ImageVector? = null,
) {
    Chip(
        label = { Text(text) },
        secondaryLabel = { if (secondaryLabel != null) Text(secondaryLabel) },
        onClick = onClick,
        icon = {
            if (icon != null) Icon(imageVector = icon, contentDescription = null)
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview
@Composable
private fun PrimaryChipPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(Twelve)
    ) {
        PrimaryChip(
            text = "Choose a coin",
            onClick = {},
            icon = Icons.Outlined.MonetizationOn
        )
        PrimaryChip(
            text = "Choose a coin",
            secondaryLabel = "Subtext",
            onClick = {},
            icon = Icons.Outlined.MonetizationOn
        )
        PrimaryChip(
            text = "No icon",
            onClick = {}
        )
        PrimaryChip(
            text = "No icon",
            secondaryLabel = "Subtext",
            onClick = {},
        )
    }
}