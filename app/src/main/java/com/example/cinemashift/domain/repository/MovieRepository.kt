package com.example.cinemashift.domain.repository

import com.example.cinemashift.domain.entity.Movie

interface MoviesRepository {
    suspend fun getMovies(): List<Movie>
}
