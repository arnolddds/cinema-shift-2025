package com.example.cinemashift.domain.usecases

import com.example.cinemashift.domain.entity.Movie
import com.example.cinemashift.domain.repository.MoviesRepository
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(private val repository: MoviesRepository) {
    suspend operator fun invoke(): List<Movie> = repository.getMovies()
}
