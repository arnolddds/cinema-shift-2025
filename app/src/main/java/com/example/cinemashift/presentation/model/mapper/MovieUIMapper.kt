package com.example.cinemashift.presentation.model.mapper

import com.example.cinemashift.domain.entity.Movie
import com.example.cinemashift.presentation.model.MovieUI

fun Movie.toUI() = MovieUI(
    id = id,
    title = title,
    rating = rating,
    imageUrl = imageUrl,
    genres = genres,
    country = country,
    year = year
)