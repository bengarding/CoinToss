package com.helsinkiwizard.cointoss.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.foundation.rememberActiveFocusRequester
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.helsinkiwizard.cointoss.BuildConfig
import com.helsinkiwizard.cointoss.R
import com.helsinkiwizard.cointoss.ui.composables.PrimaryButton
import com.helsinkiwizard.cointoss.ui.viewmodel.AboutContent
import com.helsinkiwizard.cointoss.ui.viewmodel.AboutDialogs
import com.helsinkiwizard.cointoss.ui.viewmodel.AboutViewModel
import com.helsinkiwizard.core.theme.Eight
import com.helsinkiwizard.core.theme.Four
import com.helsinkiwizard.core.theme.Twelve
import com.helsinkiwizard.core.theme.Twenty
import com.helsinkiwizard.core.ui.composable.appIconPainterResource
import com.helsinkiwizard.core.utils.getLastUpdatedDate
import com.helsinkiwizard.core.viewmodel.DialogState
import com.helsinkiwizard.core.viewmodel.UiState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

private val AppIconSize = 60.dp

@Composable
fun AboutScreen(
    viewModel: AboutViewModel = hiltViewModel()
) {
    AboutContent(viewModel)
    AboutDialogs(viewModel)
}

@Composable
private fun AboutDialogs(viewModel: AboutViewModel) {
    when (val state = viewModel.dialogState.collectAsState().value) {
        is DialogState.ShowContent -> {
            when (state.type as AboutDialogs) {
                is AboutDialogs.OpenOnPhone -> {
                    ShowOnPhoneConfirmation(
                        messageRes = R.string.download_coin_toss_mobile_app,
                        onTimeout = viewModel::resetDialogState
                    )
                }

                is AboutDialogs.DownloadMobileApp -> {
                    DownloadMobileAppConfirmation(
                        onClick = viewModel::resetDialogState
                    )
                }
            }
        }

        else -> {}
    }
}

@Composable
private fun AboutContent(viewModel: AboutViewModel) {
    when (val state = viewModel.uiState.collectAsState().value) {
        is UiState.ShowContent -> {
            when (val type = state.type as AboutContent) {
                is AboutContent.LoadingComplete -> About(
                    showButton = type.showMobileAppButton,
                    onButtonClick = viewModel::onDownloadButtonClicked
                )
            }
        }

        is UiState.Loading -> ProgressIndicator()
        else -> {}
    }
}

@OptIn(ExperimentalWearFoundationApi::class) // rememberActiveFocusRequester
@Composable
private fun About(
    dateUpdated: LocalDate = getLastUpdatedDate(LocalContext.current),
    showButton: Boolean,
    onButtonClick: () -> Unit,
) {
    val listState = rememberScalingLazyListState()
    Scaffold(
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) }
    ) {
        val focusRequester = rememberActiveFocusRequester()
        val coroutineScope = rememberCoroutineScope()

        ScalingLazyColumn(
            state = listState,
            contentPadding = PaddingValues(vertical = Twenty),
            verticalArrangement = Arrangement.spacedBy(Twenty),
            modifier = Modifier
                .fillMaxSize()
                .onRotaryScrollEvent {
                    coroutineScope.launch {
                        listState.scrollBy(it.verticalScrollPixels)
                        listState.animateScrollBy(0f)
                    }
                    true
                }
                .focusRequester(focusRequester)
                .focusable()
        ) {
            item {
                AppInfo(dateUpdated)
            }
            item {
                if (showButton) {
                    PrimaryButton(
                        text = stringResource(id = R.string.download_mobile_app),
                        onClick = onButtonClick
                    )
                }
            }
        }
    }
}

@Composable
private fun AppInfo(
    dateUpdated: LocalDate
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
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
    About(LocalDate.now(), true, {})
}