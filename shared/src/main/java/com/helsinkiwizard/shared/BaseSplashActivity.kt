package com.helsinkiwizard.shared

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.helsinkiwizard.shared.Constants.EXTRA_COIN_TYPE
import com.helsinkiwizard.shared.Constants.EXTRA_START_FLIPPING
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

@SuppressLint("CustomSplashScreen")
abstract class BaseSplashActivity : ComponentActivity() {

    companion object {
        const val TAG = "SplashActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val splashScreen = installSplashScreen()
            splashScreen.setKeepOnScreenCondition { true }
        }
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            val repo = Repository(this@BaseSplashActivity)
            val coinType = repo.getCoinType.filterNotNull().first()
            val startFlipping = intent.extras?.getBoolean(EXTRA_START_FLIPPING)

            val intent = getMainActivityIntent().apply {
                putExtra(EXTRA_COIN_TYPE, coinType)
                if (startFlipping == true)
                    putExtra(EXTRA_START_FLIPPING, true)
            }
            startActivity(intent)
            finish()
        }
    }

    abstract fun getMainActivityIntent(): Intent
}