package com.example.cinemashift.presentation.screen.moviedeatail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemashift.data.api.CinemaApiService
import com.example.cinemashift.domain.entity.Movie
import com.example.cinemashift.domain.entity.Schedule
import com.example.cinemashift.domain.usecases.GetMovieByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getMovieByIdUseCase: GetMovieByIdUseCase,
    private val apiService: CinemaApiService
) : ViewModel() {

    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> = _movie

    private val _schedule = MutableLiveData<List<Schedule>>()
    val schedule: LiveData<List<Schedule>> = _schedule

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadMovie(movieId: String) {
        if (_loading.value == true || _movie.value != null) return

        viewModelScope.launch {
            _loading.value = true
            try {
                val movie = getMovieByIdUseCase(movieId)
                _movie.value = movie
                val scheduleResponse = apiService.getSchedule(movieId)
                _schedule.value = scheduleResponse.schedules
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