package com.helsinkiwizard.shared

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

@SuppressLint("CustomSplashScreen")
abstract class BaseSplashActivity : ComponentActivity() {

    companion object {
        const val TAG = "SplashActivity"
    }

    protected var coinType = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val splashScreen = installSplashScreen()
            splashScreen.setKeepOnScreenCondition { true }
        }
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            val repository = Repository(this@BaseSplashActivity)
            coinType = repository.getCoinType.filterNotNull().first()

            startActivity(getMainActivityIntent())
            finish()
        }
    }

    abstract suspend fun getMainActivityIntent(): Intent
}