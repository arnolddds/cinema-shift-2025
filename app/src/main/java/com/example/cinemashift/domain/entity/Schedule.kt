package com.example.cinemashift.domain.entity

data class Schedule(
    val date: String,
    val seances: List<Seance>
)