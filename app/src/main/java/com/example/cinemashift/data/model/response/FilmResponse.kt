package com.example.cinemashift.data.model.response

data class FilmResponse(
    val id: String,
    val name: String,
    val originalName: String,
    val description: String,
    val releaseDate: String,
    val actors: List<PersonResponse>,
    val directors: List<PersonResponse>,
    val runtime: Int,
    val ageRating: String,
    val genres: List<String>,
    val userRatings: RatingsResponse,
    val img: String,
    val country: CountryResponse
)