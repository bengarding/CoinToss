package com.helsinkiwizard.cointoss.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.Switch
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.ToggleChip

@Composable
fun PrimarySwitchChip(
    label: String,
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    secondaryLabel: String? = null,
) {
    ToggleChip(
        checked = checked,
        label = { Text(label) },
        secondaryLabel = { if (secondaryLabel != null) Text(secondaryLabel) },
        onCheckedChange = onCheckedChanged,
        toggleControl = { Switch(checked) },
        modifier = modifier.fillMaxWidth()
    )
}