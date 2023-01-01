package com.selim.cryptomarket.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.selim.cryptomarket.ui.search.SearchDataStore
import com.selim.cryptomarket.ui.settings.SettingsDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val Context.searchDataStore: DataStore<Preferences> by preferencesDataStore(name = "search")

    @Provides
    @Singleton
    fun provideCurrencyDataStore(@ApplicationContext context: Context) = SettingsDataStore(context.settingsDataStore)

    @Provides
    @Singleton
    fun provideSearchDataStore(@ApplicationContext context: Context) = SearchDataStore(context.searchDataStore)

}
