package com.helsinkiwizard.cointoss.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.helsinkiwizard.cointoss.Constants.NAV_TRANSITION_DURATION
import com.helsinkiwizard.cointoss.R
import com.helsinkiwizard.cointoss.data.Repository
import com.helsinkiwizard.cointoss.data.ThemeMode
import com.helsinkiwizard.cointoss.navigation.MAIN_ROUTE
import com.helsinkiwizard.cointoss.navigation.NavRoute
import com.helsinkiwizard.cointoss.navigation.mainGraph
import com.helsinkiwizard.cointoss.ui.theme.CoinTossTheme
import com.helsinkiwizard.cointoss.ui.theme.LocalNavController
import com.helsinkiwizard.cointoss.ui.viewmodel.MainActivityContent
import com.helsinkiwizard.cointoss.ui.viewmodel.MainActivityViewModel
import com.helsinkiwizard.cointoss.utils.AdManager
import com.helsinkiwizard.core.theme.LocalActivity
import com.helsinkiwizard.core.theme.ThirtyTwo
import com.helsinkiwizard.core.theme.TwentyEight
import com.helsinkiwizard.core.theme.Zero
import com.helsinkiwizard.core.viewmodel.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var repository: Repository

    private val viewModel: MainActivityViewModel by viewModels()

    private val bottomBarItems = listOf(NavRoute.CoinList, NavRoute.Home, NavRoute.CreateCoin)

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.uiState.value is UiState.Loading }
        }
        super.onCreate(savedInstanceState)

        setContent {
            val uiState by viewModel.uiState.collectAsState()
            if (uiState is UiState.ShowContent) {
                val content = (uiState as UiState.ShowContent).type as MainActivityContent
                val themeMode = content.themeMode.collectAsState().value
                val materialYou = content.materialYou.collectAsState().value
                val adsRemoved = content.adsRemoved.collectAsState().value

                val isDarkTheme = when (themeMode) {
                    ThemeMode.LIGHT -> false
                    ThemeMode.DARK -> true
                    ThemeMode.SYSTEM -> isSystemInDarkTheme()
                }

                val navController: NavHostController = rememberNavController()
                val currentDestination = navController.currentBackStackEntryAsState().value?.destination
                val currentRoute = NavRoute.valueOf(currentDestination?.route ?: NavRoute.Home.name)

                val transparent = android.graphics.Color.TRANSPARENT
                enableEdgeToEdge(
                    statusBarStyle =
                        when (currentRoute) {
                            in bottomBarItems if isDarkTheme -> SystemBarStyle.dark(transparent)
                            in bottomBarItems -> SystemBarStyle.light(transparent, transparent)
                            else -> SystemBarStyle.dark(transparent)
                        },
                    navigationBarStyle =
                        if (isDarkTheme) {
                            SystemBarStyle.dark(transparent)
                        } else {
                            SystemBarStyle.light(transparent, transparent)
                        }
                )

                CoinTossTheme(isDarkTheme, materialYou) {
                    CompositionLocalProvider(
                        LocalActivity provides this@MainActivity,
                        LocalNavController provides navController
                    ) {
                        CoinToss(navController, currentRoute, isDarkTheme)
                    }
                }

                LaunchedEffect(Unit) {
                    if (adsRemoved.not()) {
                        AdManager.updateConsentStatus(this@MainActivity)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repository.resetCoinTossCount()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AdManager.clearLoadedAds()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun CoinToss(
        navController: NavHostController,
        currentRoute: NavRoute,
        invertColors: Boolean,
    ) {
        val primary = if (invertColors) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
        val onPrimary = if (invertColors) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary

        Box {
            Scaffold(
                topBar = {
                    TopBar(
                        navController = navController,
                        currentRoute = currentRoute,
                        primary = primary,
                        onPrimary = onPrimary
                    )
                },
                bottomBar = {
                    BottomAppBar(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        bottomBarItems.forEach { item ->
                            BottomNavItem(item, currentRoute, navController)
                        }
                    }
                }
            ) { paddingValues ->
                // Screens with a top bar use top padding values. Bottom bar screens set systemBarsPadding()
                // on their own
                val topPadding = if (currentRoute in bottomBarItems) Zero else paddingValues.calculateTopPadding()
                Surface(
                    modifier = Modifier
                        .padding(top = topPadding, bottom = paddingValues.calculateBottomPadding())
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = MAIN_ROUTE,
                        enterTransition = { fadeIn(tween(NAV_TRANSITION_DURATION)) },
                        exitTransition = { fadeOut(tween(NAV_TRANSITION_DURATION)) },
                    ) {
                        mainGraph()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun TopBar(
        navController: NavHostController,
        currentRoute: NavRoute,
        primary: Color,
        onPrimary: Color
    ) {
        AnimatedVisibility(
            visible = currentRoute !in bottomBarItems && currentRoute != NavRoute.RemoveAds,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            TopAppBar(
                title = { Title(currentRoute) },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                            modifier = Modifier.size(TwentyEight)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primary,
                    scrolledContainerColor = primary,
                    navigationIconContentColor = onPrimary,
                    titleContentColor = onPrimary,
                    actionIconContentColor = onPrimary
                )
            )
        }
    }

    @Composable
    private fun Title(currentRoute: NavRoute) {
        AnimatedContent(
            targetState = stringResource(id = currentRoute.titleRes),
            label = "title"
        ) {
            Text(
                text = it,
                style = MaterialTheme.typography.displayLarge,
            )
        }
    }

    @Composable
    private fun RowScope.BottomNavItem(
        item: NavRoute,
        currentRoute: NavRoute,
        navController: NavHostController
    ) {
        NavigationBarItem(
            selected = item.name == currentRoute.name,
            icon = {
                if (item.icon != null) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        modifier = Modifier.size(ThirtyTwo)
                    )
                } else if (item.iconRes != null) {
                    Icon(
                        painter = painterResource(item.iconRes),
                        contentDescription = null,
                        modifier = Modifier.size(ThirtyTwo)
                    )
                }
            },
            label = {
                Text(
                    text = stringResource(id = item.titleRes),
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center
                )
            },
            onClick = {
                if (item != currentRoute) {
                    navController.navigate(item.name) {
                        // Pop up to the start destination of the graph to avoid building up a large stack
                        // of destinations on the back stack as users select items
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                // Only save the state if the user didn't navigate to another screen via the context
                                // menu (settings, about, etc.)
                                saveState = bottomBarItems.contains(currentRoute)
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )
    }
}
