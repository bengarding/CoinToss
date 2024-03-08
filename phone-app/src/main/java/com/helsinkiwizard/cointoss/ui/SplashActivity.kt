package com.helsinkiwizard.cointoss.ui

import android.annotation.SuppressLint
import android.content.Intent
import com.helsinkiwizard.cointoss.Constants.EXTRA_THEME_MODE
import com.helsinkiwizard.cointoss.Repository
import com.helsinkiwizard.cointoss.navigation.NavRoute
import com.helsinkiwizard.core.BaseRepository
import com.helsinkiwizard.core.BaseSplashActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseSplashActivity() {

    companion object {
        const val TAG = "SplashActivity"
    }

    @Inject
    override lateinit var repository: BaseRepository

    private lateinit var themeMode: String

    override suspend fun executeInCoroutine() {
        themeMode = (repository as Repository).getThemeMode.filterNotNull().first().name
        // Reset NavRoute on launch
        (repository as Repository).setCurrentNavRoute(NavRoute.Home)
    }

    override suspend fun getMainActivityIntent(): Intent {
        return Intent(this, MainActivity::class.java).apply {
            putExtra(EXTRA_THEME_MODE, themeMode)
        }
    }
}
