package com.helsinkiwizard.cointoss.ui.coinlist

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.helsinkiwizard.cointoss.R
import com.helsinkiwizard.cointoss.ui.composables.Chevron
import com.helsinkiwizard.core.coin.CoinAnimation
import com.helsinkiwizard.core.coin.CoinType
import com.helsinkiwizard.core.ui.model.CustomCoinUiModel

@OptIn(ExperimentalPagerApi::class) // pager
@Composable
fun Coin(
    coinType: CoinType,
    customCoin: CustomCoinUiModel?,
    speed: Float,
    playSound: Boolean,
    pagerState: PagerState,
    startFlipping: Boolean,
    onStartFlipping: () -> Unit,
) {
    var showChevron by remember { mutableStateOf(startFlipping.not()) }

    LaunchedEffect(startFlipping) {
        if (showChevron) {
            showChevron = false
        }
    }

    Chevron(showChevron)

    Box(
        contentAlignment = Alignment.Center
    ) {

        val context = LocalContext.current
        val soundEffect = remember { MediaPlayer.create(context, R.raw.coin_toss) }

        LaunchedEffect(coinType, startFlipping) {
            // When a new coin type is selected, move page to this Composable
            if (pagerState.currentPage != 0) {
                pagerState.animateScrollToPage(0)
            }
        }

        CoinAnimation(
            coinType = coinType,
            customCoin = customCoin,
            speed = speed,
            startFlipping = startFlipping,
            onStartFlipping = onStartFlipping,
            onFlip = {
                showChevron = false
                if (playSound) soundEffect.start()
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
}
