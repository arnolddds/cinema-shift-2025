package com.example.cinemashift.data.model.response.dto

data class FilmsDto(
    val success: Boolean,
    val reason: String?,
    val films: List<FilmDto>
)
