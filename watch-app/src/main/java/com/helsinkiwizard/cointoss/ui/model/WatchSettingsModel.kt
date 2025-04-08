package com.helsinkiwizard.cointoss.ui.model

import com.helsinkiwizard.core.ui.model.MutableInputWrapper

class WatchSettingsModel(
    speed: Float,
    playSoundEffect: Boolean,
) {
    val speed = MutableInputWrapper(speed)
    val playSound = MutableInputWrapper(playSoundEffect)
}
