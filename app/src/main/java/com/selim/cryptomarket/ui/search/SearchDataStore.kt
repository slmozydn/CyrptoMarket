package com.selim.cryptomarket.ui.search

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class SearchDataStore @Inject constructor(
    @Named("search") private val searchDataStore: DataStore<Preferences>
) {

    val searchQueries: Flow<String> = searchDataStore.data.map { preferences ->
        preferences[KEY_SEARCH_QUERY] ?: ""
    }

    suspend fun updateSearchPreference(query: String) {
        searchDataStore.edit { search ->
            val previousHistory = search[KEY_SEARCH_QUERY] ?: ""
            val searchQueries = previousHistory.split(" ").toList().dropWhile { it == "" }.takeLast(3).toMutableList()
            searchQueries.add(query)
            search[KEY_SEARCH_QUERY] = searchQueries.joinToString(separator = " ")
        }
    }

    suspend fun clearSearchPreference() {
        searchDataStore.edit { search ->
            search[KEY_SEARCH_QUERY] = ""
        }
    }

    companion object {
        val KEY_SEARCH_QUERY = stringPreferencesKey("searchQuery")
    }
}
