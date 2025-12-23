package com.selim.cryptomarket.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val Context.searchDataStore: DataStore<Preferences> by preferencesDataStore(name = "search")

    @Provides
    @Singleton
    @Named("settings")
    fun provideSettingsDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.settingsDataStore
    }

    @Provides
    @Singleton
    @Named("search")
    fun provideSearchDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.searchDataStore
    }
}
