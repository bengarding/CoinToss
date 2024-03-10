package com.helsinkiwizard.cointoss.ui.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import com.helsinkiwizard.core.CoreConstants.EMPTY_STRING
import com.helsinkiwizard.core.theme.Eight

@Composable
fun PrimarySwitch(
    checked: Boolean,
    onCheckChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    label: String = EMPTY_STRING,
    labelOnLeft: Boolean = true,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    enabled: Boolean = true,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    textComposable: @Composable () -> Unit = {
        Text(
            text = label,
            style = textStyle
        )
    }
) {
    val combinedModifier = Modifier
        .fillMaxWidth()
        .minimumInteractiveComponentSize()
        .toggleable(
            value = checked,
            onValueChange = onCheckChanged
        )
        .semantics(mergeDescendants = true) {}
        // the passed in modifier values should be added to or override the above values
        .then(modifier)

    val switch = @Composable {
        Switch(
            modifier = Modifier
                .padding(end = Eight)
                .clearAndSetSemantics { },
            checked = checked,
            onCheckedChange = onCheckChanged,
            enabled = enabled
        )
    }

    Row(
        modifier = combinedModifier,
        verticalAlignment = verticalAlignment
    ) {
        if (labelOnLeft) {
            textComposable()
            Spacer(modifier = Modifier.weight(1f))
            switch()
        } else {
            switch()
            textComposable()
        }
    }
}
