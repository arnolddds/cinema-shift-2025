package com.example.cinemashift.domain.entity

data class Movie(
    val id: String,
    val title: String,
    val rating: Float,
    val imageUrl: String,
    val genres: List<String>,
    val country: String,
    val year: String
)