package com.helsinkiwizard.cointoss

import android.annotation.SuppressLint
import android.content.Intent
import com.helsinkiwizard.shared.BaseSplashActivity
import com.helsinkiwizard.shared.Constants

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseSplashActivity() {

    override suspend fun getMainActivityIntent(): Intent {
        val startFlipping = intent.extras?.getBoolean(Constants.EXTRA_START_FLIPPING)

        return Intent(this, MainActivity::class.java).apply {
            putExtra(Constants.EXTRA_COIN_TYPE, coinType)
            if (startFlipping == true)
                putExtra(Constants.EXTRA_START_FLIPPING, true)
        }
    }
}