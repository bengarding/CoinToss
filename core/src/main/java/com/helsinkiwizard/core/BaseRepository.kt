package com.helsinkiwizard.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.helsinkiwizard.core.coin.CoinType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class BaseRepository(private val context: Context) {

    companion object {
        @JvmStatic
        protected val Context.dataStore: DataStore<Preferences> by preferencesDataStore("preferences")
        val COIN_TYPE = intPreferencesKey("coin_type")
    }

    val getCoinType: Flow<CoinType> = context.dataStore.data
        .map { preferences ->
            CoinType.parse(preferences[COIN_TYPE] ?: CoinType.BITCOIN.value)
        }

    suspend fun setCoinType(coinType: CoinType) = savePreference(COIN_TYPE, coinType.value)

    protected suspend fun <T> savePreference(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }
}
