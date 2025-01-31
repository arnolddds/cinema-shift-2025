package com.example.cinemashift.presentation.screen.movielist

import com.example.cinemashift.domain.entity.Movie


sealed class MovieListUiState {
    data object Initial : MovieListUiState()
    data object Loading : MovieListUiState()
    data class Success(val movies: List<Movie>) : MovieListUiState()
    data class Error(val message: String) : MovieListUiState()
}
