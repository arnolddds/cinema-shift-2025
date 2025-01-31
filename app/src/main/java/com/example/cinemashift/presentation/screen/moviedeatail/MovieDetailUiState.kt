package com.example.cinemashift.presentation.screen.moviedeatail

import com.example.cinemashift.domain.entity.Movie
import com.example.cinemashift.domain.entity.Schedule

sealed class MovieDetailUiState {
    data object Loading : MovieDetailUiState()
    data class Success(
        val movie: Movie,
        val schedule: List<Schedule>
    ) : MovieDetailUiState()
    data class Error(val message: String) : MovieDetailUiState()
}