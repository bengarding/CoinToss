package com.helsinkiwizard.cointoss.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helsinkiwizard.cointoss.Repository
import com.helsinkiwizard.core.BaseRepository.Companion.COIN_TYPE
import com.helsinkiwizard.core.coin.CoinType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun onCoinClick(coinType: CoinType) {
        viewModelScope.launch {
            repository.setCoinType(COIN_TYPE, coinType.value)
        }
    }
}
