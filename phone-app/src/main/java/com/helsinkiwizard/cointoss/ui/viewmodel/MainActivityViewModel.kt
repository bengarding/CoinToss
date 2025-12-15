package com.helsinkiwizard.cointoss.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.helsinkiwizard.cointoss.data.Repository
import com.helsinkiwizard.cointoss.data.ThemeMode
import com.helsinkiwizard.core.viewmodel.AbstractViewModel
import com.helsinkiwizard.core.viewmodel.BaseType
import com.helsinkiwizard.core.viewmodel.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: Repository
) : AbstractViewModel() {

    init {
        viewModelScope.launch {
            mutableUiStateFlow.value = UiState.ShowContent(
                MainActivityContent(
                    themeMode = repository.getThemeMode.stateIn(viewModelScope),
                    materialYou = repository.getMaterialYou.stateIn(viewModelScope),
                    adsRemoved = repository.getAdsRemoved.stateIn(viewModelScope)
                )
            )
        }
    }
}

data class MainActivityContent(
    val themeMode: StateFlow<ThemeMode>,
    val materialYou: StateFlow<Boolean>,
    val adsRemoved: StateFlow<Boolean>
) : BaseType
