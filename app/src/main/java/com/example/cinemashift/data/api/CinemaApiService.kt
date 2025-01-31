package com.example.cinemashift.data.api

import com.example.cinemashift.data.model.response.dto.FilmDetailDto
import com.example.cinemashift.data.model.response.dto.FilmsDto
import com.example.cinemashift.data.model.response.dto.ScheduleDto
import retrofit2.http.GET
import retrofit2.http.Path

interface CinemaApiService {
    @GET("cinema/today")
    suspend fun getTodayFilms(): FilmsDto

    @GET("cinema/film/{filmId}")
    suspend fun getFilm(@Path("filmId") filmId: String): FilmDetailDto

    @GET("cinema/film/{filmId}/schedule")
    suspend fun getSchedule(@Path("filmId") filmId: String): ScheduleDto
}