package com.helsinkiwizard.cointoss.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.helsinkiwizard.cointoss.Repository
import com.helsinkiwizard.cointoss.ui.model.WatchSettingsModel
import com.helsinkiwizard.core.viewmodel.AbstractViewModel
import com.helsinkiwizard.core.viewmodel.BaseType
import com.helsinkiwizard.core.viewmodel.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchSettingsViewModel @Inject constructor(
    private val repository: Repository
) : AbstractViewModel() {

    private lateinit var model: WatchSettingsModel

    init {
        viewModelScope.launch {
            model = WatchSettingsModel(
                speed = repository.getSpeed.filterNotNull().first(),
                playSoundEffect = repository.getPlaySound.filterNotNull().first()
            )
            mutableUiStateFlow.value = UiState.ShowContent(WatchSettingsContent.LoadingComplete(model))
        }
    }

    fun onSpeedSelected(speed: Float) {
        viewModelScope.launch {
            model.speed.value = speed
            repository.setSpeed(speed)
        }
    }

    fun onPlaySoundChecked(checked: Boolean) {
        viewModelScope.launch {
            model.playSound.value = checked
            repository.setPlaySound(checked)
        }
    }
}

internal sealed interface WatchSettingsContent : BaseType {
    data class LoadingComplete(val model: WatchSettingsModel) : WatchSettingsContent
}