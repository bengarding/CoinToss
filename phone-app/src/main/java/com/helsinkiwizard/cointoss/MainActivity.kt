package com.helsinkiwizard.cointoss

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.helsinkiwizard.cointoss.ui.CoinTossTheme
import com.helsinkiwizard.shared.Constants
import com.helsinkiwizard.shared.Repository
import com.helsinkiwizard.shared.coin.CoinAnimation
import com.helsinkiwizard.shared.coin.CoinType

class MainActivity : ComponentActivity() {

    private lateinit var repo: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repo = Repository(this)
        val initialCoinType = intent.extras?.getInt(Constants.EXTRA_COIN_TYPE) ?: 0

        setContent {
            CoinTossTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    CoinFlip(initialCoinType)
                }
            }
        }
    }

    @Composable
    private fun CoinFlip(initialCoinType: Int) {
        val coinType = CoinType.parse(
            repo.getCoinType.collectAsState(
                initial = initialCoinType,
                lifecycleScope.coroutineContext
            ).value
        )

        Column(
            modifier = Modifier.fillMaxSize(.5f)
        ) {
            CoinAnimation(coinType = coinType)
        }

    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CoinTossTheme {
        Greeting("Android")
    }
}

//private fun sendFlipCountAnalytics(analytics: FirebaseAnalytics?) {
//    if (analytics == null) return
//    val params = Bundle().apply {
//        putInt(Constants.FLIP_COUNT, flipCount)
//    }
//    analytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, params)
//}