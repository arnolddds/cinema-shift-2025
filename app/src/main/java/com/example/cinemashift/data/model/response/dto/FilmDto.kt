package com.example.cinemashift.data.model.response.dto

data class FilmDto(
    val id: String,
    val name: String,
    val originalName: String,
    val description: String,
    val releaseDate: String,
    val actors: List<PersonDto>,
    val directors: List<PersonDto>,
    val runtime: Int,
    val ageRating: String,
    val genres: List<String>,
    val userRatings: RatingsDto,
    val img: String,
    val country: CountryDto
)