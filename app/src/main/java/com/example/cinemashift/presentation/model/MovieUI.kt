package com.example.cinemashift.presentation.model

data class MovieUI(
    val id: String,
    val title: String,
    val rating: Float,
    val imageUrl: String,
    val genres: List<String>,
    val country: String,
    val year: String
)