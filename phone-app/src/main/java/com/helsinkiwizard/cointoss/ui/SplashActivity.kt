package com.helsinkiwizard.cointoss.ui

import android.annotation.SuppressLint
import android.content.Intent
import com.helsinkiwizard.core.BaseRepository
import com.helsinkiwizard.core.BaseSplashActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseSplashActivity() {

    companion object {
        const val TAG = "SplashActivity"
    }

    @Inject
    override lateinit var repository: BaseRepository

    override suspend fun getMainActivityIntent() = Intent(this, MainActivity::class.java)
}
