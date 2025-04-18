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
                playSoundEffect = repository.getPlaySound.filterNotNull().first(),
                tossFromWristMotion = repository.getTossFromWristFlip.filterNotNull().first(),
                wristSensitivity = repository.getWristSensitivity.filterNotNull().first(),
                tossFromBezel = repository.getTossFromBezel.filterNotNull().first(),
                bezelSensitivity = repository.getBezelSensitivity.filterNotNull().first(),
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

    fun onTossFromWristMovement(checked: Boolean) {
        viewModelScope.launch {
            model.tossFromWristMotion.value = checked
            model.wristSensitivity.isVisible = checked
            repository.setTossFromWristFlip(checked)
        }
    }

    fun onWristSensitivitySelected(sensitivity: Int) {
        viewModelScope.launch {
            model.wristSensitivity.value = sensitivity
            repository.setWristSensitivity(sensitivity)
        }
    }

    fun onTossFromBezelChecked(checked: Boolean) {
        viewModelScope.launch {
            model.tossFromBezel.value = checked
            model.bezelSensitivity.isVisible = checked
            repository.setTossFromBezel(checked)
        }
    }

    fun onBezelSensitivitySelected(sensitivity: Int) {
        viewModelScope.launch {
            model.bezelSensitivity.value = sensitivity
            repository.setBezelSensitivity(sensitivity)
        }
    }
}

internal sealed interface WatchSettingsContent : BaseType {
    data class LoadingComplete(val model: WatchSettingsModel) : WatchSettingsContent
}