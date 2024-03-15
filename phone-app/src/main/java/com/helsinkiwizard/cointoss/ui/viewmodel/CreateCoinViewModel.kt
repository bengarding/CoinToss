package com.helsinkiwizard.cointoss.ui.viewmodel

import android.net.Uri
import com.helsinkiwizard.cointoss.Repository
import com.helsinkiwizard.cointoss.ui.model.CreateCoinModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateCoinViewModel @Inject constructor(
    private val repository: Repository
) : AbstractViewModel() {

    private val model = CreateCoinModel()

    init {
        mutableUiStateFlow.value = UiState.ShowContent(CreateCoinContent.LoadingComplete(model))
    }

    fun setHeadsUri(uri: Uri) {
        model.headsUri = uri
    }
}

sealed interface CreateCoinContent: BaseType {
    data class LoadingComplete(val model: CreateCoinModel) : CreateCoinContent
}

sealed interface CreateCoinDialogs: BaseDialogType {
    data object MediaPicker : CreateCoinDialogs
}
