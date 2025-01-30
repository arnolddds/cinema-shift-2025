package com.example.cinemashift.data.api

import com.example.cinemashift.data.model.response.FilmDetailResponse
import com.example.cinemashift.data.model.response.FilmsResponse
import com.example.cinemashift.data.model.response.ScheduleResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CinemaApiService {
    @GET("cinema/today")
    suspend fun getTodayFilms(): FilmsResponse

    @GET("cinema/film/{filmId}")
    suspend fun getFilm(@Path("filmId") filmId: String): FilmDetailResponse

    @GET("cinema/film/{filmId}/schedule")
    suspend fun getSchedule(@Path("filmId") filmId: String): ScheduleResponse
}