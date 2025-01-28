package com.example.cinemashift.di

import androidx.test.espresso.core.internal.deps.dagger.Module
import com.example.cinemashift.data.api.MoviesApi
import com.example.cinemashift.data.repository.MoviesRepositoryImpl
import com.example.cinemashift.domain.repository.MoviesRepository
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object MoviesModule {

    @Provides
    fun provideMoviesApi(retrofit: Retrofit): MoviesApi {
        return retrofit.create(MoviesApi::class.java)
    }

    @Provides
    fun provideMoviesRepository(api: MoviesApi): MoviesRepository {
        return MoviesRepositoryImpl(api)
    }
}
