package com.selim.cryptomarket.ui.search

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchDataStore @Inject constructor(private val searchDataStore: DataStore<Preferences>) {

    val searchQueries: Flow<String> = searchDataStore.data.map { preferences ->
        preferences[KEY_SEARCH_QUERY] ?: ""
    }

    suspend fun updateSearchPreference(query: String) {
        searchDataStore.edit { search ->
            val ss = search[KEY_SEARCH_QUERY] ?: ""
            search[KEY_SEARCH_QUERY] = "$ss $query"
        }
    }

    companion object {
        val KEY_SEARCH_QUERY = stringPreferencesKey("searchQuery")
    }
}
