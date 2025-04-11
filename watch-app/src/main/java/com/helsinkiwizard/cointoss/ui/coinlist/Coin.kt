package com.helsinkiwizard.cointoss.ui.coinlist

import android.media.MediaPlayer
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.helsinkiwizard.cointoss.R
import com.helsinkiwizard.cointoss.ui.composables.Chevron
import com.helsinkiwizard.core.coin.CoinAnimation
import com.helsinkiwizard.core.coin.CoinType
import com.helsinkiwizard.core.ui.model.CustomCoinUiModel
import kotlin.math.absoluteValue

private const val SENSITIVITY_MULTIPLIER = 100

@OptIn(ExperimentalPagerApi::class) // pager
@Composable
fun Coin(
    coinType: CoinType,
    customCoin: CustomCoinUiModel?,
    speed: Float,
    playSound: Boolean,
    tossFromBezel: Boolean,
    bezelSensitivity: Int,
    pagerState: PagerState,
    startFlipping: Boolean,
    onStartFlipping: () -> Unit,
) {
    var showChevron by remember { mutableStateOf(startFlipping.not()) }
    val focusRequester: FocusRequester = remember { FocusRequester() }

    LaunchedEffect(startFlipping) {
        if (showChevron) {
            showChevron = false
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        // Request focus each time this page loads, so the rotary event can be handled
        if (pagerState.currentPage == 0) {
            focusRequester.requestFocus()
        }
    }

    Chevron(showChevron)

    Box(
        contentAlignment = Alignment.Center
    ) {
        val context = LocalContext.current
        val soundEffect = remember { MediaPlayer.create(context, R.raw.coin_toss) }
        var tossFromRotaryInput by remember { mutableStateOf(false) }
        var accumulatedDelta by remember { mutableFloatStateOf(0f) }

        LaunchedEffect(coinType, startFlipping) {
            // When a new coin type is selected, move page to this Composable
            if (pagerState.currentPage != 0) {
                pagerState.animateScrollToPage(0)
            }
            focusRequester.requestFocus()
        }

        CoinAnimation(
            coinType = coinType,
            customCoin = customCoin,
            speed = speed,
            startFlipping = startFlipping || tossFromRotaryInput,
            onStartFlipping = onStartFlipping,
            onFlip = {
                if (playSound) soundEffect.start()
                showChevron = false
                tossFromRotaryInput = false
            },
            modifier = Modifier
                .fillMaxSize()
                .onRotaryScrollEvent {
                    if (tossFromBezel.not()) return@onRotaryScrollEvent false

                    accumulatedDelta += it.verticalScrollPixels.absoluteValue
                    val threshold = SENSITIVITY_MULTIPLIER * bezelSensitivity
                    if (accumulatedDelta >= threshold) {
                        accumulatedDelta = 0f
                        tossFromRotaryInput = true
                    }
                    true
                }
                .focusRequester(focusRequester)
                .focusable(),
        )
    }
}
