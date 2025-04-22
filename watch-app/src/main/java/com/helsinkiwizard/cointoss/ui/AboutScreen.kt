package com.helsinkiwizard.cointoss.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.helsinkiwizard.cointoss.BuildConfig
import com.helsinkiwizard.cointoss.R
import com.helsinkiwizard.core.theme.Eight
import com.helsinkiwizard.core.theme.Four
import com.helsinkiwizard.core.theme.Twelve
import com.helsinkiwizard.core.theme.Twenty
import com.helsinkiwizard.core.ui.composable.appIconPainterResource
import com.helsinkiwizard.core.utils.getLastUpdatedDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

private val AppIconSize = 60.dp

@Composable
internal fun AboutScreen(
    dateUpdated: LocalDate = getLastUpdatedDate(LocalContext.current)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AppInfo(dateUpdated)
    }
}

@Composable
private fun AppInfo(
    dateUpdated: LocalDate
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = Twenty)
    ) {
        Image(
            painter = appIconPainterResource(id = R.mipmap.ic_launcher_round),
            contentDescription = null,
            modifier = Modifier
                .size(AppIconSize)
                .padding(bottom = Twelve)
        )
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.title1,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = Twelve)
        )

        val dateString = dateUpdated.format(
            DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.getDefault())
        )
        Text(
            text = "v${BuildConfig.VERSION_NAME} - $dateString",
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = Eight)
        )
        val appOwner = stringResource(id = R.string.app_owner)
        Text(
            text = stringResource(id = R.string.copyright_split, dateUpdated.year, appOwner),
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = Four)
        )
    }
}

@Preview(name = "large round", device = WearDevices.LARGE_ROUND)
@Preview(name = "square", device = WearDevices.SQUARE)
@Composable
private fun AboutScreenPreview() {
    AboutScreen(LocalDate.now())
}