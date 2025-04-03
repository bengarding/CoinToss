package com.helsinkiwizard.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.helsinkiwizard.core.CoreConstants.SPEED_DEFAULT
import com.helsinkiwizard.core.coin.CoinType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class BaseRepository(private val context: Context) {

    companion object {
        @JvmStatic
        protected val Context.dataStore: DataStore<Preferences> by preferencesDataStore("preferences")
        private val COIN_TYPE = intPreferencesKey("coin_type")
        private val SPEED = floatPreferencesKey("speed")
        private val PLAY_SOUND_EFFECT = booleanPreferencesKey("play_sound_effect")
    }

    suspend fun setCoinType(coinType: CoinType) = savePreference(COIN_TYPE, coinType.value)
    val getCoinType: Flow<CoinType> = context.dataStore.data
        .map { preferences ->
            CoinType.parse(preferences[COIN_TYPE] ?: CoinType.BITCOIN.value)
        }

    suspend fun setSpeed(speed: Float) = savePreference(SPEED, speed)
    val getSpeed: Flow<Float> = context.dataStore.data
        .map { preferences ->
            preferences[SPEED] ?: SPEED_DEFAULT
        }

    suspend fun setPlaySound(play: Boolean) = savePreference(PLAY_SOUND_EFFECT, play)
    val getPlaySound: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PLAY_SOUND_EFFECT] ?: true
        }

    protected suspend fun <T> savePreference(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }
}
