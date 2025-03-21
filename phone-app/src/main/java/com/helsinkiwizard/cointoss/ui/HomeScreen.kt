package com.helsinkiwizard.cointoss.ui

import android.media.MediaPlayer
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.helsinkiwizard.cointoss.Constants.MAIN_BANNER_AD_ID
import com.helsinkiwizard.cointoss.R
import com.helsinkiwizard.cointoss.ui.viewmodel.HomeScreenContent
import com.helsinkiwizard.cointoss.ui.viewmodel.HomeViewModel
import com.helsinkiwizard.cointoss.utils.AdManager.BannerAd
import com.helsinkiwizard.core.coin.CoinAnimation
import com.helsinkiwizard.core.coin.CoinType
import com.helsinkiwizard.core.theme.PercentEighty
import com.helsinkiwizard.core.theme.Twenty
import com.helsinkiwizard.core.ui.model.CustomCoinUiModel
import com.helsinkiwizard.core.viewmodel.UiState

@Composable
internal fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        when (val state = viewModel.uiState.collectAsState().value) {
            is UiState.ShowContent -> {
                when (val type = state.type as HomeScreenContent) {
                    is HomeScreenContent.LoadingComplete -> {
                        val coinType = viewModel.coinTypeFlow.collectAsState(initial = type.initialCoinType).value
                        val speed = viewModel.speedFlow.collectAsState(initial = type.initialSpeed).value
                        val customCoin = viewModel.customCoinFlow.collectAsState(initial = null).value
                        val adsRemoved = viewModel.adsRemoved.collectAsState(initial = true).value
                        val playSound = viewModel.playSound.collectAsState(initial = type.playSound).value
                        Content(coinType, speed, customCoin, adsRemoved, playSound)
                    }
                }
            }

            else -> {}
        }
    }
}

@Composable
private fun Content(
    coinType: CoinType,
    speed: Float,
    customCoinUiModel: CustomCoinUiModel?,
    adsRemoved: Boolean,
    playSound: Boolean,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val context = LocalContext.current
        val soundEffect = remember { MediaPlayer.create(context, R.raw.coin_toss) }

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(PercentEighty)
                .animateContentSize()
                .weight(1f)
        ) {
            CoinAnimation(
                coinType = coinType,
                customCoin = customCoinUiModel,
                speed = speed,
                onFlip = { if (playSound) soundEffect.start() },
                modifier = Modifier
                    .align(Alignment.Center)
                    .aspectRatio(1f)
                    .padding(vertical = Twenty)
            )
        }
        if (adsRemoved.not()) {
            BannerAd(
                adId = MAIN_BANNER_AD_ID,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
