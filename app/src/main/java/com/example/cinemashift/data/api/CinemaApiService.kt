package com.example.cinemashift.data.api

import com.example.cinemashift.data.model.response.FilmDetailResponse
import com.example.cinemashift.data.model.response.FilmResponse
import com.example.cinemashift.data.model.response.FilmsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CinemaApiService {
    @GET("cinema/today")
    suspend fun getTodayFilms(): FilmsResponse

    @GET("cinema/film/{filmId}")
    suspend fun getFilm(@Path("filmId") filmId: String): FilmDetailResponse
}