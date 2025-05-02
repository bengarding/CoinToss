package com.helsinkiwizard.cointoss.ui.model

import com.helsinkiwizard.core.ui.model.MutableInputWrapper

class WatchSettingsModel(
    speed: Float,
    playSoundEffect: Boolean,
    tossFromWristMotion: Boolean,
    wristSensitivity: Int,
    tossFromBezel: Boolean,
    bezelSensitivity: Int,
) {
    val speed = MutableInputWrapper(speed)
    val playSound = MutableInputWrapper(playSoundEffect)
    val tossFromWristMotion = MutableInputWrapper(tossFromWristMotion)
    val wristSensitivity = MutableInputWrapper(wristSensitivity, initialVisibility = tossFromWristMotion)
    val tossFromBezel = MutableInputWrapper(tossFromBezel)
    val bezelSensitivity = MutableInputWrapper(bezelSensitivity, initialVisibility = tossFromBezel)
}
