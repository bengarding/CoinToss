package com.helsinkiwizard.cointoss.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.helsinkiwizard.cointoss.Repository
import com.helsinkiwizard.cointoss.theme.CoinTossTheme
import com.helsinkiwizard.core.CoreConstants.EXTRA_COIN_TYPE
import com.helsinkiwizard.core.coin.CoinAnimation
import com.helsinkiwizard.core.coin.CoinType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var repo: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoinTossTheme {
                CoinToss()
            }
        }
    }

    @Composable
    private fun CoinToss() {
        val initialCoinType = intent.extras?.getInt(EXTRA_COIN_TYPE) ?: CoinType.BITCOIN.value
        val coinType = CoinType.parse(
            repo.getCoinType.collectAsState(initial = initialCoinType).value
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            CoinAnimation(
                coinType = coinType,
                modifier = Modifier
                    .fillMaxWidth(.8f)
                    .aspectRatio(1f)
            )
        }
    }
}
