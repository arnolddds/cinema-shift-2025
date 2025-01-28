package com.example.cinemashift.data.repository

import android.util.Log
import com.example.cinemashift.data.api.CinemaApiService
import com.example.cinemashift.data.model.response.mapper.toDomain
import com.example.cinemashift.domain.entity.Movie
import com.example.cinemashift.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: CinemaApiService
) : MovieRepository {
    override suspend fun getTodayMovies(): List<Movie> {
        val response = apiService.getTodayFilms()
        response.films.forEach {
            Log.d("MovieRepository", "Film: ${it.name}, Image path: ${it.img}")
        }
        return response.films.map { it.toDomain() }
    }
}