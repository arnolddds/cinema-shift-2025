package com.example.cinemashift.data.repository

import com.example.cinemashift.data.api.CinemaApiService
import com.example.cinemashift.data.model.response.mapper.toDomain
import com.example.cinemashift.domain.entity.Movie
import com.example.cinemashift.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: CinemaApiService
) : MovieRepository {
    override suspend fun getTodayMovies(): List<Movie> {
        return apiService.getTodayFilms().films.map { it.toDomain() }
    }
}