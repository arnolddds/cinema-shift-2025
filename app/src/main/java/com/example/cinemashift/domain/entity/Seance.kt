package com.example.cinemashift.domain.entity

data class Seance(
    val hall: Hall,
    val places: List<Place>,
)