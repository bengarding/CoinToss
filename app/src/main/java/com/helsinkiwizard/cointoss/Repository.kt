package com.helsinkiwizard.cointoss

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.helsinkiwizard.cointoss.coin.CoinType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Repository(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("preferences")
        val COIN_TYPE = intPreferencesKey("coin_type")
        val TILE_RESOURCE_VERSION = intPreferencesKey("tile_resources_version")
    }

    val getCoinType: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[COIN_TYPE] ?: CoinType.BITCOIN.ordinal
        }

    val getResourceVersion: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[TILE_RESOURCE_VERSION] ?: 0
        }

    suspend fun saveIntPreference(key: Preferences.Key<Int>, value: Int) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }
}