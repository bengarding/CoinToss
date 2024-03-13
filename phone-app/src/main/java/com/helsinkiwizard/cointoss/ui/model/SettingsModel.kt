package com.helsinkiwizard.cointoss.ui.model

import com.helsinkiwizard.cointoss.data.ThemeMode

class SettingsModel(
    themeMode: ThemeMode,
    materialYou: Boolean,
    speed: Float
) {
    val themeMode = MutableInputWrapper(themeMode)
    val materialYou = MutableInputWrapper(materialYou)
    val speed = MutableInputWrapper(speed)
}
