package com.example.cinemashift.data.model.response.mapper

import com.example.cinemashift.data.model.response.FilmResponse
import com.example.cinemashift.domain.entity.Movie

fun FilmResponse.toDomain() = Movie(
    id = id,
    title = name.trim(),
    imageUrl = img,
    rating = userRatings.kinopoisk.toFloatOrNull() ?: 0f,
    genres = genres,
    country = country.name,
    year = releaseDate.split(" ").lastOrNull() ?: ""
)