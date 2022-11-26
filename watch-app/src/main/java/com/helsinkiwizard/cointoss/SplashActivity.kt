package com.helsinkiwizard.cointoss

import android.annotation.SuppressLint
import android.content.Intent
import com.helsinkiwizard.shared.BaseSplashActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseSplashActivity() {

    override fun getMainActivityIntent() = Intent(this, MainActivity::class.java)
}