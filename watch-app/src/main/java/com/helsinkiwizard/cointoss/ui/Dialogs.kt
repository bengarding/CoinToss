package com.helsinkiwizard.cointoss.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.SendToMobile
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Alert
import androidx.wear.compose.material.dialog.Confirmation
import com.helsinkiwizard.cointoss.R
import com.helsinkiwizard.core.theme.Eight
import com.helsinkiwizard.core.theme.Forty
import com.helsinkiwizard.core.theme.Text20
import com.helsinkiwizard.core.theme.ThirtyTwo
import com.helsinkiwizard.core.theme.Twelve

@Composable
internal fun ShowOnPhoneConfirmation(
    onTimeout: () -> Unit,
    messageRes: Int
) {
    Confirmation(
        icon = {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.SendToMobile,
                contentDescription = null,
                modifier = Modifier.size(Forty)
            )
        },
        onTimeout = onTimeout,
        content = {
            Text(
                text = stringResource(id = messageRes),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = Twelve)
            )
        }
    )
}

@Composable
internal fun DownloadMobileAppConfirmation(
    onClick: () -> Unit
) {
    Alert(
        contentPadding = PaddingValues(vertical = ThirtyTwo, horizontal = Twelve),
        verticalArrangement = Arrangement.spacedBy(Eight, Alignment.CenterVertically),
        title = {
            Text(
                stringResource(id = R.string.download_mobile_app),
                color = MaterialTheme.colors.onPrimary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = Text20
            )
        },
        content = {
            item {
                Text(
                    stringResource(id = R.string.download_mobile_app_message),
                )
            }
            item {
                Chip(
                    label = { Text(stringResource(id = R.string.ok)) },
                    onClick = onClick,
                )
            }
        }
    )
}
