package com.example.cinemashift.domain.usecases

import com.example.cinemashift.domain.entity.Movie
import com.example.cinemashift.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieByIdUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(id: String): Movie {
        return repository.getMovie(id)
    }
}