package com.helsinkiwizard.cointoss.ui

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.helsinkiwizard.cointoss.Constants.MAIN_BANNER_AD_ID
import com.helsinkiwizard.cointoss.Constants.MAIN_INTERSTITIAL_AD_ID
import com.helsinkiwizard.cointoss.R
import com.helsinkiwizard.cointoss.navigation.NavRoute
import com.helsinkiwizard.cointoss.ui.theme.LocalNavController
import com.helsinkiwizard.cointoss.ui.viewmodel.HomeScreenContent
import com.helsinkiwizard.cointoss.ui.viewmodel.HomeScreenDialogs
import com.helsinkiwizard.cointoss.ui.viewmodel.HomeViewModel
import com.helsinkiwizard.cointoss.utils.AdManager
import com.helsinkiwizard.cointoss.utils.AdManager.BannerAd
import com.helsinkiwizard.cointoss.utils.AdManager.ShowInterstitialAd
import com.helsinkiwizard.core.coin.CoinAnimation
import com.helsinkiwizard.core.coin.CoinType
import com.helsinkiwizard.core.theme.PercentEighty
import com.helsinkiwizard.core.theme.Twenty
import com.helsinkiwizard.core.theme.TwentyEight
import com.helsinkiwizard.core.theme.Two
import com.helsinkiwizard.core.ui.model.CustomCoinUiModel
import com.helsinkiwizard.core.viewmodel.DialogState
import com.helsinkiwizard.core.viewmodel.UiState

private const val CONTEXT_MENU_WIDTH_FRACTION = .45f
private val contextMenuItems = listOf(NavRoute.Settings, NavRoute.About, NavRoute.RemoveAds)

@Composable
internal fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        AdManager.loadInterstitialAds(context)
    }

    HomeScreenContent(viewModel)
    HomeScreenDialogs(viewModel)
}

@Composable
private fun HomeScreenDialogs(viewModel: HomeViewModel) {
    when (val state = viewModel.dialogState.collectAsState().value) {
        is DialogState.ShowContent -> {
            when (state.type as HomeScreenDialogs) {
                HomeScreenDialogs.ShowInterstitialAd -> {
                    ShowInterstitialAd(adId = MAIN_INTERSTITIAL_AD_ID)
                    viewModel.resetDialogState()
                }
            }
        }

        else -> {}
    }
}

@Composable
private fun HomeScreenContent(viewModel: HomeViewModel) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        when (val state = viewModel.uiState.collectAsState().value) {
            is UiState.ShowContent -> {
                when (val type = state.type as HomeScreenContent) {
                    is HomeScreenContent.LoadingComplete -> {
                        with(viewModel) {
                            val coinType = coinTypeFlow.collectAsState(initial = type.initialCoinType).value
                            val speed = speedFlow.collectAsState(initial = type.initialSpeed).value
                            val customCoin = customCoinFlow.collectAsState(initial = null).value
                            val adsRemoved = adsRemoved.collectAsState(initial = true).value
                            val playSound = playSound.collectAsState(initial = type.playSound).value
                            val onFlip = { onFlip() }
                            Content(coinType, speed, customCoin, adsRemoved, playSound, onFlip)
                        }
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
    onFlip: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val context = LocalContext.current
        val soundEffect = remember { MediaPlayer.create(context, R.raw.coin_toss) }

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(PercentEighty)
        ) {
            CoinAnimation(
                coinType = coinType,
                customCoin = customCoinUiModel,
                speed = speed,
                onFlip = {
                    onFlip()
                    if (playSound) soundEffect.start()
                },
                modifier = Modifier
                    .align(Alignment.Center)
                    .aspectRatio(1f)
                    .padding(vertical = Twenty)
            )
        }
        if (adsRemoved.not()) {
            BannerAd(
                adId = MAIN_BANNER_AD_ID,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        MoreMenu(
            adsRemoved = adsRemoved,
            modifier = Modifier.align(Alignment.TopEnd)
        )
    }
}

@Composable
private fun MoreMenu(
    adsRemoved: Boolean,
    modifier: Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val navController = LocalNavController.current

    Box(
        modifier = modifier
    ) {
        IconButton(
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(id = R.string.more)
            )
        }

        val displayMetrics = LocalConfiguration.current.screenWidthDp.toFloat()
        val menuWidth = (displayMetrics * CONTEXT_MENU_WIDTH_FRACTION).dp

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.widthIn(min = menuWidth)
        ) {
            val menuItems = contextMenuItems.filterNot { adsRemoved && it == NavRoute.RemoveAds }
            menuItems.forEach { item ->
                DropdownMenuItem(
                    text = { Text(stringResource(id = item.titleRes)) },
                    leadingIcon = {
                        when {
                            item.icon != null -> {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(TwentyEight)
                                )
                            }

                            item.iconRes != null -> {
                                Icon(
                                    painter = painterResource(item.iconRes),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(TwentyEight)
                                        .padding(Two)
                                )
                            }
                        }
                    },
                    onClick = {
                        expanded = false
                        navController.navigate(item.name)
                    }
                )
            }
        }
    }
}
