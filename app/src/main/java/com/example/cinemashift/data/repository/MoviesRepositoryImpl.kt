package com.example.cinemashift.data.repository

import com.example.cinemashift.data.api.MoviesApi
import com.example.cinemashift.domain.entity.Movie
import com.example.cinemashift.domain.repository.MoviesRepository
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val api: MoviesApi
) : MoviesRepository {
    override suspend fun getMovies(): List<Movie> = api.getMovies().map { it.toDomain() }
}
