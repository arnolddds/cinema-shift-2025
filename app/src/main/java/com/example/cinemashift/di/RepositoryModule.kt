package com.example.cinemashift.di

import com.example.cinemashift.data.repository.MovieRepositoryImpl
import com.example.cinemashift.data.repository.ScheduleRepositoryImpl
import com.example.cinemashift.domain.repository.MovieRepository
import com.example.cinemashift.domain.repository.ScheduleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository

    @Binds
    @Singleton
    abstract fun bindScheduleRepository(
        scheduleRepositoryImpl: ScheduleRepositoryImpl
    ): ScheduleRepository
}
