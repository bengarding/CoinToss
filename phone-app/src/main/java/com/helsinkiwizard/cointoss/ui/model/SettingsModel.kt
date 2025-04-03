package com.helsinkiwizard.cointoss.ui.model

import com.helsinkiwizard.cointoss.data.ThemeMode
import com.helsinkiwizard.core.ui.model.MutableInputWrapper

class SettingsModel(
    themeMode: ThemeMode,
    materialYou: Boolean,
    speed: Float,
    showSendToWatchButton: Boolean,
    playSoundEffect: Boolean,
) {
    val themeMode = MutableInputWrapper(themeMode)
    val materialYou = MutableInputWrapper(materialYou)
    val speed = MutableInputWrapper(speed)
    val showSendToWatchButton = MutableInputWrapper(showSendToWatchButton)
    val playSound = MutableInputWrapper(playSoundEffect)
}
