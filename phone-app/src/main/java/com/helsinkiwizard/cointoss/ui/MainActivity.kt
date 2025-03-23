package com.helsinkiwizard.cointoss.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
import com.helsinkiwizard.cointoss.ui.drawer.DrawerContent
import com.helsinkiwizard.cointoss.ui.theme.CoinTossTheme
import com.helsinkiwizard.cointoss.ui.theme.LocalNavController
import com.helsinkiwizard.cointoss.utils.AdManager
import com.helsinkiwizard.core.theme.LocalActivity
import com.helsinkiwizard.core.theme.ThirtyTwo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var repository: Repository

    private val bottomBarItems = listOf(NavRoute.CoinList, NavRoute.Home, NavRoute.CreateCoin)

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        var initialThemeMode: ThemeMode
        var initialMaterialYou: Boolean
        var adsRemoved: Boolean

        runBlocking {
            initialThemeMode = repository.getThemeMode.firstOrNull() ?: ThemeMode.SYSTEM
            initialMaterialYou = repository.getMaterialYou.firstOrNull() ?: true
            adsRemoved = repository.getAdsRemoved.firstOrNull() ?: false
        }

        setContent {
            val themeMode = repository.getThemeMode.collectAsState(initial = initialThemeMode).value
            val navController: NavHostController = rememberNavController()

            CoinTossTheme(repository, themeMode, initialMaterialYou) {
                CompositionLocalProvider(
                    LocalActivity provides this@MainActivity,
                    LocalNavController provides navController
                ) {
                    CoinToss(navController)
                }
            }
        }

        if (adsRemoved.not()) {
            AdManager.updateConsentStatus(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AdManager.clearLoadedAds()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun CoinToss(navController: NavHostController) {
        val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val coroutineScope = rememberCoroutineScope()

        val currentDestination = navController.currentBackStackEntryAsState().value?.destination
        val currentRoute = NavRoute.valueOf(currentDestination?.route ?: NavRoute.Home.name)

        val adsRemoved = repository.getAdsRemoved.collectAsState(initial = false).value

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Title(currentRoute) },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Menu,
                                contentDescription = stringResource(id = R.string.menu)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        scrolledContainerColor = MaterialTheme.colorScheme.primary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
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
            Surface(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                color = MaterialTheme.colorScheme.surface
            ) {
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        DrawerContent(
                            onClick = { selectedRoute ->
                                coroutineScope.launch {
                                    if (currentRoute != selectedRoute) {
                                        navController.navigate(selectedRoute.name)
                                    }
                                    drawerState.close()
                                }
                            },
                            adsRemoved = adsRemoved
                        )
                    }
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = MAIN_ROUTE,
                        enterTransition = { fadeIn(tween(NAV_TRANSITION_DURATION)) },
                        exitTransition = { fadeOut(tween(NAV_TRANSITION_DURATION)) }
                    ) {
                        mainGraph()
                    }
                }
            }
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
