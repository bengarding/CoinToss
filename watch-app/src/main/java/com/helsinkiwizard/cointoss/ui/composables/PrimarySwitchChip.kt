package com.helsinkiwizard.cointoss.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Switch
import androidx.wear.compose.material.SwitchDefaults
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.ToggleChip
import androidx.wear.compose.material.ToggleChipDefaults
import com.helsinkiwizard.cointoss.ui.theme.CoinTossTheme

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
        toggleControl = {
            Switch(
                checked,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colors.onSurface
                )
            )
        },
        colors = ToggleChipDefaults.toggleChipColors(
            checkedEndBackgroundColor = MaterialTheme.colors.primary,
            checkedContentColor = MaterialTheme.colors.onPrimary,
            checkedSecondaryContentColor = MaterialTheme.colors.onPrimary,
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@Preview
@Composable
private fun PrimarySwitchChipPreview() {
    CoinTossTheme {
        Column {
            PrimarySwitchChip(
                label = "Play sound",
                checked = true,
                onCheckedChanged = {}
            )
            PrimarySwitchChip(
                label = "Play sound",
                checked = false,
                onCheckedChanged = {}
            )
        }
    }
}