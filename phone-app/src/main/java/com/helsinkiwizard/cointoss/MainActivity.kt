package com.helsinkiwizard.cointoss

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.helsinkiwizard.cointoss.ui.CoinTossTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoinTossTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
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