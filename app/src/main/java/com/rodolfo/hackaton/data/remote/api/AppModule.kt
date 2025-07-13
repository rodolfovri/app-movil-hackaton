package com.rodolfo.hackaton.data.remote.api

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.rodolfo.hackaton.data.local.PreferencesManager
import com.rodolfo.hackaton.data.remote.ApiServiceImpl
import com.rodolfo.hackaton.data.repository.AuthRepositoryImpl
import com.rodolfo.hackaton.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return ApiClient.create()
    }

    @Provides
    @Singleton
    fun provideApiService(httpClient: HttpClient): ApiService {
        return ApiServiceImpl(httpClient)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { appContext.preferencesDataStoreFile("user_preferences") }
        )
    }

    @Provides
    @Singleton
    fun providePreferencesManager(dataStore: DataStore<Preferences>): PreferencesManager {
        return PreferencesManager(dataStore)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(apiService: ApiService): AuthRepository {
        return AuthRepositoryImpl(apiService)
    }
}