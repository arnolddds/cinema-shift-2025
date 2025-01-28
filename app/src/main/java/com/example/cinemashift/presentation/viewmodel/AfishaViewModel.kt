package com.example.cinemashift.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemashift.domain.entity.Movie
import com.example.cinemashift.domain.usecases.GetMoviesUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class AfishaViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies

    fun loadMovies() {
        viewModelScope.launch {
            val movies = getMoviesUseCase()
            _movies.value = movies
        }
    }
}