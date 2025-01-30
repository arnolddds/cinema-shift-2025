package com.example.cinemashift.domain.repository


import com.example.cinemashift.domain.entity.Movie
import com.example.cinemashift.domain.entity.Schedule

interface MovieRepository {
    suspend fun getTodayMovies(): List<Movie>
    suspend fun getMovie(id: String): Movie
    suspend fun getSchedule(movieId: String): List<Schedule>
}