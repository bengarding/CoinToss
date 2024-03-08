package com.helsinkiwizard.cointoss.ui.model

import com.helsinkiwizard.cointoss.data.ThemeMode

class SettingsModel(
    themeMode: ThemeMode
) {
    val theme = MutableInputWrapper(themeMode)
}
