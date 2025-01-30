package com.example.cinemashift.data.model.response.mapper

import com.example.cinemashift.data.model.response.dto.CountryDto
import com.example.cinemashift.data.model.response.dto.FilmDto
import com.example.cinemashift.data.model.response.dto.PersonDto
import com.example.cinemashift.domain.entity.Country
import com.example.cinemashift.domain.entity.Movie
import com.example.cinemashift.domain.entity.Person

fun FilmDto.toDomain() = Movie(
    id = id,
    title = name,
    originalName = originalName,
    description = description,
    releaseDate = releaseDate,
    actors = actors.map { it.toDomain() },
    directors = directors.map { it.toDomain() },
    runtime = runtime,
    ageRating = ageRating,
    genres = genres,
    rating = userRatings.kinopoisk.toFloatOrNull() ?: 0f,
    imageUrl = img,
    country = country.toDomain()
)

fun PersonDto.toDomain() = Person(
    id = id,
    professions = professions,
    fullName = fullName
)

fun CountryDto.toDomain() = Country(
    id = id,
    code = code,
    code2 = code2,
    name = name
)