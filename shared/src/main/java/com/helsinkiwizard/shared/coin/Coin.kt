package com.helsinkiwizard.shared.coin

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.helsinkiwizard.shared.R
import java.util.Random
import kotlin.math.abs

const val QUARTER_ROTATION = 90f
const val HALF_ROTATION = 180
const val THREE_QUARTER_ROTATION = 270f
const val FULL_ROTATION = 360

private var rotationAmount = 1
private var currentSide = CoinSide.HEADS
private var nextSide = CoinSide.TAILS

var flipCount = 0

@Composable
fun CoinAnimation(
    coinType: CoinType,
    startFlipping: Boolean = false,
    onStartFlipping: (() -> Unit)? = null,
    onFlip: ((Int) -> Unit)? = null
) {
    Box(contentAlignment = Alignment.Center) {
        var flipping by remember { mutableStateOf(true) }

        LaunchedEffect(coinType, startFlipping) {
            if (startFlipping) {
                flipCount++
                randomizeRotationAmount()
                flipping = !flipping
                onStartFlipping?.invoke()
                onFlip?.invoke(flipCount)
            }
        }

        val valueFloat: Float by animateFloatAsState(
            if (flipping) 0f else 1f,
            animationSpec = tween(
                durationMillis = 3000,
                easing = LinearOutSlowInEasing
            )
        )

        Box(
            Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .clickable {
                    flipCount++
                    randomizeRotationAmount()
                    flipping = !flipping
                    onFlip?.invoke(flipCount)
                }
        ) {
            FlipAnimation(
                rotationY = valueFloat * rotationAmount,
                front = {
                    if (currentSide == CoinSide.HEADS) Heads(coinType.heads) else Tails(coinType.tails)
                }, back = {
                    if (currentSide == CoinSide.HEADS) Tails(coinType.tails) else Heads(coinType.heads)
                })
        }
    }
}

@Composable
fun Heads(headsRes: Int) {
    Image(
        painter = painterResource(headsRes),
        contentDescription = stringResource(R.string.heads),
        modifier = Modifier
            .fillMaxSize()
    )
}

@Composable
fun Tails(tailsRes: Int) {
    Image(
        painter = painterResource(tailsRes),
        contentDescription = stringResource(R.string.tails),
        modifier = Modifier
            .fillMaxSize()
    )
}

@Composable
fun FlipAnimation(
    rotationY: Float = 0f,
    front: @Composable () -> Unit,
    back: @Composable () -> Unit
) {
    fun isFlipped() = (abs(rotationY) % FULL_ROTATION > QUARTER_ROTATION
            && abs(rotationY) % FULL_ROTATION < THREE_QUARTER_ROTATION)
    if (isFlipped()) {
        Box(
            Modifier
                .graphicsLayer(
                    rotationY = rotationY - HALF_ROTATION,
                )
        ) {
            back()
        }
    } else {
        Box(
            Modifier
                .graphicsLayer(
                    rotationY = rotationY,
                )
        ) {
            front()
        }
    }
}

/**
 * Randomizes the rotation amount to be used for each coin flip.
 * There is a bug in Kotlin's Random class where it repeats itself every time the app loads,
 * so use java.util.Random here and add 10 to increase the number of rotations per flip.
 * https://issuetracker.google.com/issues/234631055
 */
private fun randomizeRotationAmount() {
    val randomFlips = Random().nextInt(8) + 10
    rotationAmount = randomFlips * HALF_ROTATION

    currentSide = nextSide
    nextSide = if (randomFlips % 2 == 0) CoinSide.HEADS else CoinSide.TAILS
}

private enum class CoinSide {
    HEADS, TAILS
}
