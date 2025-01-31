package com.example.cinemashift.presentation.screen.movielist


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemashift.domain.usecases.GetTodayMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val getTodayMoviesUseCase: GetTodayMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<MovieListUiState>()
    val uiState: LiveData<MovieListUiState> = _uiState

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            _uiState.value = MovieListUiState.Loading
            try {
                val movies = getTodayMoviesUseCase()
                _uiState.value = MovieListUiState.Success(movies)
            } catch (e: Exception) {
                _uiState.value = MovieListUiState.Error(
                    when (e) {
                        is IOException -> "Проверьте подключение к интернету"
                        else -> "Что-то пошло не так"
                    }
                )
            }
        }
    }
}





