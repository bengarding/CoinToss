package com.helsinkiwizard.cointoss

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.MaterialTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.helsinkiwizard.cointoss.theme.CoinTossTheme
import com.helsinkiwizard.shared.Constants.EXTRA_COIN_TYPE
import com.helsinkiwizard.shared.Constants.EXTRA_START_FLIPPING
import com.helsinkiwizard.shared.Repository
import com.helsinkiwizard.shared.coin.CoinAnimation
import com.helsinkiwizard.shared.coin.CoinType

class MainActivity : ComponentActivity() {

    private lateinit var repo: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repo = Repository(this)
        val startFlippingIntent = intent.extras?.getBoolean(EXTRA_START_FLIPPING) ?: false

        val initialCoinType = intent.extras?.getInt(EXTRA_COIN_TYPE) ?: 0
        setContent {
            CoinFlip(initialCoinType, startFlippingIntent)
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    private fun CoinFlip(initialCoinType: Int, startFlippingIntent: Boolean) {
        val coinType = CoinType.parse(
            repo.getCoinType.collectAsState(
                initial = initialCoinType,
                lifecycleScope.coroutineContext
            ).value
        )

        val pagerState = rememberPagerState()
        var startFlipping by remember { mutableStateOf(startFlippingIntent) }

        CoinTossTheme {
            LaunchedEffect(coinType) {
                // When a new coin type is selected, move page to this Composable
                if (pagerState.currentPage != 0) {
                    pagerState.animateScrollToPage(0)
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
                verticalArrangement = Arrangement.Center
            ) {
                HorizontalPager(count = 2, state = pagerState) { page ->
                    when (page) {
                        0 -> {
                            var showChevron by remember { mutableStateOf(startFlipping.not()) }
                            Chevron(showChevron)
                            CoinAnimation(
                                coinType = coinType,
                                startFlipping = startFlipping,
                                onStartFlipping = {
                                    startFlipping = false
                                },
                                onFlip = { flipCount ->
                                    if (flipCount > 0) showChevron = false
                                }
                            )
                        }
                        1 -> CoinList()
                    }
                }
            }
        }
    }
}
