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
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Alert
import androidx.wear.compose.material.dialog.Confirmation
import androidx.wear.tooling.preview.devices.WearDevices
import com.helsinkiwizard.cointoss.R
import com.helsinkiwizard.core.R as CoreR
import com.helsinkiwizard.core.theme.Eight
import com.helsinkiwizard.core.theme.Forty
import com.helsinkiwizard.core.theme.Sixty
import com.helsinkiwizard.core.theme.Text20
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
        contentPadding = PaddingValues(vertical = Sixty, horizontal = Twelve),
        verticalArrangement = Arrangement.spacedBy(Eight, Alignment.CenterVertically),
        title = {
            // The title is the first item in content. The text is long so it was cut off when it was here.
        },
        content = {
            item {
                Text(
                    stringResource(id = R.string.download_coin_toss_mobile_app),
                    color = MaterialTheme.colors.onPrimary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = Text20
                )
            }
            item {
                Text(
                    stringResource(id = R.string.download_mobile_app_message),
                )
            }
            item {
                Chip(
                    label = { Text(stringResource(id = CoreR.string.ok)) },
                    onClick = onClick,
                )
            }
        }
    )
}

@Preview(name = "large round", device = WearDevices.LARGE_ROUND)
@Preview(name = "square", device = WearDevices.SQUARE)
@Composable
private fun DownloadMobileAppConfirmationPreview() {
    DownloadMobileAppConfirmation { }
}

@Preview(name = "large round", device = WearDevices.LARGE_ROUND)
@Preview(name = "square", device = WearDevices.SQUARE)
@Composable
private fun ShowOnPhoneConfirmationPreview() {
    ShowOnPhoneConfirmation(
        onTimeout = {},
        messageRes = R.string.create_coin_on_phone
    )
}

