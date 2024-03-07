package com.helsinkiwizard.core

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.helsinkiwizard.core.CoreConstants.EXTRA_COIN_TYPE
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
abstract class BaseSplashActivity : ComponentActivity() {

    abstract var repository: BaseRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val splashScreen = installSplashScreen()
            splashScreen.setKeepOnScreenCondition { true }
        }
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
               val coinType = repository.getCoinType.filterNotNull().first()
                val intent = getMainActivityIntent()
                intent.putExtra(EXTRA_COIN_TYPE, coinType.value)

                startActivity(getMainActivityIntent())
                finish()

            }
        }
    }

    abstract suspend fun getMainActivityIntent(): Intent
}
