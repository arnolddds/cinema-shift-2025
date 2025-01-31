package com.example.cinemashift.domain.repository


import com.example.cinemashift.domain.entity.Movie

interface MovieRepository {
    suspend fun getTodayMovies(): List<Movie>
    suspend fun getMovie(id: String): Movie
}