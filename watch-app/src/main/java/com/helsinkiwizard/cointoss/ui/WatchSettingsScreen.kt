package com.helsinkiwizard.cointoss.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.foundation.rememberActiveFocusRequester
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.scrollAway
import com.helsinkiwizard.cointoss.R
import com.helsinkiwizard.cointoss.navigation.BEZEL_SENSITIVITY_PICKER_RESULT
import com.helsinkiwizard.cointoss.navigation.NavRoute
import com.helsinkiwizard.cointoss.navigation.SPEED_PICKER_RESULT
import com.helsinkiwizard.cointoss.navigation.WRIST_SENSITIVITY_PICKER_RESULT
import com.helsinkiwizard.cointoss.ui.composables.BEZEL_SENSITIVITY_TYPE
import com.helsinkiwizard.cointoss.ui.composables.PrimaryChip
import com.helsinkiwizard.cointoss.ui.composables.PrimarySwitchChip
import com.helsinkiwizard.cointoss.ui.composables.SPEED_TYPE
import com.helsinkiwizard.cointoss.ui.composables.WRIST_SENSITIVITY_TYPE
import com.helsinkiwizard.cointoss.ui.model.WatchSettingsModel
import com.helsinkiwizard.cointoss.ui.theme.LocalNavController
import com.helsinkiwizard.cointoss.ui.viewmodel.WatchSettingsContent
import com.helsinkiwizard.cointoss.ui.viewmodel.WatchSettingsViewModel
import com.helsinkiwizard.cointoss.utils.GetResult
import com.helsinkiwizard.core.theme.Eight
import com.helsinkiwizard.core.theme.PercentEighty
import com.helsinkiwizard.core.theme.Text20
import com.helsinkiwizard.core.theme.Twenty
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
    val vignetteState by remember { mutableStateOf(VignettePosition.TopAndBottom)}
    Scaffold(
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) },
        vignette = { Vignette(vignetteState) },
        timeText = { TimeText(modifier = Modifier.scrollAway(listState)) }
    ) {
        val focusRequester = rememberActiveFocusRequester()
        val coroutineScope = rememberCoroutineScope()

        ScalingLazyColumn(
            state = listState,
            contentPadding = PaddingValues(horizontal = Twenty),
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
                Title()
            }
            item {
                SpeedChip(
                    wrapper = model.speed,
                    onPickerResult = viewModel::onSpeedSelected
                )
            }
            item {
                PrimarySwitchChip(
                    label = stringResource(id = R.string.play_sound),
                    checked = model.playSound.value,
                    onCheckedChanged = viewModel::onPlaySoundChecked
                )
            }
            item {
                PrimarySwitchChip(
                    label = stringResource(id = R.string.flip_wrist_to_toss),
                    checked = model.tossFromWristMotion.value,
                    onCheckedChanged = viewModel::onTossFromWristMovement
                )
            }
            item {
                SensitivityChip(
                    type = WRIST_SENSITIVITY_TYPE,
                    wrapper = model.wristSensitivity,
                    onPickerResult = viewModel::onWristSensitivitySelected
                )
            }
            item {
                PrimarySwitchChip(
                    label = stringResource(id = R.string.rotate_bezel_to_toss),
                    secondaryLabel = stringResource(id = R.string.if_supported),
                    checked = model.tossFromBezel.value,
                    onCheckedChanged = viewModel::onTossFromBezelChecked
                )
            }
            item {
                SensitivityChip(
                    type = BEZEL_SENSITIVITY_TYPE,
                    wrapper = model.bezelSensitivity,
                    onPickerResult = viewModel::onBezelSensitivitySelected
                )
            }
        }
    }
}

@Composable
private fun Title() {
    Text(
        text = stringResource(id = R.string.settings),
        fontSize = Text20,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(bottom = Eight)
            .fillMaxWidth(PercentEighty)
    )
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
        secondaryLabel = String.format(stringResource(id = R.string.number_seconds), formattedSpeed),
        onClick = { navController.navigate("${NavRoute.Picker.name}/$SPEED_TYPE/${wrapper.value}") }
    )
}

@Composable
private fun SensitivityChip(
    type: String,
    wrapper: MutableInputWrapper<Int>,
    onPickerResult: (Int) -> Unit,
) {
    val navController = LocalNavController.current
    val titleRes: Int
    val pickerResult: String
    if (type == BEZEL_SENSITIVITY_TYPE) {
        titleRes = R.string.bezel_sensitivity
        pickerResult = BEZEL_SENSITIVITY_PICKER_RESULT
    } else {
        titleRes = R.string.wrist_sensitivity
        pickerResult = WRIST_SENSITIVITY_PICKER_RESULT
    }

    navController.GetResult(
        key = pickerResult,
        onResult = onPickerResult
    )

    AnimatedVisibility(
        visible = wrapper.isVisible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        PrimaryChip(
            text = stringResource(id = titleRes),
            secondaryLabel = wrapper.value.toString(),
            onClick = { navController.navigate("${NavRoute.Picker.name}/$type/${wrapper.value}") }
        )
    }
}
