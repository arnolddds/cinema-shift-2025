package com.example.cinemashift.data.api

import com.example.cinemashift.data.models.MovieDto
import retrofit2.http.GET

interface MoviesApi {
    @GET("cinema/today")
    suspend fun getMovies(): List<MovieDto>
}
