package com.helsinkiwizard.cointoss.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Picker
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberPickerState
import androidx.wear.tooling.preview.devices.WearDevices
import com.helsinkiwizard.cointoss.R
import com.helsinkiwizard.cointoss.navigation.SPEED_PICKER_RESULT
import com.helsinkiwizard.cointoss.ui.theme.LocalNavController
import com.helsinkiwizard.core.CoreConstants.VALUE_UNDEFINED
import com.helsinkiwizard.core.theme.Text28

private const val SPEED_PICKER_OPTIONS_COUNT = 12
private const val DEFAULT_SPEED_INDEX = 5

@Composable
internal fun SpeedPicker(startValue: Float) {
    val navController = LocalNavController.current
    val speedList = List(SPEED_PICKER_OPTIONS_COUNT) { i -> (i + 1) * 0.5f }
    val startValueIndex = speedList.indexOf(startValue)

    PrimaryPicker(
        label = stringResource(id = R.string.speed_seconds),
        items = speedList,
        initiallySelectedOption = startValueIndex.takeIf { it != VALUE_UNDEFINED } ?: DEFAULT_SPEED_INDEX,
        onSelected = { result ->
            navController.previousBackStackEntry?.savedStateHandle?.set(SPEED_PICKER_RESULT, result)
            navController.popBackStack()
        }
    )
}

@Composable
internal fun <T> PrimaryPicker(
    label: String,
    items: List<T>,
    initiallySelectedOption: Int,
    onSelected: (T) -> Unit,
    repeatItems: Boolean = false,
) {
    val pickerState = rememberPickerState(
        initialNumberOfOptions = items.size,
        initiallySelectedOption = initiallySelectedOption,
        repeatItems = repeatItems
    )
    val contentDescription by remember { derivedStateOf { "${pickerState.selectedOption + 1}" } }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label,
            )
        }
        Picker(
            state = pickerState,
            contentDescription = contentDescription,
            modifier = Modifier.weight(1f)
        ) { index ->
            Text(
                text = items[index].toString(),
                fontSize = Text28,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
        }
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { onSelected(items[pickerState.selectedOption]) }
            ) {
                Text(text = stringResource(id = R.string.done))
            }
        }
    }
}

@Preview(device = WearDevices.LARGE_ROUND)
@Preview(device = WearDevices.RECT)
@Preview(device = WearDevices.SQUARE)
@Composable
private fun PrimaryPickerPreview() {
    PrimaryPicker(
        label = "Seconds",
        items = listOf(1, 2, 3, 4, 5, 6, 7),
        initiallySelectedOption = 3,
        onSelected = {},
    )
}
