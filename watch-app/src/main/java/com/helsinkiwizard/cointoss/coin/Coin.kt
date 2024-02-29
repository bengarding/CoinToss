package com.helsinkiwizard.cointoss.coin

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.firebase.analytics.FirebaseAnalytics
import com.helsinkiwizard.core.coin.CoinAnimation
import com.helsinkiwizard.core.coin.CoinType

var flipCount = 0

@OptIn(ExperimentalPagerApi::class) // pager
@Composable
fun Coin(
    coinType: CoinType,
    pagerState: PagerState,
    analytics: FirebaseAnalytics,
    startFlipping: Boolean,
    onStartFlipping: () -> Unit
) {
    var showChevron by remember { mutableStateOf(startFlipping.not()) }
    Chevron(showChevron)

    Box(
        contentAlignment = Alignment.Center
    ) {
        LaunchedEffect(coinType, startFlipping) {
            // When a new coin type is selected, move page to this Composable
            if (pagerState.currentPage != 0) {
                pagerState.animateScrollToPage(0)
            }
        }

        CoinAnimation(
            coinType = coinType,
            analytics = analytics,
            startFlipping = startFlipping,
            onStartFlipping = onStartFlipping,
            onFlip = { showChevron = false }
        )
    }
}
