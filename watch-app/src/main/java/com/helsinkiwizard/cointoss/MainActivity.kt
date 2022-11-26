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
import androidx.wear.compose.material.MaterialTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.firebase.analytics.FirebaseAnalytics
import com.helsinkiwizard.cointoss.theme.CoinTossTheme
import com.helsinkiwizard.shared.Constants.APP_DRAWER
import com.helsinkiwizard.shared.Constants.EXTRA_COIN_TYPE
import com.helsinkiwizard.shared.Constants.EXTRA_START_FLIPPING
import com.helsinkiwizard.shared.Constants.TILE
import com.helsinkiwizard.shared.Repository
import com.helsinkiwizard.shared.coin.CoinAnimation
import com.helsinkiwizard.shared.coin.CoinType

class MainActivity : ComponentActivity() {

    private lateinit var repo: Repository
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repo = Repository(this)
        firebaseAnalytics = FirebaseAnalytics.getInstance(applicationContext)
        val startFlippingIntent = intent.extras?.getBoolean(EXTRA_START_FLIPPING) ?: false

        val initialCoinType = intent.extras?.getInt(EXTRA_COIN_TYPE) ?: 0
        setContent {
            CoinFlip(initialCoinType, startFlippingIntent)
        }
        val params = Bundle().apply {
            putString(FirebaseAnalytics.Param.ORIGIN, if (startFlippingIntent) TILE else APP_DRAWER)
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, params)
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun CoinFlip(initialCoinType: Int, startFlippingIntent: Boolean) {
        val coinType = CoinType.parse(
            repo.getCoinType.collectAsState(initial = initialCoinType).value
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
                                analytics = firebaseAnalytics,
                                startFlipping = startFlipping,
                                onStartFlipping = {
                                    startFlipping = false
                                },
                                onFlip = { flipCount ->
                                    if (flipCount > 0) showChevron = false
                                }
                            )
                        }
                        1 -> CoinList(analytics = firebaseAnalytics)
                    }
                }
            }
        }
    }
}
