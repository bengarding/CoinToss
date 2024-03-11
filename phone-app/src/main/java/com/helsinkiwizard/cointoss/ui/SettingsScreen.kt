package com.helsinkiwizard.cointoss.ui

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.hilt.navigation.compose.hiltViewModel
import com.helsinkiwizard.cointoss.R
import com.helsinkiwizard.cointoss.data.ThemeMode
import com.helsinkiwizard.cointoss.ui.composable.PrimarySwitch
import com.helsinkiwizard.cointoss.ui.model.MutableInputWrapper
import com.helsinkiwizard.cointoss.ui.model.SettingsModel
import com.helsinkiwizard.cointoss.ui.viewmodel.SettingsContent
import com.helsinkiwizard.cointoss.ui.viewmodel.SettingsViewModel
import com.helsinkiwizard.cointoss.ui.viewmodel.UiState
import com.helsinkiwizard.core.theme.Eight
import com.helsinkiwizard.core.theme.Four
import com.helsinkiwizard.core.theme.Text14
import com.helsinkiwizard.core.theme.Twelve
import com.helsinkiwizard.core.theme.Twenty
import com.helsinkiwizard.core.theme.TwentyFour
import com.helsinkiwizard.core.theme.Two

@Composable
internal fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier.fillMaxSize()
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
}

@Composable
private fun Content(
    model: SettingsModel,
    viewModel: SettingsViewModel
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Twelve),
        modifier = Modifier            .fillMaxWidth()
    ) {
        ThemeButtons(
            themeModeWrapper = model.themeMode,
            materialYouWrapper = model.materialYou,
            themeModeOnclick = { themeMode -> viewModel.onThemeModeClicked(themeMode) },
            materialYouOnclick = { checked -> viewModel.onMaterialYouClicked(checked) }
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
private fun Title(@StringRes textRes: Int) {
    Text(
        text = stringResource(id = textRes),
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(all = Twenty)
    )
}

@Composable
private fun PillButton(
    text: String,
    iconVector: ImageVector?,
    @DrawableRes iconRes: Int?,
    selected: Boolean,
    onclick: () -> Unit
) {
    val textColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        label = "text color"
    )
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        label = "background color"
    )
    val borderColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outline
        },
        label = "background color"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .toggleable(
                value = selected,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                role = Role.Checkbox,
                onValueChange = { onclick() }
            )
            .background(color = backgroundColor, shape = CircleShape)
            .border(width = Two, color = borderColor, shape = CircleShape)
            .padding(vertical = Eight, horizontal = Twenty)
    ) {
        if (iconVector != null) {
            Icon(
                imageVector = iconVector,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(TwentyFour)
            )
        } else if (iconRes != null) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(TwentyFour)
            )
        }

        Text(
            text = text,
            color = textColor,
            fontSize = Text14,
            modifier = Modifier.padding(start = Four)
        )
    }
}
