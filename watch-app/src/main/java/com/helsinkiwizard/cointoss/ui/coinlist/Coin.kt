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
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.helsinkiwizard.cointoss.R
import com.helsinkiwizard.cointoss.ui.composables.Chevron
import com.helsinkiwizard.cointoss.utils.FlipGestureDetector
import com.helsinkiwizard.core.coin.CoinAnimation
import com.helsinkiwizard.core.coin.CoinType
import com.helsinkiwizard.core.theme.LocalActivity
import com.helsinkiwizard.core.ui.model.CustomCoinUiModel
import kotlin.math.absoluteValue

private const val SENSITIVITY_MULTIPLIER = 100
private const val ONE_SECOND_MILLIS = 1000L
private const val TEN_SECONDS = 10

@OptIn(ExperimentalPagerApi::class) // pager
@Composable
fun Coin(
    coinType: CoinType,
    customCoin: CustomCoinUiModel?,
    speed: Float,
    playSound: Boolean,
    tossFromWristFlip: Boolean,
    wristSensitivity: Int,
    tossFromBezel: Boolean,
    bezelSensitivity: Int,
    showChevron: Boolean,
    pagerState: PagerState,
    startFlipping: Boolean,
    onStartFlipping: () -> Unit,
    onFlip: () -> Unit,
) {
    val focusRequester: FocusRequester = remember { FocusRequester() }

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
        val activity = LocalActivity.current
        val soundEffect = remember { MediaPlayer.create(activity, R.raw.coin_toss) }
        var tossFromRotaryInput by remember { mutableStateOf(false) }
        var accumulatedDelta by remember { mutableFloatStateOf(0f) }

        val gestureDetector = remember {
            FlipGestureDetector(
                context = activity,
                onFlipDetected = { tossFromRotaryInput = true }
            )
        }
        gestureDetector.sensitivity = wristSensitivity

        LaunchedEffect(coinType, startFlipping) {
            // When a new coin type is selected, move page to this Composable
            if (pagerState.currentPage != 0) {
                pagerState.animateScrollToPage(0)
            }
            focusRequester.requestFocus()
        }

        LifecycleResumeEffect(tossFromWristFlip) {
            if (tossFromWristFlip) gestureDetector.start()

            onPauseOrDispose {
                gestureDetector.stop()
            }
        }

        CoinAnimation(
            coinType = coinType,
            customCoin = customCoin,
            speed = speed,
            startFlipping = startFlipping || tossFromRotaryInput,
            onStartFlipping = onStartFlipping,
            onFlip = {
                if (playSound) soundEffect.start()
                tossFromRotaryInput = false
                onFlip()
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
