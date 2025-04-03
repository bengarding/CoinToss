package com.helsinkiwizard.cointoss.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
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
import com.helsinkiwizard.cointoss.utils.AdManager
import com.helsinkiwizard.core.theme.LocalActivity
import com.helsinkiwizard.core.theme.ThirtyTwo
import com.helsinkiwizard.core.theme.TwentyEight
import com.helsinkiwizard.core.theme.Two
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        private const val CONTEXT_MENU_WIDTH_FRACTION = .45f
    }

    @Inject
    lateinit var repository: Repository

    private val bottomBarItems = listOf(NavRoute.CoinList, NavRoute.Home, NavRoute.CreateCoin)
    private val contextMenuItems = listOf(NavRoute.Settings, NavRoute.About, NavRoute.RemoveAds)

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = false

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
            val darkTheme = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            val navController: NavHostController = rememberNavController()

            CoinTossTheme(repository, themeMode, initialMaterialYou) {
                CompositionLocalProvider(
                    LocalActivity provides this@MainActivity,
                    LocalNavController provides navController
                ) {
                    CoinToss(navController, darkTheme)
                }
            }
        }

        if (adsRemoved.not()) {
            AdManager.updateConsentStatus(this)
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
        invertColors: Boolean
    ) {
        val currentDestination = navController.currentBackStackEntryAsState().value?.destination
        val currentRoute = NavRoute.valueOf(currentDestination?.route ?: NavRoute.Home.name)

        val adsRemoved = repository.getAdsRemoved.collectAsState(initial = true).value

        val primary = if (invertColors) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
        val onPrimary = if (invertColors) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Title(currentRoute) },
                    actions = {
                        MoreMenu(navController, adsRemoved)
                    },
                    navigationIcon = {
                        AnimatedVisibility(
                            visible = bottomBarItems.contains(currentRoute)
                                .not() && currentRoute != NavRoute.RemoveAds,
                            enter = fadeIn(),
                            exit = fadeOut(),
                            modifier = Modifier.semantics(mergeDescendants = true) {}
                        ) {
                            IconButton(
                                onClick = { navController.popBackStack() }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(id = R.string.back),
                                    modifier = Modifier.size(TwentyEight)
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = primary,
                        scrolledContainerColor = primary,
                        navigationIconContentColor = onPrimary,
                        titleContentColor = onPrimary,
                        actionIconContentColor = onPrimary
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

    @Composable
    private fun MoreMenu(
        navController: NavHostController,
        adsRemoved: Boolean
    ) {
        var expanded by remember { mutableStateOf(false) }

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
