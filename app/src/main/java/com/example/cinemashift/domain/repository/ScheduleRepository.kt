package com.example.cinemashift.domain.repository

import com.example.cinemashift.domain.entity.Schedule

interface ScheduleRepository {
    suspend fun getSchedule(movieId: String): List<Schedule>
}