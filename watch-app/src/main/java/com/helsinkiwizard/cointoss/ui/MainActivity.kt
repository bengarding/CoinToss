package com.helsinkiwizard.cointoss.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.helsinkiwizard.cointoss.Constants.APP_DRAWER
import com.helsinkiwizard.cointoss.Constants.EXTRA_START_FLIPPING
import com.helsinkiwizard.cointoss.Constants.TILE
import com.helsinkiwizard.cointoss.Repository.Companion.DEFAULT_SENSITIVITY
import com.helsinkiwizard.cointoss.navigation.MAIN_ROUTE
import com.helsinkiwizard.cointoss.navigation.mainGraph
import com.helsinkiwizard.cointoss.ui.coinlist.Coin
import com.helsinkiwizard.cointoss.ui.menu.WatchMenu
import com.helsinkiwizard.cointoss.ui.theme.CoinTossTheme
import com.helsinkiwizard.cointoss.ui.theme.LocalNavController
import com.helsinkiwizard.cointoss.ui.viewmodel.CoinTossViewModel
import com.helsinkiwizard.core.CoreConstants.SPEED_DEFAULT
import com.helsinkiwizard.core.theme.LocalActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    private val viewModel: CoinTossViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            CoinTossTheme {
                CompositionLocalProvider(
                    LocalActivity provides this,
                    LocalNavController provides rememberSwipeDismissableNavController()
                ) {
                    HomeScreen()
                }
            }
        }

        val params = Bundle().apply {
            putString(FirebaseAnalytics.Param.ORIGIN, if (viewModel.startFlipping) TILE else APP_DRAWER)
        }
        FirebaseAnalytics.getInstance(applicationContext).logEvent(FirebaseAnalytics.Event.APP_OPEN, params)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        viewModel.startFlipping = intent.extras?.getBoolean(EXTRA_START_FLIPPING) ?: false
    }
}

@Composable
private fun HomeScreen() {
    SwipeDismissableNavHost(
        navController = LocalNavController.current,
        startDestination = MAIN_ROUTE
    ) {
        mainGraph()
    }
}

@Composable
fun CoinTossScreen(
    viewModel: CoinTossViewModel = hiltViewModel(LocalActivity.current)
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val scope = rememberCoroutineScope()
    BackHandler(enabled = pagerState.currentPage == 1) {
        scope.launch {
            pagerState.animateScrollToPage(0)
        }
    }

    val coinType = viewModel.coinTypeFlow.collectAsState().value
    val customCoin = viewModel.customCoinFlow.collectAsState(initial = null).value
    val coinSpeed = viewModel.coinSpeedFlow.collectAsState(initial = SPEED_DEFAULT).value
    val playSound = viewModel.playSoundFlow.collectAsState(initial = false).value
    val tossFromWristFlip = viewModel.tossFromWristFlipFlow.collectAsState(initial = false).value
    val wristSensitivity = viewModel.wristSensitivityFlow.collectAsState(initial = DEFAULT_SENSITIVITY).value
    val tossFromBezel = viewModel.tossFromBezelFlow.collectAsState(initial = false).value
    val bezelSensitivity = viewModel.bezelSensitivityFlow.collectAsState(initial = DEFAULT_SENSITIVITY).value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> Coin(
                    coinType = coinType,
                    customCoin = customCoin,
                    speed = coinSpeed,
                    playSound = playSound,
                    tossFromWristFlip = tossFromWristFlip,
                    wristSensitivity = wristSensitivity,
                    tossFromBezel = tossFromBezel,
                    bezelSensitivity = bezelSensitivity,
                    showChevron = viewModel.showChevron,
                    pagerState = pagerState,
                    startFlipping = viewModel.startFlipping,
                    onStartFlipping = {
                        viewModel.startFlipping = false
                    },
                    onFlip = {
                        viewModel.showChevron = false
                    }
                )

                1 -> {
                    viewModel.showChevron = false
                    WatchMenu()
                }
            }
        }
    }
}
