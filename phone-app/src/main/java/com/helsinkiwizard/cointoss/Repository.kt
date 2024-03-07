package com.helsinkiwizard.cointoss

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import com.helsinkiwizard.cointoss.navigation.NavRoute
import com.helsinkiwizard.core.BaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

@Singleton
class Repository(context: Context) : BaseRepository(context) {

    companion object {
        val CURRENT_NAV_ROUTE = stringPreferencesKey("current_nav_route")
    }

    val getCurrentNavRoute: Flow<NavRoute> = context.dataStore.data
        .map { preferences ->
            NavRoute.valueOf(preferences[CURRENT_NAV_ROUTE] ?: NavRoute.Home.name)
        }

    suspend fun setCurrentNavRoute(route: NavRoute) = savePreference(CURRENT_NAV_ROUTE, route.name)
}
