package com.example.cinemashift.domain.entity

data class Seance(
    val time: String,
    val hall: Hall,
    val places: List<Place>,
)