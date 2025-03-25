package com.helsinkiwizard.cointoss.data

import android.content.Context
import android.net.Uri
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.helsinkiwizard.cointoss.data.room.CoinTossDatabase
import com.helsinkiwizard.cointoss.data.room.CustomCoin
import com.helsinkiwizard.core.BaseRepository
import com.helsinkiwizard.core.ui.model.CustomCoinUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val context: Context,
    private val database: CoinTossDatabase? = null
) : BaseRepository(context) {

    companion object {
        private val THEME_MODE = stringPreferencesKey("selected_theme")
        private val MATERIAL_YOU = booleanPreferencesKey("material_you")
        private val SHOW_SEND_TO_WATCH_BUTTON = booleanPreferencesKey("show_send_to_watch")
        private val ADS_REMOVED = booleanPreferencesKey("ads_removed")
        private val SELECTED_COUNT = intPreferencesKey("coin_selected_count")
        private val CUSTOM_COUNT = intPreferencesKey("custom_count")

        private const val DEFAULT_COUNT = 1
        private const val MAX_COUNT = 5
    }

    suspend fun setTheme(themeMode: ThemeMode) = savePreference(THEME_MODE, themeMode.name)
    val getThemeMode: Flow<ThemeMode> = context.dataStore.data
        .map { preferences ->
            ThemeMode.valueOf(preferences[THEME_MODE] ?: ThemeMode.SYSTEM.name)
        }

    suspend fun setMaterialYou(materialYou: Boolean) = savePreference(MATERIAL_YOU, materialYou)
    val getMaterialYou: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[MATERIAL_YOU] ?: true
        }

    suspend fun setShowSendToWatchButton(show: Boolean) = savePreference(SHOW_SEND_TO_WATCH_BUTTON, show)
    val getShowSendToWatchButton: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[SHOW_SEND_TO_WATCH_BUTTON] ?: true
        }

    suspend fun setAdsRemoved(adsRemoved: Boolean) = savePreference(ADS_REMOVED, adsRemoved)
    val getAdsRemoved: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[ADS_REMOVED] ?: false
        }

    suspend fun storeCustomCoin(headsUri: Uri, tailsUri: Uri, name: String) {
        val customCoin = CustomCoin(
            heads = headsUri.toString(),
            tails = tailsUri.toString(),
            name = name,
            selected = true
        )
        database?.customCoinDao()?.deselectThenInsert(customCoin)
    }

    suspend fun updateCustomCoin(headsUri: Uri, tailsUri: Uri, name: String, oldCoinId: Int) {
        val customCoin = CustomCoin(
            heads = headsUri.toString(),
            tails = tailsUri.toString(),
            name = name,
            selected = true
        )
        database?.customCoinDao()?.updateCoin(customCoin, oldCoinId)
    }

    suspend fun deleteCustomCoin(coinId: Int, selectNextCoin: Boolean) {
        database?.customCoinDao()?.deleteById(coinId)
        if (selectNextCoin) {
            database?.customCoinDao()?.selectCoinWithHighestId()
        }
    }

    suspend fun selectCustomCoin(coinId: Int) {
        database?.customCoinDao()?.deselectAllThenSelect(coinId)
    }

    fun getSelectedCustomCoin(): Flow<CustomCoinUiModel?> {
        return database?.customCoinDao()?.getSelectedCoin()?.map { it?.toUiModel() } ?: flowOf()
    }

    fun getCustomCoins(): Flow<List<CustomCoinUiModel>> {
        return database?.customCoinDao()?.getAllFlow()?.map { list ->
            list.map { it.toUiModel() }
        } ?: flowOf()
    }

    suspend fun showCoinListInterstitialAd(): Boolean {
        if (getAdsRemoved.first()) return false

        val count = getSelectedCount()
        incrementSelectedCount()

        val shouldShow = count % MAX_COUNT == 0
        if (shouldShow) resetSelectedCount()
        return shouldShow
    }

    suspend fun showCustomCoinInterstitialAd(): Boolean {
        if (getAdsRemoved.first()) return false

        val count = getCustomCount()
        incrementCustomCount()

        val shouldShow = count % MAX_COUNT == 0
        if (shouldShow) resetCustomCount()
        return shouldShow
    }

    private suspend fun incrementSelectedCount() = savePreference(SELECTED_COUNT, getSelectedCount().inc())
    private suspend fun resetSelectedCount() = savePreference(SELECTED_COUNT, DEFAULT_COUNT)
    private suspend fun getSelectedCount(): Int = context.dataStore.data
        .map { preferences -> preferences[SELECTED_COUNT] ?: DEFAULT_COUNT }
        .first()

    private suspend fun incrementCustomCount() = savePreference(CUSTOM_COUNT, getCustomCount().inc())
    private suspend fun resetCustomCount() = savePreference(CUSTOM_COUNT, DEFAULT_COUNT)
    private suspend fun getCustomCount(): Int = context.dataStore.data
        .map { preferences -> preferences[CUSTOM_COUNT] ?: DEFAULT_COUNT }
        .first()
}
