package com.example.cinemashift.presentation.screen.movielist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemashift.domain.usecase.GetTodayMoviesUseCase
import com.example.cinemashift.presentation.model.MovieUI
import com.example.cinemashift.presentation.model.mapper.toUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val getTodayMoviesUseCase: GetTodayMoviesUseCase
) : ViewModel() {
    private val _movies = MutableLiveData<List<MovieUI>>()
    val movies: LiveData<List<MovieUI>> = _movies

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val movies = getTodayMoviesUseCase()
                _movies.value = movies.map { it.toUI() }
                Log.d("MovieListViewModel", "Loaded movies: ${movies.size}")
                movies.forEach {
                    Log.d("MovieListViewModel", "Movie: ${it.title}")
                }
            } catch (e: Exception) {
                Log.e("MovieListViewModel", "Error loading movies", e)
                _error.value = e.message ?: "Неизвестная ошибка"
            } finally {
                _loading.value = false
            }
        }
    }
}