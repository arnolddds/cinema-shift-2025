package com.example.cinemashift.domain.usecases

import com.example.cinemashift.domain.entity.Schedule
import com.example.cinemashift.domain.repository.MovieRepository
import javax.inject.Inject



class GetMovieScheduleUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: String): List<Schedule> {
        return repository.getSchedule(movieId)
    }
}
