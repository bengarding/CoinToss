package com.helsinkiwizard.cointoss.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.helsinkiwizard.cointoss.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: Repository
) : ViewModel() {
    val coinTypeFlow = repository.getCoinType
}
