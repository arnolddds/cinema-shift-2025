package com.example.cinemashift.presentation.screen.movielist


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemashift.domain.entity.Movie
import com.example.cinemashift.domain.usecase.GetTodayMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val getTodayMoviesUseCase: GetTodayMoviesUseCase
) : ViewModel() {
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadMovies() {
        if (_loading.value == true || _movies.value != null) return

        viewModelScope.launch {
            _loading.value = true
            try {
                val movies = getTodayMoviesUseCase()
                _movies.value = movies
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _error.value = e.message ?: "Error"
            } finally {
                _loading.value = false
            }
        }
    }
}
