package com.example.cinemashift.data.model.response

data class FilmsResponse(
    val success: Boolean,
    val reason: String?,
    val films: List<FilmResponse>
)
