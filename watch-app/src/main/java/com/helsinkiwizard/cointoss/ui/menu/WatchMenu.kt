package com.helsinkiwizard.cointoss.ui.menu

import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.foundation.requestFocusOnHierarchyActive
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.scrollAway
import com.helsinkiwizard.cointoss.R
import com.helsinkiwizard.core.R as CoreR
import com.helsinkiwizard.cointoss.navigation.NavRoute
import com.helsinkiwizard.cointoss.ui.composables.PrimaryChip
import com.helsinkiwizard.cointoss.ui.theme.LocalNavController
import com.helsinkiwizard.core.theme.Twenty
import kotlinx.coroutines.launch

private object MenuParams {
    val menuItems = listOf(
        MenuItem(
            NavRoute.CoinList,
            CoreR.string.choose_a_coin,
            Icons.Outlined.MonetizationOn
        ),
        MenuItem(
            NavRoute.Settings,
            CoreR.string.settings,
            Icons.Outlined.Settings
        ),
        MenuItem(
            NavRoute.About,
            CoreR.string.about,
            Icons.Outlined.Info
        ),
    )
}

@Composable
internal fun WatchMenu() {
    val listState = rememberScalingLazyListState()
    val vignetteState by remember { mutableStateOf(VignettePosition.TopAndBottom) }
    Scaffold(
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) },
        vignette = { Vignette(vignetteState) },
        timeText = { TimeText(modifier = Modifier.scrollAway(listState)) }
    ) {
        val coroutineScope = rememberCoroutineScope()
        val navController = LocalNavController.current

        ScalingLazyColumn(
            state = listState,
            contentPadding = PaddingValues(horizontal = Twenty),
            modifier = Modifier
                .fillMaxSize()
                .onRotaryScrollEvent {
                    // https://developer.android.com/training/wearables/compose/rotary-input
                    coroutineScope.launch {
                        listState.scrollBy(it.verticalScrollPixels)
                        listState.animateScrollBy(0f)
                    }
                    true
                }
                .focusable()
                .requestFocusOnHierarchyActive()
        ) {
            item {
                // Empty item so that the first chip shows in the center of the screen
            }
            items(MenuParams.menuItems) { menuItem ->
                PrimaryChip(
                    text = stringResource(menuItem.title),
                    icon = menuItem.icon,
                    onClick = { navController.navigate(menuItem.route.name) }
                )
            }
        }
    }
}
