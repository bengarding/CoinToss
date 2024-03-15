package com.helsinkiwizard.cointoss.ui.composable.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.helsinkiwizard.cointoss.theme.CoinTossTheme
import com.helsinkiwizard.core.theme.Four
import com.helsinkiwizard.core.theme.Twelve
import com.helsinkiwizard.core.theme.Twenty
import com.helsinkiwizard.core.theme.Two

@Composable
fun MediaPicker(
    onDismiss: () -> Unit,
) {
    var openDialog by remember { mutableStateOf(true) }

    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                openDialog = false
                onDismiss.invoke()
            },
            confirmButton = { },
            title = {
                Text(
                    text = "Get picture from",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Twenty)
                ) {
                    IconButton(
                        icon = Icons.Outlined.CameraAlt,
                        text = "Camera"
                    )
                    IconButton(
                        icon = Icons.Outlined.Image,
                        text = "Gallery"
                    )
                }
            }
        )
    }
}

@Composable
private fun RowScope.IconButton(icon: ImageVector, text: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .weight(1f)
            .border(
                width = Two,
                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                shape = RoundedCornerShape(Four)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = MaterialTheme.colorScheme.primary),
                onClick = {}
            )
            .padding(all = Twelve)
    ) {
        Image(
            imageVector = icon,
            contentDescription = null
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MediaPickerPreview() {
    CoinTossTheme {
        Surface {
            MediaPicker { }
        }
    }
}
