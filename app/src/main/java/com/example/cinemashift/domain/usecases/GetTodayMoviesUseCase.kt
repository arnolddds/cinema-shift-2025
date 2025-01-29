package com.example.cinemashift.domain.usecase

import com.example.cinemashift.domain.entity.Movie
import com.example.cinemashift.domain.repository.MovieRepository
import javax.inject.Inject


class GetTodayMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(): List<Movie> {
        return repository.getTodayMovies()
    }
}