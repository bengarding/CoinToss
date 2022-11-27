package com.helsinkiwizard.cointoss

import android.annotation.SuppressLint
import android.content.Intent
import com.helsinkiwizard.core.BaseSplashActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseSplashActivity() {

    override suspend fun getMainActivityIntent() = Intent(this, MainActivity::class.java)
}
