package com.example.cinemashift.domain.entity

data class Movie(
    val id: String,
    val title: String,
    val originalName: String,
    val description: String,
    val releaseDate: String,
    val actors: List<Person>,
    val directors: List<Person>,
    val runtime: Int,
    val ageRating: String,
    val genres: List<String>,
    val rating: Float,
    val imageUrl: String,
    val country: Country
)