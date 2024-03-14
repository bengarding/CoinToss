package com.helsinkiwizard.cointoss.ui.viewmodel

import com.helsinkiwizard.cointoss.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateCoinViewModel @Inject constructor(
    private val repository: Repository
) : AbstractViewModel() {

}
