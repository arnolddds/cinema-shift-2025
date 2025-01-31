package com.example.cinemashift.domain.usecases

import com.example.cinemashift.data.repository.ScheduleRepositoryImpl
import com.example.cinemashift.domain.entity.Schedule
import javax.inject.Inject


class GetMovieScheduleUseCase @Inject constructor(
    private val repository: ScheduleRepositoryImpl
) {
    suspend operator fun invoke(movieId: String): List<Schedule> {
        return repository.getSchedule(movieId)
    }
}
