package com.example.myfintech.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.myfintech.data.remote.AuthApiService
import com.example.myfintech.data.repository.AuthRepositoryImpl
import com.example.myfintech.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // 1. Sediakan SharedPreferences (untuk simpan sesi login sederhana)
    @Provides
    @Singleton
    fun provideSharedPreferences(app: Application): SharedPreferences {
        return app.getSharedPreferences("my_fintech_prefs", Context.MODE_PRIVATE)
    }

    // 2. Sediakan API Service (Mock/Dummy untuk saat ini)
    @Provides
    @Singleton
    fun provideAuthApiService(): AuthApiService {
        return AuthApiService()
    }

    // 3. Sediakan Repository Implementasi
    // Hilt akan melihat bahwa AuthRepositoryImpl butuh ApiService & SharedPrefs,
    // dan Hilt sudah tahu cara membuatnya dari fungsi di atas.
    @Provides
    @Singleton
    fun provideAuthRepository(
        apiService: AuthApiService,
        sharedPreferences: SharedPreferences
    ): AuthRepository {
        return AuthRepositoryImpl(apiService, sharedPreferences)
    }
}