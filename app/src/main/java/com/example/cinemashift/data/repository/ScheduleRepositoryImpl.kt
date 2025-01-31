package com.example.cinemashift.data.repository

import com.example.cinemashift.data.api.CinemaApiService
import com.example.cinemashift.domain.entity.Schedule
import com.example.cinemashift.domain.repository.ScheduleRepository
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val apiService: CinemaApiService
) : ScheduleRepository {
    override suspend fun getSchedule(movieId: String): List<Schedule> {
        return apiService.getSchedule(movieId).schedules
    }
}
