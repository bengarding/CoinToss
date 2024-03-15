package com.helsinkiwizard.cointoss.ui.viewmodel

import android.net.Uri
import com.helsinkiwizard.cointoss.Repository
import com.helsinkiwizard.cointoss.ui.model.CreateCoinModel
import com.helsinkiwizard.core.coin.CoinSide
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

    fun onCoinSideClicked(coinSide: CoinSide) {
        mutableDialogStateFlow.value = DialogState.ShowContent(CreateCoinDialogs.MediaPicker(coinSide))
    }
}

sealed interface CreateCoinContent: BaseType {
    data class LoadingComplete(val model: CreateCoinModel) : CreateCoinContent
}

sealed interface CreateCoinDialogs: BaseDialogType {
    data class MediaPicker(val coinSide: CoinSide) : CreateCoinDialogs
}
