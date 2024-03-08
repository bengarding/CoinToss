package com.helsinkiwizard.cointoss.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.helsinkiwizard.cointoss.Repository
import com.helsinkiwizard.cointoss.data.ThemeMode
import com.helsinkiwizard.cointoss.ui.model.SettingsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    private val repository: Repository
) : AbstractViewModel() {

    private lateinit var model: SettingsModel

    init {
        viewModelScope.launch {
            val theme = repository.getThemeMode.filterNotNull().first()
            model = SettingsModel(theme)
            mutableUiStateFlow.value = UiState.ShowContent(SettingsContent.LoadingComplete(model))
        }
    }

    fun onThemeModeClicked(themeMode: ThemeMode) {
        model.themeMode.value = themeMode
        viewModelScope.launch {
            repository.setTheme(themeMode)
        }
    }
}

internal sealed interface SettingsContent : BaseType {
    data class LoadingComplete(val model: SettingsModel) : SettingsContent
}
