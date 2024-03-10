package com.helsinkiwizard.cointoss

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.helsinkiwizard.cointoss.data.ThemeMode
import com.helsinkiwizard.cointoss.navigation.NavRoute
import com.helsinkiwizard.core.BaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

@Singleton
class Repository(context: Context) : BaseRepository(context) {

    companion object {
        private val CURRENT_NAV_ROUTE = stringPreferencesKey("current_nav_route")
        private val THEME_MODE = stringPreferencesKey("selected_theme")
        private val MATERIAL_YOU = booleanPreferencesKey("material_you")
    }

    suspend fun setCurrentNavRoute(route: NavRoute) = savePreference(CURRENT_NAV_ROUTE, route.name)
    val getCurrentNavRoute: Flow<NavRoute> = context.dataStore.data
        .map { preferences ->
            NavRoute.valueOf(preferences[CURRENT_NAV_ROUTE] ?: NavRoute.Home.name)
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
}
