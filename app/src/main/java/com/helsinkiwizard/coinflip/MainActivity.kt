package com.helsinkiwizard.coinflip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.MaterialTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.helsinkiwizard.coinflip.coin.CoinAnimation
import com.helsinkiwizard.coinflip.coin.CoinList
import com.helsinkiwizard.coinflip.coin.CoinType
import com.helsinkiwizard.coinflip.theme.CoinFlipTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoinFlip()
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CoinFlip() {
    val dataStore = Repository(LocalContext.current)
    val coinType = CoinType.parse(dataStore.getCoinType.collectAsState(initial = 0).value)

    val pagerState = rememberPagerState()

    CoinFlipTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center
        ) {
            HorizontalPager(count = 2, state = pagerState) { page ->
                when (page) {
                    0 -> CoinAnimation(coinType = coinType, pagerState = pagerState)
                    1 -> CoinList()
                }
            }
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    CoinFlip()
}
