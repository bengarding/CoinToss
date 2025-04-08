package com.helsinkiwizard.cointoss.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.foundation.rememberActiveFocusRequester
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import com.helsinkiwizard.cointoss.R
import com.helsinkiwizard.cointoss.navigation.NavRoute
import com.helsinkiwizard.cointoss.navigation.SPEED_PICKER_RESULT
import com.helsinkiwizard.cointoss.ui.composables.PrimaryChip
import com.helsinkiwizard.cointoss.ui.model.WatchSettingsModel
import com.helsinkiwizard.cointoss.ui.theme.LocalNavController
import com.helsinkiwizard.cointoss.ui.viewmodel.WatchSettingsContent
import com.helsinkiwizard.cointoss.ui.viewmodel.WatchSettingsViewModel
import com.helsinkiwizard.cointoss.utils.GetResult
import com.helsinkiwizard.core.theme.Twelve
import com.helsinkiwizard.core.ui.model.MutableInputWrapper
import com.helsinkiwizard.core.viewmodel.UiState
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun WatchSettingsScreen(
    viewModel: WatchSettingsViewModel = hiltViewModel()
) {
    WatchSettingsContent(viewModel)
}

@Composable
private fun WatchSettingsContent(viewModel: WatchSettingsViewModel) {
    when (val state = viewModel.uiState.collectAsState().value) {
        is UiState.ShowContent -> {
            when (val type = state.type as WatchSettingsContent) {
                is WatchSettingsContent.LoadingComplete -> Content(type.model, viewModel)
            }
        }

        is UiState.Loading -> ProgressIndicator()
        else -> {}
    }
}

@OptIn(ExperimentalWearFoundationApi::class) // rememberActiveFocusRequester
@Composable
internal fun Content(
    model: WatchSettingsModel,
    viewModel: WatchSettingsViewModel
) {
    val listState = rememberScalingLazyListState()
    Scaffold(
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) }
    ) {
        val focusRequester = rememberActiveFocusRequester()
        val coroutineScope = rememberCoroutineScope()

        ScalingLazyColumn(
            state = listState,
            contentPadding = PaddingValues(all = Twelve),
            modifier = Modifier
                .background(color = MaterialTheme.colors.background)
                .fillMaxSize()
                .onRotaryScrollEvent {
                    // https://developer.android.com/training/wearables/compose/rotary-input
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
                SpeedChip(
                    wrapper = model.speed,
                    onPickerResult = viewModel::onSpeedSelected
                )
            }
        }
    }
}

@Composable
private fun SpeedChip(
    wrapper: MutableInputWrapper<Float>,
    onPickerResult: (Float) -> Unit
) {
    val navController = LocalNavController.current
    navController.GetResult(
        key = SPEED_PICKER_RESULT,
        onResult = onPickerResult
    )

    val formattedSpeed = if (wrapper.value % 1 == 0f) {
        wrapper.value.toInt().toString()
    } else {
        String.format(Locale.getDefault(), "%.1f", wrapper.value)
    }
    PrimaryChip(
        text = stringResource(id = R.string.speed),
        subLabel = String.format(stringResource(id = R.string.number_seconds), formattedSpeed),
        onClick = { navController.navigate("${NavRoute.Picker.name}/${wrapper.value}") }
    )
}
