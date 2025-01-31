package com.example.cinemashift.data.model.response.dto

import com.example.cinemashift.domain.entity.Schedule

data class ScheduleDto(
    val success: Boolean,
    val reason: String?,
    val schedules: List<Schedule>
)
