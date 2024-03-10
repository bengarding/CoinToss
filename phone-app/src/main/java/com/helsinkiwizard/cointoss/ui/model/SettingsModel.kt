package com.helsinkiwizard.cointoss.ui.model

import com.helsinkiwizard.cointoss.data.ThemeMode

class SettingsModel(
    themeMode: ThemeMode,
    materialYou: Boolean
) {
    val themeMode = MutableInputWrapper(themeMode)
    val materialYou = MutableInputWrapper(materialYou)
}
