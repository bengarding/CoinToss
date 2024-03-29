package com.helsinkiwizard.cointoss.ui

import android.os.Build
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.helsinkiwizard.cointoss.R
import com.helsinkiwizard.cointoss.Repository
import com.helsinkiwizard.cointoss.data.ThemeMode
import com.helsinkiwizard.cointoss.theme.CoinTossTheme
import com.helsinkiwizard.cointoss.ui.composable.PillButton
import com.helsinkiwizard.cointoss.ui.composable.PrimarySlider
import com.helsinkiwizard.cointoss.ui.composable.PrimarySwitch
import com.helsinkiwizard.cointoss.ui.model.MutableInputWrapper
import com.helsinkiwizard.cointoss.ui.model.SettingsModel
import com.helsinkiwizard.cointoss.ui.viewmodel.SettingsContent
import com.helsinkiwizard.cointoss.ui.viewmodel.SettingsViewModel
import com.helsinkiwizard.cointoss.ui.viewmodel.UiState
import com.helsinkiwizard.core.CoreConstants.SPEED_MAX
import com.helsinkiwizard.core.CoreConstants.SPEED_MIN
import com.helsinkiwizard.core.CoreConstants.SPEED_STEPS
import com.helsinkiwizard.core.theme.Twelve
import com.helsinkiwizard.core.theme.Twenty

@Composable
internal fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    when (val state = viewModel.uiState.collectAsState().value) {
        is UiState.ShowContent -> {
            when (val type = state.type as SettingsContent) {
                is SettingsContent.LoadingComplete -> Content(type.model, viewModel)
            }
        }

        else -> {}
    }
}

@Composable
private fun Content(
    model: SettingsModel,
    viewModel: SettingsViewModel
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ThemeButtons(
            themeModeWrapper = model.themeMode,
            materialYouWrapper = model.materialYou,
            themeModeOnclick = { themeMode -> viewModel.onThemeModeClicked(themeMode) },
            materialYouOnclick = { checked -> viewModel.onMaterialYouClicked(checked) }
        )
        HorizontalDivider(
            modifier = Modifier.padding(top = Twelve)
        )
        CoinSettings(
            speedWrapper = model.speed,
            onValueChangeFinished = { viewModel.onSpeedValueChangeFinished() }
        )
    }
}

@Composable
private fun ThemeButtons(
    themeModeWrapper: MutableInputWrapper<ThemeMode>,
    materialYouWrapper: MutableInputWrapper<Boolean>,
    themeModeOnclick: (ThemeMode) -> Unit = {},
    materialYouOnclick: (Boolean) -> Unit = {}
) {
    Column {
        Title(R.string.theme)
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Twelve)
        ) {
            ThemeMode.entries.forEach { themeMode ->
                PillButton(
                    text = stringResource(id = themeMode.textRes),
                    iconVector = themeMode.iconVector,
                    iconRes = themeMode.iconRes,
                    selected = themeModeWrapper.value == themeMode,
                    onclick = { themeModeOnclick(themeMode) }
                )
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Spacer(modifier = Modifier.height(Twelve))
            PrimarySwitch(
                checked = materialYouWrapper.value,
                onCheckChanged = materialYouOnclick,
                label = stringResource(id = R.string.use_device_color_scheme),
                modifier = Modifier.padding(horizontal = Twenty)
            )
        }
    }
}

@Composable
private fun CoinSettings(
    speedWrapper: MutableInputWrapper<Float>,
    onValueChangeFinished: () -> Unit
) {
    Column {
        Title(textRes = R.string.coin)
        PrimarySlider(
            value = speedWrapper.value,
            minRange = SPEED_MIN,
            maxRange = SPEED_MAX,
            steps = SPEED_STEPS,
            title = stringResource(id = R.string.speed_seconds),
            onValueChange = { value -> speedWrapper.value = value },
            onValueChangeFinished = onValueChangeFinished,
            modifier = Modifier.padding(horizontal = Twelve)
        )
    }
}

@Composable
private fun Title(@StringRes textRes: Int) {
    Text(
        text = stringResource(id = textRes),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(start = Twelve, top = Twenty, end = Twelve, bottom = Twelve)
    )
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    val repository = Repository(LocalContext.current)
    val viewModel = SettingsViewModel(repository)
    val model = SettingsModel(ThemeMode.DARK, true, 3f)
    Surface {
        CoinTossTheme {
            Content(model, viewModel)
        }
    }
}
