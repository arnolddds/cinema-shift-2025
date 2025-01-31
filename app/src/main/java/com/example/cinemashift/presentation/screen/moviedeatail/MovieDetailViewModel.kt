package com.example.cinemashift.presentation.screen.moviedeatail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemashift.domain.usecases.GetMovieByIdUseCase
import com.example.cinemashift.domain.usecases.GetMovieScheduleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getMovieByIdUseCase: GetMovieByIdUseCase,
    private val getMovieScheduleUseCase: GetMovieScheduleUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<MovieDetailUiState>()
    val uiState: LiveData<MovieDetailUiState> = _uiState

    fun loadMovie(movieId: String) {
        if (_uiState.value is MovieDetailUiState.Loading) return

        viewModelScope.launch {
            _uiState.value = MovieDetailUiState.Loading
            try {
                val movie = getMovieByIdUseCase(movieId)
                val schedule = getMovieScheduleUseCase(movieId)
                _uiState.value = MovieDetailUiState.Success(
                    movie = movie,
                    schedule = schedule
                )
            } catch (e: Exception) {
                _uiState.value = MovieDetailUiState.Error(
                    when (e) {
                        is IOException -> "Проверьте подключение к интернету"
                        else -> "Что-то пошло не так"
                    }
                )
            }
        }
    }
}